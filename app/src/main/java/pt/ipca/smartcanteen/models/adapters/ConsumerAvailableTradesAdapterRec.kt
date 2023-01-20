package pt.ipca.smartcanteen.models.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.RetroTrade
import pt.ipca.smartcanteen.models.adapters.viewHolders.ConsumerAvailableTradesRecViewHolder
import pt.ipca.smartcanteen.models.adapters.viewHolders.TradesAdapterRecViewHolder
import pt.ipca.smartcanteen.models.helpers.AlertDialogManager
import pt.ipca.smartcanteen.views.activities.ConsumerOrderDetailsActivity

class ConsumerAvailableTradesAdapterRec(
    private var tradesList: List<RetroTrade>,
    private var context: Context
) :
    RecyclerView.Adapter<ConsumerAvailableTradesRecViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumerAvailableTradesRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ConsumerAvailableTradesRecViewHolder(
            inflater,
            context,
            parent
        )
    }

    override fun onBindViewHolder(holder: ConsumerAvailableTradesRecViewHolder, position: Int) {
        val trade = tradesList[position]
        val ticketid = trade.ticketid
        val nencomenda =trade.norder
        val total = "${trade.total}€"
        val statename = trade.statename
        val owner = trade.ownername
        val meals = trade.ticketmeals.take(5)
        holder.bindData(owner, total,statename,meals)


        holder.itemView.setOnClickListener {
            val intent = Intent(context, ConsumerOrderDetailsActivity::class.java).apply {
                putExtra("ticketid", trade.ticketid)
                putExtra("norder", trade.norder)
                putExtra("generaltradeid", trade.generaltradeid)
                putExtra("total", trade.total)
                putExtra("isMyOrder", false)
                putExtra("isFreeTrade", trade.isfree)
            }
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return tradesList.size
    }
}