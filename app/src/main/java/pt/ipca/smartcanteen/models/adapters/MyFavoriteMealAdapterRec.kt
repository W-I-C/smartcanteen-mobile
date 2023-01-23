package pt.ipca.smartcanteen.models.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.adapters.viewHolders.MyFavoriteMealRecViewHolder
import pt.ipca.smartcanteen.models.retrofit.response.RetroFavoriteMeal
import pt.ipca.smartcanteen.views.activities.AddMealCartActivity

class MyFavoriteMealAdapterRec(
    private var listFavorite: List<RetroFavoriteMeal>, val activity: Activity, var linearLayoutManager: LinearLayoutManager,
    val FavAdapterRec: RecyclerView
) :
    RecyclerView.Adapter<MyFavoriteMealRecViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFavoriteMealRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyFavoriteMealRecViewHolder(inflater, parent, activity, linearLayoutManager, FavAdapterRec)
    }

    override fun onBindViewHolder(holder: MyFavoriteMealRecViewHolder, position: Int) {
        val meal = listFavorite.get(position)
        val name = meal.name
        val time = "${meal.time}min"
        val price = "${meal.price}€"

        val mealId = meal.mealId

        holder.bindData(mealId, name, time, price)
        holder.itemView.setOnClickListener {
            mealDetails(mealId, name, meal.description, price, time)
        }

    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }

    fun mealDetails(mealid: String, mealName: String, mealDescription: String, mealPrice: String, mealPreptime: String) {
        var intent = Intent(activity, AddMealCartActivity::class.java)
        intent.putExtra("mealId", mealid)
        intent.putExtra("name", mealName)
        intent.putExtra("description", mealDescription)
        intent.putExtra("price", mealPrice)
        intent.putExtra("time", mealPreptime)
        activity.startActivity(intent)
    }


}

