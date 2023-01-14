package pt.ipca.smartcanteen.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroBar
import pt.ipca.smartcanteen.models.RetroState
import pt.ipca.smartcanteen.models.RetroTicketMeal
import pt.ipca.smartcanteen.models.adapters.OrderDetailsAdapterRec
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.LoadingDialogManager
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.CampusService
import pt.ipca.smartcanteen.services.OrdersService
import pt.ipca.smartcanteen.services.StatesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EmployeeTicketDetails : AppCompatActivity() {
    private val deliverBtn: Button by lazy { findViewById<View>(R.id.employee_order_details_deliver_btn) as Button }
    private val backArrow: ImageView by lazy { findViewById<View>(R.id.employee_order_details_back_arrow_iv) as ImageView }

    private val detailsRv: RecyclerView by lazy { findViewById<View>(R.id.employee_order_details_rv) as RecyclerView }
    private val loadingBar: ProgressBar by lazy { findViewById<View>(R.id.employee_order_details_progress_bar) as ProgressBar }
    private val loadingText: TextView by lazy { findViewById<View>(R.id.employee_order_details_progress_bar_text) as TextView }

    private val titleTv: TextView by lazy { findViewById<View>(R.id.employee_order_details_title_tv) as TextView }

    private val stateTitleTv: TextView by lazy { findViewById<View>(R.id.employee_order_current_state_title_tv) as TextView }
    private val stateTv: TextView by lazy { findViewById<View>(R.id.employee_order_details_current_state_tv) as TextView }


    private lateinit var loadingDialogManager: LoadingDialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_ticket_details)

        val retrofit = SmartCanteenRequests().retrofit

        val ticketid = intent.getStringExtra("ticketid")
        val norder = intent.getIntExtra("norder", 0)
        val statename = intent.getStringExtra("statename")

        val orderMealsRecyclerView = findViewById<RecyclerView>(R.id.employee_order_details_rv)
        val orderMealsLinearLayoutManager = LinearLayoutManager(this@EmployeeTicketDetails)

        backArrow.setOnClickListener{
            finish()
        }

        titleTv.text = "${getString(R.string.ordernum)}: ${norder}"

        stateTitleTv.text = "${getString(R.string.current_state)}: "
        stateTv.text = statename

        loadingDialogManager = LoadingDialogManager(layoutInflater, this@EmployeeTicketDetails)
        loadingDialogManager.createLoadingAlertDialog()

        val spinner: Spinner = findViewById<View>(R.id.employee_order_details_current_state_sp) as Spinner

        getStatesInfo(spinner, retrofit, ticketid!!,statename!!, orderMealsRecyclerView, orderMealsLinearLayoutManager)


    }

    private fun getStatesInfo(
        spinner: Spinner,
        retrofit: Retrofit,
        ticketid: String,
        statename: String,
        orderMealsRecyclerView: RecyclerView,
        orderMealsLinearLayoutManager: LinearLayoutManager
    ) {
        val service = retrofit.create(StatesService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@EmployeeTicketDetails)
        val token = sp.getString("token", null)

        loadingDialogManager.dialog.show()

        service.getStates("Bearer $token").enqueue(object :
            Callback<List<RetroState>> {
            override fun onResponse(
                call: Call<List<RetroState>>,
                response: Response<List<RetroState>>
            ) {
                if (response.code() == 200) {
                    loadingDialogManager.dialog.dismiss()
                    val body = response.body()

                    if (body != null) {
                        if (body.isNotEmpty()) {
                            val states = body.sortedBy { state :RetroState -> state.priority  }
                            val adapter = ArrayAdapter(
                                this@EmployeeTicketDetails,
                                android.R.layout.simple_spinner_item,
                                states.map { state -> state.name }
                            )

                            deliverBtn.setOnClickListener {
                                setTicketState(retrofit,ticketid,states[states.size-1].stateid,true)
                                val pos = adapter.getPosition(states[states.size-1].name)
                                spinner.setSelection(pos)
                                stateTv.text = states[states.size-1].name
                            }

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.adapter = adapter

                            val pos = adapter.getPosition(statename)

                            spinner.setSelection(pos)
                            spinner.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {

                                        val stateId = states[position].stateid
                                        stateTv.text = states[position].name
                                        if (stateId == states[states.size-1].stateid)
                                            setTicketState(retrofit,ticketid, stateId, true)
                                        else
                                            setTicketState(retrofit,ticketid, stateId, false)
                                        getTicket(ticketid, orderMealsRecyclerView, orderMealsLinearLayoutManager)
                                    }
                                }

                            getTicket(ticketid, orderMealsRecyclerView, orderMealsLinearLayoutManager)
                        }
                    }
                }else if(response.code()==401){
                    AuthHelper().newSessionToken(this@EmployeeTicketDetails)
                    getStatesInfo(spinner, retrofit, ticketid,statename, orderMealsRecyclerView, orderMealsLinearLayoutManager)
                }
            }

            override fun onFailure(call: Call<List<RetroState>>, t: Throwable) {
                //mealsProgressBar.visibility = View.GONE
                //mealsTextProgress.visibility = View.GONE
                loadingDialogManager.dialog.dismiss()
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

                }
                else if(response.code()==401){
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
        finishActivity:Boolean,
    ) {


        val service = retrofit.create(OrdersService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@EmployeeTicketDetails)
        val token = sp.getString("token", null)

        loadingDialogManager.dialog.show()

        service.setTicketStates(ticketid, stateId, "Bearer $token").enqueue(object :
            Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                loadingDialogManager.dialog.dismiss()
                if (response.code() == 200) {
                    if(finishActivity){
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                //mealsProgressBar.visibility = View.GONE
                //mealsTextProgress.visibility = View.GONE
                loadingDialogManager.dialog.dismiss()
                print("error")
            }

        })

    }
}