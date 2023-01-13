package pt.ipca.smartcanteen.views.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroPaymentMethod
import pt.ipca.smartcanteen.models.helpers.LoadingDialogManager
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.TradesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TradePaymentActivity : AppCompatActivity() {

    private val spinner_payment_methods: Spinner by lazy {findViewById<View>(R.id.trade_payment_method_general_spinner) as Spinner }
    private val confirm_button: Button by lazy {findViewById<Button>(R.id.trade_payment_pay) as Button }
    private val price_text: TextView by lazy {findViewById<TextView>(R.id.trade_payment_price_textview) as TextView }
    var paymentmethodid: String? = null
    private lateinit var loadingDialogManager: LoadingDialogManager

    // receber o ticketid e o generaltradeid
    //private lateinit var generaltradeid: String
    // receber o price
    //private lateinit var total: Int
    // receber o isfree
    // private lateinit var isfree: Boolean

    // TODO: mexer na API - paymentmethodid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade_payment)

        loadingDialogManager = LoadingDialogManager(layoutInflater, this)
        loadingDialogManager.createLoadingAlertDialog()

        // fazer isto só se recebermos um int (price), se recebermos gratuito não chama isto
        // if(isfree == false)
        // price_text.text(price)
        getPaymentMethods()
        // else
        // price_text.setText("Gratuito")
    }

    fun getPaymentMethods() {
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(TradesService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@TradePaymentActivity)
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
                            var adapter =
                                ArrayAdapter(
                                    this@TradePaymentActivity,
                                    android.R.layout.simple_spinner_item,
                                    paymentMethods.map { retroPaymentMethods -> retroPaymentMethods.name }
                                )
                            adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_payment_methods.adapter = adapter
                        }
                    }

                    spinner_payment_methods.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

                    Toast.makeText(this@TradePaymentActivity, "Métodos de pagamento obtidos com sucesso!", Toast.LENGTH_LONG)
                        .show()
                } else if(response.code() == 500){
                    loadingDialogManager.dialog.dismiss()
                    Toast.makeText(this@TradePaymentActivity, "Erro! Não foi possível obter os métodos de pagamento.", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(calll: Call<List<RetroPaymentMethod>>, t: Throwable) {
                loadingDialogManager.dialog.dismiss()
                Toast.makeText(this@TradePaymentActivity, "Erro! Tente novamente.", Toast.LENGTH_LONG)
                    .show()
            }

        })
    }

//    fun confirmPaymentTrade(){
//        confirm_button.setOnClickListener(){
//
//            // aceder ao paymentmethodid
//
//
//            val retrofit = SmartCanteenRequests().retrofit
//
//            val service = retrofit.create(TradesService::class.java)
//
//            val sp = SharedPreferencesHelper.getSharedPreferences(this@TradePaymentActivity)
//            val token = sp.getString("token", null)
//
//            loadingDialogManager.dialog.show()
//
//            service.acceptGeneralTrade(generaltradeid,"Bearer $token").enqueue(object :
//                Callback<String> {
//                override fun onResponse(
//                    call: Call<String>,
//                    response: Response<String>
//                ) {
//                    if (response.code() == 200) {
//
//                        loadingDialogManager.dialog.dismiss()
//
//                        finish()
//                        Toast.makeText(this@TradePaymentActivity, "Métodos de pagamento obtidos com sucesso!", Toast.LENGTH_LONG)
//                            .show()
//                    }
//                }
//
//                override fun onFailure(calll: Call<String>, t: Throwable) {
//                    loadingDialogManager.dialog.dismiss()
//                    Toast.makeText(this@TradePaymentActivity, "Erro! Tente novamente.", Toast.LENGTH_LONG)
//                        .show()
//                }
//
//            })
//
//        }
//    }
}