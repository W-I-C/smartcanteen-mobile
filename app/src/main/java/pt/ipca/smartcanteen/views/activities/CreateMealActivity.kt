package pt.ipca.smartcanteen.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import pt.ipca.smartcanteen.R

class CreateMealActivity: AppCompatActivity() {
    fun addIngredient(view: View){
        val intent = Intent(this, AddIngredientActivity::class.java)
        startActivity(intent)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_meal)
    }

}