package pt.ipca.smartcanteen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
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

class UndeliveredOrdersActivity : AppCompatActivity() {

    private val textError: TextView by lazy {findViewById<TextView>(R.id.undelivered_orders_empty_message) as TextView }
    private val undeliveredOrdersAdater: RecyclerView by lazy {findViewById<RecyclerView>(R.id.undelivered_orders_recycler_view) as RecyclerView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.undelivered_orders)

        val BASE_URL = "https://smartcanteen-api.herokuapp.com"
        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(UndeliveredOrdersService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@UndeliveredOrdersActivity)
        val token = sp.getString("token", null)

        var call =
            service.seeUndeliveredOrders("Bearer $token").enqueue(object : Callback<List<RetroTicket>> {
                override fun onResponse(
                    call: Call<List<RetroTicket>>,
                    response: Response<List<RetroTicket>>
                ) {
                    if (response.code() == 200) {
                        val retroFit2 = response.body()

                        if (retroFit2 != null)
                            if(retroFit2.isEmpty()){
                                textError.visibility = View.VISIBLE
                                undeliveredOrdersAdater.visibility = View.GONE
                            } else {
                                undeliveredOrdersAdater.visibility = View.VISIBLE
                                textError.visibility = View.GONE
                                rebuildlist(UndeliveredOrdersAdaterRec(retroFit2))
                            }
                    }
                }

                override fun onFailure(calll: Call<List<RetroTicket>>, t: Throwable) {
                    print("error")
                }
            })
    }

    fun rebuildlist(adapter: UndeliveredOrdersAdaterRec) {
        val linearLayoutManager = LinearLayoutManager(this@UndeliveredOrdersActivity)
        undeliveredOrdersAdater.layoutManager = linearLayoutManager
        undeliveredOrdersAdater.itemAnimator = DefaultItemAnimator()
        undeliveredOrdersAdater.adapter = adapter

        adapter.onItemClick = this@UndeliveredOrdersActivity::onItemClick
        //UndeliveredOrdersAdaterRec(retrofit2).onItemClick = { order ->
        //    val intent = Intent(this, DetailedActivity::class.java).apply {
        //        putExtra("order_nencomenda", order.nencomenda)
        //        putExtra("order_name", order.name)
        //        putExtra("order_statename", order.statename)
        //    }
        //    startActivity(intent)
    }

    fun onItemClick(order: RetroTicket) {
        val intent = Intent(this, DetailedActivity::class.java).apply {
            putExtra("order_nencomenda", order.nencomenda)
            putExtra("order_name", order.name)
            putExtra("order_statename", order.statename)
        }
        startActivity(intent)
    }
}


