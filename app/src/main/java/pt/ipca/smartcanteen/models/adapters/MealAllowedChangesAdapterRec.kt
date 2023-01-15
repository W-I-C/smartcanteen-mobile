package pt.ipca.smartcanteen.models.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.RetroAllowedChanges
import pt.ipca.smartcanteen.models.RetroMealChange
import pt.ipca.smartcanteen.models.adapters.viewHolders.AllowedChangesAdapterRecViewHolder
import pt.ipca.smartcanteen.models.adapters.viewHolders.MealAllowedChangesAdapterRecViewHolder
import pt.ipca.smartcanteen.models.adapters.viewHolders.MealsAdapterRecViewHolder
import pt.ipca.smartcanteen.models.adapters.viewHolders.OrderDetailsMealsChangesViewHolder


class MealAllowedChangesAdapterRec(private var allowedChanges: List<RetroAllowedChanges>) :
    RecyclerView.Adapter<MealAllowedChangesAdapterRecViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealAllowedChangesAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MealAllowedChangesAdapterRecViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MealAllowedChangesAdapterRecViewHolder, position: Int) {
        val allowedChanges = allowedChanges[position]
        val changeid = allowedChanges.changeid
        val mealid = allowedChanges.mealid
        val ingname = allowedChanges.ingname
        val isremoveonly = allowedChanges.isremoveonly
        val canbeincremented = allowedChanges.canbeincremented
        val canbedecremented = allowedChanges.canbedecremented

        holder.bindData(changeid,mealid,ingname,isremoveonly,canbeincremented,canbedecremented,)
    }

    override fun getItemCount(): Int {
        return allowedChanges.size
    }
}