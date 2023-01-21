package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.helpers.ImagesHelper

class BarMenuMealsAdapterRecViewHolder(inflater: LayoutInflater, val parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.bar_menu_meal_card, parent, false)){
    val titleTv = itemView.findViewById<TextView>(R.id.bar_menu_card_title_tv)
    val priceTv = itemView.findViewById<TextView>(R.id.bar_menu_card_price_tv)
    val preptimeTv = itemView.findViewById<TextView>(R.id.bar_menu_card_time_tv)
    val mealImage = itemView.findViewById<ImageView>(R.id.bar_menu_card_iv)
    private val storageRef = FirebaseStorage.getInstance().reference

    fun bindData(mealId:String, titleText:String, prepTimeText:String,priceText:String){
        titleTv.text=titleText
        preptimeTv.text = prepTimeText
        priceTv.text=priceText
        Log.d("MAIN", mealId)
        getImage(mealId)
    }

    private fun getImage(mealId:String){
        Log.d("MAIN", mealId)
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