package pt.ipca.smartcanteen

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class OrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)


        val spinner = findViewById<Spinner>(R.id.spinner)

        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                listOf("Cantina Barcelos","\n","Cantina Braga","\n","Cantina Taipas","\n","Cantina Vila Verde","\n","Cantina Famalicão" )
            )
            spinner.adapter = adapter
        }

    }



}