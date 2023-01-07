package pt.ipca.smartcanteen

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailedMyOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailed_my_order)

        // Recuperar os dados da Intent
        val identifier = intent.getIntExtra("order_nencomenda", 0)
        val quantity = intent.getIntExtra("order_ticketamount", 0)
        val price = intent.getIntExtra("order_total", 0)
        val state = intent.getStringExtra("order_statename")

        // Atualizar os campos de texto com os dados da Intent
        findViewById<TextView>(R.id.detailed_my_order_identifier).text = identifier.toString()
        findViewById<TextView>(R.id.detailed_my_order_quantity).text = quantity.toString()
        findViewById<TextView>(R.id.detailed_my_order_price).text = price.toString()
        findViewById<TextView>(R.id.detailed_my_order_state).text = state
    }
}