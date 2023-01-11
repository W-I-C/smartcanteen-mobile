package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroTrade
import pt.ipca.smartcanteen.models.adapters.OrdersAdapterRec
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.services.MyOrdersService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OrdersAdapterRecViewHolder(val linearLayoutManager: LinearLayoutManager, val sp: SharedPreferences, val myOrdersAdapter: RecyclerView, inflater: LayoutInflater, val parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.my_order_card, parent, false)){
    val identifierTv = itemView.findViewById<TextView>(R.id.my_orders_card_identifier)
    val quantityTv = itemView.findViewById<TextView>(R.id.my_orders_card_quantity)
    val priceTv = itemView.findViewById<TextView>(R.id.my_orders_card_price)
    val stateTv = itemView.findViewById<TextView>(R.id.my_orders_card_state)

    val buttonTrade = itemView.findViewById<Button>(R.id.my_orders_card_button_trade)
    val deleteButton = itemView.findViewById<Button>(R.id.my_orders_card_delete)

    fun setOnClickListener(listener: (View) -> Unit) {
        buttonTrade.setOnClickListener(listener)
    }

    fun setDeleteClickListener(ticketid: String){
        deleteButton.setOnClickListener{

            val BASE_URL = "https://smartcanteen-api.herokuapp.com"
            var retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(MyOrdersService::class.java)

            val token = sp.getString("token", null)

            service.removeTicket(ticketid,"Bearer $token").enqueue(object :
                Callback<List<RetroTrade>> {
                override fun onResponse(
                    call: Call<List<RetroTrade>>,
                    response: Response<List<RetroTrade>>
                ) {
                    if (response.code() == 200) {
                        val retroFit2 = response.body()
                        println("Aqui")
                        if (retroFit2 != null)
                            if(retroFit2.isEmpty()){
                            } else {
                                rebuildlistOrders(OrdersAdapterRec(linearLayoutManager, sp, myOrdersAdapter, retroFit2))
                            }
                    }
                }

                override fun onFailure(calll: Call<List<RetroTrade>>, t: Throwable) {
                    println("Erro")
                }
            })
        }
    }

    fun rebuildlistOrders(adapter: OrdersAdapterRec) {
        myOrdersAdapter.layoutManager = linearLayoutManager
        myOrdersAdapter.itemAnimator = DefaultItemAnimator()
        myOrdersAdapter.adapter = adapter
    }

    fun bindData(identifierText: Int, quantityText: Int, priceText: Float, stateText: String){
        identifierTv.text= identifierText.toString()
        quantityTv.text = quantityText.toString()
        priceTv.text= priceText.toString()
        stateTv.text=stateText
    }
}