package pt.ipca.smartcanteen.models.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.RetroTicket
import pt.ipca.smartcanteen.models.adapters.viewHolders.OrdersAdapterRecViewHolder
import pt.ipca.smartcanteen.models.RetroTrade
import pt.ipca.smartcanteen.views.activities.ConsumerOrderDetailsActivity

class OrdersAdapterRec(val progressBar: ProgressBar, val textProgress: TextView, val linearLayoutManager: LinearLayoutManager, val sp: SharedPreferences, val myOrdersAdapter: RecyclerView, private var ordersList: List<RetroTicket>, private val activity: Activity, private val context: Context) :
    RecyclerView.Adapter<OrdersAdapterRecViewHolder>() {

    var onItemClick : ((RetroTicket) -> Unit)? = null
    var onButtonTradeClick : ((View) -> Unit)? = null
    // função que aceita um inteiro por entrada (posição da lista) e não tem retornbo - inicializado a null
    // var onDeleteButtonClick : ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return OrdersAdapterRecViewHolder(progressBar, textProgress, linearLayoutManager, sp, myOrdersAdapter, inflater, parent, activity, context)
    }

    override fun onBindViewHolder(holder: OrdersAdapterRecViewHolder, position: Int) {
        val ticketid = ordersList.get(position).ticketid
        val nencomenda = ordersList.get(position).norder
        val ticketamount = ordersList.get(position).ticketamount
        val total = ordersList.get(position).total
        val statename = ordersList.get(position).statename
        holder.bindData(nencomenda,ticketamount,total,statename)

        holder.itemView.setOnClickListener{
            var intent = Intent(activity, ConsumerOrderDetailsActivity::class.java)
            intent.putExtra("ticketid", ticketid)
            intent.putExtra("norder", nencomenda)
            intent.putExtra("total", total)
            activity.startActivity(intent)
        }

        holder.setTradeClickListener(ticketid)
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }
}