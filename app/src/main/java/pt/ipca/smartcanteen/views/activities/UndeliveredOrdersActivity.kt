package pt.ipca.smartcanteen.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.adapters.UndeliveredOrdersAdaterRec
import pt.ipca.smartcanteen.models.RetroTicket
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.OrdersService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UndeliveredOrdersActivity : AppCompatActivity() {

    private val textError: TextView by lazy {findViewById<TextView>(R.id.undelivered_orders_empty_message) as TextView }
    private val undeliveredOrdersAdater: RecyclerView by lazy {findViewById<RecyclerView>(R.id.undelivered_orders_recycler_view) as RecyclerView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_undelivered_orders)

        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(OrdersService::class.java)

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


