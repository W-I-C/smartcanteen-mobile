package pt.ipca.smartcanteen.models.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.RetroCartMeals
import pt.ipca.smartcanteen.models.adapters.viewHolders.MyFavoriteMealRecViewHolder
import pt.ipca.smartcanteen.models.adapters.viewHolders.MyOrdersCartRecViewHolder
import pt.ipca.smartcanteen.models.helpers.RetroFavoriteMeal

class MyFavoriteMealAdapterRec(private var listFavorite: List<RetroFavoriteMeal>) :
    RecyclerView.Adapter<MyFavoriteMealRecViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFavoriteMealRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyFavoriteMealRecViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: MyFavoriteMealRecViewHolder, position: Int) {
        val order = listFavorite.get(position)
        val name = order.name
        val time = order.time
        val price = "${order.price}€"

        holder.bindData(name,time.toString(),price)

    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }


}

