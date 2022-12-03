
package pt.ipca.smartcanteen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyOrdersCartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        /** Orders **/
        val myOrdersCartRecyclerView = findViewById<RecyclerView>(R.id.myorders_cart_recycler_view)
        val myOrders = mutableListOf<MyOrderCart>(
            MyOrderCart("Francesinha", 5, 15.0 ),
            MyOrderCart("Panado", 3, 5.0 ),
            MyOrderCart("Salada", 1, 1.0),
            MyOrderCart("Frango", 1, 2.0),
            MyOrderCart("Pizza", 1, 5.0),


            )

        var myOrdersCartAdapter = MyOrdersCartRec(myOrders)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        myOrdersCartRecyclerView.layoutManager = linearLayoutManager
        myOrdersCartRecyclerView.itemAnimator = DefaultItemAnimator()
        myOrdersCartRecyclerView.adapter = myOrdersCartAdapter
    }
}

