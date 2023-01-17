package pt.ipca.smartcanteen.views.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import pt.ipca.smartcanteen.R

class CreateMealActivity: AppCompatActivity() {
    var listChange=findViewById<RecyclerView>(R.id.card_add_ingredient_recycler_view)
    var mealName= findViewById<EditText>(R.id.cart_text_meals_name)
    var preparationTime= findViewById<EditText>(R.id.cart_preparation_time)
    var price=findViewById<EditText>(R.id.cart_price)
    var descriptionMeal=findViewById<EditText>(R.id.cart_description)

    fun addIngredient(view: View){
        val intent = Intent(this, AddMealChangeActivity::class.java)
        startActivity(intent)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_meal)

        }

}



