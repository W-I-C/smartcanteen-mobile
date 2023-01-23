package pt.ipca.smartcanteen.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.views.fragments.consumer_fragments.MenuConsumerFragment

class OrderActivity : AppCompatActivity() {

    private val backBtn: ImageView by lazy { findViewById<ImageView>(R.id.order_arrow) as ImageView }

    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var camp: EditText
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        OnClickTime()
        finalPay()


        val spinner = findViewById<Spinner>(R.id.spinner)

        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                listOf(
                    "Cantina Barcelos",
                    "\n",
                    "Cantina Braga",
                    "\n",
                    "Cantina Taipas",
                    "\n",
                    "Cantina Vila Verde",
                    "\n",
                    "Cantina Famalicão"
                )
            )
            adapter.setDropDownViewResource(R.layout.spinner_item)
            spinner.adapter = adapter
        }

        backBtn.setOnClickListener {
            finish()
        }
    }


    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val option = view.isChecked
            when (view.getId()) {
                R.id.levar_nao -> if (option) {

                }
                R.id.levar_sim -> if (option) {

                }
                R.id.MB -> if (option) {

                }
                R.id.mbWay -> if (option) {

                }

            }
        }
    }

    private fun OnClickTime() {
        val textViewHour = findViewById<TextView>(R.id.textViewHour)
        val timePicker1 = findViewById<TimePicker>(R.id.timePicker)
        timePicker1.setIs24HourView(true)

        timePicker1.setOnTimeChangedListener { _, hour, minute ->
            var hour = hour

            if (textViewHour != null) {
                val hour = hour
                val min = minute
                // display format of time
                val msg = "Hora escolhida: $hour : $min"
                textViewHour.text = msg
                textViewHour.visibility = ViewGroup.GONE
            }
        }
    }

    private fun finalPay() {
        val button = findViewById<Button>(R.id.final_pay)

        button.setOnClickListener {

            Toast.makeText(this, "Carrinho finalizado. Aguarda confirmação de pagamento", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@OrderActivity, MenuConsumerFragment::class.java)
            startActivity(intent)
        }
    }
}








