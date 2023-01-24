package pt.ipca.smartcanteen.views.fragments.consumer_fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.adapters.MenuOrdersAdapterRec
import pt.ipca.smartcanteen.models.adapters.OrdersAdapterRec
import pt.ipca.smartcanteen.models.adapters.TradesAdapterRec
import pt.ipca.smartcanteen.models.helpers.*
import pt.ipca.smartcanteen.models.retrofit.response.RetroMealChange
import pt.ipca.smartcanteen.models.retrofit.response.RetroTicket
import pt.ipca.smartcanteen.models.retrofit.response.RetroTicketMeal
import pt.ipca.smartcanteen.models.retrofit.response.RetroTrade
import pt.ipca.smartcanteen.services.OrdersService
import pt.ipca.smartcanteen.services.TradesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyOrdersFragment : Fragment() {

    private val ordersTextError: TextView by lazy { requireView().findViewById<TextView>(R.id.my_orders_empty_message) as TextView }
    private val tradesTextError: TextView by lazy { requireView().findViewById<TextView>(R.id.my_trades_empty_message) as TextView }
    val myOrdersAdater: RecyclerView by lazy { requireView().findViewById<RecyclerView>(R.id.my_orders_recycler_view) as RecyclerView }
    val myTradesAdater: RecyclerView by lazy { requireView().findViewById<RecyclerView>(R.id.my_trades_recycler_view) as RecyclerView }
    private val buttonMyOrders: Button by lazy { requireView().findViewById<Button>(R.id.my_orders_button) as Button }
    private val buttonMyTrades: Button by lazy { requireView().findViewById<Button>(R.id.my_trades_button) as Button }
    private val textTittle: TextView by lazy { requireView().findViewById<TextView>(R.id.my_orders_title) as TextView }
    private val progressBar: ProgressBar by lazy { requireView().findViewById<ProgressBar>(R.id.my_orders_progress_bar) as ProgressBar }
    private val textProgress: TextView by lazy { requireView().findViewById<TextView>(R.id.my_orders_progress_bar_text) as TextView }
    var orders = ArrayList<RetroTicket>()
    val linearLayoutManager = LinearLayoutManager(activity)
    val linearLayoutTradeManager = LinearLayoutManager(activity)

    private var localTickets = mutableListOf<RetroTicket>()

    private lateinit var alertDialogManager: AlertDialogManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alertDialogManager = AlertDialogManager(layoutInflater, requireActivity())
        alertDialogManager.createLoadingAlertDialog()



        buttonMyOrders.setOnClickListener {
            myOrders()
        }

        buttonMyTrades.setOnClickListener {
            myTrades()
        }
        /*val db = SmartCanteenDBHelper.getInstance(requireContext().applicationContext)
        GlobalScope.launch {
            val ticketsData = db.ticketsDao().getAllTickets()
            if (ticketsData.isNotEmpty()) {
                Log.d("MAIN", "Tickets NOT EMPTY")
                ticketsData.forEach { ticket ->
                    run {
                        var ticketMeals = mutableListOf<RetroTicketMeal>()
                        var cartData = db.cartDao().getCart(ticket.cartId)
                        if (cartData != null) {
                            var cartMealsData = db.cartMealsDao().getAllCartMeals(ticket.cartId)
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

                            }
                        }
                        localTickets.add(
                            RetroTicket(
                                ticket.barname,
                                ticket.ticketid,
                                ticket.ownername,
                                ticket.stateName,
                                ticket.cartId,
                                ticket.emissionDate,
                                ticket.pickupTime,
                                ticket.ticketAmount,
                                ticket.total,
                                ticket.nEncomenda,
                                ticket.isFree,
                                ticketMeals
                            )
                        )
                    }

                }
                val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
                var ordersAdapter =
                    OrdersAdapterRec(
                        progressBar,
                        textProgress,
                        linearLayoutManager,
                        sp,
                        myOrdersAdater,
                        orders,
                        requireActivity(),
                        requireContext()
                    )
                rebuildlistOrders(ordersAdapter)

            }
        }*/
        myOrders()

    }

    fun rebuildlistOrders(adapter: OrdersAdapterRec) {
        myOrdersAdater.layoutManager = linearLayoutManager
        myOrdersAdater.itemAnimator = DefaultItemAnimator()
        myOrdersAdater.adapter = adapter
    }

    fun rebuildlistTrades(adapter: TradesAdapterRec) {
        myTradesAdater.layoutManager = linearLayoutTradeManager
        myTradesAdater.itemAnimator = DefaultItemAnimator()
        myTradesAdater.adapter = adapter
    }

    private fun myOrders() {
        progressBar.visibility = View.GONE
        textProgress.visibility = View.GONE

        myTradesAdater.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        textProgress.visibility = View.VISIBLE
        textTittle.text = getString(R.string.my_orders)

        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(OrdersService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)


        var call =
            service.seeMyOrders("Bearer $token").enqueue(object :
                Callback<List<RetroTicket>> {
                override fun onResponse(
                    call: Call<List<RetroTicket>>,
                    response: Response<List<RetroTicket>>
                ) {
                    if (response.code() == 200) {
                        progressBar.visibility = View.GONE
                        textProgress.visibility = View.GONE
                        if (isAdded) {

                            val retroFit2 = response.body()

                            if (retroFit2 != null)
                                if (retroFit2.isEmpty()) {
                                    ordersTextError.visibility = View.VISIBLE
                                    myOrdersAdater.visibility = View.GONE
                                } else {
                                    myOrdersAdater.visibility = View.VISIBLE
                                    ordersTextError.visibility = View.GONE
                                    orders.clear()
                                    orders.addAll(retroFit2)

                                    rebuildlistOrders(
                                        OrdersAdapterRec(
                                            progressBar,
                                            textProgress,
                                            linearLayoutManager,
                                            sp,
                                            myOrdersAdater,
                                            orders,
                                            requireActivity(),
                                            requireContext()
                                        )
                                    )
                                }
                        }
                    } else if (response.code() == 500) {
                        progressBar.visibility = View.GONE
                        textProgress.visibility = View.GONE

                        Toasty.error(requireActivity(), getString(R.string.no_orders), Toast.LENGTH_LONG).show()
                    } else if (response.code() == 401) {
                        progressBar.visibility = View.GONE
                        textProgress.visibility = View.GONE
                        AuthHelper().newSessionToken(requireActivity())
                        myOrders()
                    }
                }

                override fun onFailure(calll: Call<List<RetroTicket>>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    textProgress.visibility = View.GONE
                    Toasty.error(requireActivity(), getString(R.string.error), Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun myTrades() {
        ordersTextError.visibility = View.GONE
        myOrdersAdater.visibility = View.GONE

        textTittle.text = getString(R.string.my_trades)

        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(TradesService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        myTradesAdater.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        textProgress.visibility = View.VISIBLE

        var call =
            service.seeMyTrades("Bearer $token").enqueue(object :
                Callback<List<RetroTrade>> {
                override fun onResponse(
                    call: Call<List<RetroTrade>>,
                    response: Response<List<RetroTrade>>
                ) {
                    if (response.code() == 200) {

                        myTradesAdater.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        textProgress.visibility = View.GONE

                        val retroFit2 = response.body()

                        retroFit2?.forEach { retroTrade ->
                            val generaltradeid = retroTrade.generaltradeid
                            val isgeneraltrade = retroTrade.isgeneraltrade
                        }

                        if (retroFit2 != null)
                            if (retroFit2.isEmpty()) {
                                tradesTextError.visibility = View.VISIBLE
                                myTradesAdater.visibility = View.GONE
                            } else {
                                myTradesAdater.visibility = View.VISIBLE
                                tradesTextError.visibility = View.GONE
                                rebuildlistTrades(
                                    TradesAdapterRec(
                                        progressBar,
                                        textProgress,
                                        linearLayoutTradeManager,
                                        sp,
                                        myTradesAdater,
                                        retroFit2,
                                        requireActivity(),
                                        requireContext(),
                                        getString(R.string.wish_remove_trade_ask),
                                        alertDialogManager
                                    )
                                )
                            }
                    } else if (response.code() == 500) {
                        myTradesAdater.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        textProgress.visibility = View.GONE

                        Toasty.error(requireActivity(), getString(R.string.no_trades), Toast.LENGTH_LONG).show()
                    } else if (response.code() == 401) {
                        AuthHelper().newSessionToken(requireActivity())
                        myTrades()
                    }
                }

                override fun onFailure(calll: Call<List<RetroTrade>>, t: Throwable) {
                    myTradesAdater.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    textProgress.visibility = View.GONE

                    Toasty.error(requireActivity(), getString(R.string.error), Toast.LENGTH_LONG).show()
                }
            })
    }
}


