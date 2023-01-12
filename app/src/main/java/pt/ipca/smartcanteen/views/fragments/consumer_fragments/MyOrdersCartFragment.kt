package pt.ipca.smartcanteen.views.fragments.consumer_fragmentshttps://smartcanteen-api.herokuapp.com/

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.models.adapters.MyOrdersCartRec
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroCartMeals
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.MealsService
import pt.ipca.smartcanteen.views.activities.OrderActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyOrdersCartFragment : Fragment() {
    private val Finalizar: Button by lazy {requireView().findViewById<Button>(R.id.pay_button) as Button }

    private val cartMeals: RecyclerView by lazy { requireView().findViewById<RecyclerView>(R.id.myorders_cart_recycler_view) as RecyclerView }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_cart, parent, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(MealsService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        var call =
            service.getMealsCart("Bearer $token").enqueue(object :
                Callback<List<RetroCartMeals>> {
                override fun onResponse(
                    call: Call<List<RetroCartMeals>>,
                    response: Response<List<RetroCartMeals>>
                ) {
                    if (response.code() == 200) {
                        val retroFit2 = response.body()

                        if (retroFit2 != null)
                            if(!retroFit2.isEmpty()){

                                rebuildlist(MyOrdersCartRec(retroFit2))
                            }
                    }
                }

                override fun onFailure(calll: Call<List<RetroCartMeals>>, t: Throwable) {
                    print("error")
                }
            })

        Finalizar.setOnClickListener {
            var intent = Intent(requireActivity(), OrderActivity::class.java)
            startActivity(intent)
        }
    }
    fun rebuildlist(adapter: MyOrdersCartRec) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        cartMeals.layoutManager = linearLayoutManager
        cartMeals.itemAnimator = DefaultItemAnimator()
        cartMeals.adapter = adapter

    }


}