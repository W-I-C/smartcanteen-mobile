package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.views.activities.TradePaymentActivity

class NotificationAdapterRecViewHolder(inflater: LayoutInflater, val parent: ViewGroup, private val activity: Activity) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.card_notification, parent, false)) {
    val description = itemView.findViewById<TextView>(R.id.description_notification)
    val time = itemView.findViewById<TextView>(R.id.time_notification)
    val tradeButton = itemView.findViewById<Button>(R.id.trade_notification)


    fun bindData(descriptionText: String, timeText: String, istradeproposal: Boolean,tradeid: String?, isfree: Boolean?, price: Float?, ticketid: String?) {
        description.text = descriptionText
        time.text = timeText

        if(istradeproposal == true){
            tradeButton.visibility = View.VISIBLE
        } else {
            tradeButton.visibility = View.GONE
        }

        tradeButton.setOnClickListener{
            val intent = Intent(activity, TradePaymentActivity::class.java)
            intent.putExtra("ticketid", ticketid)
            intent.putExtra("istradeproposal", istradeproposal)
            intent.putExtra("directtradeid", tradeid)
            intent.putExtra("price", price)
            intent.putExtra("isfree", isfree)
            activity.startActivity(intent)
        }
    }
}