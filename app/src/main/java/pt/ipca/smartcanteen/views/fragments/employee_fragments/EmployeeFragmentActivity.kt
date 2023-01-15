package pt.ipca.smartcanteen.views.fragments.employee_fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.views.fragments.consumer_fragments.ProfileFragment

class EmployeeFragmentActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_navigation_employee)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_employee)

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.add(R.id.employee_fragment_container, EmployeeMenuFragment(supportFragmentManager,bottomNavigationView))
        ft.commit()


        bottomNavigationView.selectedItemId = R.id.menu_employee_main
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_employee_main -> {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.employee_fragment_container, EmployeeMenuFragment(supportFragmentManager,bottomNavigationView))
                    ft.commit()
                }
                R.id.menu_employee_meals -> {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.employee_fragment_container, EmployeeBarMenuFragment())
                    ft.commit()
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