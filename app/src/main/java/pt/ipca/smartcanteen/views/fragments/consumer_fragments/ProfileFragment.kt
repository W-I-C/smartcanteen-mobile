package pt.ipca.smartcanteen.views.fragments.consumer_fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroBar
import pt.ipca.smartcanteen.models.RetroProfile
import pt.ipca.smartcanteen.models.helpers.AlertDialogManager
import pt.ipca.smartcanteen.models.helpers.AuthHelper
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.CampusService
import pt.ipca.smartcanteen.services.ProfileService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class ProfileFragment : Fragment() {

    private val name: EditText by lazy { requireView().findViewById<EditText>(R.id.main_Name_editText) as EditText }
    private val spinnerCampus: EditText by lazy { requireView().findViewById<EditText>(R.id.main_PreferenceCantine_editText) as EditText }
    private val spinnerBar: EditText by lazy { requireView().findViewById<EditText>(R.id.main_Institute_editText) as EditText }

    private lateinit var alertDialogManager : AlertDialogManager
    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.fragment_profile, parent, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()
    }

    private fun getData(){
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(ProfileService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        var call =
            service.getViewProfile("Bearer $token").enqueue(object :
                Callback<RetroProfile> {
                override fun onResponse(
                    call: Call<RetroProfile>,
                    response: Response<RetroProfile>
                ) {
                    if (response.code() == 200) {
                        val retroFit2 = response.body()
                        name.setText(retroFit2?.name)
                        spinnerCampus.setText(retroFit2?.campusname)
                        spinnerBar.setText(retroFit2?.barname)



                    }else if(response.code()==401){
                        AuthHelper().newSessionToken(requireActivity())
                        getData()
                    }
                }

                override fun onFailure(calll: Call<RetroProfile>, t: Throwable) {
                    print("error")
                }
            })
    }






}

