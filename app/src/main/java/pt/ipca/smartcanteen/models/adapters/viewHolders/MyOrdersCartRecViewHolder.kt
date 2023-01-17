package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroBar
import pt.ipca.smartcanteen.models.RetroCartMeals
import pt.ipca.smartcanteen.models.adapters.MyOrdersCartRec
import pt.ipca.smartcanteen.models.adapters.TradesAdapterRec
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.CampusService
import pt.ipca.smartcanteen.services.MealsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyOrdersCartRecViewHolder(inflater: LayoutInflater, val parent: ViewGroup, val activity: Activity, var linearLayoutManager: LinearLayoutManager,val cartAdapterRec:RecyclerView):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.activity_cart_recycle, parent, false)){
    val nameTv = itemView.findViewById<TextView>(R.id.my_orders_cart_card_name)
    val quantityTv = itemView.findViewById<TextView>(R.id.my_orders_cart_card_quantity)
    val priceTv = itemView.findViewById<TextView>(R.id.my_orders_cart_card_price)
    val deleteIv=itemView.findViewById<ImageView>(R.id.cart_delete)
    val image=itemView.findViewById<ImageView>(R.id.my_orders_cart_card_image)

    fun bindData(nameText: String, quantityText: String, priceText: String){
        nameTv.text=nameText?:""
        quantityTv.text = quantityText
        priceTv.text=priceText
    }

    fun deleteMeal(cartmealId:String){
        deleteIv.setOnClickListener {

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

                                rebuildlistOrders(MyOrdersCartRec(body, activity,linearLayoutManager,cartAdapterRec))
                            }
                        }
                    } else if (response.code() == 401) {
                        AuthHelper().newSessionToken(activity)
                        deleteMeal(cartmealId)
                    }
                }

                override fun onFailure(call: Call<List<RetroCartMeals>>, t: Throwable) {
                    print("error")
                }

            })
        }
    }

    fun rebuildlistOrders(adapter: MyOrdersCartRec) {
        cartAdapterRec.layoutManager = linearLayoutManager
        cartAdapterRec.itemAnimator = DefaultItemAnimator()
        cartAdapterRec.adapter = adapter
    }
    }
