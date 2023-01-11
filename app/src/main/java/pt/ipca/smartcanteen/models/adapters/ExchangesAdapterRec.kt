package pt.ipca.smartcanteen.models.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.adapters.viewHolders.ExchangesAdapterRecViewHolder
import pt.ipca.smartcanteen.models.RetroTrade

class ExchangesAdapterRec(private var exchangesList: List<RetroTrade>) :
    RecyclerView.Adapter<ExchangesAdapterRecViewHolder>() {

    var onItemTradesClick : ((RetroTrade) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangesAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ExchangesAdapterRecViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: ExchangesAdapterRecViewHolder, position: Int) {
        val nencomenda = exchangesList.get(position).norder
        val ticketamount = exchangesList.get(position).ticketamount
        val total = exchangesList.get(position).total
        val statename = exchangesList.get(position).statename
        holder.bindData(nencomenda,ticketamount,total,statename)

        holder.itemView.setOnClickListener {
            onItemTradesClick?.invoke(exchangesList[position])
        }
    }

    override fun getItemCount(): Int {
        return exchangesList.size
    }
}