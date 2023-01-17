
package pt.ipca.smartcanteen.models.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.RetroCartMeals
import pt.ipca.smartcanteen.models.adapters.viewHolders.MyOrdersCartRecViewHolder


class MyOrdersCartRec(private var ordersList: List<RetroCartMeals>, val activity: Activity, var linearLayoutManager: LinearLayoutManager,
                      val cartAdapterRec:RecyclerView) :
    RecyclerView.Adapter<MyOrdersCartRecViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersCartRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyOrdersCartRecViewHolder(inflater,parent,activity,linearLayoutManager ,cartAdapterRec)
    }

    override fun onBindViewHolder(holder: MyOrdersCartRecViewHolder, position: Int) {
        val order = ordersList.get(position)
        val name = order.name
        val quantity = "${order.quantity} ${if(order.quantity > 1) "doses" else "dose"}"
        val price = "${order.price}€"
        val url = ""
        val cartmealId=order.cartmealId

        holder.bindData(name,quantity,price, "https://firebasestorage.googleapis.com/v0/b/smartcanteen-9b4a5.appspot.com/o/francesinha.jpeg?alt=media&token=d23bcad0-9b7a-499c-b5e8-d47e50d38025")
        holder.deleteMeal(cartmealId)
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }
}


