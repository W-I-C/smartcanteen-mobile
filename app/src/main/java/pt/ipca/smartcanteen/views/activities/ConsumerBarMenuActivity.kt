package pt.ipca.smartcanteen.views.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.adapters.BarMenuMealsAdapterRec
import pt.ipca.smartcanteen.models.helpers.*
import pt.ipca.smartcanteen.models.retrofit.response.RetroBar
import pt.ipca.smartcanteen.models.retrofit.response.RetroMeal
import pt.ipca.smartcanteen.services.CampusService
import pt.ipca.smartcanteen.services.MealsService
import pt.ipca.smartcanteen.views.fragments.consumer_fragments.MenuConsumerFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ConsumerBarMenuActivity : AppCompatActivity() {
    private val mealsProgressBar: ProgressBar by lazy { findViewById<ProgressBar>(R.id.consumer_bar_menu_meals_progress_bar) as ProgressBar }
    private val mealsTextProgress: TextView by lazy { findViewById<TextView>(R.id.consumer_bar_menu_meals_progress_bar_text) as TextView }
    private val backArrow: ImageView by lazy { findViewById<ImageView>(R.id.consumer_bar_menu_arrow) as ImageView }

    private val barMealsRecyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.consumer_bar_menu_rv) as RecyclerView }

    private val barSpinner: Spinner by lazy { findViewById<Spinner>(R.id.consumer_bar_menu_bar_select_sp) as Spinner }
    private val searchBar: EditText by lazy { findViewById<EditText>(R.id.consumer_menu_meals) as EditText }

    private val listRetroMeals= mutableListOf<RetroMeal>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumer_bar_menu)
        searchBar.addTextChangedListener {
            if(it!=null){
                if(it.isNotEmpty()){
                   val data= listRetroMeals.filter{item->item.name.lowercase().contains(it.toString().lowercase())}
                    buildMeals(data)
                    Log.d("teste",it.toString())
                }
            }
        }

        backArrow.setOnClickListener{
            finish()
        }

        val db = SmartCanteenDBHelper.getInstance(applicationContext)
        GlobalScope.launch {
            val mealsData = db.mealsDao().getAllMeals()
            if (mealsData.isNotEmpty()) {
                Log.d("MAIN", "MEALS NOT EMPTY")
                mealsData.forEach { meal ->
                    run {
                        listRetroMeals.add(
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
                buildMeals(listRetroMeals)
            }
        }
        getBarInfo()
    }
    private var cameFromOtherScreen = false

    override fun onResume() {
        super.onResume()
        if (intent.getBooleanExtra("shouldFocusEditText", false)) {
            searchBar.requestFocus()
        }
    }



    fun getBarInfo(
    ) {
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(CampusService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@ConsumerBarMenuActivity)
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

                            var adapter = ArrayAdapter(
                                this@ConsumerBarMenuActivity,
                                android.R.layout.simple_spinner_dropdown_item,
                                body.map { retroBar -> retroBar.name }
                            )

                            adapter.setDropDownViewResource(R.layout.spinner_item)
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


                                        getMealsList(
                                            barId,
                                            retrofit
                                        )

                                    }
                                }
                        }
                    }
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(this@ConsumerBarMenuActivity)
                    getBarInfo()
                }
            }

            override fun onFailure(call: Call<List<RetroBar>>, t: Throwable) {

            }
        })
    }

    private fun buildMeals(meals:List<RetroMeal>){
        val barMealsLayoutManager = GridLayoutManager(this@ConsumerBarMenuActivity, 2)
        barMealsLayoutManager.orientation = LinearLayoutManager.VERTICAL
        val barMealsAdapter = BarMenuMealsAdapterRec(meals, this@ConsumerBarMenuActivity, layoutInflater)
        barMealsRecyclerView.layoutManager = barMealsLayoutManager
        barMealsRecyclerView.itemAnimator = DefaultItemAnimator()
        barMealsRecyclerView.adapter = barMealsAdapter
    }
    private fun getMealsList(
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
                            body.forEach{listRetroMeals.add(it)}

                            buildMeals(body)

                        }
                    }
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(this@ConsumerBarMenuActivity)
                    getMealsList(
                        barId,
                        retrofit
                    )
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