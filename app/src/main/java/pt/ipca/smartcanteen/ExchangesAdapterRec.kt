package pt.ipca.smartcanteen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ExchangesAdapterRec(private var exchangesList: List<RetroTrade>) :
    RecyclerView.Adapter<ExchangesAdapterRecVIewHolder>() {

    var onItemClick : ((RetroTrade) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangesAdapterRecVIewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ExchangesAdapterRecVIewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: ExchangesAdapterRecVIewHolder, position: Int) {
        val nencomenda = exchangesList.get(position).nencomenda
        val ticketamount = exchangesList.get(position).ticketamount
        val total = exchangesList.get(position).total
        val statename = exchangesList.get(position).statename
        holder.bindData(nencomenda,ticketamount,total,statename)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(exchangesList[position])
        }
    }

    override fun getItemCount(): Int {
        return exchangesList.size
    }
}