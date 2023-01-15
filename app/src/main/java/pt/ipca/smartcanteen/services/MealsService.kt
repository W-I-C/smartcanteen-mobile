package pt.ipca.smartcanteen.services

import pt.ipca.smartcanteen.models.*
import retrofit2.Call
import retrofit2.http.*

interface MealsService {
    @GET("/api/v1/bar/{barId}/meals")
    fun getBarMeals(@Path("barId") barId: String, @Header("Authorization") authorization: String): Call<List<RetroMeal>>

    @GET("/api/v1/consumer/cart/meals")
    fun getMealsCart(@Header("Authorization") authorization: String): Call<List<RetroCartMeals>>

    @GET("/api/v1/meals/{mealId}/allowedChanges")
    fun getAllowedChanges(@Path("mealId") mealId: String, @Header("Authorization") authorization: String): Call<List<RetroAllowedChanges>>

    @GET("/api/v1/employee/bar/menu")
    fun getEmployeeBarMeals(@Header("Authorization") authorization: String): Call<List<RetroMeal>>

    @DELETE("/api/v1/employee/meal/{mealId}")
    fun deleteEmployeeBarMeal(@Path("mealId") mealId:String,@Header("Authorization") authorization: String): Call<String>

    @PUT("/api/v1/employee/meal/{mealId}/canBeMade")
    fun canBeMade(@Path("mealId") mealId: String, @Header("Authorization") authorization: String, @Body body: CanBeMadeBody): Call<String>

    @POST("/api/v1/employee//meals/{mealId}/allowedchanges")
    fun addMealChange(@Path("mealId") mealId: String, @Header("Authorization") authorization: String, @Body body: MealChangeBody): Call<List<RetroAllowedChanges>>

    @DELETE("/api/v1/employee/meals/{mealId}/allowedchanges/{changeId}")
    fun removeMealChange(@Path("mealId") mealId: String,@Path("changeId") changeId: String, @Header("Authorization") authorization: String): Call<List<RetroAllowedChanges>>
}