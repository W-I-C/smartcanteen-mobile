package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroCartMeals
import pt.ipca.smartcanteen.models.adapters.MyFavoriteMealAdapterRec
import pt.ipca.smartcanteen.models.adapters.MyOrdersCartRec
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.RetroFavoriteMeal
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.FavoritemealService
import pt.ipca.smartcanteen.services.MealsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFavoriteMealRecViewHolder(inflater: LayoutInflater, val parent: ViewGroup, val activity: Activity, var linearLayoutManager: LinearLayoutManager, val FavAdapterRec:RecyclerView):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_card_meal_with_rmv, parent, false)){
    val nameTv = itemView.findViewById<TextView>(R.id.layout_card_with_remove_title_tv)
    val timeTv = itemView.findViewById<TextView>(R.id.layout_card_with_remove_time_tv)
    val priceTv = itemView.findViewById<TextView>(R.id.layout_card_with_remove_price_tv)
    val delete= itemView.findViewById<ImageView>(R.id.layout_card_with_remove_delete)


    fun bindData(nameText: String, quantityText: String, priceText: String){
        nameTv.text=nameText
        timeTv.text = quantityText
        priceTv.text=priceText
    }

    fun deleteMeal(mealId:String){
        delete.setOnClickListener {

            val retrofit = SmartCanteenRequests().retrofit

            val service = retrofit.create(FavoritemealService::class.java)

            val sp = SharedPreferencesHelper.getSharedPreferences(activity)
            val token = sp.getString("token", null)


            service.removeFavoriteMeal(mealId, "Bearer $token").enqueue(object :
                Callback<List<RetroFavoriteMeal>> {
                override fun onResponse(
                    call: Call<List<RetroFavoriteMeal>>,
                    response: Response<List<RetroFavoriteMeal>>
                ) {
                    if (response.code() == 200) {


                        val body = response.body()

                        if (body != null) {
                            if (body.isNotEmpty()) {

                                rebuildlistOrders(MyFavoriteMealAdapterRec(body, activity,linearLayoutManager,FavAdapterRec))
                            }
                        }
                    } else if (response.code() == 401) {
                        AuthHelper().newSessionToken(activity)
                        deleteMeal(mealId)
                    }
                }

                override fun onFailure(call: Call<List<RetroFavoriteMeal>>, t: Throwable) {
                    print("error")
                }

            })
        }
    }
    fun rebuildlistOrders(adapter: MyFavoriteMealAdapterRec) {
        FavAdapterRec.layoutManager = linearLayoutManager
        FavAdapterRec.itemAnimator = DefaultItemAnimator()
        FavAdapterRec.adapter = adapter
    }
}