package pt.ipca.smartcanteen

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExchangePaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exchange_payment)
    }

    fun onRadioButtonClicked(view: View) {
        // Verifica qual radio button foi selecionado
        val checked = (view as RadioButton).isChecked
        // val textView = findViewById<TextView>(R.id.type_payment_textview_1)

        // Verifica qual radio button foi selecionado e atualiza a textview de acordo
        // when (view.id) {
        //    R.id.mbway -> if (checked) textView.text = "MBWAY selecionado"
        //    R.id.multibanco -> if (checked) textView.text = "Multibanco selecionado"
        //}
    }
}