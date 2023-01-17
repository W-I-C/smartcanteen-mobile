package pt.ipca.smartcanteen.views.fragments.employee_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroBarStatistics
import pt.ipca.smartcanteen.models.helpers.*
import pt.ipca.smartcanteen.services.CampusService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EmployeeMenuFragment(private val supportFragmentManager:FragmentManager, private val bottomNavigationMenuView: BottomNavigationView) : Fragment() {
    private val seeMenuIv: ImageView by lazy { requireView().findViewById<View>(R.id.employee_menu_see_menu_button) as ImageView }
    private val seeOrdersIv: ImageView by lazy { requireView().findViewById<View>(R.id.employee_menu_see_orders_button) as ImageView }

    private val totalOrdersTv: TextView by lazy { requireView().findViewById<View>(R.id.employee_menu_total_orders_card_value) as TextView }
    private val deliveredOrdersTv: TextView by lazy { requireView().findViewById<View>(R.id.employee_menu_delivered_orders_card_value) as TextView }
    private val undeliveredOrdersTv: TextView by lazy { requireView().findViewById<View>(R.id.employee_menu_total_undelivered_card_value) as TextView }
    private val tradesMadeTv: TextView by lazy { requireView().findViewById<View>(R.id.employee_menu_trades_done_card_value) as TextView }

    private val totalOrdersTitleTv: TextView by lazy { requireView().findViewById<View>(R.id.employee_menu_total_requests_title) as TextView }

    private val deliveredOrdersProgress: ProgressBar by lazy { requireView().findViewById<View>(R.id.employee_menu_delivered_requests_progress_bar) as ProgressBar }
    private val undeliveredOrdersProgress: ProgressBar by lazy { requireView().findViewById<View>(R.id.employee_menu_undelivered_requests_progress_bar) as ProgressBar }

    private lateinit var alertDialogManager: AlertDialogManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        alertDialogManager = AlertDialogManager(inflater, requireActivity())
        alertDialogManager.createLoadingAlertDialog()

        return inflater.inflate(R.layout.fragment_employee_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCanteenStatistics()

        seeMenuIv.setOnClickListener {
            bottomNavigationMenuView.selectedItemId = R.id.menu_employee_meals
        }

        seeOrdersIv.setOnClickListener {
            bottomNavigationMenuView.selectedItemId = R.id.menu_employee_orders
        }
    }

    private fun getCanteenStatistics() {
        val retrofit = SmartCanteenRequests().retrofit
        val service = retrofit.create(CampusService::class.java)
        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        alertDialogManager.dialog.show()
        var call = service.getBarStatistics("Bearer $token").enqueue(object :
            Callback<RetroBarStatistics> {
            override fun onResponse(
                call: Call<RetroBarStatistics>,
                response: Response<RetroBarStatistics>
            ) {

                if (response.code() == 200) {
                    val body = response.body()

                    if (body != null) {
                        placeValues(
                            body.totalTickets,
                            body.deliveredTickets,
                            body.toDeliverTickets,
                            body.tradedTickets
                        )

                    }
                    alertDialogManager.dialog.dismiss()
                } else if (response.code() == 401) {
                    alertDialogManager.dialog.dismiss()
                    AuthHelper().newSessionToken(requireActivity())
                    getCanteenStatistics()
                }
            }

            override fun onFailure(calll: Call<RetroBarStatistics>, t: Throwable) {
                alertDialogManager.dialog.dismiss()
            }
        })
    }

    private fun placeValues(
        totalOrders: Int,
        totalDeliveredOrders: Int,
        totalUndeliveredOrders: Int,
        totalTradesMade: Int
    ) {
        totalOrdersTv.text = totalOrders.toString()
        deliveredOrdersTv.text = totalDeliveredOrders.toString()
        undeliveredOrdersTv.text = totalUndeliveredOrders.toString()
        tradesMadeTv.text = totalTradesMade.toString()

        totalOrdersTitleTv.text = "${requireActivity().getString(R.string.total_requests)}: ${totalOrders}"

        deliveredOrdersProgress.max = totalOrders
        undeliveredOrdersProgress.max = totalOrders
        deliveredOrdersProgress.setProgress(totalDeliveredOrders)
        undeliveredOrdersProgress.setProgress(totalUndeliveredOrders)
    }
}