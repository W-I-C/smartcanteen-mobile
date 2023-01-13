package pt.ipca.smartcanteen.views.fragments.consumer_fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroCartMeals
import pt.ipca.smartcanteen.models.adapters.MyFavoriteMealAdapterRec
import pt.ipca.smartcanteen.models.adapters.MyOrdersCartRec
import pt.ipca.smartcanteen.models.helpers.RetroFavoriteMeal
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.FavoritemealService
import pt.ipca.smartcanteen.services.MealsService
import pt.ipca.smartcanteen.views.activities.OrderActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class FavoriteMealFragment : Fragment() {

    private val cartMeals: RecyclerView by lazy { requireView().findViewById<RecyclerView>(R.id.idGRV) as RecyclerView }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.activity_favorite_meal, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = GridLayoutManager(requireContext(), 2)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        val retrofit = SmartCanteenRequests().retrofit
        val service = retrofit.create(FavoritemealService::class.java)
        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        var call = service.getFavoriteMeals("Bearer $token").enqueue(object :
            Callback<List<RetroFavoriteMeal>> {
            override fun onResponse(
                call: Call<List<RetroFavoriteMeal>>,
                response: Response<List<RetroFavoriteMeal>>
            ) {
                if (response.code() == 200) {
                    val retroFit2 = response.body()

                    if (retroFit2 != null) {
                        if(!retroFit2.isEmpty()){
                            val adapter = MyFavoriteMealAdapterRec(retroFit2)
                            rebuildlist(adapter)

                        }
                    }
                }
            }

            override fun onFailure(calll: Call<List<RetroFavoriteMeal>>, t: Throwable) {
                print("error")
            }
        })

    }



    fun rebuildlist(adapter: MyFavoriteMealAdapterRec) {
        val linearLayoutManager = GridLayoutManager(requireContext(), 2)
        cartMeals.layoutManager = linearLayoutManager
        cartMeals.itemAnimator = DefaultItemAnimator()
        cartMeals.adapter = adapter

    }
}