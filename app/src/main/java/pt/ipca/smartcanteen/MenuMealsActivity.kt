package pt.ipca.smartcanteen

import android.graphics.Point
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.LayoutInflater
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MenuMealsActivity : AppCompatActivity() {

    private val button: Button by lazy {findViewById<View>(R.id.menu_meals_button) as Button }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meals)

        button.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this@MenuMealsActivity, R.style.BottomSheetDialogTheme)

            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                R.layout.meal_details,
                findViewById(R.id.meal_bottom_sheet) as ConstraintLayout?
            )

            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()

            val buttonIncrement = bottomSheetView.findViewById<View>(R.id.meal_bottom_sheet_quantity_increment)
            val buttonDecrement = bottomSheetView.findViewById<View>(R.id.meal_bottom_sheet_quantity_decrement)
            val quantity = bottomSheetView.findViewById<TextView>(R.id.meal_bottom_sheet_text_quantity_number)

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
        }

        // TODO: tem que receber aqui também os dados da ementa
    }
}