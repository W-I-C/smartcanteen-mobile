package pt.ipca.smartcanteen.views.fragments.consumer_fragmentshttps

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.adapters.MyOrdersCartRec
import pt.ipca.smartcanteen.models.helpers.AlertDialogManager
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.models.retrofit.response.RetroCartMeals
import pt.ipca.smartcanteen.services.MealsService
import pt.ipca.smartcanteen.views.activities.OrderActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyOrdersCartFragment : Fragment() {
    private val Finalizar: Button by lazy { requireView().findViewById<Button>(R.id.pay_button) as Button }
    private val total: TextView by lazy { requireView().findViewById<TextView>(R.id.numeric) as TextView }
    private val cartAdapterRec: RecyclerView by lazy { requireView().findViewById<RecyclerView>(R.id.myorders_cart_recycler_view) as RecyclerView }

    val linearLayoutManager = LinearLayoutManager(activity)
    private lateinit var alertDialogManager: AlertDialogManager


    override fun onCreateView(
        inflater: LayoutInflater, parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_cart, parent, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alertDialogManager = AlertDialogManager(layoutInflater, requireActivity())
        alertDialogManager.createLoadingAlertDialog()
        getMyOrders()

        Finalizar.setOnClickListener {
            var intent = Intent(requireActivity(), OrderActivity::class.java)
            startActivity(intent)
        }
    }

    fun getMyOrders() {
        val retrofit = SmartCanteenRequests().retrofit
        val service = retrofit.create(MealsService::class.java)
        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        var call = service.getMealsCart("Bearer $token").enqueue(object :
            Callback<List<RetroCartMeals>> {
            override fun onResponse(
                call: Call<List<RetroCartMeals>>,
                response: Response<List<RetroCartMeals>>
            ) {
                if (response.code() == 200) {
                    val retroFit2 = response.body()

                    if (retroFit2 != null) {
                        if (!retroFit2.isEmpty()) {
                            if (isAdded) {

                                rebuildlist(MyOrdersCartRec(retroFit2, requireActivity(), linearLayoutManager, cartAdapterRec, getString(R.string.wish_remove_meal_cart),
                                    alertDialogManager))
                                if (retroFit2.size >= 1)
                                    total.text = "${retroFit2[0].cartTotal} €"
                            }
                        }
                    }
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(requireActivity())
                    getMyOrders()
                }

            }

            override fun onFailure(calll: Call<List<RetroCartMeals>>, t: Throwable) {
                print("error")
            }
        })
    }

    fun rebuildlist(adapter: MyOrdersCartRec) {
        cartAdapterRec.layoutManager = linearLayoutManager
        cartAdapterRec.itemAnimator = DefaultItemAnimator()
        cartAdapterRec.adapter = adapter

    }
}