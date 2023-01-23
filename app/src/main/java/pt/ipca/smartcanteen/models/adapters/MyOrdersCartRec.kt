package pt.ipca.smartcanteen.models.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.adapters.viewHolders.MyOrdersCartRecViewHolder
import pt.ipca.smartcanteen.models.helpers.AlertDialogManager
import pt.ipca.smartcanteen.models.retrofit.response.RetroCartMeals


class MyOrdersCartRec(
    private var ordersList: List<RetroCartMeals>, val activity: Activity, var linearLayoutManager: LinearLayoutManager,
    val cartAdapterRec: RecyclerView,private var removeTradeAskString: String,
    private var alertDialogManager: AlertDialogManager
) :
    RecyclerView.Adapter<MyOrdersCartRecViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersCartRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyOrdersCartRecViewHolder(inflater, parent, activity, linearLayoutManager, cartAdapterRec)
    }

    override fun onBindViewHolder(holder: MyOrdersCartRecViewHolder, position: Int) {
        val meal = ordersList.get(position)
        val mealId = meal.mealId
        val name = meal.name
        val quantity = "${meal.quantity} ${if (meal.quantity > 1) "doses" else "dose"}"
        val price = "${meal.price}€"

        val cartmealId = meal.cartmealId

        holder.bindData(mealId, name, quantity, price)
        holder.deleteMeal(cartmealId,alertDialogManager, removeTradeAskString)
    }


    override fun getItemCount(): Int {
        return ordersList.size
    }
}


