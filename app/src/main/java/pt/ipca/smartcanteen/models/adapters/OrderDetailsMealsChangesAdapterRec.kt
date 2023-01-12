package pt.ipca.smartcanteen.models.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.RetroMealChange
import pt.ipca.smartcanteen.models.adapters.viewHolders.OrderDetailsMealsChangesViewHolder

class OrderDetailsMealsChangesAdapterRec(private var mealChanges: List<RetroMealChange>) :
    RecyclerView.Adapter<OrderDetailsMealsChangesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsMealsChangesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return OrderDetailsMealsChangesViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: OrderDetailsMealsChangesViewHolder, position: Int) {
        val mealChange = mealChanges[position]
        val isRemoveOnly = mealChange.isremoveonly
        val amount = mealChange.ingamount
        val name = mealChange.ingname
        holder.bindData(name,amount,isRemoveOnly)
    }

    override fun getItemCount(): Int {
        return mealChanges.size
    }
}