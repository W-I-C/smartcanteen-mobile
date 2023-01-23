package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.dmoral.toasty.Toasty
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.adapters.MealAllowedChangesEditAdapterRec
import pt.ipca.smartcanteen.models.helpers.AlertDialogManager
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.models.retrofit.response.RetroAllowedChanges
import pt.ipca.smartcanteen.services.MealsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealAllowedChangesEditAdapterRecViewHolder(
    inflater: LayoutInflater,
    val parent: ViewGroup,
    val linearLayoutManager: LinearLayoutManager,
    val sp: SharedPreferences,
    val allowedChangesEditRecyclerView: RecyclerView,
    private val activity: Activity,
    private val context: Context,
    private val alertDialogManager: AlertDialogManager
) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.meals_allowed_changes_card_edit, parent, false)) {

    var ingNameTv = itemView.findViewById<TextView>(R.id.meals_allowed_change_edit_card_name)
    var typeTv = itemView.findViewById<TextView>(R.id.meals_allowed_change_edit_card_inc_dec)
    var deleteBtn = itemView.findViewById<Button>(R.id.meals_allowed_change_edit_card_delete)

    fun setDeleteClickListener(mealid: String, changeid: String, removeAllowedChangeAskString: String, alertDialogManager: AlertDialogManager) {
        deleteBtn.setOnClickListener {
            alertDialogManager.createConfirmAlertDialog(
                removeAllowedChangeAskString,
                { remove(mealid, changeid, removeAllowedChangeAskString, alertDialogManager) }
            )
        }
    }

    fun remove(mealid: String, changeid: String, removeAllowedChangeAskString: String, alertDialogManager: AlertDialogManager) {
        val retrofit = SmartCanteenRequests().retrofit
        val service = retrofit.create(MealsService::class.java)

        val token = sp.getString("token", null)

        alertDialogManager.dialog.show()

        service.removeMealChange(mealid, changeid, "Bearer $token").enqueue(object :
            Callback<List<RetroAllowedChanges>> {
            override fun onResponse(
                call: Call<List<RetroAllowedChanges>>,
                response: Response<List<RetroAllowedChanges>>
            ) {
                if (response.code() == 200) {

                    alertDialogManager.dialog.dismiss()

                    val retroFit2 = response.body()

                    if (retroFit2 != null) {
                        if (!retroFit2.isEmpty()) {
                            rebuildlistOrders(
                                MealAllowedChangesEditAdapterRec(
                                    retroFit2,
                                    sp,
                                    activity,
                                    context,
                                    allowedChangesEditRecyclerView,
                                    linearLayoutManager,
                                    alertDialogManager,
                                    removeAllowedChangeAskString
                                )
                            )
                        }
                    }

                    Toasty.success(activity, activity.getString(R.string.succes_change), Toast.LENGTH_LONG).show()
                } else if (response.code() == 500) {

                    alertDialogManager.dialog.dismiss()

                    Toasty.error(activity, activity.getString(R.string.error_change), Toast.LENGTH_LONG).show()
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(activity)
                    setDeleteClickListener(mealid, changeid, removeAllowedChangeAskString, alertDialogManager)
                }
            }

            override fun onFailure(calll: Call<List<RetroAllowedChanges>>, t: Throwable) {
                Toasty.error(activity, activity.getString(R.string.error), Toast.LENGTH_LONG).show()
                alertDialogManager.dialog.dismiss()
            }
        })
    }

    fun rebuildlistOrders(adapter: MealAllowedChangesEditAdapterRec) {
        allowedChangesEditRecyclerView.layoutManager = linearLayoutManager
        allowedChangesEditRecyclerView.itemAnimator = DefaultItemAnimator()
        allowedChangesEditRecyclerView.adapter = adapter
    }

    fun bindData(changeid: String, mealid: String, ingname: String, isremoveonly: Boolean, canbeincremented: Boolean, canbedecremented: Boolean) {

        ingNameTv.text = ingname

        if (isremoveonly) {
            typeTv.text = activity.getString(R.string.only_remove)
        } else {
            if (canbeincremented == true && canbedecremented == true) {
                typeTv.text = activity.getString(R.string.increment_decrement)
            } else if (canbeincremented == true && canbedecremented == false) {
                typeTv.text = activity.getString(R.string.only_increment)
            } else if (canbeincremented == false && canbedecremented == true) {
                typeTv.text = activity.getString(R.string.only_decrement)
            }
        }
    }
}