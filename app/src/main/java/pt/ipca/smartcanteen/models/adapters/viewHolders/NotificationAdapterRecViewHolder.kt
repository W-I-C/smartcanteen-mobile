package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R

class NotificationAdapterRecViewHolder (inflater: LayoutInflater, val parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.card_notification, parent, false)) {
    val description = itemView.findViewById<TextView>(R.id.description)


    fun bindData(descriptionText: String) {
        description.text = descriptionText

    }
}