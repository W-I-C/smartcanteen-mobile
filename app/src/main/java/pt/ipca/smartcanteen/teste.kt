package pt.ipca.smartcanteen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class teste : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView

    private val cartActivity: View by lazy {findViewById<View>(R.id.activity_cart) as View }
    private val profileActivity: View by lazy {findViewById<View>(R.id.profile) as View }
    private val myOrdersActivity: View by lazy {findViewById<View>(R.id.my_orders) as View }
    private val mainActivity: View by lazy {findViewById<View>(R.id.activity_main) as View }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_navigation)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_activity_cart -> {
                    // Mostra o layout activity_cart
                    cartActivity.visibility = View.VISIBLE
                    profileActivity.visibility = View.GONE
                    myOrdersActivity.visibility = View.GONE
                    mainActivity.visibility = View.GONE
                }
                R.id.menu_activity_favorites -> {
                    // Mostra o layout activity_favorites
                }
                R.id.menu_activity_main -> {
                    // Mostra o layout activity_main
                    mainActivity.visibility = View.VISIBLE
                    profileActivity.visibility = View.GONE
                    myOrdersActivity.visibility = View.GONE
                    cartActivity.visibility = View.GONE
                }
                R.id.menu_activity_orders -> {

                    // Mostra o layout my_orders
                    //myOrdersActivity.visibility = View.VISIBLE


                    // Cria uma instância da sua classe e chama o método de inicialização
                    val myOrdersActivity = MyOrdersActivity()
                    // myOrdersActivity.onCreate(savedInstanceState)

                    // Atualiza a RecyclerView
                    //val myOrdersRecyclerView = findViewById<RecyclerView>(R.id.myorders_recycler_view)
                    //myOrdersRecyclerView.adapter = myOrdersAdapter
                    //val adapter = recyclerView.myOrders
                    //adapter?.notifyDataSetChanged()

                    //val intent = Intent(this, MyOrdersActivity::class.java)
                    //startActivity(intent)

                    profileActivity.visibility = View.GONE
                    cartActivity.visibility = View.GONE
                    mainActivity.visibility = View.GONE
                }
                R.id.menu_activity_profile -> {
                    // Mostra o layout activity_profile
                    profileActivity.visibility = View.VISIBLE
                    cartActivity.visibility = View.GONE
                    myOrdersActivity.visibility = View.GONE
                    mainActivity.visibility = View.GONE
                }
            }
            true
        }

    }
}