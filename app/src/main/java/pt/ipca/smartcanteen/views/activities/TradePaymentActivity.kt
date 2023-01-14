package pt.ipca.smartcanteen.views.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroPaymentMethod
import pt.ipca.smartcanteen.models.RetroTradePayment
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.LoadingDialogManager
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.TradesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TradePaymentActivity : AppCompatActivity() {

    private val confirm_button: Button by lazy { findViewById<Button>(R.id.trade_payment_pay) as Button }
    private val cancel_button: Button by lazy { findViewById<Button>(R.id.trade_cancel_payment_pay) as Button }
    private val price_text: TextView by lazy { findViewById<TextView>(R.id.trade_payment_price_textview) as TextView }
    private val payment_method: TextView by lazy { findViewById<TextView>(R.id.trade_payment_method_general_textview) as TextView }
    private val payment_method_tittle: TextView by lazy { findViewById<TextView>(R.id.type_payment_textview) as TextView }
    var paymentmethodid: String? = null
    private lateinit var loadingDialogManager: LoadingDialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade_payment)

        loadingDialogManager = LoadingDialogManager(layoutInflater, this)
        loadingDialogManager.createLoadingAlertDialog()

        val generaltradeid = intent.getStringExtra("generaltradeid").toString()
        val isfree = intent.getBooleanExtra("isfree", false)
        val price = intent.getFloatExtra("price", 0.0f)

        println(generaltradeid)
        println(isfree)
        println(price)

        // fazer isto só se recebermos um int (price), se recebermos gratuito não chama isto
        if (isfree == true) {
            price_text.text = "Gratuito"
            payment_method.visibility = View.GONE
            payment_method_tittle.visibility = View.GONE
            confirm_button.setOnClickListener {
                confirmPaymentTrade(generaltradeid)
            }
        } else {
            price_text.text = "${price.toString()}€"
            payment_method.visibility = View.VISIBLE
            payment_method_tittle.visibility = View.VISIBLE
            getPaymentMethods(generaltradeid)
            confirm_button.setOnClickListener {
                confirmPaymentTrade(generaltradeid)
            }
        }

        cancel()
    }

    fun cancel() {
        cancel_button.setOnClickListener {
            finish()
            Toast.makeText(this@TradePaymentActivity, "Operação cancelada!", Toast.LENGTH_LONG)
                .show()
        }
    }

    fun getPaymentMethods(generaltradeid: String) {
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(TradesService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@TradePaymentActivity)
        val token = sp.getString("token", null)

        loadingDialogManager.dialog.show()

        service.getTradePaymentMethod(generaltradeid, "Bearer $token").enqueue(object :
            Callback<RetroTradePayment> {
            override fun onResponse(
                call: Call<RetroTradePayment>,
                response: Response<RetroTradePayment>
            ) {
                if (response.code() == 200) {

                    loadingDialogManager.dialog.dismiss()

                    val paymentMethods = response.body()

                    if (paymentMethods != null) {
                        payment_method.text = paymentMethods.name
                    }

                    Toast.makeText(
                        this@TradePaymentActivity,
                        "Método de pagamento obtidos com sucesso!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else if (response.code() == 500) {
                    loadingDialogManager.dialog.dismiss()
                    Toast.makeText(
                        this@TradePaymentActivity,
                        "Erro! Não foi possível obter o método de pagamento.",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(this@TradePaymentActivity)
                    getPaymentMethods(generaltradeid)
                }
            }

            override fun onFailure(calll: Call<RetroTradePayment>, t: Throwable) {
                loadingDialogManager.dialog.dismiss()
                Toast.makeText(
                    this@TradePaymentActivity,
                    "Erro! Tente novamente.",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })
    }

    fun confirmPaymentTrade(generaltradeid: String) {

        Log.d("teste", generaltradeid)

        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(TradesService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@TradePaymentActivity)
        val token = sp.getString("token", null)

        loadingDialogManager.dialog.show()

        service.acceptGeneralTrade(generaltradeid, "Bearer $token").enqueue(object :
            Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.code() == 200) {

                    loadingDialogManager.dialog.dismiss()

                    finish()
                    Toast.makeText(
                        this@TradePaymentActivity,
                        "Troca obtida com sucesso!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else if (response.code() == 500) {
                    loadingDialogManager.dialog.dismiss()
                    Toast.makeText(
                        this@TradePaymentActivity,
                        "Erro! Não foi possível realizar a troca.",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(this@TradePaymentActivity)
                    getPaymentMethods(generaltradeid)
                }
            }

            override fun onFailure(calll: Call<String>, t: Throwable) {
                loadingDialogManager.dialog.dismiss()
                Toast.makeText(
                    this@TradePaymentActivity,
                    "Erro! Tente novamente.",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })
    }
}