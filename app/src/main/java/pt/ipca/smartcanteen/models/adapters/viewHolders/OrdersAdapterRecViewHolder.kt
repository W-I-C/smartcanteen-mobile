package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroTicket
import pt.ipca.smartcanteen.models.RetroTrade
import pt.ipca.smartcanteen.models.adapters.OrdersAdapterRec
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.OrdersService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersAdapterRecViewHolder(val progressBar: ProgressBar, val textProgress: TextView, val linearLayoutManager: LinearLayoutManager, val sp: SharedPreferences, val myOrdersAdapter: RecyclerView, inflater: LayoutInflater, val parent: ViewGroup):
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

            val retrofit = SmartCanteenRequests().retrofit

            val service = retrofit.create(OrdersService::class.java)

            val token = sp.getString("token", null)

            myOrdersAdapter.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            textProgress.visibility = View.VISIBLE

            service.removeTicket(ticketid,"Bearer $token").enqueue(object :
                Callback<List<RetroTicket>> {
                override fun onResponse(
                    call: Call<List<RetroTicket>>,
                    response: Response<List<RetroTicket>>
                ) {
                    if (response.code() == 200) {

                        myOrdersAdapter.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        textProgress.visibility = View.GONE

                        val retroFit2 = response.body()
                        println("Aqui")
                        if (retroFit2 != null)
                            if(!retroFit2.isEmpty()){
                                rebuildlistOrders(OrdersAdapterRec(progressBar, textProgress, linearLayoutManager, sp, myOrdersAdapter, retroFit2))
                                // rebuildlistOrders(OrdersAdapterRec(retroFit2))
                            }
                    }
                }

                override fun onFailure(calll: Call<List<RetroTicket>>, t: Throwable) {
                    myOrdersAdapter.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    textProgress.visibility = View.GONE
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

        if(stateText == "Não Iniciado" || stateText == "Atraso"){
            stateTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.redLogout))
        } else if(stateText == "Pronto" || stateText == "Entregue"){
            stateTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.background_color))
        } else if(stateText == "Preparação") {
            stateTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.orange))
        }

        if(stateText == "Entregue" || stateText == "Não Iniciado"){
            deleteButton.visibility = Button.VISIBLE
        } else {
            deleteButton.visibility = Button.GONE
        }
    }
}