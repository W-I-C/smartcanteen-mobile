package pt.ipca.smartcanteen.views.fragments.consumer_fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.*
import pt.ipca.smartcanteen.models.RetroBar
import pt.ipca.smartcanteen.models.RetroMeal
import pt.ipca.smartcanteen.models.adapters.MealsAdapterRec
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.BarMealsService
import pt.ipca.smartcanteen.services.CampusBarsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return inflater.inflate(R.layout.activity_main, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val barSpinner: Spinner = view.findViewById<Spinner>(R.id.main_bar_select_sp)
        val barMealsRecyclerView =
            view.findViewById<RecyclerView>(R.id.main_bar_meals_rv)
        val barMealsLinearLayoutManager = LinearLayoutManager(view.context)
        barMealsLinearLayoutManager.orientation =
            LinearLayoutManager.HORIZONTAL


        /** Get All Info **/
        getBarInfo(barSpinner, barMealsRecyclerView, barMealsLinearLayoutManager)

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


    fun getBarInfo(
        spinner: Spinner,
        barMealsRecyclerView: RecyclerView,
        barMealsLinearLayoutManager: LinearLayoutManager
    ) {
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(CampusBarsService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        service.getCampusBars("Bearer $token").enqueue(object :
            Callback<List<RetroBar>> {
            override fun onResponse(
                call: Call<List<RetroBar>>,
                response: Response<List<RetroBar>>
            ) {
                if (response.code() == 200) {
                    val body = response.body()

                    if (body != null) {
                        if (body.isNotEmpty()) {
                            Log.d("body:", body.toString())
                            var adapter = getActivity()?.let {
                                ArrayAdapter(
                                    it,
                                    android.R.layout.simple_spinner_item,
                                    body.map { retroBar -> retroBar.name }
                                )
                            }
                            adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.adapter = adapter

                            spinner.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {

                                        val barId = body[position].barid
                                        getMealsList(
                                            barMealsRecyclerView,
                                            barMealsLinearLayoutManager,
                                            barId,
                                            retrofit
                                        )
                                    }
                                }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<RetroBar>>, t: Throwable) {
                print("error")
            }

        })

    }

    private fun getMealsList(
        barMealsRecyclerView: RecyclerView,
        barMealsLinearLayoutManager: LinearLayoutManager,
        barId: String,
        retrofit: Retrofit
    ) {

        val service = retrofit.create(BarMealsService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)


        service.getBarMeals(barId, "Bearer $token").enqueue(object :
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

                            val barMealsAdapter = MealsAdapterRec(retroFit2)

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

    private fun getTradeList(
        barMealsRecyclerView: RecyclerView,
        barMealsLinearLayoutManager: LinearLayoutManager,
        barId: String,
        retrofit: Retrofit
    ) {

        val service = retrofit.create(BarMealsService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)


        service.getBarMeals(barId, "Bearer $token").enqueue(object :
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

                            val barMealsAdapter = MealsAdapterRec(retroFit2)

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

