package pt.ipca.smartcanteen

import org.json.JSONArray
import org.json.JSONObject

class Trade(var ticketid: String, var nencomenda: String, ticketamount: String, var total: String, var statename: String) {
    companion object{

        fun importFromJSON(jsonObject: JSONObject): Trade {

            val ticketid = jsonObject.getString("ticketid");
            val nencomenda = jsonObject.getString("nencomenda");
            val ticketamount = jsonObject.getString("ticketamount");
            val total = jsonObject.getString("name");
            val statename = jsonObject.getString("statename");

            val trade = Trade(ticketid, nencomenda, ticketamount, total, statename)

            return trade
        }

        fun importFromArray(jsonArray: String): List<Trade> {
            val array = JSONArray(jsonArray)

            val list = mutableListOf<Trade>()

            for(i in 0 until array.length()){
                val json = array.getJSONObject(i)
                val trade = Trade.importFromJSON(json)

                list.add(trade)
            }

            return list
        }
    }
}