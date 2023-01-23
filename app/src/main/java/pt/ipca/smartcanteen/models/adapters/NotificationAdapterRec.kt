package pt.ipca.smartcanteen.models.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.adapters.viewHolders.NotificationAdapterRecViewHolder
import pt.ipca.smartcanteen.models.retrofit.response.RetroNotification
import pt.ipca.smartcanteen.models.retrofit.response.RetroTicket
import java.text.SimpleDateFormat

class NotificationAdapterRec(private val notification_list: List<RetroNotification>, private val activity: Activity) :
    RecyclerView.Adapter<NotificationAdapterRecViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NotificationAdapterRecViewHolder(inflater, parent, activity)
    }

    override fun onBindViewHolder(holder: NotificationAdapterRecViewHolder, position: Int) {
        val notifications = notification_list[position]
        val ticketid = notifications.ticketid
        val description = notifications.description
        val time = notifications.time
        val istradeproposal = notifications.istradeproposal
        val tradeid = notifications.tradeid
        val isfree = notifications.isfree
        val price = notifications.total

        fun getFormattedDate(): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val date = dateFormat.parse(time)
            val formattedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
            return formattedDate
        }

        holder.bindData(description, getFormattedDate(), istradeproposal,tradeid, isfree, price, ticketid)

    }

    override fun getItemCount(): Int {
        return notification_list.size
    }
}