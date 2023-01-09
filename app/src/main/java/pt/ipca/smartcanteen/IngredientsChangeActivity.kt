package pt.ipca.smartcanteen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class IngredientsChangeActivity: AppCompatActivity() {

    private val cancelButton: Button by lazy {findViewById<View>(R.id.ingredients_change_cancel) as Button }
    private val saveButton: Button by lazy {findViewById<View>(R.id.ingredients_change_save) as Button }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ingredients_change)

        // TODO: tem que receber o id da refeição da modal e no saveButtom o POST para adicionar as alterações à Meal
        cancelButton.setOnClickListener {
            finish()
        }

        saveButton.setOnClickListener {
            finish()
            Toast.makeText(this@IngredientsChangeActivity, "Alterações ao ingrediente guardadas com sucesso!", Toast.LENGTH_LONG)
                .show()
        }
    }
}