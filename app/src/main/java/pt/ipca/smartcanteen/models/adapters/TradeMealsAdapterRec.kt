package pt.ipca.smartcanteen.models.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroTrade
import pt.ipca.smartcanteen.models.adapters.viewHolders.TradeMealsAdapterRecViewHolder

class TradeMealsAdapterRec(private val orderString:String, private val freeString: String, private var tradesList: List<RetroTrade>) :
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
        val price = if (trade.isfree) "${trade.total}€" else freeString
        val status = trade.statename
        val norder = "${orderString}: ${trade.norder}"
        holder.bindData(title, price, status, norder)
    }

    override fun getItemCount(): Int {
        return tradesList.size
    }
}