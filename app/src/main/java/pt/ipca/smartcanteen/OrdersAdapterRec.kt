package pt.ipca.smartcanteen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class OrdersAdapterRec(private var ordersList: MutableList<Order>) :
    RecyclerView.Adapter<OrdersAdapterRecViewHolder>() {

    var onItemClick : ((Order) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return OrdersAdapterRecViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: OrdersAdapterRecViewHolder, position: Int) {
        val order = ordersList.get(position)
        val name = order.name
        val quantity = "${order.quantity} ${if(order.quantity > 1) "doses" else "dose"}"
        val price = "${order.price}€"
        val state = order.state
        holder.bindData(name,quantity,price,state)

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(order)
        }
    }

    override fun getItemCount(): Int {
       return ordersList.size
    }
}