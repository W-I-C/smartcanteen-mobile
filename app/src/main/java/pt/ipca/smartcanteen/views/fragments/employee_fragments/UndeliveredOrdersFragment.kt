package pt.ipca.smartcanteen.views.fragments.employee_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.dmoral.toasty.Toasty
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.adapters.UndeliveredOrdersAdaterRec
import pt.ipca.smartcanteen.models.helpers.AlertDialogManager
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.models.retrofit.response.RetroTicket
import pt.ipca.smartcanteen.services.OrdersService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class UndeliveredOrdersFragment : Fragment() {

    private val textError: TextView by lazy { requireView().findViewById<TextView>(R.id.undelivered_orders_empty_message) as TextView }
    private val undeliveredOrdersAdater: RecyclerView by lazy { requireView().findViewById<RecyclerView>(R.id.undelivered_orders_recycler_view) as RecyclerView }
    private lateinit var alertDialogManager: AlertDialogManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        alertDialogManager = AlertDialogManager(inflater, requireActivity())
        alertDialogManager.createLoadingAlertDialog()
        return inflater.inflate(R.layout.fragment_undelivered_orders, container, false)
    }

    override fun onResume() {
        super.onResume()
        val retrofit = SmartCanteenRequests().retrofit
        getOrders(retrofit)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit = SmartCanteenRequests().retrofit
        getOrders(retrofit)


    }

    private fun getOrders(
        retrofit: Retrofit
    ) {

        val service = retrofit.create(OrdersService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        alertDialogManager.dialog.show()

        var call =
            service.seeUndeliveredOrders("Bearer $token").enqueue(object :
                Callback<List<RetroTicket>> {
                override fun onResponse(
                    call: Call<List<RetroTicket>>,
                    response: Response<List<RetroTicket>>
                ) {
                    if (response.code() == 200) {
                        if (isAdded) {
                            alertDialogManager.dialog.dismiss()
                            val retroFit2 = response.body()

                            if (retroFit2 != null)
                                if (retroFit2.isEmpty()) {
                                    textError.visibility = View.VISIBLE
                                    undeliveredOrdersAdater.visibility = View.GONE
                                } else {
                                    undeliveredOrdersAdater.visibility = View.VISIBLE
                                    textError.visibility = View.GONE
                                    rebuildlist(UndeliveredOrdersAdaterRec(requireActivity(), retroFit2))
                                }
                        }
                    } else if (response.code() == 401) {
                        AuthHelper().newSessionToken(requireActivity())
                        getOrders(retrofit)
                    }
                }

                override fun onFailure(calll: Call<List<RetroTicket>>, t: Throwable) {
                    Toasty.error(requireActivity(), getString(R.string.error), Toast.LENGTH_LONG).show()
                }
            })
    }

    fun rebuildlist(adapter: UndeliveredOrdersAdaterRec) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        undeliveredOrdersAdater.layoutManager = linearLayoutManager
        undeliveredOrdersAdater.itemAnimator = DefaultItemAnimator()
        undeliveredOrdersAdater.adapter = adapter
    }
}