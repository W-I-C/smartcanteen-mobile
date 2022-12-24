package pt.ipca.smartcanteen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UndeliveredOrdersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.undelivered_orders)

        /** Undelivered Orders **/
        val undeliveredOrdersRecyclerView = findViewById<RecyclerView>(R.id.undelivered_orders_recycler_view)
        val undeliveredOrders = mutableListOf<UndeliveredOrder>(
            UndeliveredOrder(1, "João Castro", "Pronto"),
            UndeliveredOrder(2, "João Miguel", "Em Atraso"),
            UndeliveredOrder(3, "Roberto Barreto", "Em Preparação"),
            UndeliveredOrder(4, "Henrique Cartucho", "Pronto"),
            UndeliveredOrder(5, "Sara Pereira", "Em Atraso"),
            UndeliveredOrder(6, "António Silva", "Em Preparação"),
            UndeliveredOrder(7, "António José", "Pronto"),
            UndeliveredOrder(123, "Pedro", "Em Atraso"),
            UndeliveredOrder(244, "Sousa", "Em Preparação"),
            UndeliveredOrder(345, "Joaquim", "Pronto"),
            UndeliveredOrder(456, "Faria", "Em Atraso"),
            UndeliveredOrder(576, "Paulo", "Em Preparação"),
            UndeliveredOrder(689, "Tiago", "Pronto"),
        )

        var undeliveredOrdersAdater = UndeliveredOrdersAdaterRec(undeliveredOrders)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        undeliveredOrdersRecyclerView.layoutManager = linearLayoutManager
        undeliveredOrdersRecyclerView.itemAnimator = DefaultItemAnimator()
        undeliveredOrdersRecyclerView.adapter = undeliveredOrdersAdater
    }
}

