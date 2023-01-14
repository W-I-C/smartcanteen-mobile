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
import pt.ipca.smartcanteen.models.adapters.viewHolders.TradesAdapterRecViewHolder
import pt.ipca.smartcanteen.models.RetroTrade
import pt.ipca.smartcanteen.views.activities.ConsumerOrderDetailsActivity

class TradesAdapterRec(val progressBar: ProgressBar, val textProgress: TextView, val linearLayoutManager: LinearLayoutManager, val sp: SharedPreferences, val myTradesAdapter: RecyclerView, private var tradesList: List<RetroTrade>, private val activity: Activity,  private var context: Context) :
    RecyclerView.Adapter<TradesAdapterRecViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradesAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TradesAdapterRecViewHolder(progressBar, textProgress, linearLayoutManager, sp, myTradesAdapter, inflater,parent, activity, context)
    }

    override fun onBindViewHolder(holder: TradesAdapterRecViewHolder, position: Int) {
        val ticketid = tradesList.get(position).ticketid
        val isgeneraltrade = tradesList.get(position).isgeneraltrade
        val generaltradeid = tradesList.get(position).generaltradeid
        val nencomenda = tradesList.get(position).norder
        val ticketamount = tradesList.get(position).ticketamount
        val total = tradesList.get(position).total
        val statename = tradesList.get(position).statename
        val receivername = tradesList.get(position).receivername
        holder.bindData(nencomenda,ticketamount,total,statename,isgeneraltrade,receivername)


        holder.itemView.setOnClickListener{
            var intent = Intent(activity, ConsumerOrderDetailsActivity::class.java)
            intent.putExtra("ticketid", ticketid)
            intent.putExtra("norder", nencomenda)
            intent.putExtra("total", total)
            activity.startActivity(intent)
        }

        holder.setDeleteClickListener(ticketid, isgeneraltrade, generaltradeid)
    }

    override fun getItemCount(): Int {
        return tradesList.size
    }
}