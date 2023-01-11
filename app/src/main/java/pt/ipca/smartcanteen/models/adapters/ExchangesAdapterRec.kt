package pt.ipca.smartcanteen.models.adapters

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.adapters.viewHolders.ExchangesAdapterRecViewHolder
import pt.ipca.smartcanteen.models.RetroTrade

class ExchangesAdapterRec(val progressBar: ProgressBar, val textProgress: TextView, val linearLayoutManager: LinearLayoutManager, val sp: SharedPreferences, val myTradesAdater: RecyclerView, private var exchangesList: List<RetroTrade>) :
    RecyclerView.Adapter<ExchangesAdapterRecViewHolder>() {

    var onItemTradesClick : ((RetroTrade) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangesAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ExchangesAdapterRecViewHolder(progressBar, textProgress, linearLayoutManager, sp, myTradesAdater,inflater,parent)
    }

    override fun onBindViewHolder(holder: ExchangesAdapterRecViewHolder, position: Int) {
        val ticketid = exchangesList.get(position).ticketid
        val nencomenda = exchangesList.get(position).nencomenda
        val ticketamount = exchangesList.get(position).ticketamount
        val total = exchangesList.get(position).total
        val statename = exchangesList.get(position).statename
        holder.bindData(nencomenda,ticketamount,total,statename)

        holder.itemView.setOnClickListener {
            onItemTradesClick?.invoke(exchangesList[position])
        }

        holder.setDeleteClickListener(ticketid)
    }

    override fun getItemCount(): Int {
        return exchangesList.size
    }
}