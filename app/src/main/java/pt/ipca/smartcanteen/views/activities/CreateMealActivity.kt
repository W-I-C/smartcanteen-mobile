package pt.ipca.smartcanteen.views.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.helpers.ImagesHelper
import pt.ipca.smartcanteen.views.fragments.consumer_fragments.ProfileFragment

class CreateMealActivity : AppCompatActivity() {
    private val listChange: RecyclerView by lazy { findViewById<RecyclerView>(R.id.crate_meal_recycler_view) as RecyclerView }
    private val mealName: EditText by lazy { findViewById<EditText>(R.id.create_meal_name_edittext) as EditText }
    private val preparationTime: EditText by lazy { findViewById<EditText>(R.id.create_meal_preparation_time_edittext) as EditText }
    private val price: EditText by lazy { findViewById<EditText>(R.id.create_meal_price_edittext) as EditText }
    private val descriptionMeal: EditText by lazy { findViewById<EditText>(R.id.create_meal_cart_description_edittext) as EditText }
    private val mealImage: ImageView by lazy { findViewById<EditText>(R.id.create_meal_image) as ImageView }
    private val saveBtn: Button by lazy { findViewById<Button>(R.id.create_meal_save_button) as Button }

    private val storageRef = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null

    companion object {
        val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_meal)
        saveBtn.setOnClickListener {
            /* TODO: criar a meal, obter o seu id e dps enviar a imagem para o firebase com o id */
        }
    }

    fun addIngredient(view: View) {
        val intent = Intent(this, AddMealChangeActivity::class.java)
        startActivity(intent)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ProfileFragment.IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                ImagesHelper().getImageFromDevice(data.data as Uri, this@CreateMealActivity, mealImage, false)

            }

        }

    }


    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, ProfileFragment.IMAGE_REQUEST_CODE)
    }


    private fun sendImage(file: Uri, mealId: String) {

        val stRef = storageRef.child("images/meals/${mealId}")
        val uploadTask = stRef.putFile(file)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            Log.d("MAIN", it.toString())
        }.addOnSuccessListener { taskSnapshot ->

        }


    }


}



