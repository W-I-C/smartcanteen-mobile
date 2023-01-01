package pt.ipca.smartcanteen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class UndeliveredOrdersAdaterRec(private var undeliveredOrdersList: MutableList<UndeliveredOrder>) :
    RecyclerView.Adapter<UndeliveredOrdersAdapterRecViewOlder>() {

    var onItemClick : ((UndeliveredOrder) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UndeliveredOrdersAdapterRecViewOlder {
        val inflater = LayoutInflater.from(parent.context)
        return UndeliveredOrdersAdapterRecViewOlder(inflater,parent)
    }

    override fun onBindViewHolder(holder: UndeliveredOrdersAdapterRecViewOlder, position: Int) {
        val undeliveredOrder = undeliveredOrdersList.get(position)
        val identifier = "${undeliveredOrder.identifier}"
        val name = undeliveredOrder.name
        val state = undeliveredOrder.state
        holder.bindData(identifier,name,state)

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(undeliveredOrder)
        }
    }

    override fun getItemCount(): Int {
        return undeliveredOrdersList.size
    }
}