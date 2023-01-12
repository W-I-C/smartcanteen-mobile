package pt.ipca.smartcanteen.models.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.RetroTicket
import pt.ipca.smartcanteen.models.adapters.viewHolders.NotificationAdapterRecViewHolder
import pt.ipca.smartcanteen.models.adapters.viewHolders.UndeliveredOrdersAdapterRecViewHolder
import pt.ipca.smartcanteen.models.helpers.RetroNotification

class NotificationAdapterRec (private val notification_list: List<RetroNotification>) :
    RecyclerView.Adapter<NotificationAdapterRecViewHolder>() {

    var onItemClick : ((RetroTicket) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NotificationAdapterRecViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: NotificationAdapterRecViewHolder, position: Int) {
        val description = notification_list.get(position).description

        holder.bindData(description)


    }

    override fun getItemCount(): Int {
        return notification_list.size
    }
}