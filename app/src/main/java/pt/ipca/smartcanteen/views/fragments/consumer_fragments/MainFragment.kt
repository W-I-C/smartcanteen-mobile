package pt.ipca.smartcanteen.views.fragments.consumer_fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.*
import pt.ipca.smartcanteen.models.RetroBar
import pt.ipca.smartcanteen.models.RetroMeal
import pt.ipca.smartcanteen.models.RetroTrade
import pt.ipca.smartcanteen.models.adapters.MealsAdapterRec
import pt.ipca.smartcanteen.models.adapters.MenuOrdersAdapterRec
import pt.ipca.smartcanteen.models.adapters.TradeMealsAdapterRec
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.LoadingDialogManager
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.*
import pt.ipca.smartcanteen.views.activities.Login
import pt.ipca.smartcanteen.views.fragments.employee_fragments.EmployeeFragmentActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class MainFragment : Fragment() {

    private val tradesProgressBar: ProgressBar by lazy {requireView().findViewById<ProgressBar>(R.id.consumer_menu_trades_progress_bar) as ProgressBar }
    private val tradesTextProgress: TextView by lazy {requireView().findViewById<TextView>(R.id.consumer_menu_trades_progress_bar_text) as TextView }
    private val mealsProgressBar: ProgressBar by lazy {requireView().findViewById<ProgressBar>(R.id.consumer_menu_meals_progress_bar) as ProgressBar }
    private val mealsTextProgress: TextView by lazy {requireView().findViewById<TextView>(R.id.consumer_menu_meals_progress_bar_text) as TextView }
    private val ordersProgressBar: ProgressBar by lazy {requireView().findViewById<ProgressBar>(R.id.consumer_menu_orders_progress_bar) as ProgressBar }
    private val ordersTextProgress: TextView by lazy {requireView().findViewById<TextView>(R.id.consumer_menu_orders_progress_bar_text) as TextView }
    private val logoutIc: ImageView by lazy {requireView().findViewById<ImageView>(R.id.main_logout) as ImageView }
    private val notiIc: ImageView by lazy {requireView().findViewById<ImageView>(R.id.main_notification_bell) as ImageView }
    private lateinit var loadingDialogManager :LoadingDialogManager

    override fun onCreateView(
        inflater: LayoutInflater, parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        loadingDialogManager = LoadingDialogManager(inflater, requireActivity())
        loadingDialogManager.createLoadingAlertDialog()
        return inflater.inflate(R.layout.activity_main, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ordersProgressBar.visibility = View.VISIBLE
        ordersTextProgress.visibility = View.VISIBLE
        tradesProgressBar.visibility = View.VISIBLE
        tradesTextProgress.visibility = View.VISIBLE

        val retrofit = SmartCanteenRequests().retrofit

        logoutIc.setOnClickListener{
            AuthHelper().doLogout(retrofit,requireActivity(),loadingDialogManager)
        }

        notiIc.setOnClickListener{
            Toast.makeText(requireActivity(), "notifications", Toast.LENGTH_SHORT).show()
        }



        val barSpinner: Spinner = view.findViewById<Spinner>(R.id.main_bar_select_sp)

        val barMealsRecyclerView = view.findViewById<RecyclerView>(R.id.main_bar_meals_rv)
        val tradeMealsRecyclerView = view.findViewById<RecyclerView>(R.id.main_available_trades_rv)
        val ordersRecyclerView = view.findViewById<RecyclerView>(R.id.main_orders_rv)

        val barMealsLinearLayoutManager = LinearLayoutManager(view.context)
        barMealsLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val tradeMealsLinearLayoutManager = LinearLayoutManager(view.context)
        tradeMealsLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val ordersLinearLayoutManager = LinearLayoutManager(view.context)
        ordersLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        getTradeList(
            tradeMealsRecyclerView,
            tradeMealsLinearLayoutManager,
            retrofit
        )
        tradesProgressBar.visibility = View.GONE
        tradesTextProgress.visibility = View.GONE

        getOrdersList(
            ordersRecyclerView,
            ordersLinearLayoutManager,
            retrofit
        )
        ordersProgressBar.visibility = View.GONE
        ordersTextProgress.visibility = View.GONE

        /** Get All Info **/
        getBarInfo(
            barSpinner,
            barMealsRecyclerView,
            barMealsLinearLayoutManager,
            retrofit
        )
    }


    fun getBarInfo(
        spinner: Spinner,
        barMealsRecyclerView: RecyclerView,
        barMealsLinearLayoutManager: LinearLayoutManager,
        retrofit:Retrofit
    ) {


        val service = retrofit.create(CampusBarsService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        loadingDialogManager.dialog.show()

        service.getCampusBars("Bearer $token").enqueue(object :
            Callback<List<RetroBar>> {
            override fun onResponse(
                call: Call<List<RetroBar>>,
                response: Response<List<RetroBar>>
            ) {
                if (response.code() == 200) {
                    loadingDialogManager.dialog.dismiss()
                    val body = response.body()

                    if (body != null) {
                        if (body.isNotEmpty()) {

                            Log.d("bar:", body.toString())
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


                                        Log.d("spinner:","Before")
                                        getMealsList(
                                            barMealsRecyclerView,
                                            barMealsLinearLayoutManager,
                                            barId,
                                            retrofit
                                        )
                                        Log.d("spinner:","After")

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

        mealsProgressBar.visibility = View.VISIBLE
        mealsTextProgress.visibility = View.VISIBLE

        barMealsRecyclerView.visibility = View.GONE

        service.getBarMeals(barId, "Bearer $token").enqueue(object :
            Callback<List<RetroMeal>> {
            override fun onResponse(
                call: Call<List<RetroMeal>>,
                response: Response<List<RetroMeal>>
            ) {
                if (response.code() == 200) {
                    val body = response.body()

                    if (body != null) {
                        if (body.isNotEmpty()) {
                            /** bar Meals **/
                            val barMealsAdapter = MealsAdapterRec(body)

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
        barMealsRecyclerView.visibility = View.VISIBLE
        mealsProgressBar.visibility = View.GONE
        mealsTextProgress.visibility = View.GONE

    }

    private fun getTradeList(
        barMealsRecyclerView: RecyclerView,
        barMealsLinearLayoutManager: LinearLayoutManager,
        retrofit: Retrofit
    ) {

        val service = retrofit.create(CampusTradesService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)


        service.getCampusTrades("Bearer $token").enqueue(object :
            Callback<List<RetroTrade>> {
            override fun onResponse(
                call: Call<List<RetroTrade>>,
                response: Response<List<RetroTrade>>
            ) {
                if (response.code() == 200) {
                    val body = response.body()
                    Log.d("Trades: ", body.toString())
                    if (body != null) {
                        if (body.isNotEmpty()) {
                            /** Campus trades **/
                            val barMealsAdapter = TradeMealsAdapterRec(getString(R.string.ordernum),getString(R.string.free), body)

                            barMealsRecyclerView.layoutManager = barMealsLinearLayoutManager
                            barMealsRecyclerView.itemAnimator = DefaultItemAnimator()
                            barMealsRecyclerView.adapter = barMealsAdapter
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<RetroTrade>>, t: Throwable) {
                print("error")
            }

        })

    }

    private fun getOrdersList(
        ordersRecyclerView: RecyclerView,
        ordersLinearLayoutManager: LinearLayoutManager,
        retrofit: Retrofit
    ) {

        val service = retrofit.create(MyOrdersService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)


        service.seeMyOrders("Bearer $token").enqueue(object :
            Callback<List<RetroTrade>> {
            override fun onResponse(
                call: Call<List<RetroTrade>>,
                response: Response<List<RetroTrade>>
            ) {
                if (response.code() == 200) {
                    val body = response.body()
                    Log.d("Encomendas: ", body.toString())
                    if (body != null) {
                        if (body.isNotEmpty()) {
                            /** my orders **/
                            var ordersAdapter = MenuOrdersAdapterRec(getString(R.string.qty),getString(R.string.ordernum),body)

                            ordersRecyclerView.layoutManager = ordersLinearLayoutManager
                            ordersRecyclerView.itemAnimator = DefaultItemAnimator()
                            ordersRecyclerView.adapter = ordersAdapter
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<RetroTrade>>, t: Throwable) {
                print("error")
            }

        })

    }

}

