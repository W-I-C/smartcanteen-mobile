package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import es.dmoral.toasty.Toasty
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.helpers.AlertDialogManager
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
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

        removeIv.setOnClickListener {
            alertDialogManager.createConfirmAlertDialog(
                removeMealAskString,
                { removeMeal(mealId, cantRemoveMealString,rebuildList) }
            )
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
                    removeMeal(mealId,cantRemoveMealString, rebuildList)
                }else{
                    alertDialogManager.dialog.dismiss()
                    Toasty.error(activity, cantRemoveMealString, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                print("error")
                alertDialogManager.dialog.dismiss()
            }

        })
    }
}