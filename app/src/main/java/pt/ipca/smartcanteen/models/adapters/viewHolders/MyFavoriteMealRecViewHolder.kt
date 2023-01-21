package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroCartMeals
import pt.ipca.smartcanteen.models.adapters.MyFavoriteMealAdapterRec
import pt.ipca.smartcanteen.models.adapters.MyOrdersCartRec
import pt.ipca.smartcanteen.models.helpers.*
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
    val mealImage= itemView.findViewById<ImageView>(R.id.layout_card_with_remove_iv)
    private val storageRef = FirebaseStorage.getInstance().reference


    fun bindData(mealId: String,nameText: String, quantityText: String, priceText: String){
        nameTv.text=nameText
        timeTv.text = quantityText
        priceTv.text=priceText
        getImage(mealId)
        deleteMeal(mealId)

    }
    private fun getImage(mealId:String){

        storageRef.child("images/meals/${mealId}").downloadUrl.addOnSuccessListener {
            Log.d("MAIN", it.toString())
            ImagesHelper().getImage(it.toString(),mealImage,false)
        }.addOnFailureListener {
            storageRef.child("images/meals/missing_image.jpg").downloadUrl.addOnSuccessListener {
                Log.d("MAIN", it.toString())
                ImagesHelper().getImage(it.toString(),mealImage,false)
            }.addOnFailureListener {
                Log.d("MAIN", it.toString())
            }
        }

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