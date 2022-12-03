package pt.ipca.smartcanteen

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
            Exchange("Francesinha", 5, 15.0, "Pronto"),
            Exchange("Panado", 3, 5.0, "Em preparação"),
            Exchange("Salada", 1, 1.0, "Em atraso"),
        )

        var myExchangesAdapter = ExchangesAdapterRec(myExchanges)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        myExchangesRecyclerView.layoutManager = linearLayoutManager
        myExchangesRecyclerView.itemAnimator = DefaultItemAnimator()
        myExchangesRecyclerView.adapter = myExchangesAdapter
    }
}

