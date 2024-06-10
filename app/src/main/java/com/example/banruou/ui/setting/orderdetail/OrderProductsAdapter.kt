package com.example.banruou.ui.setting.orderdetail

import Ct_PhieuDat
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.banruou.R
import com.example.banruou.data.reponsive.WineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class OrderProductsAdapter(
    private val context: Context,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val orderItems: List<Ct_PhieuDat>
) : RecyclerView.Adapter<OrderProductsAdapter.OrderItemViewHolder>() {

    private val wineRepository = WineRepository()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_order_details, parent, false)
        return OrderItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val orderItem = orderItems[position]
        holder.bind(context, lifecycleScope, orderItem)
    }

    override fun getItemCount(): Int = orderItems.size

    inner class OrderItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewWine: ImageView = itemView.findViewById(R.id.image_view_wine)
        private val textViewWineName: TextView = itemView.findViewById(R.id.text_view_wine_name)
        private val textViewWineDescription: TextView = itemView.findViewById(R.id.text_view_wine_description)
        private val textViewWinePrice: TextView = itemView.findViewById(R.id.text_view_wine_price)
        private val textViewQuantity: TextView = itemView.findViewById(R.id.text_view_quantity)
        private val textViewTotalPrice: TextView = itemView.findViewById(R.id.text_view_total_price)

        fun bind(context: Context, lifecycleScope: LifecycleCoroutineScope, orderItem: Ct_PhieuDat) {
            // Calculate total price
            val totalPrice = orderItem.SOLUONG * orderItem.GIA

            // Set basic details


            // Fetch wine details
            lifecycleScope.launch {
                val wineDetails = withContext(Dispatchers.IO) {
                    wineRepository.fetchWineLinesById(orderItem.MADONG)
                }

                wineDetails?.let {
                    textViewWineName.text = it.TENDONG
                    textViewWineDescription.text = "Mô tả: ${wineDetails.MOTA}"
                    textViewWinePrice.text = "Đơn Giá: ${orderItem.GIA} $"
                    textViewQuantity.text= "Số lượng: "+ orderItem.SOLUONG.toString()
                    textViewTotalPrice.text = "Tổng Tiền: $totalPrice $"
                    loadImageFromAssets(context, imageViewWine, it.HINHANH)
                }
            }
        }

        private fun loadImageFromAssets(context: Context, imageView: ImageView, imagePath: String) {
            try {
                context.assets.open(imagePath).use { inputStream ->
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    imageView.setImageBitmap(bitmap)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                // Handle the exception as needed
            }
        }
    }
}
