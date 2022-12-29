package pt.ipca.smartcanteen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class CreateMealActivity: AppCompatActivity() {
    fun addIngredient(view: View){
        val intent = Intent(this, AddIngredient::class.java)
        startActivity(intent)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_meal)
    }

}