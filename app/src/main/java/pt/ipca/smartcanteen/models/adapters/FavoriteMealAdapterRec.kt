//package pt.ipca.smartcanteen.models.adapters
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import pt.ipca.smartcanteen.models.adapters.viewHolders.FavoriteMealsAdapterRecViewHolder
//import pt.ipca.smartcanteen.models.helpers.RetroFavoriteMeal
//
//class FavoriteMealAdapterRec(private val favoriteMeallist: List<RetroFavoriteMeal>) :
//    RecyclerView.Adapter<FavoriteMealsAdapterRecViewHolder>(), ListAdapter {
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMealsAdapterRecViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        return FavoriteMealsAdapterRecViewHolder(inflater,parent)
//    }
//
//    override fun onBindViewHolder(holder: FavoriteMealsAdapterRecViewHolder, position: Int) {
//        val name = favoriteMeallist.get(position).name
//        val price = favoriteMeallist.get(position).price
//        val time = favoriteMeallist.get(position).time
//        holder.bindData(name,price,time)
//
//
//    }
//
//    override fun getItemCount(): Int {
//        return favoriteMeallist.size
//    }
//}