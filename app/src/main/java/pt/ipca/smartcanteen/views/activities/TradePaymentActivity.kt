package pt.ipca.smartcanteen.views.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.dmoral.toasty.Toasty
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.helpers.AlertDialogManager
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.models.retrofit.response.RetroTradePayment
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
    private val backBtn: ImageView by lazy { findViewById<ImageView>(R.id.trade_payment_arrow) as ImageView }
    var paymentmethodid: String? = null
    private lateinit var alertDialogManager: AlertDialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade_payment)

        alertDialogManager = AlertDialogManager(layoutInflater, this)
        alertDialogManager.createLoadingAlertDialog()

        val generaltradeid = intent.getStringExtra("generaltradeid").toString()
        val isfree = intent.getBooleanExtra("isfree", false)
        val price = intent.getFloatExtra("price", 0.0f)

        // fazer isto só se recebermos um int (price), se recebermos gratuito não chama isto
        if (isfree == true) {
            price_text.text = getString(R.string.free)
            payment_method.visibility = View.GONE
            payment_method_tittle.visibility = View.GONE
            confirm_button.setOnClickListener {
                alertDialogManager.createConfirmAlertDialog(
                    getString(R.string.confirm_allowed_changes),
                    {
                        confirmPaymentTrade(generaltradeid)
                    }
                )
            }
        } else {
            price_text.text = "${price}€"
            payment_method.visibility = View.VISIBLE
            payment_method_tittle.visibility = View.VISIBLE
            getPaymentMethods(generaltradeid)
            confirm_button.setOnClickListener {
                alertDialogManager.createConfirmAlertDialog(
                    getString(R.string.confirm_allowed_changes),
                    {
                        confirmPaymentTrade(generaltradeid)
                    }
                )
            }
        }

        backBtn.setOnClickListener {
            finish()
        }

        cancel_button.setOnClickListener {
            alertDialogManager.createConfirmAlertDialog(
                getString(R.string.cancel_operation),
                {
                    cancel()
                }
            )
        }
    }

    fun cancel() {
        finish()
        Toasty.error(this@TradePaymentActivity, getString(R.string.canceled_operation), Toast.LENGTH_LONG).show()
    }

    fun getPaymentMethods(generaltradeid: String) {
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(TradesService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@TradePaymentActivity)
        val token = sp.getString("token", null)

        alertDialogManager.dialog.show()

        service.getTradePaymentMethod(generaltradeid, "Bearer $token").enqueue(object :
            Callback<RetroTradePayment> {
            override fun onResponse(
                call: Call<RetroTradePayment>,
                response: Response<RetroTradePayment>
            ) {
                if (response.code() == 200) {

                    alertDialogManager.dialog.dismiss()

                    val paymentMethods = response.body()

                    if (paymentMethods != null) {
                        payment_method.text = paymentMethods.name
                    }

                } else if (response.code() == 500) {
                    alertDialogManager.dialog.dismiss()

                    Toasty.error(this@TradePaymentActivity, getString(R.string.error_methods), Toast.LENGTH_LONG).show()
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(this@TradePaymentActivity)
                    getPaymentMethods(generaltradeid)
                }
            }

            override fun onFailure(calll: Call<RetroTradePayment>, t: Throwable) {
                alertDialogManager.dialog.dismiss()

                Toasty.error(this@TradePaymentActivity, getString(R.string.canceled_operation), Toast.LENGTH_LONG).show()
            }

        })
    }

    fun confirmPaymentTrade(generaltradeid: String) {


        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(TradesService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@TradePaymentActivity)
        val token = sp.getString("token", null)

        alertDialogManager.dialog.show()

        service.acceptGeneralTrade(generaltradeid, "Bearer $token").enqueue(object :
            Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.code() == 200) {

                    alertDialogManager.dialog.dismiss()

                    finish()

                    Toasty.success(this@TradePaymentActivity, getString(R.string.success_trade), Toast.LENGTH_LONG).show()
                } else if (response.code() == 500) {
                    alertDialogManager.dialog.dismiss()

                    Toasty.error(this@TradePaymentActivity, getString(R.string.error_trade), Toast.LENGTH_LONG).show()
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(this@TradePaymentActivity)
                    getPaymentMethods(generaltradeid)
                }
            }

            override fun onFailure(calll: Call<String>, t: Throwable) {
                alertDialogManager.dialog.dismiss()

                Toasty.error(this@TradePaymentActivity, getString(R.string.canceled_operation), Toast.LENGTH_LONG).show()
            }
        })
    }
}