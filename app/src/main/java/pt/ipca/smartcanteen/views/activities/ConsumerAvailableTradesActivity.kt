package pt.ipca.smartcanteen.views.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.adapters.ConsumerAvailableTradesAdapterRec
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.models.retrofit.response.RetroBar
import pt.ipca.smartcanteen.models.retrofit.response.RetroTrade
import pt.ipca.smartcanteen.services.CampusService
import pt.ipca.smartcanteen.services.TradesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ConsumerAvailableTradesActivity : AppCompatActivity() {
    private val mealsProgressBar: ProgressBar by lazy { findViewById<ProgressBar>(R.id.consumer_trades_progress_bar) as ProgressBar }
    private val mealsTextProgress: TextView by lazy { findViewById<TextView>(R.id.consumer_trades_progress_bar_text) as TextView }
    private val backArrow: ImageView by lazy { findViewById<ImageView>(R.id.consumer_trades_arrow) as ImageView }

    private val tradesRv: RecyclerView by lazy { findViewById<RecyclerView>(R.id.consumer_trades_rv) as RecyclerView }

    private val barSpinner: Spinner by lazy { findViewById<Spinner>(R.id.consumer_trades_bar_select_sp) as Spinner }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumer_available_trades)

        getBarInfo()

        backArrow.setOnClickListener{
            finish()
        }
    }

    fun getBarInfo(
    ) {
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(CampusService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@ConsumerAvailableTradesActivity)
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


                            Log.d("bar:", body.toString())
                            val adapter = ArrayAdapter(
                                this@ConsumerAvailableTradesActivity,
                                android.R.layout.simple_spinner_item,
                                body.map { retroBar -> retroBar.name }
                            )

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
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


                                        getTradeList(
                                            retrofit,
                                            barId
                                        )

                                    }
                                }
                        }
                    }
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(this@ConsumerAvailableTradesActivity)
                    getBarInfo()
                }
            }

            override fun onFailure(call: Call<List<RetroBar>>, t: Throwable) {

            }
        })
    }

    private fun getTradeList(
        retrofit: Retrofit,
        barId: String
    ) {

        val service = retrofit.create(TradesService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@ConsumerAvailableTradesActivity)
        val token = sp.getString("token", null)

        mealsProgressBar.visibility = View.VISIBLE
        mealsTextProgress.visibility = View.VISIBLE
        tradesRv.visibility = View.GONE
        service.getCampusTrades("Bearer $token").enqueue(object :
            Callback<List<RetroTrade>> {
            override fun onResponse(
                call: Call<List<RetroTrade>>,
                response: Response<List<RetroTrade>>
            ) {
                mealsProgressBar.visibility = View.GONE
                mealsTextProgress.visibility = View.GONE
                tradesRv.visibility = View.VISIBLE
                if (response.code() == 200) {
                    val body = response.body()
                    if (body != null) {
                        if (body.isNotEmpty()) {
                            val trades = body.filter { it.barid == barId }
                            val barMealsAdapter = ConsumerAvailableTradesAdapterRec(trades, this@ConsumerAvailableTradesActivity)
                            val barMealsLayoutManager = LinearLayoutManager(this@ConsumerAvailableTradesActivity)
                            tradesRv.layoutManager = barMealsLayoutManager
                            tradesRv.itemAnimator = DefaultItemAnimator()
                            tradesRv.adapter = barMealsAdapter
                        }
                    }
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(this@ConsumerAvailableTradesActivity)
                    getTradeList(
                        retrofit,
                        barId
                    )
                }
            }

            override fun onFailure(call: Call<List<RetroTrade>>, t: Throwable) {
                mealsProgressBar.visibility = View.GONE
                mealsTextProgress.visibility = View.GONE
                tradesRv.visibility = View.VISIBLE
            }

        })

    }
}