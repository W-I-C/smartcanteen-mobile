package pt.ipca.smartcanteen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.ipca.smartcanteen.consumer_fragments.MainFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoadingScreen : AppCompatActivity() {

    private var sessionToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_screen)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@LoadingScreen)
        val token = sp.getString("token", null)

        if(token == null){
            var intent = Intent(this@LoadingScreen, Login::class.java)
            startActivity(intent)
        }
        else{
            val BASE_URL = "https://smartcanteen-api.herokuapp.com"
            var retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
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

                            val sp = SharedPreferencesHelper.getSharedPreferences(this@LoadingScreen)
                            sp.edit().putString("token", token).commit()

                            if(role == "consumer"){
                                var intent = Intent(this@LoadingScreen, ConsumerFragmentActivity::class.java)
                                startActivity(intent)
                            } else if(role == "employee"){
                                var intent = Intent(this@LoadingScreen, EmployeeFragmentActivity::class.java)
                                startActivity(intent)
                            }

                        } else if (response.code() == 401) {
                            var intent = Intent(this@LoadingScreen, Login::class.java)
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(calll: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@LoadingScreen, "Erro! Tente novamente.", Toast.LENGTH_LONG)
                            .show()
                    }
                })
            }
        }
    }
