package pt.ipca.smartcanteen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyOrdersActivity : AppCompatActivity() {
    private val buttonTrade: Button by lazy {findViewById<View>(R.id.my_orders_card_button_trade) as Button }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_orders)

        /** Orders **/
        val myOrdersRecyclerView = findViewById<RecyclerView>(R.id.myorders_recycler_view)
        val myOrders = mutableListOf<Order>(
            Order(1, 5, 15.0, "Pronto"),
            Order(123, 3, 5.0, "Em preparação"),
            Order(1233, 1, 1.0, "Em atraso"),
            Order(126, 1, 1.0, "Em atraso"),
            Order(167, 1, 1.0, "Em atraso"),
            Order(234, 1, 1.0, "Em atraso"),
            Order(225, 1, 1.0, "Em atraso"),
            Order(247, 1, 1.0, "Em atraso"),
            Order(365, 1, 1.0, "Em atraso"),
            Order(456, 1, 1.0, "Em atraso"),
            Order(789, 1, 1.0, "Em atraso"),
            Order(533, 1, 1.0, "Em atraso"),
            Order(344, 1, 1.0, "Em atraso"),
            Order(345, 1, 1.0, "Em atraso"),
            Order(222, 1, 1.0, "Em atraso"),
            Order(335, 1, 1.0, "Em atraso")
        )

        var myOrdersAdapter = OrdersAdapterRec(myOrders)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        myOrdersRecyclerView.layoutManager = linearLayoutManager
        myOrdersRecyclerView.itemAnimator = DefaultItemAnimator()
        myOrdersRecyclerView.adapter = myOrdersAdapter

        myOrdersAdapter.onItemClick = { order ->
            val intent = Intent(this, DetailedMyOrderActivity::class.java).apply {
                putExtra("order_identifier", order.identifier)
                putExtra("order_price", order.price)
                putExtra("order_quantity", order.quantity)
                putExtra("order_state", order.state)
            }
            startActivity(intent)
        }

        //myOrdersAdapter.onButtonTradeClick= {
        //    var intent = Intent(this@MyOrdersActivity, ConsumerExchangeActivity::class.java)
        //    startActivity(intent)
        //}


    }

    fun doTrade(view: View) {
        var intent = Intent(this@MyOrdersActivity, ConsumerExchangeActivity::class.java)
        startActivity(intent)
    }
}

