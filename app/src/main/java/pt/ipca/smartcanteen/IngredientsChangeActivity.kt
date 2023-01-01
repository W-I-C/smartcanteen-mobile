package pt.ipca.smartcanteen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class IngredientsChangeActivity: AppCompatActivity() {

    private val cancelButton: Button by lazy {findViewById<View>(R.id.ingredients_change_cancel) as Button }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ingredients_change)

        cancelButton.setOnClickListener {
            finish()
        }
    }
}