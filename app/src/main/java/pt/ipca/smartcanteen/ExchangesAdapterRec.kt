package pt.ipca.smartcanteen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ExchangesAdapterRec(private var exchangesList: MutableList<Exchange>) :
    RecyclerView.Adapter<ExchangesAdapterRecVIewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangesAdapterRecVIewHolder {
        val inflater = LayoutInflater.from(parent.context)
            return ExchangesAdapterRecVIewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: ExchangesAdapterRecVIewHolder, position: Int) {
        val exchange = exchangesList.get(position)
        val name = exchange.name
        val quantity = "${exchange.quantity} ${if(exchange.quantity > 1) "doses" else "dose"}"
        val price = "${exchange.price}€"
        val state = exchange.state
        holder.bindData(name,quantity,price,state)
    }

    override fun getItemCount(): Int {
        return exchangesList.size
    }
}