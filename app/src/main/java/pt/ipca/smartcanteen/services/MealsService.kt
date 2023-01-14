package pt.ipca.smartcanteen.services

import pt.ipca.smartcanteen.models.RetroCartMeals
import pt.ipca.smartcanteen.models.RetroMeal
import pt.ipca.smartcanteen.models.RetroTrade
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface MealsService {
    @GET("/api/v1/bar/{barId}/meals")
    fun getBarMeals(@Path("barId") barId: String, @Header("Authorization") authorization: String): Call<List<RetroMeal>>

    @GET("/api/v1/consumer/cart/meals")
    fun getMealsCart(@Header("Authorization") authorization: String): Call<List<RetroCartMeals>>

    @GET("/api/v1/employee/bar/menu")
    fun getEmployeeBarMeals(@Header("Authorization") authorization: String): Call<List<RetroMeal>>

    @DELETE("/api/v1/employee/meal/{mealId}")
    fun deleteEmployeeBarMeal(@Path("mealId") mealId:String,@Header("Authorization") authorization: String): Call<String>
}