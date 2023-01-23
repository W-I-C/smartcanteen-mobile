package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R

class OrderDetailsMealsChangesViewHolder(inflater: LayoutInflater, val parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.ingredients_change_card, parent, false)) {
    val ingNameTv = itemView.findViewById<TextView>(R.id.ingredients_change_card_1_name)

    // incrementar e decrementar
    val decrementCircle1 = itemView.findViewById<ImageView>(R.id.ingredients_change_card_1_decrement_circle)
    val decrementCircle1Btn = itemView.findViewById<ImageView>(R.id.ingredients_change_card_1_decrement)

    val quantity1 = itemView.findViewById<TextView>(R.id.ingredients_change_card_1_quantity)

    val incrementCircle1 = itemView.findViewById<ImageView>(R.id.ingredients_change_card_1_increment_circle)
    val incrementCircle1Btn = itemView.findViewById<ImageView>(R.id.ingredients_change_card_1_increment)

    // só incrementar ou só decrementar
    val quantity2 = itemView.findViewById<TextView>(R.id.ingredients_change_card_2_quantity)

    val incrementCircle2 = itemView.findViewById<ImageView>(R.id.ingredients_change_card_2_increment_circle)
    val incrementCircle2Btn = itemView.findViewById<ImageView>(R.id.ingredients_change_card_2_increment)

    val decrementCircle3 = itemView.findViewById<ImageView>(R.id.ingredients_change_card_3_decrement_circle)
    val decrementCircle3Btn = itemView.findViewById<ImageView>(R.id.ingredients_change_card_3_decrement)

    // só remover e adicionar
    //    remover
    val removeWhiteBckg = itemView.findViewById<ImageView>(R.id.ingredients_change_card_4_uncheck_circle)
    val removeGreenBckg = itemView.findViewById<ImageView>(R.id.ingredients_change_card_4_uncheck_circle_green)
    val removeBlack = itemView.findViewById<ImageView>(R.id.ingredients_change_card_4_remove)
    val removeWhite = itemView.findViewById<ImageView>(R.id.ingredients_change_card_4_remove_white)

    //    adicionar
    val addWhiteBckg = itemView.findViewById<ImageView>(R.id.ingredients_change_card_4_check_circle)
    val addGreenBckg = itemView.findViewById<ImageView>(R.id.ingredients_change_card_4_check_circle_green)
    val addBlack = itemView.findViewById<ImageView>(R.id.ingredients_change_card_4_check)
    val addWhite = itemView.findViewById<ImageView>(R.id.ingredients_change_card_4_check_white)


    fun bindData(title: String, amount: Int, isremoveonly: Boolean) {

        // incrementar e decrementar
        decrementCircle1.visibility = View.GONE
        decrementCircle1Btn.visibility = View.GONE
        quantity1.visibility = View.GONE
        incrementCircle1.visibility = View.GONE
        incrementCircle1Btn.visibility = View.GONE
        // só incrementar ou só decrementar
        quantity2.visibility = View.GONE
        incrementCircle2.visibility = View.GONE
        incrementCircle2Btn.visibility = View.GONE
        decrementCircle3.visibility = View.GONE
        decrementCircle3Btn.visibility = View.GONE
        // só remover e adicionar
        removeWhiteBckg.visibility = View.GONE
        removeGreenBckg.visibility = View.GONE
        removeWhite.visibility = View.GONE
        removeBlack.visibility = View.GONE
        addWhiteBckg.visibility = View.GONE
        addGreenBckg.visibility = View.GONE
        addBlack.visibility = View.GONE
        addWhite.visibility = View.GONE

        ingNameTv.text = title
        if (isremoveonly) {
            if (amount == 0) {
                // removido
                removeGreenBckg.visibility = View.VISIBLE
                removeWhite.visibility = View.VISIBLE

                addWhiteBckg.visibility = View.VISIBLE
                addBlack.visibility = View.VISIBLE

            } else {
                // adicionado
                removeWhiteBckg.visibility = View.VISIBLE
                removeBlack.visibility = View.VISIBLE

                addGreenBckg.visibility = View.VISIBLE
                addWhite.visibility = View.VISIBLE
            }
        } else {
            quantity2.visibility = View.VISIBLE
            quantity2.text = amount.toString()
        }
    }
}