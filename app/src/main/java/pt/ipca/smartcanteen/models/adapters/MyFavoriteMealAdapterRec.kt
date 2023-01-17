package pt.ipca.smartcanteen.models.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.RetroCartMeals
import pt.ipca.smartcanteen.models.RetroMeal
import pt.ipca.smartcanteen.models.adapters.viewHolders.MyFavoriteMealRecViewHolder
import pt.ipca.smartcanteen.models.adapters.viewHolders.MyOrdersCartRecViewHolder
import pt.ipca.smartcanteen.models.helpers.RetroFavoriteMeal

class MyFavoriteMealAdapterRec(private var listFavorite: List<RetroFavoriteMeal>, val activity: Activity, var linearLayoutManager: LinearLayoutManager,
                                val FavAdapterRec:RecyclerView) :
    RecyclerView.Adapter<MyFavoriteMealRecViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFavoriteMealRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyFavoriteMealRecViewHolder(inflater,parent,activity,linearLayoutManager ,FavAdapterRec)
    }

    override fun onBindViewHolder(holder: MyFavoriteMealRecViewHolder, position: Int) {
        val order = listFavorite.get(position)
        val name = order.name
        val time = "${order.time}min"
        val price = "${order.price}€"

        val mealId= order.mealId

        holder.bindData(name,time.toString(),price)
        holder.deleteMeal(mealId)


    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }


}

