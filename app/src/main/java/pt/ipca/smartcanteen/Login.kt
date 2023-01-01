package pt.ipca.smartcanteen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Login : AppCompatActivity() {

    private var sessionToken: String? = null
    private val email: EditText by lazy {findViewById<View>(R.id.login_email_edittext) as EditText};
    private val password: EditText by lazy {findViewById<View>(R.id.login_password_edittext) as EditText}
    private val button: Button by lazy {findViewById<View>(R.id.login_button_login) as Button}
    // val myButton = findViewById<Button>(R.id.login_button_login)

    private fun validatePassword(password:String):Boolean{
        val regexChars = """(.*([A-Z][a-z]|[a-z][A-Z]).*)""".toRegex()
        val regexSymbols = """[^A-Za-z0-9]""".toRegex()

        return regexChars.containsMatchIn(password) && regexSymbols.containsMatchIn(password) && password.length >= 6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

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

                            // val BASE_URL = "http://192.168.1.106:3000"
                            val BASE_URL = "http://10.0.2.2:3000"

                            // Cria um objeto Retrofit
                            val retrofit = Retrofit.Builder()
                                .baseUrl(BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()

                            // Cria um objeto LoginService
                            val service = retrofit.create(LoginService::class.java)
                            val call = service.login(body)
                            call.enqueue(object : Callback<LoginBody> {
                                override fun onResponse(call: Call<LoginBody>, response: Response<LoginBody>) {
                                    if (response.code() == 200) {
                                        Toast.makeText(this@Login, "Login realizado com sucesso!", Toast.LENGTH_LONG)
                                            .show()

                                        // atribui o token de sessão à variável sessionToken - este token vamos recebê-lo depois do user estar autenticado
                                        // vamos receber o token do user.body
                                        // sessionToken = response.body()?.token

                                        // guarda o token de sessão no Shared Preferences
                                        // Context.MODE_PRIVATE inidca que este arquivo de preferências só pode ser acedido pela nossa aplicação
                                        // sharedPreferences permite guardar dados na forma chave-valor para manter as informações mesmo quando a app é fechada ou o dispositivo reiniciado
                                        // val sharedPreferences = getSharedPreferences("smartcanteen", Context.MODE_PRIVATE)
                                        // val editor = sharedPreferences.edit()
                                        // é armazenado o sessionToken na sharedPreferences
                                        // editor.putString("sessionToken", sessionToken)
                                        // editor.apply()



                                        // TODO - ver o role da pessoa e reencaminhar para o sítio certo
                                        var intent = Intent(this@Login, MainActivity::class.java)
                                        startActivity(intent)

                                        // TODO - guardar o token de sessão gerado para posteriormente ser possível fazer pedidos
                                    } else {
                                        Toast.makeText(this@Login, "123", Toast.LENGTH_LONG)
                                            .show()
                                    }
                                }

                                override fun onFailure(call: Call<LoginBody>, t: Throwable) {
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
}