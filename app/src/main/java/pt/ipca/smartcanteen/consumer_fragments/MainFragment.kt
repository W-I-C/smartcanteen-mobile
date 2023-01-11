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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.activity_main, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Get All Info **/
        getRefeicoesList(view)
        /**
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
        **/
    }

    fun getRefeicoesList(view: View){
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(BarMealsService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)
        var barId = "0d9c0499-f2f8-44d5-9b49-a0529266433a"


        service.getBarMeals(barId,"Bearer $token").enqueue(object :
            Callback<List<RetroMeal>> {
            override fun onResponse(
                call: Call<List<RetroMeal>>,
                response: Response<List<RetroMeal>>
            ) {
                if (response.code() == 200) {
                    val retroFit2 = response.body()

                    if (retroFit2 != null) {
                        if (retroFit2.isNotEmpty()) {
                            /** Bar Meals **/
                            val barMealsRecyclerView =
                                view.findViewById<RecyclerView>(R.id.main_bar_meals_rv)
                            val barMealsAdapter = MealsAdapterRec(retroFit2)
                            val barMealsLinearLayoutManager = LinearLayoutManager(view.context)
                            barMealsLinearLayoutManager.orientation =
                                LinearLayoutManager.HORIZONTAL
                            barMealsRecyclerView.layoutManager = barMealsLinearLayoutManager
                            barMealsRecyclerView.itemAnimator = DefaultItemAnimator()
                            barMealsRecyclerView.adapter = barMealsAdapter
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<RetroMeal>>, t: Throwable) {
                print("error")
            }

        })

    }
}

