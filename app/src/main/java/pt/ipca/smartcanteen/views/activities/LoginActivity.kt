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
import androidx.appcompat.app.AppCompatActivity
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.LoginBody
import pt.ipca.smartcanteen.models.LoginResponse
import pt.ipca.smartcanteen.models.helpers.LoadingDialogManager
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.AuthService
import pt.ipca.smartcanteen.views.fragments.consumer_fragments.ConsumerFragmentActivity
import pt.ipca.smartcanteen.views.fragments.employee_fragments.EmployeeFragmentActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private var sessionToken: String? = null
    private val checkBoxPassword: CheckBox by lazy {findViewById<View>(R.id.login_password_visibility_checkbox) as CheckBox };
    private val email: EditText by lazy {findViewById<View>(R.id.login_email_edittext) as EditText};
    private val password: EditText by lazy {findViewById<View>(R.id.login_password_edittext) as EditText}
    private val button: Button by lazy {findViewById<View>(R.id.login_button_login) as Button}
    private lateinit var loadingDialogManager: LoadingDialogManager
    // val myButton = findViewById<Button>(R.id.login_button_login)

    private fun validatePassword(password:String):Boolean{
        val regexChars = """(.*([A-Z][a-z]|[a-z][A-Z]).*)""".toRegex()
        val regexSymbols = """[^A-Za-z0-9]""".toRegex()

        return regexChars.containsMatchIn(password) && regexSymbols.containsMatchIn(password) && password.length >= 6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loadingDialogManager = LoadingDialogManager(layoutInflater, this)
        loadingDialogManager.createLoadingAlertDialog()

        button.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()

            println(emailText)
            println(passwordText)

            if (emailText.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Email vazio", Toast.LENGTH_LONG).show()
            } else {
                if (passwordText.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Password vazia", Toast.LENGTH_LONG)
                        .show()
                } else {
                    if (Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                        if (validatePassword(passwordText)) {

                            // é criado um objeto do tipo JSON
                            val body = LoginBody(emailText, passwordText)

                            val retrofit = SmartCanteenRequests().retrofit

                            loadingDialogManager.dialog.show()

                            // Cria um objeto LoginService
                            val service = retrofit.create(AuthService::class.java)
                            val call = service.login(body)
                            call.enqueue(object : Callback<LoginResponse> {
                                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                                    if (response.code() == 200) {
                                        loadingDialogManager.dialog.dismiss()

                                        val loginBody = response.body()
                                        val token = loginBody?.token
                                        val role = loginBody?.role

                                        if(token != null){
                                            Log.d("token", token)
                                        }

                                        val sp = SharedPreferencesHelper.getSharedPreferences(this@LoginActivity)
                                        sp.edit().putString("token", token).commit()
                                        sp.edit().putString("role", role).commit()


                                        if (role == "consumer") {
                                            // O usuário é um consumidor, então encaminhe-o para a tela específica para consumidores
                                            var intent = Intent(this@LoginActivity, ConsumerFragmentActivity::class.java)
                                            finish()
                                            startActivity(intent)
                                        } else if (role == "employee") {
                                            // O usuário é um funcionário, então encaminhe-o para a tela específica para funcionários
                                            var intent = Intent(this@LoginActivity, EmployeeFragmentActivity::class.java)
                                            finish()
                                            startActivity(intent)
                                        } else {
                                            // O usuário não é nem consumidor nem funcionário, então exiba uma mensagem de erro
                                            Toast.makeText(this@LoginActivity, "Erro! Role não existe", Toast.LENGTH_LONG)
                                                .show()
                                        }

                                    } else {
                                        loadingDialogManager.dialog.dismiss()
                                        Toast.makeText(this@LoginActivity, "Erro! Não foi possível realizar o login, tente novamente", Toast.LENGTH_LONG)
                                            .show()
                                    }
                                }

                                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                    loadingDialogManager.dialog.dismiss()
                                    Toast.makeText(this@LoginActivity, "Erro! Tente novamente.", Toast.LENGTH_LONG)
                                        .show()
                                }
                            })


                        } else {
                            Toast.makeText(this@LoginActivity, "Password inválida", Toast.LENGTH_LONG)
                                .show()
                        }

                    } else {
                        Toast.makeText(this@LoginActivity, "Email inválido", Toast.LENGTH_LONG).show()

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


}