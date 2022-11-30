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
            Order("Francesinha", 5.0, 15.0, "Pronto"),
            Order("Panado", 3.5, 5.0, "Em preparação"),
            Order("Salada", 1.5, 1.0, "Em atraso"),
        )

        var myOrdersAdapter = OrdersAdapterRec(myOrders)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        myOrdersRecyclerView.layoutManager = linearLayoutManager
        myOrdersRecyclerView.itemAnimator = DefaultItemAnimator()
        myOrdersRecyclerView.adapter = myOrdersAdapter
    }
}