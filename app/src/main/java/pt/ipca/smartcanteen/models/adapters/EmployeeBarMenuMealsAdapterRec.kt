package pt.ipca.smartcanteen.models.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.RetroMeal
import pt.ipca.smartcanteen.models.adapters.viewHolders.EmployeeBarMenuMealsAdapterRecViewHolder
import pt.ipca.smartcanteen.models.helpers.AlertDialogManager


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
        val name = meal.name
        val time = "${meal.preparationtime}min"
        val price = "${meal.price}€"

        holder.bindData(name, time, price, removeMealAskString, cantRemoveMealString, meal.mealid, rebuildList)

    }

    override fun getItemCount(): Int {
        return meals.size
    }


}