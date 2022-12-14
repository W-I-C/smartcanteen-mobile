package pt.ipca.smartcanteen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class OrdersAdapterRec(private var ordersList: List<RetroTrade>) :
    RecyclerView.Adapter<OrdersAdapterRecViewHolder>() {

    var onItemClick : ((RetroTrade) -> Unit)? = null
    var onButtonTradeClick : ((View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return OrdersAdapterRecViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: OrdersAdapterRecViewHolder, position: Int) {
        val nencomenda = ordersList.get(position).nencomenda
        val ticketamount = ordersList.get(position).ticketamount
        val total = ordersList.get(position).total
        val statename = ordersList.get(position).statename
        holder.bindData(nencomenda,ticketamount,total,statename)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(ordersList[position])
        }

        holder.setOnClickListener {
            onButtonTradeClick?.invoke(it)
        }

        //holder.buttonTrade.setOnClickListener {
        //    onButtonTradeClick?.invoke(it)
        //}

        //fun setOnButtonTradeClick(onButtonTradeClick: ((View) -> Unit)?) {
        //    this.onButtonTradeClick = onButtonTradeClick
        //}
    }



    override fun getItemCount(): Int {
       return ordersList.size
    }
}