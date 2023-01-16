package pt.ipca.smartcanteen.models.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.RetroMeal
import pt.ipca.smartcanteen.models.adapters.viewHolders.EmployeeBarMenuMealsAdapterRecViewHolder
import pt.ipca.smartcanteen.models.helpers.AlertDialogManager
import pt.ipca.smartcanteen.views.activities.ConsumerOrderDetailsActivity
import pt.ipca.smartcanteen.views.activities.EditMealActivity


class EmployeeBarMenuMealsAdapterRec(
    private val rebuildList: () -> Unit,
    private var activity: Activity,
    private var alertDialogManager: AlertDialogManager,
    private var removeMealAskString: String,
    private var cantRemoveMealString: String,
    private var meals: List<RetroMeal>
) :
    RecyclerView.Adapter<EmployeeBarMenuMealsAdapterRecViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeBarMenuMealsAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EmployeeBarMenuMealsAdapterRecViewHolder(activity, alertDialogManager, inflater, parent)
    }

    override fun onBindViewHolder(holder: EmployeeBarMenuMealsAdapterRecViewHolder, position: Int) {
        val meal = meals.get(position)
        val mealId = meal.mealid
        val name = meal.name
        val time = "${meal.preparationtime}min"
        val description = meal.description
        val canbemade = meal.canbemade
        val cantakeaway = meal.cantakeaway
        val price = "${meal.price}€"

        holder.bindData(name, time, price, removeMealAskString, cantRemoveMealString, meal.mealid, rebuildList)

        holder.itemView.setOnClickListener{
            var intent = Intent(activity, EditMealActivity::class.java)
            intent.putExtra("mealId", mealId)
            intent.putExtra("name", name)
            intent.putExtra("time", time)
            intent.putExtra("description", description)
            intent.putExtra("canbemade", canbemade)
            intent.putExtra("price", price)
            intent.putExtra("cantakeaway", cantakeaway)
            activity.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return meals.size
    }


}