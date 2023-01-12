package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroTrade
import pt.ipca.smartcanteen.models.adapters.TradesAdapterRec
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.TradesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TradesAdapterRecViewHolder(val progressBar: ProgressBar, val textProgress: TextView, val linearLayoutManager: LinearLayoutManager, val sp: SharedPreferences, val myTradesAdapter: RecyclerView, inflater: LayoutInflater, val parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.my_trade_card, parent, false)){
    val identifierTv = itemView.findViewById<TextView>(R.id.my_exchanges_card_identifier)
    val quantityTv = itemView.findViewById<TextView>(R.id.my_exchanges_card_quantity)
    val priceTv = itemView.findViewById<TextView>(R.id.my_exchanges_card_price)
    val stateTv = itemView.findViewById<TextView>(R.id.my_exchanges_card_state)

    val deleteButton = itemView.findViewById<Button>(R.id.my_exchanges_card_delete)

    fun setDeleteClickListener(ticketid: String){
        deleteButton.setOnClickListener{

            val retrofit = SmartCanteenRequests().retrofit

            val service = retrofit.create(TradesService::class.java)

            val token = sp.getString("token", null)

            myTradesAdapter.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            textProgress.visibility = View.VISIBLE

            service.removeTrade(ticketid,"Bearer $token").enqueue(object :
                Callback<List<RetroTrade>> {
                override fun onResponse(
                    call: Call<List<RetroTrade>>,
                    response: Response<List<RetroTrade>>
                ) {
                    if (response.code() == 200) {

                        myTradesAdapter.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        textProgress.visibility = View.GONE

                        val retroFit2 = response.body()
                        println("Aqui")
                        if (retroFit2 != null)
                            if(!retroFit2.isEmpty()){
                                rebuildlistOrders(TradesAdapterRec(progressBar, textProgress, linearLayoutManager, sp, myTradesAdapter, retroFit2))
                            }
                    }
                }

                override fun onFailure(calll: Call<List<RetroTrade>>, t: Throwable) {
                    myTradesAdapter.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    textProgress.visibility = View.GONE
                    println("Erro")
                }
            })
        }
    }

    fun rebuildlistOrders(adapter: TradesAdapterRec) {
        myTradesAdapter.layoutManager = linearLayoutManager
        myTradesAdapter.itemAnimator = DefaultItemAnimator()
        myTradesAdapter.adapter = adapter
    }

    fun bindData(identifierText: Int, quantityText: Int, priceText:Float, stateText: String){
        identifierTv.text = identifierText.toString()
        quantityTv.text = quantityText.toString()
        priceTv.text=priceText.toString()
        stateTv.text=stateText

        if(stateText == "Entregue" || stateText == "Não Iniciado"){
            deleteButton.visibility = Button.VISIBLE
        } else {
            deleteButton.visibility = Button.GONE
        }
    }
}