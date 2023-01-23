package pt.ipca.smartcanteen.models.retrofit.response

import org.json.JSONArray
import org.json.JSONObject

class MyOrderCart(var name: String, var quantity: String, var price: String) {
    companion object {

        fun importFromJSON(jsonObject: JSONObject): MyOrderCart {

            val name = jsonObject.getString("name")
            val amount = jsonObject.getString("amount")
            val price = jsonObject.getString("price")

            val cart = MyOrderCart(name, amount, price)

            return cart
        }

        fun importFromArray(jsonArray: String): List<MyOrderCart> {
            val array = JSONArray(jsonArray)

            val list = mutableListOf<MyOrderCart>()

            for (i in 0 until array.length()) {
                val json = array.getJSONObject(i)
                val cart = importFromJSON(json)

                list.add(cart)
            }

            return list
        }
    }
}