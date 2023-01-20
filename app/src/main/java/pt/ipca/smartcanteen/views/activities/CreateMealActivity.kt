package pt.ipca.smartcanteen.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R

class CreateMealActivity: AppCompatActivity() {
    private val listChange: RecyclerView by lazy { findViewById<RecyclerView>(R.id.crate_meal_recycler_view) as RecyclerView }
    private val mealName: EditText by lazy { findViewById<EditText>(R.id.create_meal_name_edittext) as EditText }
    private val preparationTime: EditText by lazy { findViewById<EditText>(R.id.create_meal_preparation_time_edittext) as EditText }
    private val price: EditText by lazy { findViewById<EditText>(R.id.create_meal_price_edittext) as EditText }
    private val descriptionMeal: EditText by lazy { findViewById<EditText>(R.id.create_meal_cart_description_edittext) as EditText }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_meal)

    }

    fun addIngredient(view: View){
        val intent = Intent(this, AddMealChangeActivity::class.java)
        startActivity(intent)

    }



}



