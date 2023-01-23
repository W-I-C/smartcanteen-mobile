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
import pt.ipca.smartcanteen.models.adapters.MyOrdersCartRec
import pt.ipca.smartcanteen.models.helpers.*
import pt.ipca.smartcanteen.models.retrofit.response.RetroCartMeals
import pt.ipca.smartcanteen.services.MealsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyOrdersCartRecViewHolder(
    inflater: LayoutInflater,
    val parent: ViewGroup,
    val activity: Activity,
    var linearLayoutManager: LinearLayoutManager,
    val cartAdapterRec: RecyclerView
) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.cart_meal_card, parent, false)) {
    val nameTv = itemView.findViewById<TextView>(R.id.my_orders_cart_card_name)
    val quantityTv = itemView.findViewById<TextView>(R.id.my_orders_cart_card_quantity)
    val priceTv = itemView.findViewById<TextView>(R.id.my_orders_cart_card_price)
    val deleteIv = itemView.findViewById<ImageView>(R.id.cart_delete)
    val image = itemView.findViewById<ImageView>(R.id.my_orders_cart_card_image)
    private val storageRef = FirebaseStorage.getInstance().reference
    fun bindData(mealId: String, nameText: String, quantityText: String, priceText: String) {
        nameTv.text = nameText
        quantityTv.text = quantityText
        priceTv.text = priceText
        getImage(mealId)
    }

    private fun getImage(mealId: String) {

        storageRef.child("images/meals/${mealId}").downloadUrl.addOnSuccessListener {
            Log.d("MAIN", it.toString())
            ImagesHelper().getImage(it.toString(), image, false)
        }.addOnFailureListener {
            storageRef.child("images/meals/missing_image.jpg").downloadUrl.addOnSuccessListener {
                Log.d("MAIN", it.toString())
                ImagesHelper().getImage(it.toString(), image, false)
            }.addOnFailureListener {
                Log.d("MAIN", it.toString())
            }
        }

    }

    fun deleteMeal(cartmealId: String,alertDialogManager: AlertDialogManager, string:String){
        deleteIv.setOnClickListener{
            alertDialogManager.createConfirmAlertDialog(
                string,
                { delete(cartmealId,alertDialogManager,string) }
            )
        }
    }

    fun delete(cartmealId: String,alertDialogManager: AlertDialogManager, string:String) {


            val retrofit = SmartCanteenRequests().retrofit

            val service = retrofit.create(MealsService::class.java)

            val sp = SharedPreferencesHelper.getSharedPreferences(activity)
            val token = sp.getString("token", null)


            service.deleteMealsCart(cartmealId, "Bearer $token").enqueue(object :
                Callback<List<RetroCartMeals>> {
                override fun onResponse(
                    call: Call<List<RetroCartMeals>>,
                    response: Response<List<RetroCartMeals>>
                ) {
                    if (response.code() == 200) {


                        val body = response.body()

                        if (body != null) {
                            if (body.isNotEmpty()) {

                                rebuildlistOrders(MyOrdersCartRec(body, activity, linearLayoutManager, cartAdapterRec, string,alertDialogManager))
                            }
                        }
                    } else if (response.code() == 401) {
                        AuthHelper().newSessionToken(activity)
                        delete(cartmealId,alertDialogManager,string)
                    }
                }

                override fun onFailure(call: Call<List<RetroCartMeals>>, t: Throwable) {
                    print("error")
                }

            })

    }

    fun rebuildlistOrders(adapter: MyOrdersCartRec) {
        cartAdapterRec.layoutManager = linearLayoutManager
        cartAdapterRec.itemAnimator = DefaultItemAnimator()
        cartAdapterRec.adapter = adapter
    }
}
