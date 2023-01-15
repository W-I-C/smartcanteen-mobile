package pt.ipca.smartcanteen.models.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.adapters.viewHolders.MealsAdapterRecViewHolder
import pt.ipca.smartcanteen.models.RetroMeal
import pt.ipca.smartcanteen.models.helpers.*
import pt.ipca.smartcanteen.services.FavoritemealService
import pt.ipca.smartcanteen.views.activities.IngredientsChangeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealsAdapterRec(private var mealsList: List<RetroMeal>, private var activity: Activity, private var layoutInflater: LayoutInflater) :
    RecyclerView.Adapter<MealsAdapterRecViewHolder>() {

    val retrofit = SmartCanteenRequests().retrofit
    private lateinit var alertDialogManager: AlertDialogManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MealsAdapterRecViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: MealsAdapterRecViewHolder, position: Int) {
        val meal = mealsList.get(position)
        val mealid = meal.mealid
        val description = meal.description
        val mealName = meal.name
        val title = meal.name
        val price = "${meal.price}€"
        val preptime = "${meal.preparationtime}min"
        holder.bindData(title,preptime,price)

        holder.itemView.setOnClickListener{
            mealDetails(mealid,mealName,description,price,preptime)
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    fun mealDetails(mealid: String,mealName: String,mealDescription: String,mealPrice: String, mealPreptime: String){

        alertDialogManager = AlertDialogManager(layoutInflater, activity)
        alertDialogManager.createLoadingAlertDialog()

        val bottomSheetDialog = BottomSheetDialog(activity,
            R.style.BottomSheetDialogTheme
        )

        val bottomSheetView = LayoutInflater.from(activity).inflate(
            R.layout.meal_details,
            activity.findViewById(R.id.meal_bottom_sheet) as ConstraintLayout?
        )

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog
        bottomSheetDialog.show()

        val buttonIncrement = bottomSheetView.findViewById<Button>(R.id.meal_bottom_sheet_quantity_increment)
        val buttonDecrement = bottomSheetView.findViewById<Button>(R.id.meal_bottom_sheet_quantity_decrement)
        val quantity = bottomSheetView.findViewById<TextView>(R.id.meal_bottom_sheet_text_quantity_number)
        val ingredientsChange = bottomSheetView.findViewById<Button>(R.id.meal_bottom_sheet_change_ingredient)
        val favoriteHeart = bottomSheetView.findViewById<TextView>(R.id.meal_bottom_sheet_heart)
        val name = bottomSheetView.findViewById<TextView>(R.id.meal_bottom_sheet_name)
        val time = bottomSheetView.findViewById<TextView>(R.id.meal_bottom_sheet_time)
        val price = bottomSheetView.findViewById<TextView>(R.id.meal_bottom_sheet_price)
        val description = bottomSheetView.findViewById<TextView>(R.id.meal_bottom_sheet_description)
        // tem que ir para a bottom sheet (manda no intent) que recebe na resposta a lista
        val numbers = arrayListOf<String>()

        name.text = mealName
        time.text = mealPreptime
        price.text = mealPrice
        description.text = mealDescription


        buttonIncrement.setOnClickListener {
            var count: Int = quantity.text.toString().toInt()
            count++
            quantity.text = count.toString()
        }

        buttonDecrement.setOnClickListener {
            var count: Int = quantity.text.toString().toInt()
            if (count > 0) {
                count--
                quantity.text = count.toString()
            }
        }

        ingredientsChange.setOnClickListener {
            val intent = Intent(activity, IngredientsChangeActivity::class.java)
            intent.putExtra("mealid", mealid)
            intent.putExtra("numbers", numbers)
            activity.startActivity(intent)
        }

        // TODO: retornar no Meals se está a favorito ou não
        var isChecked: Boolean = false
        favoriteHeart.setOnClickListener {
            isChecked = !isChecked

            if (isChecked) {
                favoriteHeart.setBackgroundResource(R.drawable.favorite_heart)
                println("123")
                addFavMeal(mealid)
            } else {
                favoriteHeart.setBackgroundResource(R.drawable.bottom_sheet_heart_not_clicked)

                removeFavMeal(mealid)
            }
        }

//        addToCart.setOnClickListener {
//            // TODO: falta chamar o POST para adicionar ao carrinho
//
//            // para fechar a botom sheet
//            bottomSheetDialog.dismiss()
//            Toast.makeText(activity, "Refeição adicionada ao carrinho", Toast.LENGTH_LONG)
//                .show()
//        }
    }

    fun addFavMeal(mealid: String){
        val service = retrofit.create(FavoritemealService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(activity)
        val token = sp.getString("token", null)

        alertDialogManager.dialog.show()

        println("Aqui")
        service.addFavoriteMeal(mealid, "Bearer $token").enqueue(object :
            Callback<List<RetroFavoriteMeal>> {
            override fun onResponse(
                call: Call<List<RetroFavoriteMeal>>,
                response: Response<List<RetroFavoriteMeal>>
            ) {
                if (response.code() == 200) {

                    alertDialogManager.dialog.dismiss()

                    Toast.makeText(activity, "Refeição adicionada aos favoritos!", Toast.LENGTH_LONG)
                        .show()
                } else if (response.code() == 500) {
                    alertDialogManager.dialog.dismiss()

                    Toast.makeText(activity, "Erro! Não foi possível adicionar a refeição aos favoritos", Toast.LENGTH_LONG)
                        .show()
                } else if(response.code()==401){
                    alertDialogManager.dialog.dismiss()

                    AuthHelper().newSessionToken(activity)
                    addFavMeal(mealid)
                }
            }

            override fun onFailure(calll: Call<List<RetroFavoriteMeal>>, t: Throwable) {
                alertDialogManager.dialog.dismiss()
                println("Deu erro")
                Toast.makeText(activity, "Erro! Tente novamente.", Toast.LENGTH_LONG)
                    .show()
            }

        })
    }

    fun removeFavMeal(mealid: String){
        val service = retrofit.create(FavoritemealService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(activity)
        val token = sp.getString("token", null)

        alertDialogManager.dialog.show()

        service.removeFavoriteMeal(mealid, "Bearer $token").enqueue(object :
            Callback<List<RetroFavoriteMeal>> {
            override fun onResponse(
                call: Call<List<RetroFavoriteMeal>>,
                response: Response<List<RetroFavoriteMeal>>
            ) {
                if (response.code() == 200) {

                    alertDialogManager.dialog.dismiss()

                    Toast.makeText(activity, "Refeição removida dos favoritos!", Toast.LENGTH_LONG)
                        .show()
                } else if (response.code() == 500) {
                    alertDialogManager.dialog.dismiss()

                    Toast.makeText(activity, "Erro! Não foi possível remover a refeição dos favoritos", Toast.LENGTH_LONG)
                        .show()
                } else if(response.code()==401){
                    alertDialogManager.dialog.dismiss()

                    AuthHelper().newSessionToken(activity)
                    removeFavMeal(mealid)
                }
            }

            override fun onFailure(calll: Call<List<RetroFavoriteMeal>>, t: Throwable) {
                alertDialogManager.dialog.dismiss()
                Toast.makeText(activity, "Erro! Tente novamente.", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}