package pt.ipca.smartcanteen.views.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.LoginResponse
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.NewSessionTokenService
import pt.ipca.smartcanteen.views.fragments.consumer_fragments.ConsumerFragmentActivity
import pt.ipca.smartcanteen.views.fragments.employee_fragments.EmployeeFragmentActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoadingScreenActivity : AppCompatActivity() {

    private var sessionToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_screen)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@LoadingScreenActivity)
        val token = sp.getString("token", null)

        if(token == null){
            var intent = Intent(this@LoadingScreenActivity, LoginActivity::class.java)
            finish()
            startActivity(intent)
        }
        else{
            val retrofit = SmartCanteenRequests().retrofit

            val service = retrofit.create(NewSessionTokenService::class.java)

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

                            if(token != null){
                                Log.d("token", token)
                            }

                            val sp = SharedPreferencesHelper.getSharedPreferences(this@LoadingScreenActivity)
                            sp.edit().putString("token", token).commit()

                            if(role == "consumer"){

                                var intent = Intent(this@LoadingScreenActivity, ConsumerFragmentActivity::class.java)
                                finish()
                                startActivity(intent)
                            } else if(role == "employee"){

                                var intent = Intent(this@LoadingScreenActivity, EmployeeFragmentActivity::class.java)
                                finish()
                                startActivity(intent)
                            }

                        } else if (response.code() == 401) {

                            var intent = Intent(this@LoadingScreenActivity, LoginActivity::class.java)
                            finish()
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(calll: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@LoadingScreenActivity, "Erro! Tente novamente.", Toast.LENGTH_LONG)
                            .show()
                    }
                })
            }
        }
    }
