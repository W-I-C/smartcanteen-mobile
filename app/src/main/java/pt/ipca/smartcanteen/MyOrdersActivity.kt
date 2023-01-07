package pt.ipca.smartcanteen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
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

class MyOrdersActivity : AppCompatActivity() {
    private val buttonTrade: Button by lazy {findViewById<View>(R.id.my_orders_card_button_trade) as Button }
    private val textError: TextView by lazy {findViewById<TextView>(R.id.my_orders_empty_message) as TextView }
    private val myOrdersAdater: RecyclerView by lazy {findViewById<RecyclerView>(R.id.my_orders_recycler_view) as RecyclerView }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_orders)

        val BASE_URL = "https://smartcanteen-api.herokuapp.com"
        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(MyOrdersService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@MyOrdersActivity)
        val token = sp.getString("token", null)

        var call =
            service.seeMyOrders("Bearer $token").enqueue(object :
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
                                myOrdersAdater.visibility = View.GONE
                            } else {
                                myOrdersAdater.visibility = View.VISIBLE
                                textError.visibility = View.GONE
                                rebuildlist(OrdersAdapterRec(retroFit2))
                            }
                    }
                }

                override fun onFailure(calll: Call<List<RetroTrade>>, t: Throwable) {
                    print("error")
                }
            })
    }

    fun rebuildlist(adapter: OrdersAdapterRec) {
        val linearLayoutManager = LinearLayoutManager(this@MyOrdersActivity)
        myOrdersAdater.layoutManager = linearLayoutManager
        myOrdersAdater.itemAnimator = DefaultItemAnimator()
        myOrdersAdater.adapter = adapter

        adapter.onItemClick = this@MyOrdersActivity::onItemClick
    }

    fun onItemClick(order: RetroTrade) {
        val intent = Intent(this, DetailedMyOrderActivity::class.java).apply {
            putExtra("order_nencomenda", order.nencomenda)
            putExtra("order_ticketamount", order.ticketamount)
            putExtra("order_total", order.total)
            putExtra("order_statename", order.statename)
        }
        startActivity(intent)
    }

    fun doTrade(view: View) {
        var intent = Intent(this@MyOrdersActivity, ConsumerExchangeActivity::class.java)
        startActivity(intent)
    }
}

