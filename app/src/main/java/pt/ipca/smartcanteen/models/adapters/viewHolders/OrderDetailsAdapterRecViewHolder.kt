package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroMealChange
import pt.ipca.smartcanteen.models.adapters.OrderDetailsMealsChangesAdapterRec
import pt.ipca.smartcanteen.models.helpers.ImagesHelper


class OrderDetailsAdapterRecViewHolder( inflater: LayoutInflater,private val context: Context, val parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.order_detail_card_layout, parent, false)){
    val titleTv = itemView.findViewById<TextView>(R.id.order_detail_item_title_tv)
    val dosageTv = itemView.findViewById<TextView>(R.id.order_detail_item_dosage_tv)
    val descritionTv = itemView.findViewById<TextView>(R.id.order_detail_item_desc_tv)
    val changesRv = itemView.findViewById<RecyclerView>(R.id.order_detail_item_alt_rv)
    val mealImage = itemView.findViewById<ImageView>(R.id.order_detail_item_meal_iv)

    private val storageRef = FirebaseStorage.getInstance().reference

    fun bindData(mealId:String, title:String,dosage:String,description:String, changes:List<RetroMealChange>){
        titleTv.text= title
        dosageTv.text = dosage
        descritionTv.text= description
        getImage(mealId)
        val ordersAdapter = OrderDetailsMealsChangesAdapterRec(changes)
        val orderMealsLinearLayoutManager = LinearLayoutManager(parent.context)
        orderMealsLinearLayoutManager.orientation=LinearLayoutManager.VERTICAL
        changesRv.layoutManager = orderMealsLinearLayoutManager
        changesRv.itemAnimator = DefaultItemAnimator()
        changesRv.adapter = ordersAdapter

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
}