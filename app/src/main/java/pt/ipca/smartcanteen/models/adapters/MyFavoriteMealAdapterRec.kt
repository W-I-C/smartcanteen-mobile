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
        val url = "https://firebasestorage.googleapis.com/v0/b/smartcanteen-9b4a5.appspot.com/o/francesinha.jpeg?alt=media&token=d23bcad0-9b7a-499c-b5e8-d47e50d38025"

        val mealId= order.mealId

        holder.bindData(name,time,price, url)
        holder.deleteMeal(mealId)


    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }


}

