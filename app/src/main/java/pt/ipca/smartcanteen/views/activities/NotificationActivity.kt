package pt.ipca.smartcanteen.views.activities

import pt.ipca.smartcanteen.models.adapters.NotificationAdapterRec
import pt.ipca.smartcanteen.models.helpers.RetroNotification
import pt.ipca.smartcanteen.services.NotificationService


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.adapters.UndeliveredOrdersAdaterRec
import pt.ipca.smartcanteen.models.RetroTicket
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.OrdersService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationActivity : AppCompatActivity() {


    private val notification: RecyclerView by lazy {findViewById<RecyclerView>(R.id.Recycler_notification) as RecyclerView }
    private val backBtn: ImageView by lazy {findViewById<ImageView>(R.id.notification_arrow) as ImageView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        backBtn.setOnClickListener{
            finish()
        }
    }

    fun getNotification(){
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(NotificationService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@NotificationActivity)
        val token = sp.getString("token", null)

        var call =
            service.getBarMeals("Bearer $token").enqueue(object : Callback<List<RetroNotification>> {
                override fun onResponse(
                    call: Call<List<RetroNotification>>,
                    response: Response<List<RetroNotification>>
                ) {
                    if (response.code() == 200) {
                        val retroFit2 = response.body()

                        if (retroFit2 != null)
                            if(retroFit2.isEmpty()){

                            } else {

                                rebuildlist(NotificationAdapterRec(retroFit2))
                            }
                    }else if(response.code()==401){
                        AuthHelper().newSessionToken(this@NotificationActivity)
                        getNotification()
                    }
                }

                override fun onFailure(calll: Call<List<RetroNotification>>, t: Throwable) {
                    print("error")
                }
            })
    }

    fun rebuildlist(adapter: NotificationAdapterRec) {
        val linearLayoutManager = LinearLayoutManager(this@NotificationActivity)
        notification.layoutManager = linearLayoutManager
        notification.itemAnimator = DefaultItemAnimator()
        notification.adapter = adapter
    }
}


