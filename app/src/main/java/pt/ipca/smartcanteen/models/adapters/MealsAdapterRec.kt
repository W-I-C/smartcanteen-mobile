package pt.ipca.smartcanteen.models.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.adapters.viewHolders.MealsAdapterRecViewHolder
import pt.ipca.smartcanteen.models.helpers.AlertDialogManager
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.models.retrofit.response.RetroMeal
import pt.ipca.smartcanteen.views.activities.AddMealCartActivity

class MealsAdapterRec(private var mealsList: List<RetroMeal>, private var activity: Activity, private var layoutInflater: LayoutInflater) :
    RecyclerView.Adapter<MealsAdapterRecViewHolder>() {

    val retrofit = SmartCanteenRequests().retrofit
    private lateinit var alertDialogManager: AlertDialogManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MealsAdapterRecViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MealsAdapterRecViewHolder, position: Int) {
        val meal = mealsList.get(position)
        val mealid = meal.mealid
        val description = meal.description
        val mealName = meal.name
        val title = meal.name
        val price = "${meal.price}€"
        val preptime = "${meal.preparationtime}min"
        holder.bindData(mealid, title, preptime, price)

        holder.itemView.setOnClickListener {
            mealDetails(mealid, mealName, description, price, preptime)
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
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