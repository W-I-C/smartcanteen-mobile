package pt.ipca.smartcanteen.views.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.DeviceRegisterBody
import pt.ipca.smartcanteen.models.LoginResponse
import pt.ipca.smartcanteen.models.adapters.MyFavoriteMealAdapterRec
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.RetroFavoriteMeal
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.AuthService
import pt.ipca.smartcanteen.services.FavoritemealService
import pt.ipca.smartcanteen.views.fragments.consumer_fragments.ConsumerFragmentActivity
import pt.ipca.smartcanteen.views.fragments.employee_fragments.EmployeeFragmentActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoadingScreenActivity : AppCompatActivity() {

    private var sessionToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_screen)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@LoadingScreenActivity)
        val token = sp.getString("token", null)

        if (token == null) {
            val intent = Intent(this@LoadingScreenActivity, LoginActivity::class.java)

            startActivity(intent)
            finish()
            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in,androidx.appcompat.R.anim.abc_fade_out);
        } else {

            getPushToken()
            val retrofit = SmartCanteenRequests().retrofit

            val service = retrofit.create(AuthService::class.java)

            var call =
                service.getSessionToken("Bearer $token").enqueue(object :
                    Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>, response: Response<LoginResponse>
                    ) {
                        if (response.code() == 200) {
                            val loadingBody = response.body()

                            val token = loadingBody?.token
                            val role = loadingBody?.role

                            if (token != null) {
                                Log.d("token", token)
                            }

                            val sp = SharedPreferencesHelper.getSharedPreferences(this@LoadingScreenActivity)
                            sp.edit().putString("token", token).commit()

                            if (role == "consumer") {

                                val intent = Intent(this@LoadingScreenActivity, ConsumerFragmentActivity::class.java)

                                startActivity(intent)
                                finish()
                                overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in,androidx.appcompat.R.anim.abc_fade_out);
                            } else if (role == "employee") {

                                val intent = Intent(this@LoadingScreenActivity, EmployeeFragmentActivity::class.java)
                                startActivity(intent)
                                finish()
                                overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in,androidx.appcompat.R.anim.abc_fade_out);
                            }

                        } else if (response.code() == 401) {

                            var intent = Intent(this@LoadingScreenActivity, LoginActivity::class.java)

                            startActivity(intent)
                            finish()
                            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in,androidx.appcompat.R.anim.abc_fade_out);
                        }
                    }

                    override fun onFailure(calll: Call<LoginResponse>, t: Throwable) {
                        var intent = Intent(this@LoadingScreenActivity, LoginActivity::class.java)

                        startActivity(intent)
                        finish()
                        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in,androidx.appcompat.R.anim.abc_fade_out);
                    }
                })
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in,androidx.appcompat.R.anim.abc_fade_out);
    }

    private fun getPushToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MAIN", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            sendToken(token)

            // Log and toast
            val msg = "InstanceID Token: "+token
            Log.d("Main", msg)
        })
    }

    private fun sendToken(deviceToken:String){
        val retrofit = SmartCanteenRequests().retrofit
        val service = retrofit.create(AuthService::class.java)
        val sp = SharedPreferencesHelper.getSharedPreferences(this@LoadingScreenActivity)
        val token = sp.getString("token", null)
        val sendBody = DeviceRegisterBody(deviceToken)
        var call = service.sendDeviceToken(sendBody,"Bearer $token").enqueue(object :
            Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d("MAIN", response.code().toString())
            }

            override fun onFailure(calll: Call<String>, t: Throwable) {
                print("error")
            }
        })
    }
}
