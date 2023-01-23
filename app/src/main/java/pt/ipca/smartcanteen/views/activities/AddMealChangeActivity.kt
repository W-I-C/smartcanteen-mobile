package pt.ipca.smartcanteen.views.activities

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import es.dmoral.toasty.Toasty
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.helpers.AlertDialogManager
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.models.retrofit.body.MealChangeBody
import pt.ipca.smartcanteen.models.retrofit.response.RetroAllowedChanges
import pt.ipca.smartcanteen.services.MealsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddMealChangeActivity : AppCompatActivity() {

    private val ingName: EditText by lazy { findViewById<View>(R.id.add_ingredient_name_edittext) as EditText }
    private val ingDosage: EditText by lazy { findViewById<EditText>(R.id.add_ingredient_dosage_edittext) as EditText }
    private val incrementLimit: EditText by lazy { findViewById<EditText>(R.id.add_ingredient_increment_limit_edittext) as EditText }
    private val decrementLimit: EditText by lazy { findViewById<EditText>(R.id.add_ingredient_decrement_limit_edittext) as EditText }
    private val defaultValue: EditText by lazy { findViewById<EditText>(R.id.add_ingredient_default_edittext) as EditText }
    private val canbeIncremented: CheckBox by lazy { findViewById<CheckBox>(R.id.add_ingredient_checkbox_can_increment) as CheckBox }
    private val canbeDecremented: CheckBox by lazy { findViewById<CheckBox>(R.id.add_ingredient_checkbox_can_decrement) as CheckBox }
    private val isRemoveonly: CheckBox by lazy { findViewById<CheckBox>(R.id.add_ingredient_checkbox_can_isremoveonly) as CheckBox }

    private val incrementLimitTittle: TextView by lazy { findViewById<TextView>(R.id.add_ingredient_increment_limit_textview) as TextView }
    private val decrementLimitTittle: TextView by lazy { findViewById<TextView>(R.id.add_ingredient_decrement_limit_textview) as TextView }

    private val cancelBtn: Button by lazy { findViewById<Button>(R.id.add_ingredient_cancel) as Button }
    private val confirmBtn: Button by lazy { findViewById<Button>(R.id.add_ingredient_save) as Button }
    private val arrowBtn: ImageView by lazy { findViewById<ImageView>(R.id.add_ingredient_arrow) as ImageView }

    private lateinit var alertDialogManager: AlertDialogManager
    val retrofit = SmartCanteenRequests().retrofit

    var isremoveonly: Boolean = false
    var canbeincremented: Boolean = false
    var canbedecremented: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredient)

        val ingName = findViewById<EditText>(R.id.add_ingredient_name_edittext)

        incrementLimit.visibility = View.GONE
        decrementLimit.visibility = View.GONE
        incrementLimitTittle.visibility = View.GONE
        decrementLimitTittle.visibility = View.GONE


        alertDialogManager = AlertDialogManager(layoutInflater, this)
        alertDialogManager.createLoadingAlertDialog()

        val mealid = intent.getStringExtra("mealid")

        if (mealid != null) {
            make(mealid)
        }

        if (mealid != null) {
            confirmBtn.setOnClickListener {
                val ingname = ingName.text.toString()
                val ingdosage = ingDosage.text.toString()
                val incrementlimit = incrementLimit.text.toString().toIntOrNull() ?: null
                val decrementlimit = decrementLimit.text.toString().toIntOrNull() ?: null
                val defaultValue = defaultValue.text.toString().toInt()
                alertDialogManager.createConfirmAlertDialog(
                    getString(R.string.confirm_allowed_changes),
                    {
                        confirmBtn(
                            mealid,
                            ingname,
                            ingdosage,
                            isremoveonly,
                            canbeincremented,
                            canbedecremented,
                            incrementlimit,
                            decrementlimit,
                            defaultValue
                        )
                    }
                )
            }
        }

        cancelBtn.setOnClickListener {
            alertDialogManager.createConfirmAlertDialog(
                getString(R.string.cancel_operation),
                {
                    cancelBtn()
                }
            )
        }

        arrowBtn.setOnClickListener {
            finish()
        }
    }

    fun make(mealid: String) {

        isRemoveonly.setOnClickListener {
            isRemoveOnly()
        }

        canbeIncremented.setOnClickListener {
            canbeIncremented()
        }

        canbeDecremented.setOnClickListener {
            canbeDecremented()
        }
    }

    fun isRemoveOnly() {
        if (isRemoveonly.isChecked) {
            incrementLimit.visibility = View.GONE
            decrementLimit.visibility = View.GONE
            incrementLimitTittle.visibility = View.GONE
            decrementLimitTittle.visibility = View.GONE

            isremoveonly = true
            canbeincremented = false
            canbedecremented = false

            if (canbeDecremented.isChecked == true) {
                canbeDecremented.isChecked = false
            }
            if (canbeIncremented.isChecked == true) {
                canbeIncremented.isChecked = false
            }
        } else {
            isremoveonly = false
        }
    }

    fun canbeIncremented() {
        if (canbeIncremented.isChecked) {
            incrementLimit.visibility = View.VISIBLE
            incrementLimitTittle.visibility = View.VISIBLE

            isremoveonly = false
            canbeincremented = true

            if (canbeDecremented.isChecked == false) {
                decrementLimit.visibility = View.GONE
                decrementLimitTittle.visibility = View.GONE
            } else {
                decrementLimit.visibility = View.VISIBLE
                decrementLimitTittle.visibility = View.VISIBLE
            }

            if (isRemoveonly.isChecked == true) {
                isRemoveonly.isChecked = false
            }
        } else {
            canbeincremented = false

            incrementLimit.visibility = View.GONE
            incrementLimitTittle.visibility = View.GONE
        }
    }

    fun canbeDecremented() {
        if (canbeDecremented.isChecked) {
            decrementLimit.visibility = View.VISIBLE
            decrementLimitTittle.visibility = View.VISIBLE

            isremoveonly = false
            canbedecremented = true

            if (canbeIncremented.isChecked == false) {
                incrementLimit.visibility = View.GONE
                incrementLimitTittle.visibility = View.GONE
            } else {
                incrementLimit.visibility = View.VISIBLE
                incrementLimitTittle.visibility = View.VISIBLE
            }
        } else {
            canbedecremented = false
            decrementLimit.visibility = View.GONE
            decrementLimitTittle.visibility = View.GONE
        }
    }

    fun cancelBtn() {
        finish()
        Toast.makeText(this@AddMealChangeActivity, getString(R.string.canceled_operation), Toast.LENGTH_LONG)
            .show()
    }

    fun confirmBtn(
        mealid: String,
        ingname: String,
        ingdosage: String,
        isremoveonly: Boolean,
        canbeincremented: Boolean,
        canbedecremented: Boolean,
        incrementlimit: Int?,
        decrementlimit: Int?,
        defaultValue: Int
    ) {

        val service = retrofit.create(MealsService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@AddMealChangeActivity)
        val token = sp.getString("token", null)


        val body = MealChangeBody(ingname, ingdosage, isremoveonly, canbeincremented, canbedecremented, incrementlimit, decrementlimit, defaultValue)

        alertDialogManager.dialog.show()

        service.addMealChange(mealid, "Bearer $token", body).enqueue(object :
            Callback<List<RetroAllowedChanges>> {
            override fun onResponse(
                call: Call<List<RetroAllowedChanges>>,
                response: Response<List<RetroAllowedChanges>>
            ) {
                if (response.code() == 200) {

                    val body = response.body()

                    alertDialogManager.dialog.dismiss()

                    Toasty.success(this@AddMealChangeActivity, getString(R.string.success_add_meal_change), Toast.LENGTH_LONG).show()

                    finish()

                } else if (response.code() == 500) {
                    alertDialogManager.dialog.dismiss()

                    Toasty.error(this@AddMealChangeActivity, getString(R.string.error_add_meal_change), Toast.LENGTH_LONG).show()
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(this@AddMealChangeActivity)
                    confirmBtn(
                        mealid,
                        ingname,
                        ingdosage,
                        isremoveonly,
                        canbeincremented,
                        canbedecremented,
                        incrementlimit,
                        decrementlimit,
                        defaultValue
                    )
                }
            }

            override fun onFailure(call: Call<List<RetroAllowedChanges>>, t: Throwable) {
                alertDialogManager.dialog.dismiss()

                Toasty.error(this@AddMealChangeActivity, getString(R.string.error), Toast.LENGTH_LONG).show()
            }
        })
    }
}