package pt.ipca.smartcanteen.services

import pt.ipca.smartcanteen.models.helpers.RetroFavoriteMeal
import retrofit2.Call
import retrofit2.http.*

interface FavoritemealService {
    @GET("/api/v1/consumer/favoriteMeals")
    fun getFavoriteMeals(@Header("Authorization") authorization: String): Call<List<RetroFavoriteMeal>>

    @POST("/api/v1/consumer/favoriteMeals/{mealId}")
    fun addFavoriteMeal(
        @Path("mealId") mealId: String,
        @Header("Authorization") authorization: String
    ): Call<List<RetroFavoriteMeal>>

    @DELETE("/api/v1/consumer/favoriteMeals/{mealId}")
    fun removeFavoriteMeal(
        @Path("mealId") mealId: String,
        @Header("Authorization") authorization: String
    ): Call<List<RetroFavoriteMeal>>
}