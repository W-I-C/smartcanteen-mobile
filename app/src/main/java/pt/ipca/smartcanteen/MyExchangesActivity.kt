package pt.ipca.smartcanteen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyExchangesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_exchanges)

        /** Exchanges **/
        val myExchangesRecyclerView = findViewById<RecyclerView>(R.id.my_exchanges_recycler_view)
        val myExchanges = mutableListOf<Exchange>(
            Exchange(123, 5, 15.0, "Pronto"),
            Exchange(145, 3, 5.0, "Em preparação"),
            Exchange(246, 1, 1.0, "Em atraso"),
        )

        var myExchangesAdapter = ExchangesAdapterRec(myExchanges)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        myExchangesRecyclerView.layoutManager = linearLayoutManager
        myExchangesRecyclerView.itemAnimator = DefaultItemAnimator()
        myExchangesRecyclerView.adapter = myExchangesAdapter

        myExchangesAdapter.onItemClick = { exchange ->
            val intent = Intent(this, DetailedMyTradeActivity::class.java).apply {
                putExtra("exchange_identifier", exchange.identifier)
                putExtra("exchange_price", exchange.price)
                putExtra("exchange_quantity", exchange.quantity)
                putExtra("exchange_state", exchange.state)
            }
            startActivity(intent)
        }
    }
}

