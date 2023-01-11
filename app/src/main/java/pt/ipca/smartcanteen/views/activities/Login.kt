package pt.ipca.smartcanteen.views.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import pt.ipca.smartcanteen.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.LoginBody
import pt.ipca.smartcanteen.models.LoginResponse
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.services.LoginService
import pt.ipca.smartcanteen.views.fragments.consumer_fragments.ConsumerFragmentActivity
import pt.ipca.smartcanteen.views.fragments.employee_fragments.EmployeeFragmentActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Login : AppCompatActivity() {

    private var sessionToken: String? = null
    private val checkBoxPassword: CheckBox by lazy {findViewById<View>(R.id.login_password_visibility_checkbox) as CheckBox };
    private val email: EditText by lazy {findViewById<View>(R.id.login_email_edittext) as EditText};
    private val password: EditText by lazy {findViewById<View>(R.id.login_password_edittext) as EditText}
    private val button: Button by lazy {findViewById<View>(R.id.login_button_login) as Button}
    private lateinit var loadingAlertDialog: AlertDialog
    // val myButton = findViewById<Button>(R.id.login_button_login)

    private fun validatePassword(password:String):Boolean{
        val regexChars = """(.*([A-Z][a-z]|[a-z][A-Z]).*)""".toRegex()
        val regexSymbols = """[^A-Za-z0-9]""".toRegex()

        return regexChars.containsMatchIn(password) && regexSymbols.containsMatchIn(password) && password.length >= 6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        createLoadingAlertDialog()

        button.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()

            println(emailText)
            println(passwordText)

            if (emailText.isEmpty()) {
                Toast.makeText(this@Login, "Email vazio", Toast.LENGTH_LONG).show()
            } else {
                if (passwordText.isEmpty()) {
                    Toast.makeText(this@Login, "Password vazia", Toast.LENGTH_LONG)
                        .show()
                } else {
                    if (Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                        if (validatePassword(passwordText)) {

                            // é criado um objeto do tipo JSON
                            val body = LoginBody(emailText, passwordText)

                            val BASE_URL = "https://smartcanteen-api.herokuapp.com"

                            // Cria um objeto Retrofit
                            val retrofit = Retrofit.Builder()
                                .baseUrl(BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()

                            loadingAlertDialog.show()

                            // Cria um objeto LoginService
                            val service = retrofit.create(LoginService::class.java)
                            val call = service.login(body)
                            call.enqueue(object : Callback<LoginResponse> {
                                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                                    if (response.code() == 200) {
                                        loadingAlertDialog.dismiss()

                                        val loginBody = response.body()
                                        val token = loginBody?.token
                                        val role = loginBody?.role

                                        if(token != null){
                                            Log.d("token", token)
                                        }

                                        val sp = SharedPreferencesHelper.getSharedPreferences(this@Login)
                                        sp.edit().putString("token", token).commit()
                                        sp.edit().putString("role", role).commit()

                                        if (role == "consumer") {
                                            // O usuário é um consumidor, então encaminhe-o para a tela específica para consumidores
                                            var intent = Intent(this@Login, ConsumerFragmentActivity::class.java)
                                            startActivity(intent)
                                        } else if (role == "employee") {
                                            // O usuário é um funcionário, então encaminhe-o para a tela específica para funcionários
                                            var intent = Intent(this@Login, EmployeeFragmentActivity::class.java)
                                            startActivity(intent)
                                        } else {
                                            // O usuário não é nem consumidor nem funcionário, então exiba uma mensagem de erro
                                            Toast.makeText(this@Login, "Erro! Role não existe", Toast.LENGTH_LONG)
                                                .show()
                                        }

                                    } else {
                                        loadingAlertDialog.dismiss()
                                        Toast.makeText(this@Login, "Erro! Não foi possível realizar o login, tente novamente", Toast.LENGTH_LONG)
                                            .show()
                                    }
                                }

                                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                    loadingAlertDialog.dismiss()
                                    Toast.makeText(this@Login, "Erro! Tente novamente.", Toast.LENGTH_LONG)
                                        .show()
                                }
                            })


                        } else {
                            Toast.makeText(this@Login, "Password inválida", Toast.LENGTH_LONG)
                                .show()
                        }

                    } else {
                        Toast.makeText(this@Login, "Email inválido", Toast.LENGTH_LONG).show()

                    }
                }
            }
        }
    }

    fun checkBoxPasswordClicked(view: View) {
        checkBoxPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }

    private fun createLoadingAlertDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_alert_dialog, null))
        builder.setCancelable(false)
        loadingAlertDialog = builder.create()
    }
}