package pt.ipca.smartcanteen.models.retrofit.response

import org.json.JSONArray
import org.json.JSONObject

class UndeliveredOrder(var ticketid: String, var nencomenda: String, var name: String, var statename: String) {
    companion object {

        fun importFromJSON(jsonObject: JSONObject): UndeliveredOrder {

            val ticketid = jsonObject.getString("ticketid")
            val nencomenda = jsonObject.getString("nencomenda")
            val name = jsonObject.getString("name")
            val statename = jsonObject.getString("statename")

            val order = UndeliveredOrder(ticketid, nencomenda, name, statename)

            return order
        }

        fun importFromArray(jsonArray: String): List<UndeliveredOrder> {
            val array = JSONArray(jsonArray)

            val list = mutableListOf<UndeliveredOrder>()

            for (i in 0 until array.length()) {
                val json = array.getJSONObject(i)
                val order = importFromJSON(json)

                list.add(order)
            }

            return list
        }
    }
}