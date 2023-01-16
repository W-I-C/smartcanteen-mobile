package pt.ipca.smartcanteen.views.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.MealChangeBody
import pt.ipca.smartcanteen.models.RetroAllowedChanges
import pt.ipca.smartcanteen.models.adapters.MealAllowedChangesEditAdapterRec
import pt.ipca.smartcanteen.models.helpers.AlertDialogManager
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.MealsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddMealChangeActivity : AppCompatActivity() {

    private val ingName: EditText by lazy { findViewById<View>(R.id.cart_name_ingredient) as EditText }
    private val ingDosage: EditText by lazy { findViewById<EditText>(R.id.cart_dosage_ingredient) as EditText }
    private val incrementLimit: EditText by lazy { findViewById<EditText>(R.id.cart_increment_limit) as EditText }
    private val decrementLimit: EditText by lazy { findViewById<EditText>(R.id.cart_decrement_limit) as EditText }
    private val defaultValue: EditText by lazy { findViewById<EditText>(R.id.cart_default_edittext) as EditText }
    private val canbeIncremented: CheckBox by lazy { findViewById<CheckBox>(R.id.checkbox_can_increment) as CheckBox }
    private val canbeDecremented: CheckBox by lazy { findViewById<CheckBox>(R.id.checkbox_can_decrement) as CheckBox }
    private val isRemoveonly: CheckBox by lazy { findViewById<CheckBox>(R.id.checkbox_can_isremoveonly) as CheckBox }

    private val cancelBtn: Button by lazy { findViewById<Button>(R.id.activity_add_cancel) as Button }
    private val confirmBtn: Button by lazy { findViewById<Button>(R.id.activity_add_save) as Button }

    private lateinit var alertDialogManager: AlertDialogManager
    val retrofit = SmartCanteenRequests().retrofit

    var isremoveonly: Boolean = false
    var canbeincremented: Boolean = false
    var canbedecremented: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredient)

        val ingName = findViewById<EditText>(R.id.cart_name_ingredient)


        alertDialogManager = AlertDialogManager(layoutInflater, this)
        alertDialogManager.createLoadingAlertDialog()

        val mealid = intent.getStringExtra("mealid")

        if (mealid != null) {
            make(mealid)
        }

        if (mealid != null) {
            confirmBtn.setOnClickListener{
                val ingname = ingName.text.toString()
                val ingdosage = ingDosage.text.toString()
                val incrementlimit = incrementLimit.text.toString().toIntOrNull() ?: null
                val decrementlimit = decrementLimit.text.toString().toIntOrNull() ?: null
                val defaultValue = defaultValue.text.toString().toInt()
                confirmBtn(mealid,ingname,ingdosage,isremoveonly,canbeincremented,canbedecremented,incrementlimit,decrementlimit,defaultValue)
            }
        }

        cancelBtn()
    }

    fun make(mealid: String){

        isRemoveonly.setOnClickListener {
            if (isRemoveonly.isChecked) {
                incrementLimit.visibility = View.GONE
                decrementLimit.visibility = View.GONE

                isremoveonly = true
                canbeincremented = false
                canbedecremented = false

                if(canbeDecremented.isChecked == true){
                    canbeDecremented.isChecked = false
                }
                if(canbeIncremented.isChecked == true){
                    canbeIncremented.isChecked = false
                }
            } else {
                isremoveonly = false
                incrementLimit.visibility = View.VISIBLE
                decrementLimit.visibility = View.VISIBLE
            }
        }

        canbeIncremented.setOnClickListener {
            if (canbeIncremented.isChecked) {
                incrementLimit.visibility = View.VISIBLE

                isremoveonly = false
                canbeincremented = true

                if(canbeDecremented.isChecked == false){
                    decrementLimit.visibility = View.GONE
                } else{
                    decrementLimit.visibility = View.VISIBLE

                }

                if(isRemoveonly.isChecked == true){
                    isRemoveonly.isChecked = false
                }
            } else {
                canbeincremented = false
                incrementLimit.visibility = View.VISIBLE
                decrementLimit.visibility = View.VISIBLE
            }
        }

        canbeDecremented.setOnClickListener {
            if (canbeDecremented.isChecked) {
                decrementLimit.visibility = View.VISIBLE

                isremoveonly = false
                canbedecremented = true

                if(canbeIncremented.isChecked == false){
                    incrementLimit.visibility = View.GONE
                } else{
                    incrementLimit.visibility = View.VISIBLE
                }
            } else {
                canbedecremented = false
                incrementLimit.visibility = View.VISIBLE
                decrementLimit.visibility = View.VISIBLE
            }
        }
    }

    fun cancelBtn(){
        cancelBtn.setOnClickListener{
            finish()
            Toast.makeText(this@AddMealChangeActivity, "Operação cancelada!", Toast.LENGTH_LONG)
                .show()
        }
    }

    fun confirmBtn(mealid: String, ingname: String, ingdosage: String, isremoveonly: Boolean, canbeincremented: Boolean, canbedecremented: Boolean, incrementlimit: Int?, decrementlimit: Int?, defaultValue: Int){

        println(mealid)
        println(ingname)
        println(ingdosage)
        println(isremoveonly)
        println(canbeincremented)
        println(canbedecremented)
        println(incrementlimit)
        println(decrementlimit)
        println(defaultValue)

        val service = retrofit.create(MealsService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@AddMealChangeActivity)
        val token = sp.getString("token", null)


        val body = MealChangeBody(ingname,ingdosage,isremoveonly,canbeincremented,canbedecremented,incrementlimit,decrementlimit,defaultValue)

        // TODO: meter mensagem de confimação
        alertDialogManager.dialog.show()

        service.addMealChange(mealid,"Bearer $token",body).enqueue(object :
            Callback<List<RetroAllowedChanges>> {
            override fun onResponse(
                call: Call<List<RetroAllowedChanges>>,
                response: Response<List<RetroAllowedChanges>>
            ) {
                if (response.code() == 200) {

                    val body = response.body()

                    alertDialogManager.dialog.dismiss()

                    Toast.makeText(
                        this@AddMealChangeActivity,
                        "Alteração adicionada à refeição com sucesso",
                        Toast.LENGTH_LONG
                    ).show()

                    finish()

                } else if(response.code()==500) {
                    alertDialogManager.dialog.dismiss()

                    Toast.makeText(this@AddMealChangeActivity, "Erro! Não foi possível adicionar a alterção", Toast.LENGTH_LONG)
                        .show()
                } else if(response.code()==401){
                    AuthHelper().newSessionToken(this@AddMealChangeActivity)
                    confirmBtn(mealid,ingname,ingdosage,isremoveonly,canbeincremented,canbedecremented,incrementlimit,decrementlimit,defaultValue)
                }
            }

            override fun onFailure(call: Call<List<RetroAllowedChanges>>, t: Throwable) {
                alertDialogManager.dialog.dismiss()

                Toast.makeText(this@AddMealChangeActivity, "Erro! Tente novamente", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}