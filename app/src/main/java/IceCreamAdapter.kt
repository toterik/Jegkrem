import UDEMX.icecream.Models.IceCream
import UDEMX.icecream.Models.Status
import UDEMX.icecream.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class IceCreamAdapter(
    private val iceCreams: List<IceCream>,
    private val basePrice: Double,
    private val onAddToCartClick: (IceCream) -> Unit
) : RecyclerView.Adapter<IceCreamAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val TWFlavour: TextView = view.findViewById(R.id.tw_flavour)
        val TWPrice: TextView = view.findViewById(R.id.tw_price)
        val TWStatus: TextView = view.findViewById(R.id.tw_status)
        val IMGIceCream: ImageView = view.findViewById(R.id.img_icecream)
        val BtAddToCart: Button = view.findViewById(R.id.bt_add_to_cart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fagylalt_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val iceCream = iceCreams[position]
        holder.TWFlavour.text = iceCream.name

        when (iceCream.status)
        {
            Status.available -> {
                holder.TWStatus.isVisible = false
                holder.TWPrice.text = "${basePrice.toInt()} €"
            }

            Status.melted ->{
                holder.TWStatus.text = "Kifogyott"
                holder.TWPrice.isVisible = false;
            }
            Status.unavailable -> {
                holder.TWStatus.text = "Nem is volt"
                holder.TWPrice.isVisible = false;
            }
        }

        Glide.with(holder.IMGIceCream.context)
            .load(iceCream.imageUrl ?: R.drawable.placeholder)
            .into(holder.IMGIceCream)

        holder.BtAddToCart.setOnClickListener {
            onAddToCartClick(iceCream)
        }
    }

    override fun getItemCount() = iceCreams.size
}