package pt.ipca.smartcanteen.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroTicket
import pt.ipca.smartcanteen.models.RetroTicketMeal
import pt.ipca.smartcanteen.models.adapters.OrderDetailsAdapterRec
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.OrdersService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsumerOrderDetailsActivity : AppCompatActivity() {
    private val tradeBtn: Button by lazy {findViewById<View>(R.id.order_details_trade_btn) as Button }
    private val cancelBtn: Button by lazy {findViewById<View>(R.id.order_details_cancel_btn) as Button }
    private val backArrow: ImageView by lazy {findViewById<View>(R.id.order_details_back_arrow_iv) as ImageView }
    private val totalTv: TextView by lazy {findViewById<View>(R.id.order_details_total_tv) as TextView }

    private val detailsRv: RecyclerView by lazy {findViewById<View>(R.id.order_details_rv) as RecyclerView }
    private val loadingBar: ProgressBar by lazy {findViewById<View>(R.id.order_details_progress_bar) as ProgressBar }
    private val loadingText: TextView by lazy {findViewById<View>(R.id.order_details_progress_bar_text) as TextView }

    private val titleTv: TextView by lazy {findViewById<View>(R.id.order_details_title_tv) as TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.consumer_activity_order_details)


        val ticketid = intent.getStringExtra("ticketid")
        val generaltradeid = intent.getStringExtra("generaltradeid")
        val norder = intent.getIntExtra("norder",0)
        val ticketTotal = intent.getFloatExtra("total", 0.0F)
        val isMyOrder = intent.getBooleanExtra("isMyOrder",true)
        val isFreeTrade = intent.getBooleanExtra("isFreeTrade", false)

        val orderMealsRecyclerView = findViewById<RecyclerView>(R.id.order_details_rv)
        val orderMealsLinearLayoutManager = LinearLayoutManager(this@ConsumerOrderDetailsActivity)
        getTicket(ticketid!!,orderMealsRecyclerView,orderMealsLinearLayoutManager)

        if(isFreeTrade){
            totalTv.text = getString(R.string.free)
        }else{
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
            if(isMyOrder){
                val intent = Intent(this@ConsumerOrderDetailsActivity, ConsumerTradeActivity::class.java)
                intent.putExtra("ticketid",ticketid)
                intent.putExtra("generaltradeid",generaltradeid)
                startActivity(intent)
            }else{
                if(!isFreeTrade) {
                    val intent = Intent(this@ConsumerOrderDetailsActivity, TradePaymentActivity::class.java)
                    intent.putExtra("ticketid",ticketid)
                    startActivity(intent)
                }
            }

        }
    }


    private fun getTicket(
        ticketid:String,
        orderMealsRecyclerView:RecyclerView,
        orderMealsLinearLayoutManager:LayoutManager
    ){
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(OrdersService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@ConsumerOrderDetailsActivity)
        val token = sp.getString("token", null)
        detailsRv.visibility=View.INVISIBLE
        loadingBar.visibility=View.VISIBLE
        loadingText.visibility=View.VISIBLE
        service.getTicketDetails(ticketid,"Bearer $token").enqueue(object :
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

                            orderMealsRecyclerView.layoutManager = orderMealsLinearLayoutManager
                            orderMealsRecyclerView.itemAnimator = DefaultItemAnimator()
                            orderMealsRecyclerView.adapter = ordersAdapter

                        }
                    }
                    detailsRv.visibility=View.VISIBLE
                    loadingBar.visibility=View.GONE
                    loadingText.visibility=View.GONE

                }
            }

            override fun onFailure(call: Call<List<RetroTicketMeal>>, t: Throwable) {
                print("error")
                detailsRv.visibility=View.VISIBLE
                loadingBar.visibility=View.GONE
                loadingText.visibility=View.GONE
            }

        })
    }
}