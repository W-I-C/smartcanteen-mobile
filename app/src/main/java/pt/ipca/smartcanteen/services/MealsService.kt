package pt.ipca.smartcanteen.services

import pt.ipca.smartcanteen.models.*
import retrofit2.Call
import retrofit2.http.*

interface MealsService {
    @GET("/api/v1/bar/{barId}/meals")
    fun getBarMeals(@Path("barId") barId: String, @Header("Authorization") authorization: String): Call<List<RetroMeal>>

    @GET("/api/v1/consumer/cart/meals")
    fun getMealsCart(@Header("Authorization") authorization: String): Call<List<RetroCartMeals>>

    @GET("/api/v1/consumer/meals/{mealId}/allowedChanges")
    fun getAllowedChanges(@Path("mealId") mealId: String, @Header("Authorization") authorization: String): Call<List<RetroAllowedChanges>>
}