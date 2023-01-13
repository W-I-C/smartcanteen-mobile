package pt.ipca.smartcanteen.models.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.adapters.viewHolders.UndeliveredOrdersAdapterRecViewHolder
import pt.ipca.smartcanteen.models.RetroTicket
import pt.ipca.smartcanteen.models.RetroTrade
import pt.ipca.smartcanteen.views.activities.EmployeeTicketDetails
import pt.ipca.smartcanteen.views.activities.NotificationActivity

class UndeliveredOrdersAdaterRec(private val activity:Activity,private val undeliveredOrdersList: List<RetroTicket>) :
    RecyclerView.Adapter<UndeliveredOrdersAdapterRecViewHolder>() {

    var onItemClick : ((RetroTicket) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UndeliveredOrdersAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UndeliveredOrdersAdapterRecViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: UndeliveredOrdersAdapterRecViewHolder, position: Int) {
        val nencomenda = undeliveredOrdersList.get(position).norder
        val name = undeliveredOrdersList.get(position).ownername
        val statename = undeliveredOrdersList.get(position).statename
        holder.bindData(nencomenda,name,statename)

        holder.itemView.setOnClickListener {
            val intent = Intent(activity, EmployeeTicketDetails::class.java)
            intent.putExtra("ticketid", undeliveredOrdersList.get(position).ticketid)
            intent.putExtra("norder", undeliveredOrdersList.get(position).norder)
            intent.putExtra("statename", statename)
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return undeliveredOrdersList.size
    }
}