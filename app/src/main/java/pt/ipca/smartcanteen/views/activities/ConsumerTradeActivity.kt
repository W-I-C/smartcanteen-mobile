package pt.ipca.smartcanteen.views.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.*
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.LoadingDialogManager
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.TradesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsumerTradeActivity : AppCompatActivity() {

    private val checkBox1: CheckBox by lazy {findViewById<View>(R.id.trade_general_checkbox) as CheckBox};
    private val checkBox2: CheckBox by lazy {findViewById<View>(R.id.trade_direct_checkbox) as CheckBox};
    private val editText: EditText by lazy {findViewById<View>(R.id.trade_email_edittext) as EditText}
    private val spinner_general_payable: Spinner by lazy {findViewById<View>(R.id.trade_ispayable_general_spinner) as Spinner}
    private val spinner_general: Spinner by lazy {findViewById<View>(R.id.trade_general_spinner) as Spinner}
    private val spinner_direct: Spinner by lazy {findViewById<View>(R.id.trade_direct_spinner) as Spinner}
    private val spinner_direct_payable: Spinner by lazy {findViewById<View>(R.id.trade_ispayable_direct_spinner) as Spinner}
    private val cancelButton: Button by lazy {findViewById<View>(R.id.trade_cancel) as Button}
    private val confirmButton: Button by lazy {findViewById<View>(R.id.trade_confirm) as Button}
    private val textError: TextView by lazy {findViewById<TextView>(R.id.trade_email_textview_error) as TextView}
    private val generalIsFree: TextView by lazy {findViewById<TextView>(R.id.trade_general_title_isfree) as TextView}
    private val generalPaymentMethod: TextView by lazy {findViewById<TextView>(R.id.trade_general_title_payment_method) as TextView}
    private val directIsFree: TextView by lazy {findViewById<TextView>(R.id.trade_direct_title_isfree) as TextView}
    private val directPaymentMethod: TextView by lazy {findViewById<TextView>(R.id.trade_direct_title_payment_method) as TextView}
    private lateinit var loadingDialogManager: LoadingDialogManager
    private lateinit var ticketId: String

    var isfree = true
    var paymentmethodid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade)

        ticketId = intent.getStringExtra("ticketId").toString()
        println(ticketId)

        loadingDialogManager = LoadingDialogManager(layoutInflater, this)
        loadingDialogManager.createLoadingAlertDialog()

        getPaymentMethods()

        spinner_general.visibility = View.GONE
        spinner_direct.visibility = View.GONE
        spinner_general_payable.visibility = View.GONE
        spinner_direct_payable.visibility = View.GONE
        editText.visibility = View.GONE
        textError.visibility = View.GONE
        generalIsFree.visibility = View.GONE
        generalPaymentMethod.visibility = View.GONE
        directIsFree.visibility = View.GONE
        directPaymentMethod.visibility = View.GONE

        // Sim -> Gratuito -> não aparece o spinner de baixo
        // Não -> Pagamento -> aparece o spinner de baixo
        if (spinner_general_payable != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                listOf("Sim", "Não")
            )
            spinner_general_payable.adapter = adapter
        }

        spinner_general_payable.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (parent != null) {
                        val selectedItem = parent.getItemAtPosition(position).toString()
                        if (selectedItem == "Sim") {
                            spinner_general.visibility = View.GONE
                            isfree = true
                            paymentmethodid = null
                            generalPaymentMethod.visibility = View.GONE
                        } else {
                            spinner_general.visibility = View.VISIBLE
                            isfree = false
                            generalPaymentMethod.visibility = View.VISIBLE
                        }
                    }
                }
            }


        if (spinner_direct_payable != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                listOf("Sim", "Não")
            )
            spinner_direct_payable.adapter = adapter
        }

        spinner_direct_payable.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (parent != null) {
                        val selectedItem = parent.getItemAtPosition(position).toString()
                        if (selectedItem == "Sim") {
                            spinner_direct.visibility = View.GONE
                            directPaymentMethod.visibility = View.GONE
                            isfree = true
                            paymentmethodid = null
                        } else {
                            spinner_direct.visibility = View.VISIBLE
                            directPaymentMethod.visibility = View.VISIBLE
                            isfree = false
                        }
                    }
                }
            }

        cancelButton.setOnClickListener {
            finish()
            Toast.makeText(this@ConsumerTradeActivity, "Operação cancelada!", Toast.LENGTH_LONG)
                .show()
        }

        confirmButton.setOnClickListener {

            val retrofit = SmartCanteenRequests().retrofit

            val service = retrofit.create(TradesService::class.java)

            val sp = SharedPreferencesHelper.getSharedPreferences(this@ConsumerTradeActivity)
            val token = sp.getString("token", null)

            // TODO: verificar se os spinners foram preenchidos
                if (checkBox1.isChecked) {
                    // Faça chamada POST para rota 1

                    // SIM -> isfree a true e paymentmethodid a null
                    // NÃO -> isfree a false e paymentmethodid string (ir buscar o id do name daquele método)

                    // verificação para ver se todos os campos foram preenchidos - os spinners metem por defeito mas a editText não (troca direta)

                    val body = GeneralTradeBody(isfree, paymentmethodid)

                    loadingDialogManager.dialog.show()

                    service.generalTicketTrade(ticketId, "Bearer $token",body).enqueue(object :
                        Callback<String> {
                        override fun onResponse(
                            call: Call<String>,
                            response: Response<String>
                        ) {
                            if (response.code() == 200) {

                                println("Aqui123")

                                loadingDialogManager.dialog.dismiss()

                                Toast.makeText(this@ConsumerTradeActivity, "Troca geral realizada com sucesso!", Toast.LENGTH_LONG)
                                    .show()
                                finish()
                            } else if (response.code() == 500) {
                                loadingDialogManager.dialog.dismiss()

                                Toast.makeText(this@ConsumerTradeActivity, "Troca geral falhada!", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }

                        override fun onFailure(calll: Call<String>, t: Throwable) {
                            loadingDialogManager.dialog.dismiss()
                            println("Deu erro")
                            Toast.makeText(this@ConsumerTradeActivity, "Erro! Tente novamente (a troca geral pode já ter sido feita).", Toast.LENGTH_LONG)
                                .show()
                        }

                    })

                } else if (checkBox2.isChecked) {
                    // Faça chamada POST para rota 2

                    val receiveremail = editText.text

                    println(receiveremail)
                    println(isfree)
                    println(paymentmethodid)
                    println(ticketId)

                    val body = DirectTradeBody(receiveremail.toString() ,isfree, paymentmethodid)

                    loadingDialogManager.dialog.show()

                    service.directTicketTrade(ticketId, "Bearer $token",body).enqueue(object :
                        Callback<String> {
                        override fun onResponse(
                            call: Call<String>,
                            response: Response<String>
                        ) {
                            Log.d("Hrllo", response.toString())
                            if (response.code() == 200) {

                                println("Aqui")
                                loadingDialogManager.dialog.dismiss()

                                Toast.makeText(this@ConsumerTradeActivity, "Troca direta realizada com sucesso!", Toast.LENGTH_LONG)
                                    .show()
                                finish()
                            } else if (response.code() == 500) {
                                loadingDialogManager.dialog.dismiss()

                                Toast.makeText(this@ConsumerTradeActivity, "Troca direta falhada!", Toast.LENGTH_LONG)
                                    .show()
                            }

                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            loadingDialogManager.dialog.dismiss()
                            println("CAo ${t.toString()} ")
                            Toast.makeText(this@ConsumerTradeActivity, "Erro! Tente novamente.", Toast.LENGTH_LONG)
                                .show()
                        }
                    })
                }
        }
    }

    fun checkBox1Clicked(view: View) {
        checkBox1.setOnClickListener {
            if (checkBox1.isChecked) {
                //spinner_general.visibility = View.GONE
                spinner_general_payable.visibility = View.VISIBLE
                checkBox2.isChecked = false
                editText.visibility = View.GONE
                textError.visibility = View.GONE
                generalIsFree.visibility = View.VISIBLE
                generalPaymentMethod.visibility = View.GONE
                spinner_direct.visibility = View.GONE
                spinner_direct_payable.visibility = View.GONE
                directIsFree.visibility = View.GONE
                directPaymentMethod.visibility = View.GONE
            } else {
                spinner_general.visibility = View.GONE
                spinner_general_payable.visibility = View.GONE
                generalIsFree.visibility = View.GONE
                generalPaymentMethod.visibility = View.GONE
            }
        }
    }

    fun checkBox2Clicked(view: View) {
        checkBox2.setOnClickListener {
            if (checkBox2.isChecked) {
                editText.visibility = View.VISIBLE
                directIsFree.visibility = View.VISIBLE
                directPaymentMethod.visibility = View.GONE
                if(editText.text.isEmpty()){
                    textError.visibility = View.VISIBLE
                } else {
                    textError.visibility = View.GONE
                }
                editText.addTextChangedListener {
                    if (it != null) {
                        if(it.isEmpty()){
                            textError.visibility = View.VISIBLE
                        } else {
                            textError.visibility = View.GONE
                        }
                    }
                }
                //spinner_direct.visibility = View.GONE
                spinner_direct_payable.visibility = View.VISIBLE
                checkBox1.isChecked = false
                spinner_general.visibility = View.GONE
                spinner_general_payable.visibility = View.GONE
                generalIsFree.visibility = View.GONE
                generalPaymentMethod.visibility = View.GONE
            } else {
                editText.visibility = View.GONE
                textError.visibility = View.GONE
                spinner_direct.visibility = View.GONE
                spinner_direct_payable.visibility = View.GONE
                directIsFree.visibility = View.GONE
                directPaymentMethod.visibility = View.GONE
            }
        }
    }

    fun getPaymentMethods() {
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(TradesService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@ConsumerTradeActivity)
        val token = sp.getString("token", null)

        loadingDialogManager.dialog.show()

        service.getPaymentMethods("Bearer $token").enqueue(object :
            Callback<List<RetroPaymentMethod>> {
            override fun onResponse(
                call: Call<List<RetroPaymentMethod>>,
                response: Response<List<RetroPaymentMethod>>
            ) {
                if (response.code() == 200) {

                    loadingDialogManager.dialog.dismiss()

                    val paymentMethods = response.body()
                    paymentMethods?.map { retroPaymentMethods -> retroPaymentMethods.methodid }


                    if (paymentMethods != null) {
                        if (paymentMethods.isNotEmpty()) {

                            Log.d("bar:", paymentMethods.toString())
                            var adapter =
                                ArrayAdapter(
                                    this@ConsumerTradeActivity,
                                    android.R.layout.simple_spinner_item,
                                    paymentMethods.map { retroPaymentMethods -> retroPaymentMethods.name }
                                )
                            adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_general.adapter = adapter
                            spinner_direct.adapter = adapter
                        }
                    }

                    spinner_general.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val selectedPaymentMethod = paymentMethods?.get(position)
                            if (selectedPaymentMethod != null) {
                                paymentmethodid = selectedPaymentMethod.methodid
                            }
                            paymentmethodid?.let { Log.d("paymentmethodid", it) }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            // do nothing
                        }
                    }

                    spinner_direct.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val selectedPaymentMethod = paymentMethods?.get(position)
                            if (selectedPaymentMethod != null) {
                                paymentmethodid = selectedPaymentMethod.methodid
                            }
                            paymentmethodid?.let { Log.d("paymentmethodid", it) }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            // do nothing
                        }
                    }



                    Toast.makeText(this@ConsumerTradeActivity, "Métodos de pagamento obtidos com sucesso!", Toast.LENGTH_LONG)
                        .show()
                } else if(response.code() == 500){
                    loadingDialogManager.dialog.dismiss()
                    Toast.makeText(this@ConsumerTradeActivity, "Erro! Não foi possível obter os métodos de pagamento.", Toast.LENGTH_LONG)
                        .show()
                } else if(response.code()==401){
                    AuthHelper().newSessionToken(this@ConsumerTradeActivity)
                    getPaymentMethods()
                }
            }

            override fun onFailure(calll: Call<List<RetroPaymentMethod>>, t: Throwable) {
                loadingDialogManager.dialog.dismiss()
                Toast.makeText(this@ConsumerTradeActivity, "Erro! Tente novamente.", Toast.LENGTH_LONG)
                    .show()
            }

            })

        // TODO: no POST de fazer troca meter um error 500 (a troca já foi submtida para troca)

    }
}