package pt.ipca.smartcanteen.models.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import es.dmoral.toasty.Toasty
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.adapters.viewHolders.MealsAdapterRecViewHolder
import pt.ipca.smartcanteen.models.RetroMeal
import pt.ipca.smartcanteen.models.helpers.*
import pt.ipca.smartcanteen.services.FavoritemealService
import pt.ipca.smartcanteen.views.activities.AddMealCartActivity
import pt.ipca.smartcanteen.views.activities.EditMealActivity
import pt.ipca.smartcanteen.views.activities.IngredientsChangeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealsAdapterRec(private var mealsList: List<RetroMeal>, private var activity: Activity, private var layoutInflater: LayoutInflater) :
    RecyclerView.Adapter<MealsAdapterRecViewHolder>() {

    val retrofit = SmartCanteenRequests().retrofit
    private lateinit var alertDialogManager: AlertDialogManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MealsAdapterRecViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: MealsAdapterRecViewHolder, position: Int) {
        val meal = mealsList.get(position)
        val mealid = meal.mealid
        val description = meal.description
        val mealName = meal.name
        val title = meal.name
        val price = "${meal.price}€"
        val preptime = "${meal.preparationtime}min"
        val url = meal.url?:""
        holder.bindData(title,preptime,price,url)

        holder.itemView.setOnClickListener{
            mealDetails(mealid,mealName,description,price,preptime, url)
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    fun mealDetails(mealid: String,mealName: String,mealDescription: String,mealPrice: String, mealPreptime: String, url:String) {
        var intent = Intent(activity, AddMealCartActivity::class.java)
        intent.putExtra("mealId", mealid)
        intent.putExtra("name", mealName)
        intent.putExtra("description", mealDescription)
        intent.putExtra("price", mealPrice)
        intent.putExtra("time", mealPreptime)
        intent.putExtra("url", url)
        activity.startActivity(intent)
    }
}