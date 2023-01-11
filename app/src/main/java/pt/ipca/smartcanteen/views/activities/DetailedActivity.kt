package pt.ipca.smartcanteen

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailed_order)

        // Recuperar os dados da Intent
        val nencomenda = intent.getIntExtra("order_nencomenda", 0)
        val name = intent.getStringExtra("order_name")
        val statename = intent.getStringExtra("order_statename")

        // Atualizar os campos de texto com os dados da Intent
        findViewById<TextView>(R.id.detailed_id).text = nencomenda.toString()
        findViewById<TextView>(R.id.detailed_name).text = name
        findViewById<TextView>(R.id.detailed_state).text = statename
    }
}