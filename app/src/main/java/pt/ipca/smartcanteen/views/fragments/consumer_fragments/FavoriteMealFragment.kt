//package pt.ipca.smartcanteen.views.fragments.consumer_fragments
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.GridView
//import androidx.recyclerview.widget.DefaultItemAnimator
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import pt.ipca.smartcanteen.R
//import pt.ipca.smartcanteen.models.RetroTicket
//import pt.ipca.smartcanteen.models.adapters.FavoriteMealAdapterRec
//import pt.ipca.smartcanteen.models.adapters.UndeliveredOrdersAdaterRec
//import pt.ipca.smartcanteen.models.helpers.RetroFavoriteMeal
//import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
//import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
//import pt.ipca.smartcanteen.services.FavoritemealService
//import pt.ipca.smartcanteen.services.OrdersService
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//@Suppress("UNREACHABLE_CODE")
//class FavoriteMealFragment : Fragment() {
//    private val gridView: GridView by lazy { requireView().findViewById<GridView>(R.id.idGRV) as GridView }
//
//
//
//    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View {
//        val view = inflater.inflate(R.layout.activity_favorite_meal, parent, false)
//        gridView = view.findViewById(R.id.idGRV)
//
//        val retrofit = SmartCanteenRequests().retrofit
//
//        val service = retrofit.create(FavoritemealService::class.java)
//
//        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
//        val token = sp.getString("token", null)
//
//        var call =
//            service.getFavoriteMeals("Bearer $token").enqueue(object :
//                Callback<List<RetroFavoriteMeal>> {
//                override fun onResponse(
//                    call: Call<List<RetroFavoriteMeal>>,
//                    response: Response<List<RetroFavoriteMeal>>
//                ) {
//                    if (response.code() == 200) {
//                        val retroFit2 = response.body()
//
//                        if (retroFit2 != null)
//                            if(retroFit2.isEmpty()){
//
//                            } else {
//
//                                rebuildlist(FavoriteMealAdapterRec(retroFit2))
//                            }
//                    }
//                }
//
//                override fun onFailure(calll: Call<List<RetroFavoriteMeal>>, t: Throwable) {
//                    print("error")
//                }
//            })
//    }
//
//    fun rebuildlist(adapter: FavoriteMealAdapterRec) {
//        val linearLayoutManager = LinearLayoutManager(requireContext())
//        gridView.layoutManager = linearLayoutManager
//        gridView.itemAnimator = DefaultItemAnimator()
//        gridView.adapter = adapter
//
//
//    }
//    }
//
//
//
//}
//
