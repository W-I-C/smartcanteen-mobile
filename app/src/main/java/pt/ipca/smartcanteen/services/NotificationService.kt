package pt.ipca.smartcanteen.services

import pt.ipca.smartcanteen.models.RetroMeal
import pt.ipca.smartcanteen.models.helpers.RetroNotification
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface NotificationService{
    @GET("api/v1/consumer/notifications")
    fun getBarMeals(@Header("Authorization") authorization: String): Call<List<RetroNotification>>
}