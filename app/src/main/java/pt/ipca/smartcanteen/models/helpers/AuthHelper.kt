package pt.ipca.smartcanteen.models.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import pt.ipca.smartcanteen.models.LoginResponse
import pt.ipca.smartcanteen.services.AuthService
import pt.ipca.smartcanteen.views.activities.LoginActivity
import pt.ipca.smartcanteen.views.fragments.consumer_fragments.ConsumerFragmentActivity
import pt.ipca.smartcanteen.views.fragments.employee_fragments.EmployeeFragmentActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AuthHelper {
    fun doLogout(
        retrofit: Retrofit,
        activity: Activity,
        loadingDialogManager: LoadingDialogManager
    ) {
        val service = retrofit.create(AuthService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(activity)
        val token = sp.getString("token", null)

        loadingDialogManager.dialog.show()

        service.logout("Bearer $token").enqueue(object :
            Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.code() == 200) {
                    sp.edit().remove("token").apply()
                    loadingDialogManager.dialog.dismiss()
                    val intent = Intent(activity, LoginActivity::class.java)
                    activity.finish()
                    startActivity(activity, intent, null)
                } else if (response.code() == 401) {
                    var intent = Intent(activity, LoginActivity::class.java)
                    activity.finish()
                    activity.startActivity(intent)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                print("error")
            }

        })
    }

    fun newSessionToken(activity: Activity) {
        val sp = SharedPreferencesHelper.getSharedPreferences(activity)
        val token = sp.getString("token", null)

        if (token == null) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.finish()
            activity.startActivity(intent)
        } else {
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

                            val sp = SharedPreferencesHelper.getSharedPreferences(activity)
                            sp.edit().putString("token", token).commit()

                        } else if (response.code() == 401) {

                            var intent = Intent(activity, LoginActivity::class.java)
                            activity.finish()
                            activity.startActivity(intent)
                        }
                    }

                    override fun onFailure(calll: Call<LoginResponse>, t: Throwable) {
                        var intent = Intent(activity, LoginActivity::class.java)
                        activity.finish()
                        activity.startActivity(intent)
                    }
                })
        }
    }
}