package pt.ipca.smartcanteen.views.fragments.consumer_fragments


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.storage.FirebaseStorage
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.helpers.*
import pt.ipca.smartcanteen.models.retrofit.body.ProfileBody
import pt.ipca.smartcanteen.models.retrofit.response.RetroBar
import pt.ipca.smartcanteen.models.retrofit.response.RetroCampus
import pt.ipca.smartcanteen.models.retrofit.response.RetroProfile
import pt.ipca.smartcanteen.models.room.tables.Profile
import pt.ipca.smartcanteen.services.CampusService
import pt.ipca.smartcanteen.services.ProfileService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {

    private val name: TextView by lazy { requireView().findViewById<TextView>(R.id.profile_name_tv) as TextView }
    private val spinnerCampus: Spinner by lazy { requireView().findViewById<Spinner>(R.id.profile_preferred_campus_sp) as Spinner }
    private val spinnerBar: Spinner by lazy { requireView().findViewById<Spinner>(R.id.profile_preferred_bar_sp) as Spinner }
    private val profilePic: ImageView by lazy { requireView().findViewById<ImageView>(R.id.profile_image_imageview) as ImageView }


    private val saveBtn: Button by lazy { requireView().findViewById<Button>(R.id.profile_save_btn) as Button }
    private val cancelBtn: Button by lazy { requireView().findViewById<Button>(R.id.profile_button_cancel) as Button }

    private lateinit var alertDialogManager: AlertDialogManager

    private var localProfile: Profile? = null


    private var selectedCampus: RetroCampus? = null
    private var selectedBar: RetroBar? = null

    private val storageRef = FirebaseStorage.getInstance().reference

    companion object {
        val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return inflater.inflate(R.layout.fragment_profile, parent, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profilePic.setOnClickListener {
            pickImageGallery()
        }

        saveBtn.setOnClickListener {
            if(selectedBar!= null && selectedCampus!= null && localProfile!=null) {
                if (!(selectedCampus!!.campusid == localProfile!!.campusId && selectedBar!!.barid == localProfile!!.barId)) {
                    saveProfile()
                } else {
                    Toasty.error(requireContext(), requireContext().getString(R.string.didnt_change)).show()
                }
            }
        }


        cancelBtn.setOnClickListener {
            var barAdapter = activity?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayOf(localProfile!!.barName)
                )
            }

            var campusAdapter = activity?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayOf(localProfile!!.campusName)
                )
            }
            buildBarSpinner(barAdapter, listOf(RetroBar(localProfile!!.barId, localProfile!!.barName)), localProfile!!.barName)
            buildCampusSpinner(
                campusAdapter,
                listOf(RetroCampus(localProfile!!.campusId, localProfile!!.campusName)),
                localProfile!!.campusName,
                localProfile!!.barName
            )
            getData()
        }

        val db = SmartCanteenDBHelper.getInstance(requireContext().applicationContext)
        GlobalScope.launch {
            localProfile = db.profileDao().getProfile()
            if (localProfile != null) {
                Log.d("MAIN", "Found Local Profile")
                Log.d("MAIN", localProfile.toString())

                var barAdapter = activity?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item,
                        arrayOf(localProfile!!.barName)
                    )
                }

                var campusAdapter = activity?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item,
                        arrayOf(localProfile!!.campusName)
                    )
                }


                buildBarSpinner(barAdapter, listOf(RetroBar(localProfile!!.barId, localProfile!!.barName)), localProfile!!.barName)
                buildCampusSpinner(
                    campusAdapter,
                    listOf(RetroCampus(localProfile!!.campusId, localProfile!!.campusName)),
                    localProfile!!.campusName,
                    localProfile!!.barName
                )
                name.text = localProfile!!.name
            }
        }
        getImage()
        getData()
    }


    fun saveProfile() {
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(ProfileService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        val body = ProfileBody(selectedCampus?.campusid!!, selectedBar?.barid!!)

        service.editProfile(body, "Bearer $token").enqueue(object :
            Callback<RetroProfile> {
            override fun onResponse(
                call: Call<RetroProfile>,
                response: Response<RetroProfile>
            ) {
                if (response.code() == 200) {
                    val db = SmartCanteenDBHelper.getInstance(requireContext().applicationContext)
                    if (localProfile != null) {
                        val profile = Profile(
                            uid = localProfile!!.uid,
                            name = localProfile!!.name,
                            email = localProfile!!.email,
                            campusId = selectedCampus!!.campusid,
                            campusName = selectedCampus!!.name,
                            barId = selectedBar!!.barid,
                            barName = selectedCampus!!.name
                        )

                        GlobalScope.launch {
                            val data = db.profileDao().getProfile()
                            if (data != null) {
                                db.profileDao().update(profile)
                                localProfile = db.profileDao().getProfile()
                                Log.d("MAIN", localProfile.toString())
                            } else {
                                db.profileDao().insertAll(profile)
                                localProfile = db.profileDao().getProfile()
                                Log.d("MAIN", localProfile.toString())
                            }
                        }
                    }

                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(requireActivity())
                    saveProfile()
                }

            }

            override fun onFailure(call: Call<RetroProfile>, t: Throwable) {
                print("error")
            }

        })
    }

    fun getCampusBarInfo(
        defaultBar: String,
        campusId: String,
    ) {
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(CampusService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        service.getCampusIdBars(campusId,"Bearer $token").enqueue(object :
            Callback<List<RetroBar>> {
            override fun onResponse(
                call: Call<List<RetroBar>>,
                response: Response<List<RetroBar>>
            ) {
                if (response.code() == 200) {
                    val body = response.body()

                    if (body != null) {
                        if (body.isNotEmpty()) {
                            if (isAdded) {

                                var adapter = activity?.let {
                                    ArrayAdapter(
                                        it,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        body.map { retroBar -> retroBar.name }
                                    )
                                }
                                Log.d("MAIN", body.toString())
                                buildBarSpinner(adapter, body, defaultBar)
                            }
                        }
                    }
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(requireActivity())
                    getBarInfo(defaultBar)
                }

            }

            override fun onFailure(call: Call<List<RetroBar>>, t: Throwable) {
                print("error")
            }

        })

    }

    fun getBarInfo(
        defaultBar: String
    ) {
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(CampusService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        service.getCampusBars("Bearer $token").enqueue(object :
            Callback<List<RetroBar>> {
            override fun onResponse(
                call: Call<List<RetroBar>>,
                response: Response<List<RetroBar>>
            ) {
                if (response.code() == 200) {
                    val body = response.body()

                    if (body != null) {
                        if (body.isNotEmpty()) {
                            if (isAdded) {

                                var adapter = activity?.let {
                                    ArrayAdapter(
                                        it,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        body.map { retroBar -> retroBar.name }
                                    )
                                }
                                buildBarSpinner(adapter, body, defaultBar)
                            }
                        }
                    }
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(requireActivity())
                    getBarInfo(defaultBar)
                }

            }

            override fun onFailure(call: Call<List<RetroBar>>, t: Throwable) {
                print("error")
            }

        })

    }

    fun getCampusInfo(
        defaultCampus: String,
        defaultBar: String
    ) {
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(CampusService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)

        service.getCampus("Bearer $token").enqueue(object :
            Callback<List<RetroCampus>> {
            override fun onResponse(
                call: Call<List<RetroCampus>>,
                response: Response<List<RetroCampus>>
            ) {
                if (response.code() == 200) {
                    val body = response.body()

                    if (body != null) {
                        if (body.isNotEmpty()) {
                            if (isAdded) {
                                var adapter = activity?.let {
                                    ArrayAdapter(
                                        it,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        body.map { campus -> campus.name }
                                    )

                                }
                                buildCampusSpinner(adapter, body, defaultCampus, defaultBar)
                            }
                        }
                    }
                } else if (response.code() == 401) {
                    AuthHelper().newSessionToken(requireActivity())
                    getBarInfo(defaultCampus)
                }

            }

            override fun onFailure(call: Call<List<RetroCampus>>, t: Throwable) {
                print("error")
            }

        })

    }

    private fun buildCampusSpinner(adapter: ArrayAdapter<String>?, body: List<RetroCampus>, defaultCampus: String, defaultBar: String) {

        adapter?.setDropDownViewResource(R.layout.spinner_item)
        spinnerCampus.adapter = adapter
        val index = body.indexOfFirst { campus -> campus.name == defaultCampus }
        spinnerCampus.setSelection(index)
        spinnerCampus.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val campusId = body[position].campusid
                    val name = body[position].name
                    selectedCampus = RetroCampus(campusId, name)
                    getCampusBarInfo(defaultBar,campusId)
                }
            }
    }

    private fun buildBarSpinner(adapter: ArrayAdapter<String>?, body: List<RetroBar>, defaultBar: String) {

        adapter?.setDropDownViewResource(R.layout.spinner_item)
        spinnerBar.adapter = adapter
        val index = body.indexOfFirst { bar -> bar.name == defaultBar }
        spinnerBar.setSelection(index)
        spinnerBar.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val barId = body[position].barid
                    val name = body[position].name
                    selectedBar = RetroBar(barId, name)

                }
            }
    }


    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                ImagesHelper().getImageFromDevice(data.data as Uri, requireActivity(), profilePic, true)
                sendImage(data.data as Uri)
            }

        }
    }

    private fun sendImage(file: Uri) {
        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val uid = sp.getString("uid", null)

        val stRef = storageRef.child("images/users/${uid}")
        val uploadTask = stRef.putFile(file)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            Log.d("MAIN", it.toString())
            Toasty.error(requireContext(), getString(R.string.image_upload_error)).show()
        }.addOnSuccessListener { taskSnapshot ->

        }
    }

    private fun getImage() {
        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val uid = sp.getString("uid", null)

        storageRef.child("images/users/${uid}").downloadUrl.addOnSuccessListener {
            Log.d("MAIN", it.toString())
            ImagesHelper().getImage(it.toString(), profilePic, true)
        }.addOnFailureListener {
            storageRef.child("images/users/default_profile_pic.jpg").downloadUrl.addOnSuccessListener {
                Log.d("MAIN", it.toString())
                ImagesHelper().getImage(it.toString(), profilePic, true)
            }.addOnFailureListener {
                Log.d("MAIN", it.toString())
            }
        }

    }


    private fun getData() {
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(ProfileService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val token = sp.getString("token", null)
        val uid = sp.getString("uid", null)

        var call =
            service.getViewProfile("Bearer $token").enqueue(object :
                Callback<RetroProfile> {
                override fun onResponse(
                    call: Call<RetroProfile>,
                    response: Response<RetroProfile>
                ) {
                    if (response.code() == 200) {
                        val body = response.body()
                        if (isAdded && body != null) {
                            val db = SmartCanteenDBHelper.getInstance(requireContext().applicationContext)
                            if (body != null && uid != null) {
                                val profile = Profile(
                                    uid = uid,
                                    name = body.name,
                                    email = body.email,
                                    campusId = body.campusid,
                                    campusName = body.campusname,
                                    barId = body.barid,
                                    barName = body.barname
                                )
                                GlobalScope.launch {
                                    val data = db.profileDao().getProfile()
                                    if (data != null) {
                                        db.profileDao().update(profile)
                                        localProfile = db.profileDao().getProfile()
                                        Log.d("MAIN", localProfile.toString())
                                    } else {
                                        db.profileDao().insertAll(profile)
                                        localProfile = db.profileDao().getProfile()
                                        Log.d("MAIN", localProfile.toString())
                                    }
                                }
                            }
                            name.text = body.name

                            getCampusInfo(body.campusname, body.barname)
                        }
                    } else if (response.code() == 401) {
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

