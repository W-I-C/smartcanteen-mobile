package pt.ipca.smartcanteen

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailedMyOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailed_my_order)

        // Recuperar os dados da Intent
        val name = intent.getStringExtra("order_name")
        val quantity = intent.getIntExtra("order_quantity", 0)
        val price = intent.getDoubleExtra("order_price", 0.0)
        val state = intent.getStringExtra("order_state")

        // Atualizar os campos de texto com os dados da Intent
        findViewById<TextView>(R.id.detailed_my_order_name).text = name
        findViewById<TextView>(R.id.detailed_my_order_quantity).text = quantity.toString()
        findViewById<TextView>(R.id.detailed_my_order_price).text = price.toString()
        findViewById<TextView>(R.id.detailed_my_order_state).text = state
    }
}