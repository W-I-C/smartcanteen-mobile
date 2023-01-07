package pt.ipca.smartcanteen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyExchangesActivity : AppCompatActivity() {

    private val textError: TextView by lazy {findViewById<TextView>(R.id.my_exchanges_empty_message) as TextView }
    private val myExchangesAdater: RecyclerView by lazy {findViewById<RecyclerView>(R.id.my_exchanges_recycler_view) as RecyclerView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_exchanges)

        // TODO: adicionar isto a uma função
        val BASE_URL = "https://smartcanteen-api.herokuapp.com"
        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(MyTradesService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@MyExchangesActivity)
        val token = sp.getString("token", null)

        var call =
            service.seeMyTrades("Bearer $token").enqueue(object :
                Callback<List<RetroTrade>> {
                override fun onResponse(
                    call: Call<List<RetroTrade>>,
                    response: Response<List<RetroTrade>>
                ) {
                    if (response.code() == 200) {
                        val retroFit2 = response.body()

                        if (retroFit2 != null)
                            if(retroFit2.isEmpty()){
                                textError.visibility = View.VISIBLE
                                myExchangesAdater.visibility = View.GONE
                            } else {
                                myExchangesAdater.visibility = View.VISIBLE
                                textError.visibility = View.GONE
                                rebuildlist(ExchangesAdapterRec(retroFit2))
                            }
                    }
                }

                override fun onFailure(calll: Call<List<RetroTrade>>, t: Throwable) {
                    print("error")
                }
            })
    }

    fun rebuildlist(adapter: ExchangesAdapterRec) {
        val linearLayoutManager = LinearLayoutManager(this@MyExchangesActivity)
        myExchangesAdater.layoutManager = linearLayoutManager
        myExchangesAdater.itemAnimator = DefaultItemAnimator()
        myExchangesAdater.adapter = adapter

        adapter.onItemClick = this@MyExchangesActivity::onItemClick
    }

    fun onItemClick(trade: RetroTrade) {
        val intent = Intent(this, DetailedMyTradeActivity::class.java).apply {
            putExtra("trade_nencomenda", trade.nencomenda)
            putExtra("trade_ticketamount", trade.ticketamount)
            putExtra("trade_total", trade.total)
            putExtra("trade_statename", trade.statename)
        }
        startActivity(intent)
    }
}

