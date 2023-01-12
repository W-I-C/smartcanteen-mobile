package pt.ipca.smartcanteen.services

import pt.ipca.smartcanteen.models.RetroCartMeals
import pt.ipca.smartcanteen.models.helpers.RetroFavoriteMeal
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface FavoritemealService {
    @GET("/api/v1/consumer/favoriteMeals")
    fun getFavoriteMeals(@Header("Authorization") authorization: String): Call<List<RetroFavoriteMeal>>
}