package pt.ipca.smartcanteen.consumer_fragments

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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import pt.ipca.smartcanteen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyOrdersFragment : Fragment() {

    // TODO: passar de myexchange para my_exchange
    private val ordersTextError: TextView by lazy {requireView().findViewById<TextView>(R.id.my_orders_empty_message) as TextView }
    private val tradesTextError: TextView by lazy {requireView().findViewById<TextView>(R.id.my_exchanges_empty_message) as TextView }
    private val myOrdersAdater: RecyclerView by lazy {requireView().findViewById<RecyclerView>(R.id.my_orders_recycler_view) as RecyclerView }
    private val myTradesAdater: RecyclerView by lazy {requireView().findViewById<RecyclerView>(R.id.my_exchanges_recycler_view) as RecyclerView }
    private val buttonMyOrders: Button by lazy {requireView().findViewById<Button>(R.id.my_orders_button) as Button }
    private val buttonMyTrades: Button by lazy {requireView().findViewById<Button>(R.id.my_exchanges_button) as Button }
    private val textTittle: TextView by lazy {requireView().findViewById<TextView>(R.id.my_orders_title) as TextView }
    private val progressBar: ProgressBar by lazy {requireView().findViewById<ProgressBar>(R.id.my_orders_progress_bar) as ProgressBar }
    private val textProgress: TextView by lazy {requireView().findViewById<TextView>(R.id.my_orders_progress_bar_text) as TextView }
    private lateinit var loadingAlertDialog: AlertDialog
    //private val buttonTradeCard: Button by lazy {requireView().findViewById<Button>(R.id.my_orders_card_button_trade) as Button }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_orders, container, false)
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
        val linearLayoutManager = LinearLayoutManager(requireContext())
        myOrdersAdater.layoutManager = linearLayoutManager
        myOrdersAdater.itemAnimator = DefaultItemAnimator()
        myOrdersAdater.adapter = adapter

        adapter.onItemClick = this::onItemOrdersClick
        adapter.onButtonTradeClick = this::onButtonTradeClick
    }

    fun onItemOrdersClick(order: RetroTrade) {
        val intent = Intent(requireActivity(), DetailedMyOrderActivity::class.java).apply {
            putExtra("order_nencomenda", order.nencomenda)
            putExtra("order_ticketamount", order.ticketamount)
            putExtra("order_total", order.total)
            putExtra("order_statename", order.statename)
        }
        startActivity(intent)
    }

    fun onButtonTradeClick(view: View) {
        var intent = Intent(requireActivity(), ConsumerExchangeActivity::class.java)
        startActivity(intent)
    }

    fun doTrade(view: View) {
        var intent = Intent(requireActivity(), ConsumerExchangeActivity::class.java)
        startActivity(intent)
    }

    fun rebuildlistTrades(adapter: ExchangesAdapterRec) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        myTradesAdater.layoutManager = linearLayoutManager
        myTradesAdater.itemAnimator = DefaultItemAnimator()
        myTradesAdater.adapter = adapter

        adapter.onItemTradesClick = this::onItemTradesClick
    }

    fun onItemTradesClick(trade: RetroTrade) {
        val intent = Intent(requireActivity(), DetailedMyTradeActivity::class.java).apply {
            putExtra("trade_nencomenda", trade.nencomenda)
            putExtra("trade_ticketamount", trade.ticketamount)
            putExtra("trade_total", trade.total)
            putExtra("trade_statename", trade.statename)
        }
        startActivity(intent)
    }

    private fun myOrders(){
        tradesTextError.visibility = View.GONE
        myTradesAdater.visibility = View.GONE

        textTittle.setText("Minhas Encomendas")

        val BASE_URL = "https://smartcanteen-api.herokuapp.com"
        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(MyOrdersService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        progressBar.visibility = View.VISIBLE
        textProgress.visibility = View.VISIBLE

        var call =
            service.seeMyOrders("Bearer $token").enqueue(object :
                Callback<List<RetroTrade>> {
                override fun onResponse(
                    call: Call<List<RetroTrade>>,
                    response: Response<List<RetroTrade>>
                ) {
                    if (response.code() == 200) {
                        progressBar.visibility = View.GONE
                        textProgress.visibility = View.GONE
                        val retroFit2 = response.body()

                        if (retroFit2 != null)
                            if(retroFit2.isEmpty()){
                                ordersTextError.visibility = View.VISIBLE
                                myOrdersAdater.visibility = View.GONE
                            } else {
                                myOrdersAdater.visibility = View.VISIBLE
                                ordersTextError.visibility = View.GONE
                                rebuildlistOrders(OrdersAdapterRec(retroFit2))
                            }
                    }
                }

                override fun onFailure(calll: Call<List<RetroTrade>>, t: Throwable) {
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

        textTittle.setText("Minhas Trocas")

        val BASE_URL = "https://smartcanteen-api.herokuapp.com"
        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(MyTradesService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

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
                        progressBar.visibility = View.GONE
                        textProgress.visibility = View.GONE
                        val retroFit2 = response.body()

                        if (retroFit2 != null)
                            if(retroFit2.isEmpty()){
                                tradesTextError.visibility = View.VISIBLE
                                myTradesAdater.visibility = View.GONE
                            } else {
                                myTradesAdater.visibility = View.VISIBLE
                                tradesTextError.visibility = View.GONE
                                rebuildlistTrades(ExchangesAdapterRec(retroFit2))
                            }
                    }
                }

                override fun onFailure(calll: Call<List<RetroTrade>>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    textProgress.visibility = View.GONE
                    Toast.makeText(requireContext(), "Erro! Tente novamente.", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }
}


