package pt.ipca.smartcanteen.views.fragments.consumer_fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.*
import pt.ipca.smartcanteen.models.RetroTicket
import pt.ipca.smartcanteen.models.RetroTrade
import pt.ipca.smartcanteen.models.adapters.TradesAdapterRec
import pt.ipca.smartcanteen.models.adapters.OrdersAdapterRec
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.OrdersService
import pt.ipca.smartcanteen.services.TradesService
import pt.ipca.smartcanteen.views.activities.ConsumerTradeActivity
import pt.ipca.smartcanteen.views.activities.DetailedMyOrderActivity
import pt.ipca.smartcanteen.views.activities.DetailedMyTradeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyOrdersFragment : Fragment() {

    // TODO: passar de myexchange para my_exchange
    private val ordersTextError: TextView by lazy {requireView().findViewById<TextView>(R.id.my_orders_empty_message) as TextView }
    private val tradesTextError: TextView by lazy {requireView().findViewById<TextView>(R.id.my_trades_empty_message) as TextView }
    val myOrdersAdater: RecyclerView by lazy {requireView().findViewById<RecyclerView>(R.id.my_orders_recycler_view) as RecyclerView }
    val myTradesAdater: RecyclerView by lazy {requireView().findViewById<RecyclerView>(R.id.my_trades_recycler_view) as RecyclerView }
    private val buttonMyOrders: Button by lazy {requireView().findViewById<Button>(R.id.my_orders_button) as Button }
    private val buttonMyTrades: Button by lazy {requireView().findViewById<Button>(R.id.my_trades_button) as Button }
    private val textTittle: TextView by lazy {requireView().findViewById<TextView>(R.id.my_orders_title) as TextView }
    private val progressBar: ProgressBar by lazy {requireView().findViewById<ProgressBar>(R.id.my_orders_progress_bar) as ProgressBar }
    private val textProgress: TextView by lazy {requireView().findViewById<TextView>(R.id.my_orders_progress_bar_text) as TextView }
    var orders = ArrayList<RetroTicket>()
    // val linearLayoutManager = LinearLayoutManager(requireContext())
    val linearLayoutManager = LinearLayoutManager(activity)
    val linearLayoutTradeManager = LinearLayoutManager(activity)
    private lateinit var loadingAlertDialog: AlertDialog
    //private val buttonTradeCard: Button by lazy {requireView().findViewById<Button>(R.id.my_orders_card_button_trade) as Button }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myOrders()

        buttonMyOrders.setOnClickListener {
            myOrders()
        }

        buttonMyTrades.setOnClickListener{
            myTrades()
        }
    }

    fun rebuildlistOrders(adapter: OrdersAdapterRec) {
        myOrdersAdater.layoutManager = linearLayoutManager
        myOrdersAdater.itemAnimator = DefaultItemAnimator()
        myOrdersAdater.adapter = adapter

        adapter.onItemClick = this::onItemOrdersClick
        // adapter.onButtonTradeClick = this::onButtonTradeClick
        // adapter.onDeleteButtonClick = this::onDeleteButtonClick
    }

    fun onItemOrdersClick(order: RetroTicket) {
        val intent = Intent(requireActivity(), DetailedMyOrderActivity::class.java).apply {
            putExtra("order_nencomenda", order.norder)
            putExtra("order_ticketamount", order.ticketamount)
            putExtra("order_total", order.total)
            putExtra("order_statename", order.statename)
        }
        startActivity(intent)
    }

    fun onButtonTradeClick(view: View) {
        var intent = Intent(requireActivity(), ConsumerTradeActivity::class.java)
        startActivity(intent)
    }

    fun onDeleteButtonClick(position: Int){
        // val ticketid = orders[position].ticketid
        val order = orders[position]
        val ticketid = order.ticketid
    }

    fun doTrade(view: View) {
        var intent = Intent(requireActivity(), ConsumerTradeActivity::class.java)
        startActivity(intent)
    }

    fun rebuildlistTrades(adapter: TradesAdapterRec) {
        myTradesAdater.layoutManager = linearLayoutTradeManager
        myTradesAdater.itemAnimator = DefaultItemAnimator()
        myTradesAdater.adapter = adapter

        adapter.onItemTradesClick = this::onItemTradesClick
    }

    fun onItemTradesClick(trade: RetroTrade) {
        val intent = Intent(requireActivity(), DetailedMyTradeActivity::class.java).apply {
            putExtra("trade_nencomenda", trade.norder)
            putExtra("trade_ticketamount", trade.ticketamount)
            putExtra("trade_total", trade.total)
            putExtra("trade_statename", trade.statename)
        }
        startActivity(intent)
    }

    private fun myOrders(){
        tradesTextError.visibility = View.GONE
        myTradesAdater.visibility = View.GONE

        textTittle.setText(getString(R.string.my_orders))

        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(OrdersService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        myOrdersAdater.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        textProgress.visibility = View.VISIBLE

        var call =
            service.seeMyOrders("Bearer $token").enqueue(object :
                Callback<List<RetroTicket>> {
                override fun onResponse(
                    call: Call<List<RetroTicket>>,
                    response: Response<List<RetroTicket>>
                ) {
                    if (response.code() == 200) {

                        myOrdersAdater.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        textProgress.visibility = View.GONE
                        val retroFit2 = response.body()

                        // response.body()?.forEach{ retroTrade ->
                            // Log.d("ticketid", retroTrade.ticketid)
                            // Aqui você pode fazer alguma outra coisa com o ticketid, como por exemplo, remover a encomenda
                            // Log.d("statename", retroTrade.statename)
                        //}



                        if (retroFit2 != null)
                            if(retroFit2.isEmpty()){
                                ordersTextError.visibility = View.VISIBLE
                                myOrdersAdater.visibility = View.GONE
                            } else {
                                myOrdersAdater.visibility = View.VISIBLE
                                ordersTextError.visibility = View.GONE
                                orders.clear()
                                orders.addAll(retroFit2)

                                rebuildlistOrders(OrdersAdapterRec(progressBar, textProgress, linearLayoutManager, sp, myOrdersAdater, orders, requireActivity(), requireContext()))
                            }
                    }
                }

                override fun onFailure(calll: Call<List<RetroTicket>>, t: Throwable) {
                    myOrdersAdater.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    textProgress.visibility = View.GONE
                    Toast.makeText(requireContext(), "Erro! Tente novamente.", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    private fun myTrades(){
        ordersTextError.visibility = View.GONE
        myOrdersAdater.visibility = View.GONE

        textTittle.setText(getString(R.string.my_trades))

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
                            //Log.d("generaltradeid", generaltradeid)
                            //Log.d("isgeneraltrade", isgeneraltrade.toString())
                            println(generaltradeid)
                            println(isgeneraltrade)

                            // tenho que passar por parametro o generaltrade e o isgeneraltrade do ticket que vou selecionar
                        }

                        if (retroFit2 != null)
                            if(retroFit2.isEmpty()){
                                tradesTextError.visibility = View.VISIBLE
                                myTradesAdater.visibility = View.GONE
                            } else {
                                myTradesAdater.visibility = View.VISIBLE
                                tradesTextError.visibility = View.GONE
                                rebuildlistTrades(TradesAdapterRec(progressBar, textProgress, linearLayoutTradeManager, sp, myTradesAdater, retroFit2, requireActivity(), requireContext()))
                            }
                    }
                }

                override fun onFailure(calll: Call<List<RetroTrade>>, t: Throwable) {
                    myTradesAdater.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    textProgress.visibility = View.GONE
                    Toast.makeText(requireContext(), "Erro! Tente novamente.", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }
}


