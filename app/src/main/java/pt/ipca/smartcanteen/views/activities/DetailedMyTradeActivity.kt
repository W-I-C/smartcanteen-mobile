package pt.ipca.smartcanteen.views.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pt.ipca.smartcanteen.R

class DetailedMyTradeActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailed_my_trade)

        // Recuperar os dados da Intent
        val identifier = intent.getIntExtra("trade_nencomenda", 0)
        val quantity = intent.getIntExtra("trade_ticketamount", 0)
        val price = intent.getIntExtra("trade_total", 0)
        val state = intent.getStringExtra("trade_statename")

        // Atualizar os campos de texto com os dados da Intent
        findViewById<TextView>(R.id.detailed_my_trade_identifier).text = identifier.toString()
        findViewById<TextView>(R.id.detailed_my_trade_quantity).text = quantity.toString()
        findViewById<TextView>(R.id.detailed_my_trade_price).text = price.toString()
        findViewById<TextView>(R.id.detailed_my_trade_state).text = state
    }
}