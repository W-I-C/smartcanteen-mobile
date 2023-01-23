package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R

class FavoriteMealsAdapterRecViewHolder(inflater: LayoutInflater, val parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_card, parent, false)) {
    val name = itemView.findViewById<TextView>(R.id.layout_card_title_tv)
    val price = itemView.findViewById<TextView>(R.id.layout_card_price_tv)
    val time = itemView.findViewById<TextView>(R.id.layout_card_time_tv)

    fun bindData(nameText: String, priceText: Float, timeText: Int) {
        name.text = nameText
        price.text = priceText.toString()
        time.text = timeText.toString()
    }
}

