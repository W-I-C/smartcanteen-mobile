package pt.ipca.smartcanteen.views.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.adapters.MenuOrdersAdapterRec
import pt.ipca.smartcanteen.models.adapters.OrderDetailsAdapterRec
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenDBHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.models.retrofit.response.RetroMealChange
import pt.ipca.smartcanteen.models.retrofit.response.RetroTicket
import pt.ipca.smartcanteen.models.retrofit.response.RetroTicketMeal
import pt.ipca.smartcanteen.services.OrdersService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsumerOrderDetailsActivity : AppCompatActivity() {
    private val tradeBtn: Button by lazy { findViewById<View>(R.id.order_details_trade_btn) as Button }
    private val cancelBtn: Button by lazy { findViewById<View>(R.id.order_details_cancel_btn) as Button }
    private val backArrow: ImageView by lazy { findViewById<View>(R.id.order_details_back_arrow_iv) as ImageView }
    private val totalTv: TextView by lazy { findViewById<View>(R.id.order_details_total_tv) as TextView }
    private val orderMealsRecyclerView: RecyclerView by lazy { findViewById<View>(R.id.order_details_rv) as RecyclerView }
    private val titleTv: TextView by lazy { findViewById<View>(R.id.order_details_title_tv) as TextView }

    private lateinit var localTicketMeals: List<RetroTicketMeal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.consumer_activity_order_details)


        val ticketid = intent.getStringExtra("ticketid")
        val generaltradeid = intent.getStringExtra("generaltradeid")
        val norder = intent.getIntExtra("norder", 0)
        val ticketTotal = intent.getFloatExtra("total", 0.0F)
        val isMyOrder = intent.getBooleanExtra("isMyOrder", true)
        val isFreeTrade = intent.getBooleanExtra("isFreeTrade", false)


        val db = SmartCanteenDBHelper.getInstance(this@ConsumerOrderDetailsActivity.applicationContext)
        GlobalScope.launch {
            val ticketData = db.ticketsDao().getTicket(ticketid!!)
            if (ticketData != null) {
                Log.d("MAIN", "Tickets NOT EMPTY")

                var ticketMeals = mutableListOf<RetroTicketMeal>()
                var cartData = db.cartDao().getCart(ticketData.cartId)
                if (cartData != null) {
                    var cartMealsData = db.cartMealsDao().getAllCartMeals(ticketData.cartId)
                    if (cartMealsData.isNotEmpty()) {
                        cartMealsData.forEach { cartMeal ->
                            var mealChanges = mutableListOf<RetroMealChange>()
                            var cartMealsChangesData = db.cartMealsChangesDao().getAllMealChanges(cartMeal.cartMealId)
                            cartMealsChangesData.forEach { change ->
                                mealChanges.add(
                                    RetroMealChange(
                                        change.cartChangeId,
                                        change.cartMealId,
                                        change.ingName,
                                        change.ingAmount,
                                        change.isRemoveOnly,
                                        change.canBeIncremented,
                                        change.canBeDecremented
                                    )
                                )
                            }
                            ticketMeals.add(
                                RetroTicketMeal(
                                    cartMeal.cartMealId,
                                    cartMeal.mealId,
                                    cartMeal.amount,
                                    cartMeal.mealPrice,
                                    cartMeal.name,
                                    cartMeal.description,
                                    cartMeal.canTakeaway,
                                    mealChanges
                                )
                            )
                        }
                        localTicketMeals=ticketMeals
                    }
                }


                var ordersAdapter =OrderDetailsAdapterRec(localTicketMeals)
                buildTicketInfo(ordersAdapter)
            }

        }

        getTicket(ticketid!!)



        if (isFreeTrade) {
            totalTv.text = getString(R.string.free)
        } else {
            totalTv.text = "Total: ${ticketTotal}€"
        }


        titleTv.text = "${getString(R.string.ordernum)}: ${norder}"

        backArrow.setOnClickListener {
            finish()
        }

        cancelBtn.setOnClickListener {
            finish()
        }

        tradeBtn.setOnClickListener {
            if (isMyOrder) {
                val intent = Intent(this@ConsumerOrderDetailsActivity, ConsumerTradeActivity::class.java)
                intent.putExtra("ticketId", ticketid)
                startActivity(intent)
            } else {
                val intent = Intent(this@ConsumerOrderDetailsActivity, TradePaymentActivity::class.java)
                intent.putExtra("ticketid", ticketid)
                intent.putExtra("generaltradeid", generaltradeid)
                intent.putExtra("price", ticketTotal)
                intent.putExtra("isfree", isFreeTrade)
                startActivity(intent)
            }
        }


    }


    private fun getTicket(
        ticketid: String
    ) {
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(OrdersService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@ConsumerOrderDetailsActivity)
        val token = sp.getString("token", null)
        service.getTicketDetails(ticketid, "Bearer $token").enqueue(object :
            Callback<List<RetroTicketMeal>> {
            override fun onResponse(
                call: Call<List<RetroTicketMeal>>,
                response: Response<List<RetroTicketMeal>>
            ) {
                if (response.code() == 200) {

                    val body = response.body()

                    if (body != null) {
                        if (body.isNotEmpty()) {
                            val ordersAdapter = OrderDetailsAdapterRec(body)



                        }
                    }
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(this@ConsumerOrderDetailsActivity)
                    getTicket(ticketid)
                }
            }

            override fun onFailure(call: Call<List<RetroTicketMeal>>, t: Throwable) {
            }

        })
    }

    private fun buildTicketInfo(adapter: OrderDetailsAdapterRec){
        val orderMealsLinearLayoutManager = LinearLayoutManager(this@ConsumerOrderDetailsActivity)
        orderMealsRecyclerView.layoutManager = orderMealsLinearLayoutManager
        orderMealsRecyclerView.itemAnimator = DefaultItemAnimator()
        orderMealsRecyclerView.adapter = adapter
    }
}