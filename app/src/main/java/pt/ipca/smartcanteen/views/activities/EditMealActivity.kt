package pt.ipca.smartcanteen.views.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import es.dmoral.toasty.Toasty
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.adapters.MealAllowedChangesAdapterRec
import pt.ipca.smartcanteen.models.adapters.MealAllowedChangesEditAdapterRec
import pt.ipca.smartcanteen.models.helpers.*
import pt.ipca.smartcanteen.models.retrofit.body.CanBeMadeBody
import pt.ipca.smartcanteen.models.retrofit.body.MealBody
import pt.ipca.smartcanteen.models.retrofit.response.RetroAllowedChanges
import pt.ipca.smartcanteen.services.MealsService
import pt.ipca.smartcanteen.views.fragments.consumer_fragments.ProfileFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditMealActivity : AppCompatActivity() {
    private val storageRef = FirebaseStorage.getInstance().reference

    companion object {
        val IMAGE_REQUEST_CODE = 100
    }

    private val arrowBack: ImageView by lazy { findViewById<ImageView>(R.id.activity_edit_meal_back_arrow_iv) as ImageView }
    private val pencilWhite: ImageView by lazy { findViewById<ImageView>(R.id.activity_edit_meal_edit_pencil_white) as ImageView }
    private val pencilGreen: ImageView by lazy { findViewById<ImageView>(R.id.activity_edit_meal_edit_pencil_green) as ImageView }
    private val canBeMadeButton: Button by lazy { findViewById<Button>(R.id.activity_edit_meal_edit_canbemade_button) as Button }

    private val mealImage: ImageView by lazy { findViewById<ImageView>(R.id.activity_edit_meal_image) as ImageView }
    private val mealEditImage: ImageView by lazy { findViewById<ImageView>(R.id.activity_edit_meal_image_black_background) as ImageView }
    private val mealEditPhoto: TextView by lazy { findViewById<TextView>(R.id.activity_edit_meal_photo) as TextView }
    private val mealName: TextView by lazy { findViewById<TextView>(R.id.activity_edit_meal_name) as TextView }
    private val mealTime: TextView by lazy { findViewById<TextView>(R.id.activity_edit_meal_time) as TextView }
    private val mealPrice: TextView by lazy { findViewById<TextView>(R.id.activity_edit_meal_price) as TextView }
    private val mealDescriptionTitle: TextView by lazy { findViewById<TextView>(R.id.activity_edit_meal_description_tittle) as TextView }
    private val mealDescription: TextView by lazy { findViewById<TextView>(R.id.activity_edit_meal_description) as TextView }
    private val mealChangesTittle: TextView by lazy { findViewById<TextView>(R.id.activity_edit_meal_item_alt_title_tv) as TextView }
    private val canTakeAway: TextView by lazy { findViewById<TextView>(R.id.activity_edit_meal_cantakeaway) as TextView }

    private val mealNameEdit: EditText by lazy { findViewById<EditText>(R.id.activity_edit_meal_name_edittext) as EditText }
    private val mealTimeEdit: EditText by lazy { findViewById<EditText>(R.id.activity_edit_meal_time_edittext) as EditText }
    private val mealPriceEdit: EditText by lazy { findViewById<EditText>(R.id.activity_edit_meal_price_edittext) as EditText }
    private val mealDescriptionTitleEdit: TextView by lazy { findViewById<TextView>(R.id.activity_edit_meal_description_tittle_edittext) as TextView }
    private val mealDescriptionEdit: EditText by lazy { findViewById<EditText>(R.id.activity_edit_meal_description_edittext) as EditText }
    private val mealChangesTittleEdit: TextView by lazy { findViewById<TextView>(R.id.activity_edit_meal_item_alt_title_tv_edittext) as TextView }
    private val incrementBtn: ImageView by lazy { findViewById<ImageView>(R.id.activity_edit_meal_card_1_increment) as ImageView }
    private val cancelBtn: Button by lazy { findViewById<Button>(R.id.activity_edit_meal_cancel) as Button }
    private val confirmBtn: Button by lazy { findViewById<Button>(R.id.activity_edit_meal_confirm) as Button }
    private val canTakeAwayCheckBox: CheckBox by lazy { findViewById<CheckBox>(R.id.activity_edit_meal_cantakeaway_checkbox) as CheckBox }


    private val textError: TextView by lazy { findViewById<TextView>(R.id.activity_edit_meal_textError) as TextView }
    private val textErrorEdit: TextView by lazy { findViewById<TextView>(R.id.activity_edit_meal_textError_edittext) as TextView }

    private val nameTextError: TextView by lazy { findViewById<TextView>(R.id.trade_name_edit_textview_error) as TextView }
    private val timeTextError: TextView by lazy { findViewById<TextView>(R.id.trade_time_edit_textview_error) as TextView }
    private val priceTextError: TextView by lazy { findViewById<TextView>(R.id.trade_price_edit_textview_error) as TextView }
    private val descriptionTextError: TextView by lazy { findViewById<TextView>(R.id.trade_description_edit_textview_error) as TextView }

    private lateinit var mealId: String
    private var cantakeaway: Boolean = true
    private lateinit var allowedChangesEditRecyclerView: RecyclerView
    private lateinit var allowedChangesEditLayoutManager: LinearLayoutManager


    private lateinit var alertDialogManager: AlertDialogManager
    val retrofit = SmartCanteenRequests().retrofit

    private var editing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_meal)

        alertDialogManager = AlertDialogManager(layoutInflater, this)
        alertDialogManager.createLoadingAlertDialog()

        mealId = intent.getStringExtra("mealId") ?: ""
        val name = intent.getStringExtra("name")
        val time = intent.getStringExtra("time")
        val description = intent.getStringExtra("description")
        var canbemade = intent.getBooleanExtra("canbemade", true)
        val price = intent.getStringExtra("price")
        cantakeaway = intent.getBooleanExtra("cantakeaway", false)

        mealName.text = name
        mealTime.text = time
        mealPrice.text = price
        mealDescription.text = description

        if (cantakeaway == true) {
            canTakeAway.text = getString(R.string.cantakeaway)
        } else {
            canTakeAway.text = getString(R.string.cannot_takeaway)
        }

        // Recycler View usada apenas para ver - apenas faz GET
        val allowedChangesRecyclerView = findViewById<RecyclerView>(R.id.activity_edit_meal_item_alt_rv)
        val allowedChangesLayoutManager = LinearLayoutManager(this@EditMealActivity)

        // Recycler View usada para editar - faz GET  e REMOVE
        allowedChangesEditRecyclerView = findViewById<RecyclerView>(R.id.activity_edit_meal_item_alt_rv_edit)
        allowedChangesEditLayoutManager = LinearLayoutManager(this@EditMealActivity)

        allowedChangesEditRecyclerView.visibility = View.GONE

        returnButton()

        if (mealId != null) {
            getAllowedChanges(mealId, allowedChangesRecyclerView, allowedChangesLayoutManager, textError)
        }

        var CanBeMade: Boolean = true

        // se ele no início está a true, ao carregar no butão passa a false
        // se ele no início está false, ao carregar no butão passa a true
        if (canbemade == true) {
            canBeMadeButton.setText(getString(R.string.canbemade))
            //canBeMadeButton.setBackgroundColor("#000000")
            CanBeMade = true
        } else {
            canBeMadeButton.setText(getString(R.string.cannot_be_made))
            CanBeMade = false
        }

        canBeMadeButton.setOnClickListener{
            CanBeMade = !CanBeMade

            if(CanBeMade){
                if (mealId != null) {
                    canBeMade(mealId, CanBeMade)
                    canBeMadeButton.setText(getString(R.string.canbemade))
                    //warning.setBackgroundResource(R.drawable.warning_white)
                }
            } else {
                if (mealId != null) {
                    canBeMade(mealId, CanBeMade)
                    canBeMadeButton.setText(getString(R.string.cannot_be_made))
                    //warning.setBackgroundResource(R.drawable.warning_green)
                }
            }
        }

        if (mealId != null) {
            if (name != null) {
                if (time != null) {
                    if (description != null) {
                        if (price != null) {
                            edit(
                                mealId,
                                name,
                                time,
                                price,
                                description,
                                allowedChangesRecyclerView,
                                allowedChangesEditRecyclerView,
                                allowedChangesLayoutManager,
                                allowedChangesEditLayoutManager
                            )
                        }
                    }
                }
            }
        }
        getImage()

        mealNameEdit.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ProfileFragment.IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                ImagesHelper().getImageFromDevice(data.data as Uri, this@EditMealActivity, mealImage, false)
                sendImage(data.data as Uri)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if(editing){
            getAllowedChangesEdit(mealId, allowedChangesEditRecyclerView, allowedChangesEditLayoutManager, textErrorEdit)
        }

        textErrorEdit.visibility = View.GONE
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, ProfileFragment.IMAGE_REQUEST_CODE)
    }

    private fun sendImage(file: Uri) {

        val stRef = storageRef.child("images/meals/${mealId}")
        val uploadTask = stRef.putFile(file)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            Log.d("MAIN", it.toString())
            Toasty.error(this@EditMealActivity, getString(R.string.image_upload_error)).show()
        }.addOnSuccessListener { taskSnapshot ->

        }
    }

    private fun getImage() {

        storageRef.child("images/meals/${mealId}").downloadUrl.addOnSuccessListener {
            Log.d("MAIN", it.toString())
            ImagesHelper().getImage(it.toString(), mealImage, false)
        }.addOnFailureListener {
            storageRef.child("images/meals/missing_image.jpg").downloadUrl.addOnSuccessListener {
                Log.d("MAIN", it.toString())
                ImagesHelper().getImage(it.toString(), mealImage, false)
            }.addOnFailureListener {
                Log.d("MAIN", it.toString())
            }
        }

    }

    fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @SuppressLint("MissingInflatedId")
    fun edit(
        mealId: String,
        name: String,
        time: String,
        price: String,
        description: String,
        allowedChangesRecyclerView: RecyclerView,
        allowedChangesEditRecyclerView: RecyclerView,
        allowedChangesLayoutManager: RecyclerView.LayoutManager,
        allowedChangesEditLayoutManager: LinearLayoutManager
    ) {


        pencilWhite.setOnClickListener {

            editing = true

            mealEditImage.setOnClickListener {
                pickImageGallery()
            }

            pencilWhite(name, time, price, description, allowedChangesRecyclerView)

            cancelBtn.setOnClickListener {

                mealEditImage.setOnClickListener { }


                pencilWhite.visibility = View.VISIBLE
                pencilGreen.visibility = View.GONE

                mealName.visibility = View.VISIBLE
                mealTime.visibility = View.VISIBLE
                mealPrice.visibility = View.VISIBLE
                mealDescriptionTitle.visibility = View.VISIBLE
                mealDescription.visibility = View.VISIBLE
                mealChangesTittle.visibility = View.VISIBLE

                textError.visibility = View.GONE

                mealNameEdit.visibility = View.GONE
                mealTimeEdit.visibility = View.GONE
                mealPriceEdit.visibility = View.GONE
                mealDescriptionTitleEdit.visibility = View.GONE
                mealDescriptionEdit.visibility = View.GONE
                mealChangesTittleEdit.visibility = View.GONE
                incrementBtn.visibility = View.GONE
                cancelBtn.visibility = View.GONE
                confirmBtn.visibility = View.GONE

                nameTextError.visibility = View.GONE
                timeTextError.visibility = View.GONE
                priceTextError.visibility = View.GONE
                descriptionTextError.visibility = View.GONE

                mealEditImage.visibility = View.GONE
                mealEditPhoto.visibility = View.GONE

                canTakeAwayCheckBox.visibility = View.GONE
                canTakeAway.visibility = View.VISIBLE

                Toasty.error(this@EditMealActivity, getString(R.string.canceled_operation), Toast.LENGTH_LONG).show()

                allowedChangesRecyclerView.visibility = View.VISIBLE
                allowedChangesEditRecyclerView.visibility = View.GONE

                textErrorEdit.visibility = View.GONE

                getAllowedChanges(mealId, allowedChangesRecyclerView, allowedChangesLayoutManager, textError)
            }

            incrementBtn.setOnClickListener {
                val intent = Intent(this, AddMealChangeActivity::class.java)
                intent.putExtra("mealid", mealId)
                startActivity(intent)
            }

            confirmBtn.setOnClickListener {

                val mealNameText = mealNameEdit.text.toString()
                val mealTimeText = mealTimeEdit.text.toString().toInt()
                val mealDescriptionText = mealDescriptionEdit.text.toString()
                val mealPriceText = mealTimeEdit.text.toString().toFloat()

                val service = retrofit.create(MealsService::class.java)

                val sp = SharedPreferencesHelper.getSharedPreferences(this@EditMealActivity)
                val token = sp.getString("token", null)

                alertDialogManager.dialog.show()
                textError.visibility = View.GONE

                val body = MealBody(mealNameText, mealTimeText, mealDescriptionText, cantakeaway, mealPriceText)

                service.editMeal(mealId, "Bearer $token", body).enqueue(object :
                    Callback<String> {
                    override fun onResponse(
                        call: Call<String>,
                        response: Response<String>
                    ) {
                        if (response.code() == 200) {

                            alertDialogManager.dialog.dismiss()

                            Toasty.success(this@EditMealActivity, getString(R.string.edited_meal), Toast.LENGTH_LONG).show()

                            val body = response.body()

                            finish()

                        } else if (response.code() == 500) {
                            alertDialogManager.dialog.dismiss()

                            Toasty.error(this@EditMealActivity, getString(R.string.error_edit_meal), Toast.LENGTH_LONG).show()
                        } else if (response.code() == 401) {
                            alertDialogManager.dialog.dismiss()

                            allowedChangesEditRecyclerView.visibility = View.VISIBLE
                            textError.visibility = View.GONE

                            AuthHelper().newSessionToken(this@EditMealActivity)
                            getAllowedChangesEdit(mealId, allowedChangesEditRecyclerView, allowedChangesEditLayoutManager, textError)
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        alertDialogManager.dialog.dismiss()

                        allowedChangesEditRecyclerView.visibility = View.VISIBLE
                        textError.visibility = View.GONE

                        Toasty.error(this@EditMealActivity, getString(R.string.error), Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

    fun canBeMade(mealId: String, canBeMade: Boolean) {
        val service = retrofit.create(MealsService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@EditMealActivity)
        val token = sp.getString("token", null)


        val body = CanBeMadeBody(canBeMade)

        alertDialogManager.dialog.show()

        service.canBeMade(mealId, "Bearer $token", body).enqueue(object :
            Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.code() == 200) {

                    val body = response.body()

                    alertDialogManager.dialog.dismiss()

                } else if (response.code() == 500) {
                    alertDialogManager.dialog.dismiss()

                    Toasty.success(this@EditMealActivity, getString(R.string.error_meal_status), Toast.LENGTH_LONG).show()
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(this@EditMealActivity)
                    canBeMade(mealId, canBeMade)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

                Toasty.success(this@EditMealActivity, getString(R.string.error), Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getAllowedChanges(
        mealId: String,
        allowedChangesRecyclerView: RecyclerView,
        allowedChangesLinearLayoutManager: RecyclerView.LayoutManager,
        textError: TextView
    ) {

        val service = retrofit.create(MealsService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@EditMealActivity)
        val token = sp.getString("token", null)

        alertDialogManager.dialog.show()
        textError.visibility = View.GONE

        service.getAllowedChanges(mealId, "Bearer $token").enqueue(object :
            Callback<List<RetroAllowedChanges>> {
            override fun onResponse(
                call: Call<List<RetroAllowedChanges>>,
                response: Response<List<RetroAllowedChanges>>
            ) {
                if (response.code() == 200) {

                    alertDialogManager.dialog.dismiss()

                    val body = response.body()

                    if (body != null) {
                        if (body.isNotEmpty()) {
                            val allowedChangesAdapterRec = MealAllowedChangesAdapterRec(body, this@EditMealActivity)

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

                    Toasty.error(this@EditMealActivity, getString(R.string.error_allowed_changes), Toast.LENGTH_LONG).show()
                } else if (response.code() == 401) {
                    alertDialogManager.dialog.dismiss()

                    allowedChangesRecyclerView.visibility = View.VISIBLE
                    textError.visibility = View.GONE

                    AuthHelper().newSessionToken(this@EditMealActivity)
                    getAllowedChanges(mealId, allowedChangesRecyclerView, allowedChangesLinearLayoutManager, textError)
                }
            }

            override fun onFailure(calll: Call<List<RetroAllowedChanges>>, t: Throwable) {
                alertDialogManager.dialog.dismiss()

                allowedChangesRecyclerView.visibility = View.VISIBLE
                textError.visibility = View.GONE

                Toasty.error(this@EditMealActivity, getString(R.string.error), Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getAllowedChangesEdit(
        mealId: String,
        allowedChangesEditRecyclerView: RecyclerView,
        allowedChangesEditLayoutManager: LinearLayoutManager,
        textErrorEdit: TextView
    ) {

        val service = retrofit.create(MealsService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@EditMealActivity)
        val token = sp.getString("token", null)

        alertDialogManager.dialog.show()
        textError.visibility = View.GONE

        service.getAllowedChanges(mealId, "Bearer $token").enqueue(object :
            Callback<List<RetroAllowedChanges>> {
            override fun onResponse(
                call: Call<List<RetroAllowedChanges>>,
                response: Response<List<RetroAllowedChanges>>
            ) {
                if (response.code() == 200) {

                    alertDialogManager.dialog.dismiss()

                    val body = response.body()

                    if (body != null) {
                        if (body.isNotEmpty()) {
                            val allowedChangesAdapterRec = MealAllowedChangesEditAdapterRec(
                                body,
                                sp,
                                this@EditMealActivity,
                                this@EditMealActivity,
                                allowedChangesEditRecyclerView,
                                allowedChangesEditLayoutManager,
                                alertDialogManager,
                                getString(R.string.wish_remove_allowed_change_ask)
                            )

                            allowedChangesEditRecyclerView.visibility = View.VISIBLE
                            textError.visibility = View.GONE

                            allowedChangesEditRecyclerView.layoutManager = allowedChangesEditLayoutManager
                            allowedChangesEditRecyclerView.itemAnimator = DefaultItemAnimator()
                            allowedChangesEditRecyclerView.adapter = allowedChangesAdapterRec

                        } else {
                            allowedChangesEditRecyclerView.visibility = View.GONE
                            textError.visibility = View.VISIBLE
                        }
                    }

                } else if (response.code() == 500) {
                    alertDialogManager.dialog.dismiss()

                    allowedChangesEditRecyclerView.visibility = View.VISIBLE
                    textError.visibility = View.GONE

                    Toasty.error(this@EditMealActivity, getString(R.string.error_allowed_changes), Toast.LENGTH_LONG).show()
                } else if (response.code() == 401) {
                    alertDialogManager.dialog.dismiss()

                    allowedChangesEditRecyclerView.visibility = View.VISIBLE
                    textError.visibility = View.GONE

                    AuthHelper().newSessionToken(this@EditMealActivity)
                    getAllowedChangesEdit(mealId, allowedChangesEditRecyclerView, allowedChangesEditLayoutManager, textErrorEdit)
                }
            }

            override fun onFailure(calll: Call<List<RetroAllowedChanges>>, t: Throwable) {
                alertDialogManager.dialog.dismiss()

                allowedChangesEditRecyclerView.visibility = View.VISIBLE
                textError.visibility = View.GONE

                Toasty.error(this@EditMealActivity, getString(R.string.error), Toast.LENGTH_LONG).show()
            }
        })
    }

    fun pencilWhite(name: String, time: String, price: String, description: String, allowedChangesRecyclerView: RecyclerView) {
        pencilWhite.visibility = View.GONE
        pencilGreen.visibility = View.VISIBLE

        mealName.visibility = View.GONE
        mealTime.visibility = View.GONE
        mealPrice.visibility = View.GONE
        mealDescriptionTitle.visibility = View.GONE
        mealDescription.visibility = View.GONE
        mealChangesTittle.visibility = View.GONE
        canTakeAway.visibility = View.GONE

        textError.visibility = View.GONE

        mealNameEdit.visibility = View.VISIBLE
        mealTimeEdit.visibility = View.VISIBLE
        mealPriceEdit.visibility = View.VISIBLE
        mealDescriptionTitleEdit.visibility = View.VISIBLE
        mealDescriptionEdit.visibility = View.VISIBLE
        mealChangesTittleEdit.visibility = View.VISIBLE
        canTakeAwayCheckBox.visibility = View.VISIBLE
        incrementBtn.visibility = View.VISIBLE
        cancelBtn.visibility = View.VISIBLE
        confirmBtn.visibility = View.VISIBLE

        mealEditImage.visibility = View.VISIBLE
        mealEditPhoto.visibility = View.VISIBLE

        cantakeaway = canTakeAwayCheckBox.isChecked == true

        canTakeAwayCheckBox.setOnClickListener {
            cantakeaway = canTakeAwayCheckBox.isChecked == true
        }

        val nameNotNull = name
        mealNameEdit.hint = nameNotNull

        val timeNotNull = time
        mealTimeEdit.hint = timeNotNull

        val priceNotNull = price
        mealPriceEdit.hint = priceNotNull

        val descriptionNotNull = description
        mealDescriptionEdit.hint = descriptionNotNull


        if (mealNameEdit.text.isEmpty()) {
            nameTextError.visibility = View.VISIBLE
        } else {
            nameTextError.visibility = View.GONE
        }
        mealNameEdit.addTextChangedListener {
            if (it != null) {
                if (it.isEmpty()) {
                    nameTextError.visibility = View.VISIBLE
                } else {
                    nameTextError.visibility = View.GONE
                }
            }
        }

        if (mealTimeEdit.text.isEmpty()) {
            timeTextError.visibility = View.VISIBLE
        } else {
            timeTextError.visibility = View.GONE
        }
        mealTimeEdit.addTextChangedListener {
            if (it != null) {
                if (it.isEmpty()) {
                    timeTextError.visibility = View.VISIBLE
                } else {
                    timeTextError.visibility = View.GONE
                }
            }
        }

        if (mealPriceEdit.text.isEmpty()) {
            priceTextError.visibility = View.VISIBLE
        } else {
            priceTextError.visibility = View.GONE
        }
        mealPriceEdit.addTextChangedListener {
            if (it != null) {
                if (it.isEmpty()) {
                    priceTextError.visibility = View.VISIBLE
                } else {
                    priceTextError.visibility = View.GONE
                }
            }
        }

        if (mealDescriptionEdit.text.isEmpty()) {
            descriptionTextError.visibility = View.VISIBLE
        } else {
            descriptionTextError.visibility = View.GONE
        }
        mealDescriptionEdit.addTextChangedListener {
            if (it != null) {
                if (it.isEmpty()) {
                    descriptionTextError.visibility = View.VISIBLE
                } else {
                    descriptionTextError.visibility = View.GONE
                }
            }
        }

        allowedChangesRecyclerView.visibility = View.GONE
        allowedChangesEditRecyclerView.visibility = View.VISIBLE

        getAllowedChangesEdit(mealId, allowedChangesEditRecyclerView, allowedChangesEditLayoutManager, textErrorEdit)
    }

    fun returnButton() {
        arrowBack.setOnClickListener {
            finish()
        }
    }
}