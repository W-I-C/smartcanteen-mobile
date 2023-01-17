package pt.ipca.smartcanteen.views.fragments.consumer_fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.*
import pt.ipca.smartcanteen.models.RetroBar
import pt.ipca.smartcanteen.models.RetroMeal
import pt.ipca.smartcanteen.models.RetroTicket
import pt.ipca.smartcanteen.models.RetroTrade
import pt.ipca.smartcanteen.models.adapters.MealsAdapterRec
import pt.ipca.smartcanteen.models.adapters.MenuOrdersAdapterRec
import pt.ipca.smartcanteen.models.adapters.TradeMealsAdapterRec
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.AlertDialogManager
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.*
import pt.ipca.smartcanteen.views.activities.ConsumerBarMenuActivity
import pt.ipca.smartcanteen.views.activities.NotificationActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class MenuConsumerFragment : Fragment() {

    private val tradesProgressBar: ProgressBar by lazy {requireView().findViewById<ProgressBar>(R.id.consumer_menu_trades_progress_bar) as ProgressBar }
    private val tradesTextProgress: TextView by lazy {requireView().findViewById<TextView>(R.id.consumer_menu_trades_progress_bar_text) as TextView }
    private val mealsProgressBar: ProgressBar by lazy {requireView().findViewById<ProgressBar>(R.id.consumer_menu_meals_progress_bar) as ProgressBar }
    private val mealsTextProgress: TextView by lazy {requireView().findViewById<TextView>(R.id.consumer_menu_meals_progress_bar_text) as TextView }
    private val viewMealsText: TextView by lazy {requireView().findViewById<TextView>(R.id.consumer_menu_bar_meals_view_meals_tv) as TextView }
    private val noAvailableTradesText: TextView by lazy {requireView().findViewById<TextView>(R.id.consumer_menu_trades_no_trades_text) as TextView }
    private val ordersProgressBar: ProgressBar by lazy {requireView().findViewById<ProgressBar>(R.id.consumer_menu_orders_progress_bar) as ProgressBar }
    private val ordersTextProgress: TextView by lazy {requireView().findViewById<TextView>(R.id.consumer_menu_orders_progress_bar_text) as TextView }
    private val logoutIc: ImageView by lazy {requireView().findViewById<ImageView>(R.id.consumer_menu_logout) as ImageView }
    private val notiIc: ImageView by lazy {requireView().findViewById<ImageView>(R.id.consumer_menu_notification_bell) as ImageView }

    private val barMealsTitleTv: TextView by lazy {requireView().findViewById<TextView>(R.id.consumer_menu_bar_meals_tv) as TextView }

    private val barMealsRecyclerView : RecyclerView by lazy {requireView().findViewById<RecyclerView>(R.id.consumer_menu_bar_meals_rv) as RecyclerView }
    private val tradeMealsRecyclerView : RecyclerView by lazy {requireView().findViewById<RecyclerView>(R.id.consumer_menu_available_trades_rv) as RecyclerView }
    private val ordersRecyclerView : RecyclerView by lazy {requireView().findViewById<RecyclerView>(R.id.consumer_menu_orders_rv) as RecyclerView }
    private val barSpinner : Spinner by lazy {requireView().findViewById<Spinner>(R.id.consumer_menu_bar_select_sp) as Spinner }

    private lateinit var alertDialogManager :AlertDialogManager

    override fun onCreateView(
        inflater: LayoutInflater, parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        alertDialogManager = AlertDialogManager(inflater, requireActivity())
        alertDialogManager.createLoadingAlertDialog()
        return inflater.inflate(R.layout.fragment_consumer_menu, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val retrofit = SmartCanteenRequests().retrofit
        noAvailableTradesText.visibility=View.GONE
        logoutIc.setOnClickListener{
            AuthHelper().doLogout(retrofit,requireActivity(),alertDialogManager)
        }

        notiIc.setOnClickListener{
            val intent = Intent(requireActivity(), NotificationActivity::class.java)
            startActivity(intent)
        }

        barMealsTitleTv.setOnClickListener{
            val intent = Intent(requireActivity(), ConsumerBarMenuActivity::class.java)
            startActivity(intent)
        }

        viewMealsText.setOnClickListener{
            val intent = Intent(requireActivity(), ConsumerBarMenuActivity::class.java)
            startActivity(intent)
        }

        getTradeList(
            retrofit
        )
        getOrdersList(
            retrofit
        )
        getBarInfo(
            retrofit
        )
    }




    fun getBarInfo(
        retrofit:Retrofit
    ) {


        val service = retrofit.create(CampusService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        alertDialogManager.dialog.show()

        service.getCampusBars("Bearer $token").enqueue(object :
            Callback<List<RetroBar>> {
            override fun onResponse(
                call: Call<List<RetroBar>>,
                response: Response<List<RetroBar>>
            ) {
                if (response.code() == 200) {
                    alertDialogManager.dialog.dismiss()
                    val body = response.body()

                    if (body != null) {
                        if (body.isNotEmpty()) {


                            var adapter = getActivity()?.let {
                                ArrayAdapter(
                                    it,
                                    android.R.layout.simple_spinner_item,
                                    body.map { retroBar -> retroBar.name }
                                )
                            }
                            adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            barSpinner.adapter = adapter

                            barSpinner.onItemSelectedListener =
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
                                            barId,
                                            retrofit
                                        )
                                        Log.d("spinner:","After")
                                    }
                                }
                        }
                    }
                }
                else if(response.code()==401){
                    AuthHelper().newSessionToken(requireActivity())
                    getBarInfo(
                        retrofit
                    )
                }

            }

            override fun onFailure(call: Call<List<RetroBar>>, t: Throwable) {
                //mealsProgressBar.visibility = View.GONE
                //mealsTextProgress.visibility = View.GONE
                alertDialogManager.dialog.dismiss()
                print("error")
            }

        })

    }

    private fun getMealsList(
        barId: String,
        retrofit: Retrofit
    ) {

        val service = retrofit.create(MealsService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        barMealsRecyclerView.visibility = View.INVISIBLE
        mealsProgressBar.visibility = View.VISIBLE
        mealsTextProgress.visibility = View.VISIBLE

        service.getBarMeals(barId, "Bearer $token").enqueue(object :
            Callback<List<RetroMeal>> {
            override fun onResponse(
                call: Call<List<RetroMeal>>,
                response: Response<List<RetroMeal>>
            ) {
                if (response.code() == 200) {
                    val body = response.body()

                    barMealsRecyclerView.visibility = View.VISIBLE
                    mealsProgressBar.visibility = View.INVISIBLE
                    mealsTextProgress.visibility = View.INVISIBLE

                    if (body != null) {
                        if (body.isNotEmpty()) {
                            /** bar Meals **/
                            val barMealsAdapter = MealsAdapterRec(body,requireActivity(), layoutInflater)
                            val barMealsLinearLayoutManager = LinearLayoutManager(requireContext())
                            barMealsLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                            barMealsRecyclerView.layoutManager = barMealsLinearLayoutManager
                            barMealsRecyclerView.itemAnimator = DefaultItemAnimator()
                            barMealsRecyclerView.adapter = barMealsAdapter
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<RetroMeal>>, t: Throwable) {
                barMealsRecyclerView.visibility = View.VISIBLE
                mealsProgressBar.visibility = View.INVISIBLE
                mealsTextProgress.visibility = View.INVISIBLE
                print("error")
            }

        })
        barMealsRecyclerView.visibility = View.VISIBLE
        mealsProgressBar.visibility = View.GONE
        mealsTextProgress.visibility = View.GONE

    }

    private fun getTradeList(
        retrofit: Retrofit
    ) {

        val service = retrofit.create(TradesService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        tradeMealsRecyclerView.visibility = View.INVISIBLE
        tradesProgressBar.visibility = View.VISIBLE
        tradesTextProgress.visibility = View.VISIBLE

        service.getCampusTrades("Bearer $token").enqueue(object :
            Callback<List<RetroTrade>> {
            override fun onResponse(
                call: Call<List<RetroTrade>>,
                response: Response<List<RetroTrade>>
            ) {
                if (response.code() == 200) {
                    val body = response.body()

                    tradeMealsRecyclerView.visibility = View.VISIBLE
                    tradesProgressBar.visibility = View.INVISIBLE
                    tradesTextProgress.visibility = View.INVISIBLE

                    Log.d("Trades: ", body.toString())
                    if (body != null) {
                        if (body.isNotEmpty()) {
                            /** Campus trades **/
                            val barMealsAdapter = TradeMealsAdapterRec(requireActivity(),getString(R.string.ordernum),getString(R.string.free), body)
                            val tradeMealsLinearLayoutManager = LinearLayoutManager(requireActivity())
                            tradeMealsLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                            tradeMealsRecyclerView.layoutManager = tradeMealsLinearLayoutManager
                            tradeMealsRecyclerView.itemAnimator = DefaultItemAnimator()
                            tradeMealsRecyclerView.adapter = barMealsAdapter
                        }else{
                            noAvailableTradesText.visibility=View.VISIBLE
                        }
                    }
                }else if(response.code()==401){
                    AuthHelper().newSessionToken(requireActivity())
                    getTradeList(
                        retrofit
                    )
                }
            }

            override fun onFailure(call: Call<List<RetroTrade>>, t: Throwable) {
                tradeMealsRecyclerView.visibility = View.VISIBLE
                tradesProgressBar.visibility = View.INVISIBLE
                tradesTextProgress.visibility = View.INVISIBLE
                print("error")
            }

        })

    }

    private fun getOrdersList(
        retrofit: Retrofit
    ) {

        val service = retrofit.create(OrdersService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        ordersRecyclerView.visibility = View.INVISIBLE
        ordersProgressBar.visibility = View.VISIBLE
        ordersTextProgress.visibility = View.VISIBLE

        service.seeMyOrders("Bearer $token").enqueue(object :
            Callback<List<RetroTicket>> {
            override fun onResponse(
                call: Call<List<RetroTicket>>,
                response: Response<List<RetroTicket>>
            ) {
                if (response.code() == 200) {
                    val body = response.body()

                    ordersRecyclerView.visibility = View.VISIBLE
                    ordersProgressBar.visibility = View.INVISIBLE
                    ordersTextProgress.visibility = View.INVISIBLE

                    Log.d("Encomendas: ", body.toString())
                    if (body != null) {
                        if (body.isNotEmpty()) {
                            /** my orders **/
                            var ordersAdapter = MenuOrdersAdapterRec(requireActivity(),getString(R.string.qty),getString(R.string.ordernum),body)
                            val ordersLinearLayoutManager = LinearLayoutManager(requireContext())
                            ordersLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                            ordersRecyclerView.layoutManager = ordersLinearLayoutManager
                            ordersRecyclerView.itemAnimator = DefaultItemAnimator()
                            ordersRecyclerView.adapter = ordersAdapter

                        }
                    }
                }
                else if(response.code()==401){
                    AuthHelper().newSessionToken(requireActivity())
                    getOrdersList(
                        retrofit
                    )
                }
            }

            override fun onFailure(call: Call<List<RetroTicket>>, t: Throwable) {
                ordersRecyclerView.visibility = View.VISIBLE
                ordersProgressBar.visibility = View.INVISIBLE
                ordersTextProgress.visibility = View.INVISIBLE
                print("error")
            }

        })

    }


}

