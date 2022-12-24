package pt.ipca.smartcanteen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyOrdersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_orders)

        /** Orders **/
        val myOrdersRecyclerView = findViewById<RecyclerView>(R.id.myorders_recycler_view)
        val myOrders = mutableListOf<Order>(
            Order("1", 5, 15.0, "Pronto"),
            Order("123", 3, 5.0, "Em preparação"),
            Order("1233", 1, 1.0, "Em atraso"),
            Order("126", 1, 1.0, "Em atraso"),
            Order("167", 1, 1.0, "Em atraso"),
            Order("234", 1, 1.0, "Em atraso"),
            Order("225", 1, 1.0, "Em atraso"),
            Order("247", 1, 1.0, "Em atraso"),
            Order("365", 1, 1.0, "Em atraso"),
            Order("456", 1, 1.0, "Em atraso"),
            Order("789", 1, 1.0, "Em atraso"),
            Order("533", 1, 1.0, "Em atraso"),
            Order("344", 1, 1.0, "Em atraso"),
            Order("345", 1, 1.0, "Em atraso"),
            Order("222", 1, 1.0, "Em atraso"),
            Order("335", 1, 1.0, "Em atraso")
        )

        var myOrdersAdapter = OrdersAdapterRec(myOrders)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        myOrdersRecyclerView.layoutManager = linearLayoutManager
        myOrdersRecyclerView.itemAnimator = DefaultItemAnimator()
        myOrdersRecyclerView.adapter = myOrdersAdapter
    }
}

