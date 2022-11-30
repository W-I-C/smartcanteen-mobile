package pt.ipca.smartcanteen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class OrdersAdapterRec(private var ordersList: MutableList<Order>) :
    RecyclerView.Adapter<OrdersAdapterRecViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return OrdersAdapterRecViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: OrdersAdapterRecViewHolder, position: Int) {
        val order = ordersList.get(position)
        val name = order.name
        val quantity = "${order.quantity}"
        val price = "${order.price}€"
        val state = order.state
        holder.bindData(name,quantity,price,state)
    }

    override fun getItemCount(): Int {
       return ordersList.size
    }
}