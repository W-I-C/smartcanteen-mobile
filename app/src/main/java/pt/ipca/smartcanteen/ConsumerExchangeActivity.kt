package pt.ipca.smartcanteen

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConsumerExchangeActivity : AppCompatActivity() {

    private val checkBox1: CheckBox by lazy {findViewById<View>(R.id.exchange_general_checkbox) as CheckBox};
    private val checkBox2: CheckBox by lazy {findViewById<View>(R.id.exchange_direct_checkbox) as CheckBox};
    private val editText: EditText by lazy {findViewById<View>(R.id.exchange_email_edittext) as EditText}
    private val spinner_general: Spinner by lazy {findViewById<View>(R.id.exchange_general_spinner) as Spinner}
    private val spinner_direct: Spinner by lazy {findViewById<View>(R.id.exchange_direct_spinner) as Spinner}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exchange)

        spinner_general.visibility = View.INVISIBLE
        spinner_direct.visibility = View.INVISIBLE
        editText.visibility = View.INVISIBLE

        if (spinner_general != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                listOf("MBWAY", "Multibanco", "MBWAY", "Multibanco", "MBWAY", "Multibanco",)
            )
            spinner_general.adapter = adapter
        }

        if (spinner_direct != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                listOf("MBWAY", "Multibanco", "MBWAY", "Multibanco", "MBWAY", "Multibanco",)
            )
            spinner_direct.adapter = adapter
        }
    }

    fun checkBox1Clicked(view: View) {
        checkBox1.setOnClickListener {
            if (checkBox1.isChecked) {
                spinner_general.visibility = View.VISIBLE
                checkBox2.isChecked = false
                editText.visibility = View.INVISIBLE
                spinner_direct.visibility = View.INVISIBLE
            } else {
                spinner_general.visibility = View.GONE
            }
        }
    }

    fun checkBox2Clicked(view: View) {
        checkBox2.setOnClickListener {
            if (checkBox2.isChecked) {
                editText.visibility = View.VISIBLE
                spinner_direct.visibility = View.VISIBLE
                checkBox1.isChecked = false
                spinner_general.visibility = View.INVISIBLE
            } else {
                editText.visibility = View.GONE
                spinner_direct.visibility = View.GONE
            }
        }
    }
}