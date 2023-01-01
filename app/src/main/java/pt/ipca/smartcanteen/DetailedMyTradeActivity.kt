package pt.ipca.smartcanteen

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailedMyTradeActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailed_my_trade)

        // Recuperar os dados da Intent
        val name = intent.getStringExtra("exchange_name")
        val quantity = intent.getIntExtra("exchange_quantity", 0)
        val price = intent.getDoubleExtra("exchange_price", 0.0)
        val state = intent.getStringExtra("exchange_state")

        // Atualizar os campos de texto com os dados da Intent
        findViewById<TextView>(R.id.detailed_my_trade_name).text = name
        findViewById<TextView>(R.id.detailed_my_trade_quantity).text = quantity.toString()
        findViewById<TextView>(R.id.detailed_my_trade_price).text = price.toString()
        findViewById<TextView>(R.id.detailed_my_trade_state).text = state
    }
}