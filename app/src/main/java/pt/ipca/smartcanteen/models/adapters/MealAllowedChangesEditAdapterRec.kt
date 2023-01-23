package pt.ipca.smartcanteen.models.adapters

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.adapters.viewHolders.MealAllowedChangesEditAdapterRecViewHolder
import pt.ipca.smartcanteen.models.helpers.AlertDialogManager
import pt.ipca.smartcanteen.models.retrofit.response.RetroAllowedChanges


class MealAllowedChangesEditAdapterRec(
    private var allowedChanges: List<RetroAllowedChanges>,
    val sp: SharedPreferences,
    private val activity: Activity,
    private var context: Context,
    val allowedChangesEditRecyclerView: RecyclerView,
    private val linearLayoutManager: LinearLayoutManager,
    private val alertDialogManager: AlertDialogManager,
    private var removeAllowedChangeAskString: String,
) :
    RecyclerView.Adapter<MealAllowedChangesEditAdapterRecViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealAllowedChangesEditAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MealAllowedChangesEditAdapterRecViewHolder(
            inflater,
            parent,
            linearLayoutManager,
            sp,
            allowedChangesEditRecyclerView,
            activity,
            context,
            alertDialogManager
        )
    }

    override fun onBindViewHolder(holder: MealAllowedChangesEditAdapterRecViewHolder, position: Int) {
        val allowedChanges = allowedChanges[position]
        val changeid = allowedChanges.changeid
        val mealid = allowedChanges.mealid
        val ingname = allowedChanges.ingname
        val isremoveonly = allowedChanges.isremoveonly
        val canbeincremented = allowedChanges.canbeincremented
        val canbedecremented = allowedChanges.canbedecremented

        holder.bindData(changeid, mealid, ingname, isremoveonly, canbeincremented, canbedecremented)

        holder.setDeleteClickListener(mealid, changeid, removeAllowedChangeAskString, alertDialogManager)
    }

    override fun getItemCount(): Int {
        return allowedChanges.size
    }
}