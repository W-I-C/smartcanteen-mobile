package pt.ipca.smartcanteen.views.fragments.employee_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroMeal
import pt.ipca.smartcanteen.models.adapters.EmployeeBarMenuMealsAdapterRec
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.AlertDialogManager
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.MealsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmployeeBarMenuFragment : Fragment() {
    private val menuRv: RecyclerView by lazy { requireView().findViewById<RecyclerView>(R.id.employee_bar_menu_rv) as RecyclerView }
    private val loadingProgressBar: ProgressBar by lazy { requireView().findViewById<RecyclerView>(R.id.employee_bar_menu_progress_bar) as ProgressBar }
    private val loadingProgressText: TextView by lazy { requireView().findViewById<RecyclerView>(R.id.employee_bar_menu_progress_bar_text) as TextView }

    private lateinit var alertDialogManager: AlertDialogManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        alertDialogManager = AlertDialogManager(layoutInflater, requireActivity())
        alertDialogManager.createLoadingAlertDialog()

        return inflater.inflate(R.layout.fragment_employee_bar_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMealsList()
    }


    private fun getMealsList(
    ) {

        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(MealsService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireActivity())
        val token = sp.getString("token", null)

        menuRv.visibility = View.INVISIBLE
        loadingProgressBar.visibility = View.VISIBLE
        loadingProgressText.visibility = View.VISIBLE

        service.getEmployeeBarMeals("Bearer $token").enqueue(object :
            Callback<List<RetroMeal>> {
            override fun onResponse(
                call: Call<List<RetroMeal>>,
                response: Response<List<RetroMeal>>
            ) {
                if (response.code() == 200) {
                    val body = response.body()

                    menuRv.visibility = View.VISIBLE
                    loadingProgressBar.visibility = View.INVISIBLE
                    loadingProgressText.visibility = View.INVISIBLE

                    if (body != null) {
                        if (body.isNotEmpty()) {
                            /** bar Meals **/
                            val barMealsLayoutManager = GridLayoutManager(requireActivity(), 2)
                            barMealsLayoutManager.orientation = LinearLayoutManager.VERTICAL
                            val barMealsAdapter = EmployeeBarMenuMealsAdapterRec(
                                { getMealsList() },
                                requireActivity(),
                                alertDialogManager,
                                getString(R.string.wish_remove_meal_ask),
                                getString(R.string.meal_being_used),
                                body
                            )

                            menuRv.layoutManager = barMealsLayoutManager
                            menuRv.itemAnimator = DefaultItemAnimator()
                            menuRv.adapter = barMealsAdapter
                        }
                    }
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(requireActivity())
                    getMealsList()
                }
            }

            override fun onFailure(call: Call<List<RetroMeal>>, t: Throwable) {
                menuRv.visibility = View.VISIBLE
                loadingProgressBar.visibility = View.INVISIBLE
                loadingProgressText.visibility = View.INVISIBLE
            }
        })
    }
}