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
    private val listChange: RecyclerView by lazy { findViewById<RecyclerView>(R.id.card_add_ingredient_recycler_view) as RecyclerView }
    private val mealName: EditText by lazy { findViewById<EditText>(R.id.cart_text_meals_name) as EditText }
    private val preparationTime: EditText by lazy { findViewById<EditText>(R.id.cart_preparation_time) as EditText }
    private val price: EditText by lazy { findViewById<EditText>(R.id.cart_price) as EditText }
    private val descriptionMeal: EditText by lazy { findViewById<EditText>(R.id.cart_description) as EditText }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_meal)

    }

    fun addIngredient(view: View){
        val intent = Intent(this, AddMealChangeActivity::class.java)
        startActivity(intent)

    }



}



