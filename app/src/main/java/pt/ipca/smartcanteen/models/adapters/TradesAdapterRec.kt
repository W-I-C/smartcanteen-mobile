package pt.ipca.smartcanteen.models.adapters

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.adapters.viewHolders.TradesAdapterRecViewHolder
import pt.ipca.smartcanteen.models.RetroTrade

class TradesAdapterRec(val progressBar: ProgressBar, val textProgress: TextView, val linearLayoutManager: LinearLayoutManager, val sp: SharedPreferences, val myTradesAdapter: RecyclerView, private var tradesList: List<RetroTrade>) :
    RecyclerView.Adapter<TradesAdapterRecViewHolder>() {

    var onItemTradesClick : ((RetroTrade) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradesAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TradesAdapterRecViewHolder(progressBar, textProgress, linearLayoutManager, sp, myTradesAdapter, inflater,parent)
    }

    override fun onBindViewHolder(holder: TradesAdapterRecViewHolder, position: Int) {
        val ticketid = tradesList.get(position).ticketid
        val nencomenda = tradesList.get(position).norder
        val ticketamount = tradesList.get(position).ticketamount
        val total = tradesList.get(position).total
        val statename = tradesList.get(position).statename
        //Log.d("statename", statename)
        holder.bindData(nencomenda,ticketamount,total,statename)

        holder.itemView.setOnClickListener {
            onItemTradesClick?.invoke(tradesList[position])
        }

        holder.setDeleteClickListener(ticketid)
    }

    override fun getItemCount(): Int {
        return tradesList.size
    }
}