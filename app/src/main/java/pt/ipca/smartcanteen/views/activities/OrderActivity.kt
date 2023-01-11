package pt.ipca.smartcanteen

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OrderActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var camp: EditText
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        OnClickTime()



        val spinner = findViewById<Spinner>(R.id.spinner)

        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
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
            spinner.adapter = adapter
        }
    }



     fun onRadioButtonClicked(view: View) {
        if(view is RadioButton){
            val opcion=view.isChecked
            when(view.getId()){
                R.id.levar_nao->if(opcion){

                }
                R.id.levar_sim->if(opcion){

                }
                R.id.MB->if(opcion){

                }
                R.id.mbWay->if(opcion){

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

}








