package pt.ipca.smartcanteen.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroBar
import pt.ipca.smartcanteen.models.RetroMeal
import pt.ipca.smartcanteen.models.adapters.BarMenuMealsAdapterRec
import pt.ipca.smartcanteen.models.adapters.MealsAdapterRec
import pt.ipca.smartcanteen.models.helpers.LoadingDialogManager
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.CampusService
import pt.ipca.smartcanteen.services.MealsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ConsumerBarMenuActivity : AppCompatActivity() {
    private val mealsProgressBar: ProgressBar by lazy {findViewById<ProgressBar>(R.id.consumer_bar_menu_meals_progress_bar) as ProgressBar }
    private val mealsTextProgress: TextView by lazy {findViewById<TextView>(R.id.consumer_bar_menu_meals_progress_bar_text) as TextView }
    private lateinit var loadingDialogManager :LoadingDialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumer_bar_menu)

        val barMealsRecyclerView = findViewById<RecyclerView>(R.id.consumer_bar_menu_rv)
        val barMealsLayoutManager = GridLayoutManager(this@ConsumerBarMenuActivity, 2)
        barMealsLayoutManager.orientation = LinearLayoutManager.VERTICAL

        loadingDialogManager = LoadingDialogManager(layoutInflater, this@ConsumerBarMenuActivity)
        loadingDialogManager.createLoadingAlertDialog()

        val barSpinner: Spinner = findViewById<Spinner>(R.id.consumer_bar_menu_bar_select_sp)
        val retrofit = SmartCanteenRequests().retrofit
        getBarInfo(barSpinner,barMealsRecyclerView,barMealsLayoutManager,retrofit)
    }


    fun getBarInfo(
        spinner: Spinner,
        barMealsRecyclerView: RecyclerView,
        barMealsLinearLayoutManager: LinearLayoutManager,
        retrofit:Retrofit
    ) {


        val service = retrofit.create(CampusService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@ConsumerBarMenuActivity)
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
                            var adapter = ArrayAdapter(
                                this@ConsumerBarMenuActivity,
                                android.R.layout.simple_spinner_item,
                                body.map { retroBar -> retroBar.name }
                            )

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                //mealsProgressBar.visibility = View.GONE
                //mealsTextProgress.visibility = View.GONE
                loadingDialogManager.dialog.dismiss()
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

        val service = retrofit.create(MealsService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@ConsumerBarMenuActivity)
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
                            val barMealsAdapter = BarMenuMealsAdapterRec(body)

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
}