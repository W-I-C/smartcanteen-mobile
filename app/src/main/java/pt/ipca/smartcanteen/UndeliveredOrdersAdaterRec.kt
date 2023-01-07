package pt.ipca.smartcanteen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class UndeliveredOrdersAdaterRec(private val undeliveredOrdersList: List<RetroTicket>) :
    RecyclerView.Adapter<UndeliveredOrdersAdapterRecViewOlder>() {

    var onItemClick : ((RetroTicket) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UndeliveredOrdersAdapterRecViewOlder {
        val inflater = LayoutInflater.from(parent.context)
        return UndeliveredOrdersAdapterRecViewOlder(inflater,parent)
    }

    override fun onBindViewHolder(holder: UndeliveredOrdersAdapterRecViewOlder, position: Int) {
        val nencomenda = undeliveredOrdersList.get(position).nencomenda
        val name = undeliveredOrdersList.get(position).name
        val statename = undeliveredOrdersList.get(position).statename
        holder.bindData(nencomenda,name,statename)

        //holder.itemView.setOnClickListener{
        //    onItemClick?.invoke(undeliveredOrder)
        //}
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(undeliveredOrdersList[position])
        }
    }

    override fun getItemCount(): Int {
        return undeliveredOrdersList.size
    }
}