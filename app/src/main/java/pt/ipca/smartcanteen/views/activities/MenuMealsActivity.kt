package pt.ipca.smartcanteen.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import pt.ipca.smartcanteen.R

class MenuMealsActivity : AppCompatActivity() {

    private val button: Button by lazy {findViewById<View>(R.id.menu_meals_button) as Button }

    // receber a descrição, o nome, a imgurl, tempo, preço, mealid, tenho que enviar as allowed changes num array

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meals)

        button.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this@MenuMealsActivity,
                R.style.BottomSheetDialogTheme
            )

            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                R.layout.meal_details,
                findViewById(R.id.meal_bottom_sheet) as ConstraintLayout?
            )

            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog
            bottomSheetDialog.show()

            val buttonIncrement = bottomSheetView.findViewById<Button>(R.id.meal_bottom_sheet_quantity_increment)
            val buttonDecrement = bottomSheetView.findViewById<Button>(R.id.meal_bottom_sheet_quantity_decrement)
            val quantity = bottomSheetView.findViewById<TextView>(R.id.meal_bottom_sheet_text_quantity_number)
            val ingredientsChange = bottomSheetView.findViewById<Button>(R.id.meal_bottom_sheet_change_ingredient)
            val favoriteHeart = bottomSheetView.findViewById<TextView>(R.id.meal_bottom_sheet_heart)
            val addToCart = bottomSheetView.findViewById<Button>(R.id.meal_bottom_sheet_add_cart)

            buttonIncrement.setOnClickListener {
                var count: Int = quantity.text.toString().toInt()
                count++
                quantity.text = count.toString()
            }

            buttonDecrement.setOnClickListener {
                var count: Int = quantity.text.toString().toInt()
                if (count > 0) {
                    count--
                    quantity.text = count.toString()
                }
            }

            ingredientsChange.setOnClickListener {
                val intent = Intent(this@MenuMealsActivity, IngredientsChangeActivity::class.java)
                startActivity(intent)
            }

            var isChecked: Boolean = false
            favoriteHeart.setOnClickListener {
                isChecked = !isChecked
                if (isChecked) {
                    favoriteHeart.setBackgroundResource(R.drawable.favorite_heart)
                } else {
                    favoriteHeart.setBackgroundResource(R.drawable.bottom_sheet_heart_not_clicked)
                }
            }

            addToCart.setOnClickListener {
                // TODO: falta chamar o POST para adicionar ao carrinho

                // para fechar a botom sheet
                bottomSheetDialog.dismiss()
                Toast.makeText(this@MenuMealsActivity, "Refeição adicionada ao carrinho", Toast.LENGTH_LONG)
                    .show()
            }

        }

        // TODO: tem que receber aqui também os dados da ementa
    }
}