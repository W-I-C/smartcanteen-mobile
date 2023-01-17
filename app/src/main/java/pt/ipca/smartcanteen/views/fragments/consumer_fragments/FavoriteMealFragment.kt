package pt.ipca.smartcanteen.views.fragments.consumer_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.adapters.MyFavoriteMealAdapterRec
import pt.ipca.smartcanteen.models.adapters.MyOrdersCartRec
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.RetroFavoriteMeal
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.FavoritemealService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class FavoriteMealFragment : Fragment() {

    private val menuRv: RecyclerView by lazy { requireView().findViewById<RecyclerView>(R.id.favourites_menu_rv) as RecyclerView }
    private val loadingProgressBar: ProgressBar by lazy { requireView().findViewById<RecyclerView>(R.id.favourites_menu_progress_bar) as ProgressBar }
    private val loadingProgressText: TextView by lazy { requireView().findViewById<RecyclerView>(R.id.favourites_menu_progress_bar_text) as TextView }

    val linearLayoutManager=LinearLayoutManager(activity)

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_favorite_meal, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getFavs()
    }


    fun getFavs(){
        val retrofit = SmartCanteenRequests().retrofit
        val service = retrofit.create(FavoritemealService::class.java)
        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        menuRv.visibility = View.INVISIBLE
        loadingProgressBar.visibility = View.VISIBLE
        loadingProgressText.visibility = View.VISIBLE

        var call = service.getFavoriteMeals("Bearer $token").enqueue(object :
            Callback<List<RetroFavoriteMeal>> {
            override fun onResponse(
                call: Call<List<RetroFavoriteMeal>>,
                response: Response<List<RetroFavoriteMeal>>
            ) {
                menuRv.visibility = View.VISIBLE
                loadingProgressBar.visibility = View.INVISIBLE
                loadingProgressText.visibility = View.INVISIBLE
                if (response.code() == 200) {
                    val retroFit2 = response.body()

                    if (retroFit2 != null) {
                        if (!retroFit2.isEmpty()) {

                            rebuildlist(MyFavoriteMealAdapterRec(retroFit2,requireActivity(),linearLayoutManager,menuRv ))

                        }
                    }
                }else if(response.code()==401){
                    AuthHelper().newSessionToken(requireActivity())
                    getFavs()
                }
            }

            override fun onFailure(calll: Call<List<RetroFavoriteMeal>>, t: Throwable) {
                print("error")
            }
        })
    }


    fun rebuildlist(adapter: MyFavoriteMealAdapterRec) {
        val linearLayoutManager = GridLayoutManager(requireContext(), 2)
        menuRv.layoutManager = linearLayoutManager
        menuRv.itemAnimator = DefaultItemAnimator()
        menuRv.adapter = adapter

    }
}