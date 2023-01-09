package pt.ipca.smartcanteen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import pt.ipca.smartcanteen.consumer_fragments.MainFragment
import pt.ipca.smartcanteen.consumer_fragments.MyOrdersCartFragment
import pt.ipca.smartcanteen.consumer_fragments.MyOrdersFragment
import pt.ipca.smartcanteen.consumer_fragments.ProfileFragment

class ConsumerFragmentActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_navigation_consumer)

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.add(R.id.consumer_fragment_container, MainFragment())
        ft.commit()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_consumer)
        bottomNavigationView.selectedItemId = R.id.menu_consumer_main
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_consumer_cart -> {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.consumer_fragment_container, MyOrdersCartFragment())
                    ft.commit()
                }
                R.id.menu_consumer_favorites -> {

                }
                R.id.menu_consumer_main -> {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.consumer_fragment_container, MainFragment())
                    ft.commit()
                }
                R.id.menu_consumer_orders -> {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.consumer_fragment_container, MyOrdersFragment())
                    ft.commit()
                }
                R.id.menu_consumer_profile -> {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.consumer_fragment_container, ProfileFragment())
                    ft.commit()
                }
            }
            true
        }
    }
}