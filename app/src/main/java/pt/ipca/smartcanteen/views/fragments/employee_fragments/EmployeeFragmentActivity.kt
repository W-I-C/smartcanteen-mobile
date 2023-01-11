package pt.ipca.smartcanteen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import pt.ipca.smartcanteen.views.fragments.consumer_fragments.MainFragment
import pt.ipca.smartcanteen.views.fragments.consumer_fragments.MyOrdersCartFragment
import pt.ipca.smartcanteen.views.fragments.consumer_fragments.ProfileFragment
import pt.ipca.smartcanteen.views.fragments.employee_fragments.UndeliveredOrdersFragment

class EmployeeFragmentActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_navigation_employee)

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.add(R.id.employee_fragment_container, MainFragment())
        ft.commit()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_employee)
        bottomNavigationView.selectedItemId = R.id.menu_employee_main
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_employee_main -> {

                }
                R.id.menu_employee_meals -> {

                }
                R.id.menu_employee_orders -> {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.employee_fragment_container, UndeliveredOrdersFragment())
                    ft.commit()
                }
                R.id.menu_employee_profile -> {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.employee_fragment_container, ProfileFragment())
                    ft.commit()
                }
            }
            true
        }
    }
}