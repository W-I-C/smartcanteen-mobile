package pt.ipca.smartcanteen.consumer_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.*


class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.activity_main, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Trades **/
        val tradeMealsRecyclerView = view.findViewById<RecyclerView>(R.id.main_available_trades_rv)
        val tradeMeals = mutableListOf<Meal>(
            Meal("Francesinha", 5.0, 15),
            Meal("Panado", 3.5, 5),
            Meal("Salada", 1.5, 1),
        )

        var tradeMealsAdapter = MealsAdapterRec(tradeMeals)
        val linearLayoutManager = LinearLayoutManager(view.context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        tradeMealsRecyclerView.layoutManager = linearLayoutManager
        tradeMealsRecyclerView.itemAnimator = DefaultItemAnimator()
        tradeMealsRecyclerView.adapter = tradeMealsAdapter

        /** Bar Meals **/
        val barMealsRecyclerView = view.findViewById<RecyclerView>(R.id.main_bar_meals_rv)
        val barMeals = mutableListOf<Meal>(
            Meal("Francesinha Bimba", 5.0, 15),
            Meal("Panado c/pão", 3.5, 5),
            Meal("Salada de Fruta", 1.5, 1),
        )
        var barMealsAdapter = MealsAdapterRec(barMeals)
        val barMealsLinearLayoutManager = LinearLayoutManager(view.context)
        barMealsLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        barMealsRecyclerView.layoutManager = barMealsLinearLayoutManager
        barMealsRecyclerView.itemAnimator = DefaultItemAnimator()
        barMealsRecyclerView.adapter = barMealsAdapter

        /** Orders **/
        val ordersRecyclerView = view.findViewById<RecyclerView>(R.id.main_orders_rv)
        val orders = mutableListOf<Meal>(
            Meal("Taco", 5.0, 15),
            Meal("Batatas Fritas", 3.5, 5),
            Meal("Wrap", 1.5, 1),
        )
        var ordersAdapter = MealsAdapterRec(orders)
        val ordersLinearLayoutManager = LinearLayoutManager(view.context)
        ordersLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        ordersRecyclerView.layoutManager = ordersLinearLayoutManager
        ordersRecyclerView.itemAnimator = DefaultItemAnimator()
        ordersRecyclerView.adapter = ordersAdapter
    }
}