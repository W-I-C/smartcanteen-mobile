package pt.ipca.smartcanteen.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroOrder
import pt.ipca.smartcanteen.models.RetroTrade
import pt.ipca.smartcanteen.models.adapters.MenuOrdersAdapterRec
import pt.ipca.smartcanteen.models.adapters.OrderDetailsAdapterRec
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.OrdersService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class OrderDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        val ticketid = intent.getStringExtra("ticketid")
        if(ticketid != null)
            Log.d("ticketID: ",ticketid)
        val orderMealsRecyclerView = findViewById<RecyclerView>(R.id.order_details_rv)
        val orderMealsLinearLayoutManager = LinearLayoutManager(this@OrderDetailsActivity)
        getTicket(ticketid!!,orderMealsRecyclerView,orderMealsLinearLayoutManager)
    }


    private fun getTicket(
        ticketid:String,
        orderMealsRecyclerView:RecyclerView,
        orderMealsLinearLayoutManager:LayoutManager
    ){
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(OrdersService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@OrderDetailsActivity)
        val token = sp.getString("token", null)

        service.getTicketDetails(ticketid,"Bearer $token").enqueue(object :
            Callback<List<RetroOrder>> {
            override fun onResponse(
                call: Call<List<RetroOrder>>,
                response: Response<List<RetroOrder>>
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
                }
            }

            override fun onFailure(call: Call<List<RetroOrder>>, t: Throwable) {
                print("error")
            }

        })
    }
}