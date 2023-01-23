package pt.ipca.smartcanteen.views.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import es.dmoral.toasty.Toasty
import org.w3c.dom.Text
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.helpers.*
import pt.ipca.smartcanteen.services.FavoritemealService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddMealCartActivity : AppCompatActivity() {

    val retrofit = SmartCanteenRequests().retrofit
    private lateinit var alertDialogManager: AlertDialogManager
    private val storageRef = FirebaseStorage.getInstance().reference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meal_details)

        alertDialogManager = AlertDialogManager(layoutInflater, this@AddMealCartActivity)
        alertDialogManager.createLoadingAlertDialog()

        val buttonIncrement = findViewById<Button>(R.id.meal_bottom_sheet_quantity_increment)
        val buttonDecrement = findViewById<Button>(R.id.meal_bottom_sheet_quantity_decrement)
        val quantity = findViewById<TextView>(R.id.meal_bottom_sheet_text_quantity_number)
        val ingredientsChange = findViewById<Button>(R.id.meal_bottom_sheet_change_ingredient)
        val favoriteHeart = findViewById<TextView>(R.id.meal_bottom_sheet_heart)
        val name = findViewById<TextView>(R.id.meal_bottom_sheet_name)
        val time = findViewById<TextView>(R.id.meal_bottom_sheet_time)
        val price = findViewById<TextView>(R.id.meal_bottom_sheet_price)
        val description = findViewById<TextView>(R.id.meal_bottom_sheet_description)
        val buttonConfirm = findViewById<Button>(R.id.meal_bottom_sheet_add_cart)
        val arrowBack = findViewById<ImageView>(R.id.meal_bottom_sheet_arrow)
        val mealImage = findViewById<ImageView>(R.id.meal_bottom_sheet_image)
        val cannotBeMadeMessage = findViewById<TextView>(R.id.meal_cannotbemade_error_message)
        val numbers = arrayListOf<String>()


        val mealid = intent.getStringExtra("mealId")
        val mealName = intent.getStringExtra("name")
        val mealPreptime = intent.getStringExtra("time")
        val mealDescription = intent.getStringExtra("description")
        val mealPrice = intent.getStringExtra("price")
        val canBeMade = intent.getBooleanExtra("canbemade", true)
        val isFavorite = intent.getBooleanExtra("isfavorite", false)

        name.text = mealName
        time.text = mealPreptime.toString()
        price.text = mealPrice.toString()
        description.text = mealDescription

        getImage(mealid!!, mealImage)

        buttonIncrement.setOnClickListener {
            var count: Int = quantity.text.toString().toInt()
            count++
            quantity.text = count.toString()
        }

        buttonDecrement.setOnClickListener {
            var count: Int = quantity.text.toString().toInt()
            if (count > 0) {
                count--
                quantity.text = count.toString()
            }
        }

        ingredientsChange.setOnClickListener {
            val intent = Intent(this@AddMealCartActivity, IngredientsChangeActivity::class.java)
            intent.putExtra("mealid", mealid)
            intent.putExtra("numbers", numbers)
            startActivity(intent)
        }

        var isChecked: Boolean = false

        println("Aqui")
        println(isFavorite)

        if(isFavorite == true){
            favoriteHeart.setBackgroundResource(R.drawable.favorite_heart)
            isChecked = true
        } else {
            favoriteHeart.setBackgroundResource(R.drawable.bottom_sheet_heart_not_clicked)
            isChecked = false
        }

        favoriteHeart.setOnClickListener {
            // true passa a false
            isChecked = !isChecked

            if (isChecked) {
                favoriteHeart.setBackgroundResource(R.drawable.favorite_heart)
                if (mealid != null) {
                    addFavMeal(mealid)
                }
            } else {
                favoriteHeart.setBackgroundResource(R.drawable.bottom_sheet_heart_not_clicked)
                if (mealid != null) {
                    removeFavMeal(mealid)
                }
            }
        }

        if(canBeMade == true){
            buttonConfirm.visibility = View.VISIBLE
            ingredientsChange.visibility = View.VISIBLE
            cannotBeMadeMessage.visibility = View.GONE
        } else {
            buttonConfirm.visibility = View.GONE
            ingredientsChange.visibility = View.GONE
            cannotBeMadeMessage.visibility = View.VISIBLE
        }

        buttonConfirm.setOnClickListener {
            alertDialogManager.createConfirmAlertDialog(
                getString(R.string.confirm_operation),
                {
                    finish()
                    Toasty.success(this@AddMealCartActivity, getString(R.string.add_meal_cart), Toast.LENGTH_LONG).show()
                }
            )
        }

        arrowBack.setOnClickListener {
            finish()
        }
    }

    private fun getImage(mealid: String, imageView: ImageView) {
        storageRef.child("images/meals/${mealid}").downloadUrl.addOnSuccessListener {
            Log.d("MAIN", it.toString())
            ImagesHelper().getImage(it.toString(), imageView, false)
        }.addOnFailureListener {
            storageRef.child("images/meals/missing_image.jpg").downloadUrl.addOnSuccessListener {
                Log.d("MAIN", it.toString())
                ImagesHelper().getImage(it.toString(), imageView, false)
            }.addOnFailureListener {
                Log.d("MAIN", it.toString())
            }
        }

    }


    fun addFavMeal(mealid: String) {
        val service = retrofit.create(FavoritemealService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@AddMealCartActivity)
        val token = sp.getString("token", null)

        alertDialogManager.dialog.show()

        service.addFavoriteMeal(mealid, "Bearer $token").enqueue(object :
            Callback<List<RetroFavoriteMeal>> {
            override fun onResponse(
                call: Call<List<RetroFavoriteMeal>>,
                response: Response<List<RetroFavoriteMeal>>
            ) {
                if (response.code() == 200) {

                    alertDialogManager.dialog.dismiss()

                } else if (response.code() == 500) {
                    alertDialogManager.dialog.dismiss()

                    Toasty.error(this@AddMealCartActivity, getString(R.string.error_add_fav_meal), Toast.LENGTH_LONG).show()
                } else if (response.code() == 401) {
                    alertDialogManager.dialog.dismiss()

                    AuthHelper().newSessionToken(this@AddMealCartActivity)
                    addFavMeal(mealid)
                }
            }

            override fun onFailure(calll: Call<List<RetroFavoriteMeal>>, t: Throwable) {
                alertDialogManager.dialog.dismiss()

                Toasty.error(this@AddMealCartActivity, getString(R.string.error), Toast.LENGTH_LONG).show()
            }
        })
    }

    fun removeFavMeal(mealid: String) {
        val service = retrofit.create(FavoritemealService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@AddMealCartActivity)
        val token = sp.getString("token", null)

        alertDialogManager.dialog.show()

        service.removeFavoriteMeal(mealid, "Bearer $token").enqueue(object :
            Callback<List<RetroFavoriteMeal>> {
            override fun onResponse(
                call: Call<List<RetroFavoriteMeal>>,
                response: Response<List<RetroFavoriteMeal>>
            ) {
                if (response.code() == 200) {

                    alertDialogManager.dialog.dismiss()

                } else if (response.code() == 500) {
                    alertDialogManager.dialog.dismiss()

                    Toasty.error(this@AddMealCartActivity, getString(R.string.error_remove_fav_meal), Toast.LENGTH_LONG).show()
                } else if (response.code() == 401) {
                    alertDialogManager.dialog.dismiss()
                    AuthHelper().newSessionToken(this@AddMealCartActivity)
                    removeFavMeal(mealid)
                }
            }

            override fun onFailure(calll: Call<List<RetroFavoriteMeal>>, t: Throwable) {
                alertDialogManager.dialog.dismiss()
                Toasty.error(this@AddMealCartActivity, getString(R.string.error), Toast.LENGTH_LONG).show()
            }
        })
    }
}