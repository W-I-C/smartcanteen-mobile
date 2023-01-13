package pt.ipca.smartcanteen.models.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroTrade
import pt.ipca.smartcanteen.models.adapters.viewHolders.TradeMealsAdapterRecViewHolder
import pt.ipca.smartcanteen.views.activities.ConsumerOrderDetailsActivity

class TradeMealsAdapterRec(private val activity:Activity,private val orderString:String, private val freeString: String, private var tradesList: List<RetroTrade>) :
    RecyclerView.Adapter<TradeMealsAdapterRecViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TradeMealsAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TradeMealsAdapterRecViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: TradeMealsAdapterRecViewHolder, position: Int) {
        val trade = tradesList.get(position)
        val title = trade.ownername
        val price = if (trade.isfree) freeString else "${trade.total}€"
        val status = trade.statename
        val norder = "${orderString}: ${trade.norder}"

        holder.itemView.setOnClickListener{
            Log.d("isFree",trade.isfree.toString())
            val intent = Intent(activity, ConsumerOrderDetailsActivity::class.java).apply {
                putExtra("ticketid",trade.ticketid)
                putExtra("norder",trade.norder)
                putExtra("generaltradeid",trade.generaltradeid)
                putExtra("total",trade.total)
                putExtra("isMyOrder",false)
                putExtra("isFreeTrade",trade.isfree)
            }
            activity.startActivity(intent)
        }
        holder.bindData(title, price, status, norder)
    }

    override fun getItemCount(): Int {
        return tradesList.size
    }
}