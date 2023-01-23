package pt.ipca.smartcanteen.views.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.adapters.OrderDetailsAdapterRec
import pt.ipca.smartcanteen.models.helpers.AlertDialogManager
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.models.retrofit.response.RetroState
import pt.ipca.smartcanteen.models.retrofit.response.RetroTicketMeal
import pt.ipca.smartcanteen.services.OrdersService
import pt.ipca.smartcanteen.services.StatesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class EmployeeTicketDetails : AppCompatActivity() {
    private val deliverBtn: Button by lazy { findViewById<View>(R.id.employee_order_details_deliver_btn) as Button }

    private val inPrepBtn: Button by lazy { findViewById<View>(R.id.employee_order_details_started_btn) as Button }
    private val delayedBtn: Button by lazy { findViewById<View>(R.id.employee_order_details_delayed_btn) as Button }
    private val readyBtn: Button by lazy { findViewById<View>(R.id.employee_order_details_ready_btn) as Button }

    private val backArrow: ImageView by lazy { findViewById<View>(R.id.employee_order_details_back_arrow_iv) as ImageView }

    private val detailsRv: RecyclerView by lazy { findViewById<View>(R.id.employee_order_details_rv) as RecyclerView }
    private val loadingBar: ProgressBar by lazy { findViewById<View>(R.id.employee_order_details_progress_bar) as ProgressBar }
    private val loadingText: TextView by lazy { findViewById<View>(R.id.employee_order_details_progress_bar_text) as TextView }

    private val titleTv: TextView by lazy { findViewById<View>(R.id.employee_order_details_title_tv) as TextView }

    private val stateTitleTv: TextView by lazy { findViewById<View>(R.id.employee_order_current_state_title_tv) as TextView }
    private val stateTv: TextView by lazy { findViewById<View>(R.id.employee_order_details_current_state_tv) as TextView }

    private lateinit var currentState: String
    private lateinit var alertDialogManager: AlertDialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_ticket_details)

        val retrofit = SmartCanteenRequests().retrofit

        val ticketid = intent.getStringExtra("ticketid")
        val norder = intent.getIntExtra("norder", 0)
        currentState = intent.getStringExtra("statename") ?: ""

        val orderMealsRecyclerView = findViewById<RecyclerView>(R.id.employee_order_details_rv)
        val orderMealsLinearLayoutManager = LinearLayoutManager(this@EmployeeTicketDetails)

        backArrow.setOnClickListener {
            finish()
        }

        titleTv.text = "${getString(R.string.ordernum)}: ${norder}"

        stateTitleTv.text = "${getString(R.string.current_state)}: "
        stateTv.text = currentState

        alertDialogManager = AlertDialogManager(layoutInflater, this@EmployeeTicketDetails)
        alertDialogManager.createLoadingAlertDialog()


        getStatesInfo(retrofit, ticketid!!, orderMealsRecyclerView, orderMealsLinearLayoutManager)


    }

    private fun getStatesInfo(
        retrofit: Retrofit,
        ticketid: String,
        orderMealsRecyclerView: RecyclerView,
        orderMealsLinearLayoutManager: LinearLayoutManager
    ) {
        val service = retrofit.create(StatesService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@EmployeeTicketDetails)
        val token = sp.getString("token", null)

        alertDialogManager.dialog.show()

        service.getStates("Bearer $token").enqueue(object :
            Callback<List<RetroState>> {
            override fun onResponse(
                call: Call<List<RetroState>>,
                response: Response<List<RetroState>>
            ) {
                if (response.code() == 200) {
                    alertDialogManager.dialog.dismiss()
                    val body = response.body()

                    if (body != null) {
                        if (body.isNotEmpty()) {
                            val states = body.sortedBy { state: RetroState -> state.priority }

                            deliverBtn.setOnClickListener {
                                setTicketState(retrofit, ticketid, states[states.size - 1].stateid, true)
                                stateTv.text = states[states.size - 1].name
                            }

                            inPrepBtn.setOnClickListener {
                                setTicketState(retrofit, ticketid, states[1].stateid, true)
                                stateTv.text = states[1].name
                                delayedBtn.setBackgroundResource(R.drawable.button_grey)
                                readyBtn.setBackgroundResource(R.drawable.button_grey)
                                it.setBackgroundResource(R.drawable.rounded_button_style)
                            }

                            delayedBtn.setOnClickListener {
                                setTicketState(retrofit, ticketid, states[2].stateid, true)
                                stateTv.text = states[2].name
                                inPrepBtn.setBackgroundResource(R.drawable.button_grey)
                                readyBtn.setBackgroundResource(R.drawable.button_grey)
                                it.setBackgroundResource(R.drawable.rounded_button_style)
                            }

                            readyBtn.setOnClickListener {
                                setTicketState(retrofit, ticketid, states[3].stateid, true)
                                stateTv.text = states[3].name
                                delayedBtn.setBackgroundResource(R.drawable.button_grey)
                                inPrepBtn.setBackgroundResource(R.drawable.button_grey)
                                it.setBackgroundResource(R.drawable.rounded_button_style)
                            }



                            getTicket(ticketid, orderMealsRecyclerView, orderMealsLinearLayoutManager)
                        }
                    }
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(this@EmployeeTicketDetails)
                    getStatesInfo(retrofit, ticketid, orderMealsRecyclerView, orderMealsLinearLayoutManager)
                }
            }

            override fun onFailure(call: Call<List<RetroState>>, t: Throwable) {
                //mealsProgressBar.visibility = View.GONE
                //mealsTextProgress.visibility = View.GONE
                alertDialogManager.dialog.dismiss()
                print("error")
            }

        })

    }

    private fun getTicket(
        ticketid: String,
        orderMealsRecyclerView: RecyclerView,
        orderMealsLinearLayoutManager: RecyclerView.LayoutManager
    ) {
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(OrdersService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@EmployeeTicketDetails)
        val token = sp.getString("token", null)
        detailsRv.visibility = View.INVISIBLE
        loadingBar.visibility = View.VISIBLE
        loadingText.visibility = View.VISIBLE

        service.getTicketDetails(ticketid, "Bearer $token").enqueue(object :
            Callback<List<RetroTicketMeal>> {
            override fun onResponse(
                call: Call<List<RetroTicketMeal>>,
                response: Response<List<RetroTicketMeal>>
            ) {
                if (response.code() == 200) {

                    val body = response.body()

                    if (body != null) {
                        if (body.isNotEmpty()) {
                            val ordersAdapter = OrderDetailsAdapterRec(body)


                            orderMealsRecyclerView.layoutManager = orderMealsLinearLayoutManager
                            orderMealsRecyclerView.itemAnimator = DefaultItemAnimator()
                            orderMealsRecyclerView.adapter = ordersAdapter

                        }
                    }
                    detailsRv.visibility = View.VISIBLE
                    loadingBar.visibility = View.GONE
                    loadingText.visibility = View.GONE

                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(this@EmployeeTicketDetails)
                    getTicket(ticketid, orderMealsRecyclerView, orderMealsLinearLayoutManager)
                }
            }

            override fun onFailure(call: Call<List<RetroTicketMeal>>, t: Throwable) {
                print("error")
                detailsRv.visibility = View.VISIBLE
                loadingBar.visibility = View.GONE
                loadingText.visibility = View.GONE
            }

        })
    }


    private fun setTicketState(
        retrofit: Retrofit,
        ticketid: String,
        stateId: String,
        finishActivity: Boolean,
    ) {


        val service = retrofit.create(OrdersService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@EmployeeTicketDetails)
        val token = sp.getString("token", null)



        service.setTicketStates(ticketid, stateId, "Bearer $token").enqueue(object :
            Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {

                if (response.code() == 200) {
                    if (finishActivity) {
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

                print("error")
            }

        })

    }
}