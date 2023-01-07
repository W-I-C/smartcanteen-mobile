package pt.ipca.smartcanteen.consumer_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.MyOrderCart
import pt.ipca.smartcanteen.MyOrdersCartRec
import pt.ipca.smartcanteen.R


class MyOrdersCartFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.activity_cart, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            if (view != null) {
                /** Orders **/
                val myOrdersCartRecyclerView = view.findViewById<RecyclerView>(R.id.myorders_cart_recycler_view)
                    val myOrders = mutableListOf<MyOrderCart>(
                        MyOrderCart("Francesinha", 5, 15.0 ),
                        MyOrderCart("Panado", 3, 5.0 ),
                        MyOrderCart("Salada", 1, 1.0),
                        MyOrderCart("Frango", 1, 2.0),
                        MyOrderCart("Pizza", 1, 5.0),


                        )

                    var myOrdersCartAdapter = MyOrdersCartRec(myOrders)
                    val linearLayoutManager = LinearLayoutManager(view.context)
                    myOrdersCartRecyclerView.layoutManager = linearLayoutManager
                    myOrdersCartRecyclerView.itemAnimator = DefaultItemAnimator()
                    myOrdersCartRecyclerView.adapter = myOrdersCartAdapter
            }
    }
}