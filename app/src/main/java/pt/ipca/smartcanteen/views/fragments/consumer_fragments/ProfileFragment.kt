package pt.ipca.smartcanteen.views.fragments.consumer_fragments


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.firebase.storage.FirebaseStorage
import es.dmoral.toasty.Toasty
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroProfile
import pt.ipca.smartcanteen.models.helpers.*
import pt.ipca.smartcanteen.services.ProfileService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class ProfileFragment : Fragment() {

    private val name: EditText by lazy { requireView().findViewById<EditText>(R.id.profile_name_edittext) as EditText }
    private val spinnerCampus: EditText by lazy { requireView().findViewById<EditText>(R.id.profile_preferencecantine_edittext) as EditText }
    private val spinnerBar: EditText by lazy { requireView().findViewById<EditText>(R.id.main_institute_edittext) as EditText }
    private val profilePic: ImageView by lazy { requireView().findViewById<ImageView>(R.id.profile_image_imageview) as ImageView }

    private lateinit var alertDialogManager: AlertDialogManager


    private val storageRef = FirebaseStorage.getInstance().reference

    companion object{
        val IMAGE_REQUEST_CODE=100
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
        getImage()
        getData()
    }


    private fun pickImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            if(data != null){
                ImagesHelper().getImageFromDevice(data.data as Uri, requireActivity(),profilePic,true)
                sendImage(data.data as Uri)
            }

        }
    }

    private fun sendImage(file:Uri) {
        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val uid = sp.getString("uid", null)

        val stRef = storageRef.child("images/users/${uid}")
        val uploadTask = stRef.putFile(file)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            Log.d("MAIN", it.toString())
            Toasty.error(requireContext(),getString(R.string.image_upload_error)).show()
        }.addOnSuccessListener { taskSnapshot ->

        }



    }

    private fun getImage(){
        val sp = SharedPreferencesHelper.getSharedPreferences(requireContext())
        val uid = sp.getString("uid", null)

        storageRef.child("images/users/${uid}").downloadUrl.addOnSuccessListener {
            Log.d("MAIN", it.toString())
            ImagesHelper().getImage(it.toString(),profilePic,true)
        }.addOnFailureListener {
            storageRef.child("images/users/default_profile_pic.jpg").downloadUrl.addOnSuccessListener {
                Log.d("MAIN", it.toString())
                ImagesHelper().getImage(it.toString(),profilePic,true)
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

        var call =
            service.getViewProfile("Bearer $token").enqueue(object :
                Callback<RetroProfile> {
                override fun onResponse(
                    call: Call<RetroProfile>,
                    response: Response<RetroProfile>
                ) {
                    if (response.code() == 200) {
                        if (isAdded) {
                            val retroFit2 = response.body()
                            name.setText(retroFit2?.name)
                            spinnerCampus.setText(retroFit2?.campusname)
                            spinnerBar.setText(retroFit2?.barname)
                            //ImagesHelper().getImage(retroFit2?.imgurl!!, profilePic, true)
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

