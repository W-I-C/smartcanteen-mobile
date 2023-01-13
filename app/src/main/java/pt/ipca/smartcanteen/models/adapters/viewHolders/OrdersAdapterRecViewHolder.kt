package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroTrade
import pt.ipca.smartcanteen.models.adapters.OrdersAdapterRec
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.OrdersService
import pt.ipca.smartcanteen.views.activities.ConsumerTradeActivity
import pt.ipca.smartcanteen.views.activities.TradePaymentActivity
import pt.ipca.smartcanteen.views.fragments.employee_fragments.EmployeeFragmentActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersAdapterRecViewHolder(val progressBar: ProgressBar, val textProgress: TextView, val linearLayoutManager: LinearLayoutManager, val sp: SharedPreferences, val myOrdersAdapter: RecyclerView, inflater: LayoutInflater, val parent: ViewGroup, private val activity: Activity, private val context: Context):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.my_order_card, parent, false)){
    val identifierTv = itemView.findViewById<TextView>(R.id.my_orders_card_identifier)
    val quantityTv = itemView.findViewById<TextView>(R.id.my_orders_card_quantity)
    val priceTv = itemView.findViewById<TextView>(R.id.my_orders_card_price)
    val stateTv = itemView.findViewById<TextView>(R.id.my_orders_card_state)

    val tradeButton = itemView.findViewById<Button>(R.id.my_orders_card_button_trade)
    val deleteButton = itemView.findViewById<Button>(R.id.my_orders_card_delete)

    fun setDeleteClickListener(ticketid: String){
        deleteButton.setOnClickListener{

            val retrofit = SmartCanteenRequests().retrofit

            val service = retrofit.create(OrdersService::class.java)

            val token = sp.getString("token", null)

            myOrdersAdapter.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            textProgress.visibility = View.VISIBLE

            service.removeTicket(ticketid,"Bearer $token").enqueue(object :
                Callback<List<RetroTrade>> {
                override fun onResponse(
                    call: Call<List<RetroTrade>>,
                    response: Response<List<RetroTrade>>
                ) {
                    if (response.code() == 200) {

                        Toast.makeText(context, "Encomenda removida com sucesso", Toast.LENGTH_SHORT).show()

                        myOrdersAdapter.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        textProgress.visibility = View.GONE

                        val retroFit2 = response.body()
                        if (retroFit2 != null)
                            if(!retroFit2.isEmpty()){
                                rebuildlistOrders(OrdersAdapterRec(progressBar, textProgress, linearLayoutManager, sp, myOrdersAdapter, retroFit2, activity, context))
                                // rebuildlistOrders(OrdersAdapterRec(retroFit2))
                            }
                    }
                }

                override fun onFailure(calll: Call<List<RetroTrade>>, t: Throwable) {
                    myOrdersAdapter.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    textProgress.visibility = View.GONE
                    Toast.makeText(context, "Erro! Tente novamente.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun rebuildlistOrders(adapter: OrdersAdapterRec) {
        myOrdersAdapter.layoutManager = linearLayoutManager
        myOrdersAdapter.itemAnimator = DefaultItemAnimator()
        myOrdersAdapter.adapter = adapter
    }

    fun setTradeClickListener(ticketId: String){
        tradeButton.setOnClickListener{
            var intent = Intent(activity, ConsumerTradeActivity::class.java)
            // println(ticketId)
            intent.putExtra("ticketId", ticketId)
            activity.startActivity(intent)
        }
    }

    fun setDetailClickListener(ticketId: String, nencomenda: Int, total: Float){
        var intent = Intent(activity, ConsumerTradeActivity::class.java)
        intent.putExtra("ticketid", ticketId)
        intent.putExtra("norder", nencomenda)
        intent.putExtra("total", total)
        activity.startActivity(intent)
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

        if(stateText == "Entregue"){
            tradeButton.visibility = Button.GONE
        } else {
            tradeButton.visibility = Button.VISIBLE
        }
    }
}