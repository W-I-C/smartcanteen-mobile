package pt.ipca.smartcanteen.views.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroAllowedChanges
import pt.ipca.smartcanteen.models.adapters.AllowedChangesAdapterRec
import pt.ipca.smartcanteen.models.adapters.OrderDetailsAdapterRec
import pt.ipca.smartcanteen.models.helpers.*
import pt.ipca.smartcanteen.services.MealsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IngredientsChangeActivity: AppCompatActivity() {

    private val cancelButton: Button by lazy {findViewById<View>(R.id.ingredients_change_cancel) as Button }
    private val saveButton: Button by lazy {findViewById<View>(R.id.ingredients_change_save) as Button }

    val retrofit = SmartCanteenRequests().retrofit
    private lateinit var alertDialogManager: AlertDialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredients_change)

        alertDialogManager = AlertDialogManager(layoutInflater, this@IngredientsChangeActivity)
        alertDialogManager.createLoadingAlertDialog()

        val mealid = intent.getStringExtra("mealid")
        val numbers = intent.getSerializableExtra("numbers") as ArrayList<String>

        val allowedChangesRecyclerView = findViewById<RecyclerView>(R.id.ingredients_change_recycler_view)
        val textError = findViewById<TextView>(R.id.ingredients_change_text_error)
        val allowedChangesLinearLayoutManager = LinearLayoutManager(this@IngredientsChangeActivity)

        if (mealid != null) {
            getAllowedChanges(mealid,allowedChangesRecyclerView,allowedChangesLinearLayoutManager,textError, numbers)
        }

        cancelButton.setOnClickListener {
            finish()
            Toast.makeText(this@IngredientsChangeActivity, getString(R.string.canceled_operation), Toast.LENGTH_LONG)
                .show()
        }

        // TODO: saveButtom o POST para adicionar as alterações à Meal
        saveButton.setOnClickListener {
            finish()
            Toast.makeText(this@IngredientsChangeActivity, getString(R.string.success_change_allowed_changes), Toast.LENGTH_LONG)
                .show()
        }
    }

    fun getAllowedChanges(mealid: String, allowedChangesRecyclerView:RecyclerView, allowedChangesLinearLayoutManager: RecyclerView.LayoutManager, textError: TextView,numbers: ArrayList<String>){
        val service = retrofit.create(MealsService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@IngredientsChangeActivity)
        val token = sp.getString("token", null)

        alertDialogManager.dialog.show()
        textError.visibility = View.GONE

        service.getAllowedChanges(mealid, "Bearer $token").enqueue(object :
            Callback<List<RetroAllowedChanges>> {
            override fun onResponse(
                call: Call<List<RetroAllowedChanges>>,
                response: Response<List<RetroAllowedChanges>>
            ) {
                if (response.code() == 200) {

                    alertDialogManager.dialog.dismiss()

                    Toast.makeText(this@IngredientsChangeActivity, getString(R.string.success_allowed_changes), Toast.LENGTH_LONG)
                        .show()

                    val body = response.body()

                    if (body != null) {
                        if (body.isNotEmpty()) {
                            val allowedChangesAdapterRec = AllowedChangesAdapterRec(body)

                            allowedChangesRecyclerView.visibility = View.VISIBLE
                            textError.visibility = View.GONE

                            allowedChangesRecyclerView.layoutManager = allowedChangesLinearLayoutManager
                            allowedChangesRecyclerView.itemAnimator = DefaultItemAnimator()
                            allowedChangesRecyclerView.adapter = allowedChangesAdapterRec

                        } else {
                            allowedChangesRecyclerView.visibility = View.GONE
                            textError.visibility = View.VISIBLE
                        }
                    }

                } else if (response.code() == 500) {
                    alertDialogManager.dialog.dismiss()

                    allowedChangesRecyclerView.visibility = View.VISIBLE
                    textError.visibility = View.GONE

                    Toast.makeText(this@IngredientsChangeActivity, getString(R.string.error_allowed_changes), Toast.LENGTH_LONG)
                        .show()
                } else if(response.code() == 401){
                    alertDialogManager.dialog.dismiss()

                    allowedChangesRecyclerView.visibility = View.VISIBLE
                    textError.visibility = View.GONE

                    AuthHelper().newSessionToken(this@IngredientsChangeActivity)
                    getAllowedChanges(mealid,allowedChangesRecyclerView,allowedChangesLinearLayoutManager,textError,numbers)
                }
            }

            override fun onFailure(calll: Call<List<RetroAllowedChanges>>, t: Throwable) {
                alertDialogManager.dialog.dismiss()

                allowedChangesRecyclerView.visibility = View.VISIBLE
                textError.visibility = View.GONE

                Toast.makeText(this@IngredientsChangeActivity, getString(R.string.error), Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}