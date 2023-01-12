package pt.ipca.smartcanteen.models.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.RetroTicket
import pt.ipca.smartcanteen.models.adapters.viewHolders.NotificationAdapterRecViewHolder
import pt.ipca.smartcanteen.models.adapters.viewHolders.UndeliveredOrdersAdapterRecViewHolder
import pt.ipca.smartcanteen.models.helpers.RetroNotification
import java.text.SimpleDateFormat

class NotificationAdapterRec (private val notification_list: List<RetroNotification>) :
    RecyclerView.Adapter<NotificationAdapterRecViewHolder>() {

    var onItemClick : ((RetroTicket) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NotificationAdapterRecViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: NotificationAdapterRecViewHolder, position: Int) {
        val description = notification_list.get(position).description
        val time=notification_list.get(position).time

        fun getFormattedDate(): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val date = dateFormat.parse(time)
            val formattedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
            return formattedDate
        }

        holder.bindData(description, getFormattedDate())

    }

    override fun getItemCount(): Int {
        return notification_list.size
    }
}