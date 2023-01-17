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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroTrade
import pt.ipca.smartcanteen.models.adapters.OrdersAdapterRec
import pt.ipca.smartcanteen.models.adapters.TradesAdapterRec
import pt.ipca.smartcanteen.models.helpers.AlertDialogManager
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.TradesService
import pt.ipca.smartcanteen.views.activities.ConsumerOrderDetailsActivity
import pt.ipca.smartcanteen.views.activities.ConsumerTradeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TradesAdapterRecViewHolder(val progressBar: ProgressBar, val textProgress: TextView, val linearLayoutManager: LinearLayoutManager, val sp: SharedPreferences, val myTradesAdapter: RecyclerView, inflater: LayoutInflater,
                                 val parent: ViewGroup, private val activity: Activity, private var context: Context, val alertDialogManager: AlertDialogManager):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.my_trade_card, parent, false)){
    val identifierTv = itemView.findViewById<TextView>(R.id.my_trades_card_identifier)
    val quantityTv = itemView.findViewById<TextView>(R.id.my_trades_card_quantity)
    val priceTv = itemView.findViewById<TextView>(R.id.my_trades_card_price)
    val stateTv = itemView.findViewById<TextView>(R.id.my_trades_card_state)
    val isGeneralTradeTv = itemView.findViewById<TextView>(R.id.my_trades_card_isgeneraltrade)
    val receiverNameTv = itemView.findViewById<TextView>(R.id.my_trades_card_receiver_name)
    val deleteButton = itemView.findViewById<Button>(R.id.my_trades_card_delete)

    fun setDeleteClickListener(ticketid: String, isgeneraltrade: Boolean, generaltradeid: String?,removeTradeAskString:String, alertDialogManager: AlertDialogManager){
        deleteButton.setOnClickListener{
            alertDialogManager.createConfirmAlertDialog(
                removeTradeAskString,
                { remove(ticketid,isgeneraltrade,generaltradeid,removeTradeAskString,alertDialogManager) }
            )
        }
    }

    fun remove(ticketid: String, isgeneraltrade: Boolean, generaltradeid: String?,removeTradeAskString:String, alertDialogManager: AlertDialogManager){
        val retrofit = SmartCanteenRequests().retrofit
        val service = retrofit.create(TradesService::class.java)

        // se não for uma troca geral é uma troca direta e chamamos a rota de remover trocas diretas, senão chamaos a rota de remover trocas gerais
        if(isgeneraltrade == false){

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

                        Toast.makeText(context, "Troca direta removida com sucesso", Toast.LENGTH_SHORT).show()

                        myTradesAdapter.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        textProgress.visibility = View.GONE

                        val retroFit2 = response.body()

                        if (retroFit2 != null)
                            if(!retroFit2.isEmpty()){
                                rebuildlistOrders(TradesAdapterRec(progressBar, textProgress, linearLayoutManager, sp, myTradesAdapter, retroFit2, activity, context, removeTradeAskString, alertDialogManager))
                            }
                    } else if(response.code() == 500){
                        myTradesAdapter.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        textProgress.visibility = View.GONE
                        Toast.makeText(context, "Erro! Não foi possível remover a troca direta.", Toast.LENGTH_LONG)
                            .show()
                    } else if(response.code()==401){
                        AuthHelper().newSessionToken(activity)
                        setDeleteClickListener(ticketid, isgeneraltrade, generaltradeid,removeTradeAskString,alertDialogManager)
                    }
                }

                override fun onFailure(calll: Call<List<RetroTrade>>, t: Throwable) {
                    myTradesAdapter.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    textProgress.visibility = View.GONE
                }
            })
        } else {
            if(generaltradeid != null){
                val token = sp.getString("token", null)

                myTradesAdapter.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                textProgress.visibility = View.VISIBLE

                service.removeGeneralTrade(generaltradeid,"Bearer $token").enqueue(object :
                    Callback<List<RetroTrade>> {
                    override fun onResponse(
                        call: Call<List<RetroTrade>>,
                        response: Response<List<RetroTrade>>
                    ) {
                        if (response.code() == 200) {

                            Toast.makeText(context, "Troca geral removida com sucesso", Toast.LENGTH_SHORT).show()

                            myTradesAdapter.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            textProgress.visibility = View.GONE

                            val retroFit2 = response.body()

                            if (retroFit2 != null)
                                if(!retroFit2.isEmpty()){
                                    println("Aqui123")
                                    rebuildlistOrders(TradesAdapterRec(progressBar, textProgress, linearLayoutManager, sp, myTradesAdapter, retroFit2, activity, context, removeTradeAskString, alertDialogManager))
                                }
                        } else if(response.code() == 500){
                            myTradesAdapter.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            textProgress.visibility = View.GONE
                            Toast.makeText(context, "Erro! Não foi possível remover a troca geral.", Toast.LENGTH_LONG)
                                .show()
                        } else if(response.code()==401){
                            AuthHelper().newSessionToken(activity)
                            setDeleteClickListener(ticketid, isgeneraltrade, generaltradeid, removeTradeAskString, alertDialogManager)
                        }
                    }

                    override fun onFailure(calll: Call<List<RetroTrade>>, t: Throwable) {
                        myTradesAdapter.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        textProgress.visibility = View.GONE
                    }
                })
            }
        }
    }

    fun rebuildlistOrders(adapter: TradesAdapterRec) {
        myTradesAdapter.layoutManager = linearLayoutManager
        myTradesAdapter.itemAnimator = DefaultItemAnimator()
        myTradesAdapter.adapter = adapter
    }

    fun bindData(identifierText: Int, quantityText: Int, priceText:Float, stateText: String, isgeneraltrade: Boolean, receiverNameText: String?){
        identifierTv.text = identifierText.toString()
        quantityTv.text = quantityText.toString()
        priceTv.text = priceText.toString()
        stateTv.text = stateText


        if(isgeneraltrade == true){
            isGeneralTradeTv.text = "General Trade"
        } else {
            isGeneralTradeTv.text = "Direct Trade"
            if(receiverNameText != null) {
                receiverNameTv.text = receiverNameText
            }
        }

        if(stateText == "Não Iniciado" || stateText == "Atraso"){
            stateTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.redLogout))
        } else if(stateText == "Pronto" || stateText == "Entregue"){
            stateTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.background_color))
        } else if(stateText == "Em Preparação") {
            stateTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.orange))
        }

        if(stateText == "Entregue" || stateText == "Não Iniciado"){
            deleteButton.visibility = Button.VISIBLE
        } else {
            deleteButton.visibility = Button.GONE
        }

        if(isGeneralTradeTv.text == "General Trade"){
            isGeneralTradeTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.redLogout))
        } else if(isGeneralTradeTv.text == "Direct Trade") {
            isGeneralTradeTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.orange))
        }


        if(receiverNameText == null && isgeneraltrade == true){
            deleteButton.visibility = View.VISIBLE
        } else {
            deleteButton.visibility = View.GONE
            stateTv.text = "Trocado"
            isGeneralTradeTv.text = receiverNameText
            stateTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.background_color))
            isGeneralTradeTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.background_color))
        }
    }
}