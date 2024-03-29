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


class MealsAdapterRecViewHolder(inflater: LayoutInflater, val parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_card, parent, false)) {
    val titleTv = itemView.findViewById<TextView>(R.id.layout_card_title_tv)
    val priceTv = itemView.findViewById<TextView>(R.id.layout_card_price_tv)
    val preptimeTv = itemView.findViewById<TextView>(R.id.layout_card_time_tv)
    val cardImage = itemView.findViewById<ImageView>(R.id.layout_card_iv)
    private val storageRef = FirebaseStorage.getInstance().reference

    fun bindData(mealId: String, titleText: String, prepTimeText: String, priceText: String) {
        titleTv.text = titleText
        preptimeTv.text = prepTimeText
        priceTv.text = priceText
        getImage(mealId)
    }

    private fun getImage(mealId: String) {

        storageRef.child("images/meals/${mealId}").downloadUrl.addOnSuccessListener {
            Log.d("MAIN", it.toString())
            ImagesHelper().getImage(it.toString(), cardImage, false)
        }.addOnFailureListener {
            storageRef.child("images/meals/missing_image.jpg").downloadUrl.addOnSuccessListener {
                Log.d("MAIN", it.toString())
                ImagesHelper().getImage(it.toString(), cardImage, false)
            }.addOnFailureListener {
                Log.d("MAIN", it.toString())
            }
        }

    }


}