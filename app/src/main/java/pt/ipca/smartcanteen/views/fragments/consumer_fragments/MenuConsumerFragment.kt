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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.adapters.MealsAdapterRec
import pt.ipca.smartcanteen.models.adapters.MenuOrdersAdapterRec
import pt.ipca.smartcanteen.models.adapters.TradeMealsAdapterRec
import pt.ipca.smartcanteen.models.helpers.*
import pt.ipca.smartcanteen.models.retrofit.response.*
import pt.ipca.smartcanteen.models.room.tables.*
import pt.ipca.smartcanteen.services.CampusService
import pt.ipca.smartcanteen.services.MealsService
import pt.ipca.smartcanteen.services.OrdersService
import pt.ipca.smartcanteen.services.TradesService
import pt.ipca.smartcanteen.views.activities.ConsumerAvailableTradesActivity
import pt.ipca.smartcanteen.views.activities.ConsumerBarMenuActivity
import pt.ipca.smartcanteen.views.activities.NotificationActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class MenuConsumerFragment : Fragment() {

    private val tradesProgressBar: ProgressBar by lazy { requireView().findViewById<ProgressBar>(R.id.consumer_menu_trades_progress_bar) as ProgressBar }
    private val tradesTextProgress: TextView by lazy { requireView().findViewById<TextView>(R.id.consumer_menu_trades_progress_bar_text) as TextView }
    private val seeMealsText: TextView by lazy { requireView().findViewById<TextView>(R.id.consumer_menu_bar_meals_view_meals_tv) as TextView }
    private val seeTradesText: TextView by lazy { requireView().findViewById<TextView>(R.id.consumer_menu_trades_view_tv) as TextView }
    private val tradesTitleText: TextView by lazy { requireView().findViewById<TextView>(R.id.consumer_menu_trades_tv) as TextView }
    private val noAvailableTradesText: TextView by lazy { requireView().findViewById<TextView>(R.id.consumer_menu_trades_no_trades_text) as TextView }
    private val logoutIc: ImageView by lazy { requireView().findViewById<ImageView>(R.id.consumer_menu_logout) as ImageView }
    private val notiIc: ImageView by lazy { requireView().findViewById<ImageView>(R.id.consumer_menu_notification_bell) as ImageView }

    private val barMealsTitleTv: TextView by lazy { requireView().findViewById<TextView>(R.id.consumer_menu_bar_meals_tv) as TextView }

    private val barMealsRecyclerView: RecyclerView by lazy { requireView().findViewById<RecyclerView>(R.id.consumer_menu_bar_meals_rv) as RecyclerView }
    private val tradeMealsRecyclerView: RecyclerView by lazy { requireView().findViewById<RecyclerView>(R.id.consumer_menu_available_trades_rv) as RecyclerView }
    private val ordersRecyclerView: RecyclerView by lazy { requireView().findViewById<RecyclerView>(R.id.consumer_menu_orders_rv) as RecyclerView }
    private val barSpinner: Spinner by lazy { requireView().findViewById<Spinner>(R.id.consumer_menu_bar_select_sp) as Spinner }

    private var localMeals = mutableListOf<RetroMeal>()
    private var localTickets = mutableListOf<RetroTicket>()

    private lateinit var alertDialogManager: AlertDialogManager
    private val searchBar: EditText by lazy { requireView().findViewById<EditText>(R.id.consumer_menu_search_et) }

    override fun onCreateView(
        inflater: LayoutInflater, parent: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        return inflater.inflate(R.layout.fragment_consumer_menu, parent, false)
    }

    fun goToMenu() {
        searchBar.setOnClickListener {
            val intent = Intent(requireActivity(), ConsumerBarMenuActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val retrofit = SmartCanteenRequests().retrofit
        noAvailableTradesText.visibility = View.GONE
        alertDialogManager = AlertDialogManager(layoutInflater, requireActivity())
        alertDialogManager.createLoadingAlertDialog()

        searchBar.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val intent = Intent(requireActivity(), ConsumerBarMenuActivity::class.java)
                intent.putExtra("shouldFocusEditText", true)
                startActivity(intent)
            }
        }
        logoutIc.setOnClickListener {
            AuthHelper().doLogout(retrofit, requireActivity(), alertDialogManager)
        }


        notiIc.setOnClickListener {
            val intent = Intent(requireActivity(), NotificationActivity::class.java)
            startActivity(intent)
        }

        tradesTitleText.setOnClickListener {
            val intent = Intent(requireActivity(), ConsumerAvailableTradesActivity::class.java)
            startActivity(intent)
        }


        seeTradesText.setOnClickListener {
            val intent = Intent(requireActivity(), ConsumerAvailableTradesActivity::class.java)
            startActivity(intent)
        }

        barMealsTitleTv.setOnClickListener {
            val intent = Intent(requireActivity(), ConsumerBarMenuActivity::class.java)
            startActivity(intent)
        }

        seeMealsText.setOnClickListener {
            val intent = Intent(requireActivity(), ConsumerBarMenuActivity::class.java)
            startActivity(intent)
        }

        val db = SmartCanteenDBHelper.getInstance(requireContext().applicationContext)
        GlobalScope.launch {
            val mealsData = db.mealsDao().getAllMeals()
            if (mealsData.isNotEmpty()) {
                Log.d("MAIN", "MEALS NOT EMPTY")
                mealsData.forEach { meal ->
                    run {
                        localMeals.add(
                            RetroMeal(
                                meal.mealId,
                                meal.barId,
                                meal.name,
                                meal.preparationTime,
                                meal.description,
                                meal.canTakeAway,
                                meal.price,
                                meal.canBeMade,
                                meal.isFavorite
                            )
                        )
                    }
                }
                val barMealsAdapter = MealsAdapterRec(localMeals, requireActivity(), layoutInflater)
                buildMealsList(barMealsAdapter)
            }
            val ticketsData = db.ticketsDao().getAllTickets()
            if (ticketsData.isNotEmpty()) {
                Log.d("MAIN", "Tickets NOT EMPTY")
                ticketsData.forEach { ticket ->
                    run {
                        var ticketMeals = mutableListOf<RetroTicketMeal>()
                        var cartData = db.cartDao().getCart(ticket.cartId)
                        if (cartData != null) {
                            var cartMealsData = db.cartMealsDao().getAllCartMeals(ticket.cartId)
                            if (cartMealsData.isNotEmpty()) {
                                cartMealsData.forEach { cartMeal ->
                                    var mealChanges = mutableListOf<RetroMealChange>()
                                    var cartMealsChangesData = db.cartMealsChangesDao().getAllMealChanges(cartMeal.cartMealId)
                                    cartMealsChangesData.forEach { change ->
                                        mealChanges.add(
                                            RetroMealChange(
                                                change.cartChangeId,
                                                change.cartMealId,
                                                change.ingName,
                                                change.ingAmount,
                                                change.isRemoveOnly,
                                                change.canBeIncremented,
                                                change.canBeDecremented
                                            )
                                        )
                                    }
                                    ticketMeals.add(
                                        RetroTicketMeal(
                                            cartMeal.cartMealId,
                                            cartMeal.mealId,
                                            cartMeal.amount,
                                            cartMeal.mealPrice,
                                            cartMeal.name,
                                            cartMeal.description,
                                            cartMeal.canTakeaway,
                                            mealChanges
                                        )
                                    )
                                }

                            }
                        }
                        localTickets.add(
                            RetroTicket(
                                ticket.barname,
                                ticket.ticketid,
                                ticket.ownername,
                                ticket.stateName,
                                ticket.cartId,
                                ticket.emissionDate,
                                ticket.pickupTime,
                                ticket.ticketAmount,
                                ticket.total,
                                ticket.nEncomenda,
                                ticket.isFree,
                                ticketMeals
                            )
                        )
                    }

                }
                var ordersAdapter =
                    MenuOrdersAdapterRec(requireActivity(), getString(R.string.qty), getString(R.string.ordernum), localTickets)
                builOrdersList(ordersAdapter)
            }


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
        retrofit: Retrofit
    ) {


        val service = retrofit.create(CampusService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)


        service.getCampusBars("Bearer $token").enqueue(object : Callback<List<RetroBar>> {
            override fun onResponse(
                call: Call<List<RetroBar>>, response: Response<List<RetroBar>>
            ) {
                if (response.code() == 200) {
                    val body = response.body()

                    if (body != null) {
                        if (body.isNotEmpty()) {
                            if (isAdded) {

                                var adapter = activity?.let {
                                    ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, body.map { retroBar -> retroBar.name })
                                }
                                adapter?.setDropDownViewResource(R.layout.spinner_item)
                                barSpinner.adapter = adapter

                                barSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                                    override fun onItemSelected(
                                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                                    ) {

                                        val barId = body[position].barid

                                        Log.d("spinner:", "Before")
                                        getMealsList(
                                            barId, retrofit
                                        )
                                        Log.d("spinner:", "After")
                                    }
                                }
                            }
                        }
                    }
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(requireActivity())
                    getBarInfo(
                        retrofit
                    )
                }

            }

            override fun onFailure(call: Call<List<RetroBar>>, t: Throwable) {
                print("error")
            }

        })

    }

    private fun getMealsList(
        barId: String, retrofit: Retrofit
    ) {

        val service = retrofit.create(MealsService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        service.getBarMeals(barId, "Bearer $token").enqueue(object : Callback<List<RetroMeal>> {
            override fun onResponse(
                call: Call<List<RetroMeal>>, response: Response<List<RetroMeal>>
            ) {
                if (response.code() == 200) {
                    val body = response.body()

                    if (body != null) {
                        if (body.isNotEmpty()) {
                            /** bar Meals **/
                            if (isAdded) {
                                val db = SmartCanteenDBHelper.getInstance(requireContext().applicationContext)
                                GlobalScope.launch {
                                    body.forEach { meal ->
                                        run {
                                            if (db.mealsDao().getMeal(meal.mealid) == null) {
                                                db.mealsDao().insertAll(
                                                    Meals(
                                                        meal.mealid,
                                                        meal.barid,
                                                        meal.name,
                                                        meal.description,
                                                        meal.preparationtime,
                                                        meal.cantakeaway,
                                                        meal.price,
                                                        false,
                                                        meal.canbemade,
                                                        meal.isfavorite
                                                    )
                                                )
                                            } else {
                                                db.mealsDao().update(
                                                    Meals(
                                                        meal.mealid,
                                                        meal.barid,
                                                        meal.name,
                                                        meal.description,
                                                        meal.preparationtime,
                                                        meal.cantakeaway,
                                                        meal.price,
                                                        false,
                                                        meal.canbemade,
                                                        meal.isfavorite
                                                    )
                                                )
                                            }

                                        }
                                    }

                                }
                                val barMealsAdapter = MealsAdapterRec(body, requireActivity(), layoutInflater)
                                buildMealsList(barMealsAdapter)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<RetroMeal>>, t: Throwable) {
                print("error")
            }

        })

    }

    private fun buildMealsList(adapter: MealsAdapterRec) {
        val barMealsLinearLayoutManager = LinearLayoutManager(requireContext())
        barMealsLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        barMealsRecyclerView.layoutManager = barMealsLinearLayoutManager
        barMealsRecyclerView.itemAnimator = DefaultItemAnimator()
        barMealsRecyclerView.adapter = adapter
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

        service.getCampusTrades("Bearer $token").enqueue(object : Callback<List<RetroTrade>> {
            override fun onResponse(
                call: Call<List<RetroTrade>>, response: Response<List<RetroTrade>>
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
                            if (isAdded) {
                                val tradeMealsAdapter =
                                    TradeMealsAdapterRec(requireActivity(), getString(R.string.ordernum), getString(R.string.free), body)
                                val tradeMealsLinearLayoutManager = LinearLayoutManager(requireActivity())
                                tradeMealsLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                                tradeMealsRecyclerView.layoutManager = tradeMealsLinearLayoutManager
                                tradeMealsRecyclerView.itemAnimator = DefaultItemAnimator()
                                tradeMealsRecyclerView.adapter = tradeMealsAdapter
                            }
                        } else {
                            noAvailableTradesText.visibility = View.VISIBLE
                        }
                    }
                } else if (response.code() == 401) {
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

    private fun builOrdersList(adapter: MenuOrdersAdapterRec) {
        val ordersLinearLayoutManager = LinearLayoutManager(requireContext())
        ordersLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        ordersRecyclerView.layoutManager = ordersLinearLayoutManager
        ordersRecyclerView.itemAnimator = DefaultItemAnimator()
        ordersRecyclerView.adapter = adapter
    }

    private fun getOrdersList(
        retrofit: Retrofit
    ) {

        val service = retrofit.create(OrdersService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)
        val uid = sp.getString("uid", null)


        service.seeMyOrders("Bearer $token").enqueue(object : Callback<List<RetroTicket>> {
            override fun onResponse(
                call: Call<List<RetroTicket>>, response: Response<List<RetroTicket>>
            ) {
                if (response.code() == 200) {
                    val body = response.body()


                    Log.d("Encomendas: ", body.toString())
                    if (body != null) {
                        if (body.isNotEmpty()) {
                            /** my orders **/
                            if (isAdded) {

                                val db = SmartCanteenDBHelper.getInstance(requireContext().applicationContext)
                                GlobalScope.launch {
                                    body.forEach { ticket ->
                                        run {

                                            if (db.cartDao().getCart(ticket.cartid) == null) {
                                                db.cartDao().insertAll(
                                                    Cart(
                                                        ticket.cartid,
                                                        uid!!,
                                                        ticket.emissiondate,
                                                        true
                                                    )
                                                )
                                            } else {
                                                db.cartDao().update(
                                                    Cart(
                                                        ticket.cartid,
                                                        uid!!,
                                                        ticket.emissiondate,
                                                        true
                                                    )
                                                )
                                            }

                                            ticket.ticketmeals.forEach { meal ->
                                                run {
                                                    if (db.cartMealsDao().getCartMeal(meal.cartmealid) == null) {
                                                        db.cartMealsDao().insertAll(
                                                            CartMeals(
                                                                meal.cartmealid,
                                                                meal.mealid,
                                                                ticket.cartid,
                                                                meal.amount,
                                                                meal.mealprice,
                                                                meal.name,
                                                                meal.description,
                                                                meal.cantakeaway,
                                                            )
                                                        )
                                                    } else {
                                                        db.cartMealsDao().update(
                                                            CartMeals(
                                                                meal.cartmealid,
                                                                meal.mealid,
                                                                ticket.cartid,
                                                                meal.amount,
                                                                meal.mealprice,
                                                                meal.name,
                                                                meal.description,
                                                                meal.cantakeaway,
                                                            )
                                                        )
                                                    }

                                                    meal.mealchanges.forEach { change ->
                                                        run {
                                                            if (db.cartMealsChangesDao().getMealChange(change.cartmealchangeid) == null) {
                                                                db.cartMealsChangesDao().insertAll(
                                                                    CartMealsChanges(
                                                                        change.cartmealchangeid,
                                                                        meal.cartmealid,
                                                                        change.ingname,
                                                                        change.ingamount,
                                                                        change.isremoveonly,
                                                                        change.canbeincremented,
                                                                        change.canbedecremented
                                                                    )
                                                                )
                                                            } else {
                                                                db.cartMealsChangesDao().update(
                                                                    CartMealsChanges(
                                                                        change.cartmealchangeid,
                                                                        meal.cartmealid,
                                                                        change.ingname,
                                                                        change.ingamount,
                                                                        change.isremoveonly,
                                                                        change.canbeincremented,
                                                                        change.canbedecremented
                                                                    )
                                                                )
                                                            }

                                                        }
                                                    }
                                                }

                                            }
                                            if (db.ticketsDao().getTicket(ticket.ticketid) == null) {
                                                db.ticketsDao().insertAll(
                                                    Tickets(
                                                        ticket.ticketid,
                                                        ticket.barname,
                                                        ticket.ownername,
                                                        ticket.statename,
                                                        ticket.cartid,
                                                        ticket.emissiondate,
                                                        ticket.pickuptime,
                                                        ticket.ticketamount,
                                                        ticket.total,
                                                        ticket.norder,
                                                        ticket.isfree,
                                                    )
                                                )
                                            } else {
                                                db.ticketsDao().update(
                                                    Tickets(
                                                        ticket.ticketid,
                                                        ticket.barname,
                                                        ticket.ownername,
                                                        ticket.statename,
                                                        ticket.cartid,
                                                        ticket.emissiondate,
                                                        ticket.pickuptime,
                                                        ticket.ticketamount,
                                                        ticket.total,
                                                        ticket.norder,
                                                        ticket.isfree,
                                                    )
                                                )


                                            }

                                        }
                                    }

                                }

                                var ordersAdapter =
                                    MenuOrdersAdapterRec(requireActivity(), getString(R.string.qty), getString(R.string.ordernum), body)
                                builOrdersList(ordersAdapter)
                            }

                        }
                    }
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(requireActivity())
                    getOrdersList(
                        retrofit
                    )
                }
            }

            override fun onFailure(call: Call<List<RetroTicket>>, t: Throwable) {

                print("error")
            }

        })

    }


}

