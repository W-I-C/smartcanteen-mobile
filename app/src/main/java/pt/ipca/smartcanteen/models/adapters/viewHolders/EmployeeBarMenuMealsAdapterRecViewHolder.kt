package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import es.dmoral.toasty.Toasty
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.helpers.*
import pt.ipca.smartcanteen.services.MealsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmployeeBarMenuMealsAdapterRecViewHolder(
    val activity: Activity,
    val alertDialogManager: AlertDialogManager,
    inflater: LayoutInflater,
    val parent: ViewGroup
) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_card_meal_with_rmv, parent, false)) {
    val nameTv = itemView.findViewById<TextView>(R.id.layout_card_with_remove_title_tv)
    val timeTv = itemView.findViewById<TextView>(R.id.layout_card_with_remove_time_tv)
    val priceTv = itemView.findViewById<TextView>(R.id.layout_card_with_remove_price_tv)
    val removeIv = itemView.findViewById<ImageView>(R.id.layout_card_with_remove_delete)
    val mealImage = itemView.findViewById<ImageView>(R.id.layout_card_with_remove_iv)

    private val storageRef = FirebaseStorage.getInstance().reference

    fun bindData(
        nameText: String,
        quantityText: String,
        priceText: String,
        removeMealAskString: String,
        cantRemoveMealString: String,
        mealId: String,
        rebuildList: () -> Unit
    ) {
        nameTv.text = nameText
        timeTv.text = quantityText
        priceTv.text = priceText
        getImage(mealId)
        removeIv.setOnClickListener {
            alertDialogManager.createConfirmAlertDialog(
                removeMealAskString,
                { removeMeal(mealId, cantRemoveMealString, rebuildList) }
            )
        }


    }

    private fun getImage(mealId: String) {
        Log.d("MAIN", mealId)
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

    fun removeMeal(mealId: String, cantRemoveMealString: String, rebuildList: () -> Unit) {
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(MealsService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(activity)
        val token = sp.getString("token", null)
        alertDialogManager.dialog.dismiss()
        alertDialogManager.createLoadingAlertDialog()
        alertDialogManager.dialog.show()
        service.deleteEmployeeBarMeal(mealId, "Bearer $token").enqueue(object :
            Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.code() == 200) {

                    val body = response.body()

                    if (body != null) {
                        if (body.isNotEmpty()) {
                            rebuildList()
                            alertDialogManager.dialog.dismiss()
                        }
                    }


                } else if (response.code() == 401) {
                    alertDialogManager.dialog.dismiss()
                    AuthHelper().newSessionToken(activity)
                    removeMeal(mealId, cantRemoveMealString, rebuildList)
                } else {
                    alertDialogManager.dialog.dismiss()
                    Toasty.error(activity, cantRemoveMealString, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                alertDialogManager.dialog.dismiss()
            }

        })
    }
}