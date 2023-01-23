package pt.ipca.smartcanteen.models.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.adapters.viewHolders.AllowedChangesAdapterRecViewHolder
import pt.ipca.smartcanteen.models.retrofit.response.RetroAllowedChanges


class AllowedChangesAdapterRec(private var allowedChanges: List<RetroAllowedChanges>) :
    RecyclerView.Adapter<AllowedChangesAdapterRecViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllowedChangesAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AllowedChangesAdapterRecViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: AllowedChangesAdapterRecViewHolder, position: Int) {
        val allowedChanges = allowedChanges[position]
        val changeid = allowedChanges.changeid
        val mealid = allowedChanges.mealid
        val ingname = allowedChanges.ingname
        val ingdosage = allowedChanges.ingdosage
        val isremoveonly = allowedChanges.isremoveonly
        val canbeincremented = allowedChanges.canbeincremented
        val canbedecremented = allowedChanges.canbedecremented
        val incrementlimit = allowedChanges.incrementlimit
        val decrementlimit = allowedChanges.decrementlimit
        val default = allowedChanges.default

        holder.bindData(
            changeid,
            mealid,
            ingname,
            ingdosage,
            isremoveonly,
            canbeincremented,
            canbedecremented,
            incrementlimit,
            decrementlimit,
            default
        )
    }

    override fun getItemCount(): Int {
        return allowedChanges.size
    }
}