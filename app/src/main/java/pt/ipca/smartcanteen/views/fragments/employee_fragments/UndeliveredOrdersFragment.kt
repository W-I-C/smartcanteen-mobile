package pt.ipca.smartcanteen.views.fragments.employee_fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.*
import pt.ipca.smartcanteen.models.RetroTicket
import pt.ipca.smartcanteen.models.RetroTrade
import pt.ipca.smartcanteen.models.adapters.UndeliveredOrdersAdaterRec
import pt.ipca.smartcanteen.models.helpers.LoadingDialogManager
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.UndeliveredOrdersService
import pt.ipca.smartcanteen.views.activities.DetailedActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UndeliveredOrdersFragment : Fragment() {

    private val textError: TextView by lazy { requireView().findViewById<TextView>(R.id.undelivered_orders_empty_message) as TextView }
    private val undeliveredOrdersAdater: RecyclerView by lazy { requireView().findViewById<RecyclerView>(R.id.undelivered_orders_recycler_view) as RecyclerView }
    private lateinit var loadingDialogManager :LoadingDialogManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        loadingDialogManager = LoadingDialogManager(inflater, requireActivity())
        loadingDialogManager.createLoadingAlertDialog()
        return inflater.inflate(R.layout.fragment_undelivered_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(UndeliveredOrdersService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        loadingDialogManager.dialog.show()

        var call =
            service.seeUndeliveredOrders("Bearer $token").enqueue(object :
                Callback<List<RetroTrade>> {
                override fun onResponse(
                    call: Call<List<RetroTrade>>,
                    response: Response<List<RetroTrade>>
                ) {
                    if (response.code() == 200) {
                        loadingDialogManager.dialog.dismiss()
                        val retroFit2 = response.body()

                        if (retroFit2 != null)
                            if(retroFit2.isEmpty()){
                                textError.visibility = View.VISIBLE
                                undeliveredOrdersAdater.visibility = View.GONE
                            } else {
                                undeliveredOrdersAdater.visibility = View.VISIBLE
                                textError.visibility = View.GONE
                                rebuildlist(UndeliveredOrdersAdaterRec(retroFit2))
                            }
                    }
                }

                override fun onFailure(calll: Call<List<RetroTrade>>, t: Throwable) {
                    print("error")
                }
            })
    }

    fun rebuildlist(adapter: UndeliveredOrdersAdaterRec) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        undeliveredOrdersAdater.layoutManager = linearLayoutManager
        undeliveredOrdersAdater.itemAnimator = DefaultItemAnimator()
        undeliveredOrdersAdater.adapter = adapter

        // adapter.onItemClick = this::onItemClick

    }

    fun onItemClick(order: RetroTrade) {
        val intent = Intent(requireActivity(), DetailedActivity::class.java).apply {
            putExtra("order_nencomenda", order.norder)
            putExtra("order_name", order.ownername)
            putExtra("order_statename", order.statename)
        }
        startActivity(intent)
    }
}