package pt.ipca.smartcanteen.services

import pt.ipca.smartcanteen.models.helpers.RetroCreateMeal
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface CreateMealService{

    @GET("api/v1/employee/create/meal/")
    fun getCreateMeal(@Header("Authorization") authorization: String): Call<List<RetroCreateMeal>>

}