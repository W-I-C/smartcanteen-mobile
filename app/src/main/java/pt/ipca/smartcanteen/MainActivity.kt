package pt.ipca.smartcanteen

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /** Trades **/
        val tradeMealsRecyclerView = findViewById<RecyclerView>(R.id.main_available_trades_rv)
        val tradeMeals = mutableListOf<Meal>(
            Meal("Francesinha", 5.0, 15),
            Meal("Panado", 3.5, 5),
            Meal("Salada", 1.5, 1),
        )

        var tradeMealsAdapter = MealsAdapterRec(tradeMeals)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        tradeMealsRecyclerView.layoutManager = linearLayoutManager
        tradeMealsRecyclerView.itemAnimator = DefaultItemAnimator()
        tradeMealsRecyclerView.adapter = tradeMealsAdapter

        /** Bar Meals **/
        val barMealsRecyclerView = findViewById<RecyclerView>(R.id.main_bar_meals_rv)
        val barMeals = mutableListOf<Meal>(
            Meal("Francesinha Bimba", 5.0, 15),
            Meal("Panado c/pão", 3.5, 5),
            Meal("Salada de Fruta", 1.5, 1),
        )

        var barMealsAdapter = MealsAdapterRec(barMeals)
        val barMealsLinearLayoutManager = LinearLayoutManager(applicationContext)
        barMealsLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        barMealsRecyclerView.layoutManager = barMealsLinearLayoutManager
        barMealsRecyclerView.itemAnimator = DefaultItemAnimator()
        barMealsRecyclerView.adapter = barMealsAdapter

        /** Orders **/
        val ordersRecyclerView = findViewById<RecyclerView>(R.id.main_orders_rv)
        val orders = mutableListOf<Meal>(
            Meal("Taco", 5.0, 15),
            Meal("Batatas Fritas", 3.5, 5),
            Meal("Wrap", 1.5, 1),
        )

        var ordersAdapter = MealsAdapterRec(orders)
        val ordersLinearLayoutManager = LinearLayoutManager(applicationContext)
        ordersLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        ordersRecyclerView.layoutManager = ordersLinearLayoutManager
        ordersRecyclerView.itemAnimator = DefaultItemAnimator()
        ordersRecyclerView.adapter = ordersAdapter
    }
}