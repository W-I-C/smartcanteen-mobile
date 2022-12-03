
package pt.ipca.smartcanteen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MyOrdersCartRec(private var ordersList: MutableList<MyOrderCart>) :
    RecyclerView.Adapter<MyOrdersCartRecViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersCartRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyOrdersCartRecViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: MyOrdersCartRecViewHolder, position: Int) {
        val order = ordersList.get(position)
        val name = order.name
        val quantity = "${order.quantity} ${if(order.quantity > 1) "doses" else "dose"}"
        val price = "${order.price}€"
        holder.bindData(name,quantity,price)
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }
}