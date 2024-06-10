package com.example.banruou.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.banruou.R
import com.example.banruou.data.model.WineLineResponse
import com.example.banruou.ui.cart.CartViewModel
import java.io.IOException
import kotlin.math.round

interface OnWineLineClickListener {
    fun onWineLineClick(wineLineId: String)
}
class WineLineAdapter(
    private val cartViewModel: CartViewModel,
    private val clickListener: OnWineLineClickListener
) : ListAdapter<WineLineResponse, WineLineAdapter.WineLineViewHolder>(WineLineDiffCallback()) {

    companion object {
        private const val TAG = "WineLineAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WineLineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wine_line, parent, false)
        return WineLineViewHolder(view)
    }

    override fun onBindViewHolder(holder: WineLineViewHolder, position: Int) {
        val wineLine = getItem(position)
        holder.bind(wineLine)
    }

    inner class WineLineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wineNameTextView: TextView = itemView.findViewById(R.id.text_view_wine_name)
        private val wineDescriptionTextView: TextView = itemView.findViewById(R.id.text_view_wine_description)
        private val wineOriginalPriceTextView: TextView = itemView.findViewById(R.id.text_view_original_price)
        private val wineDiscountedPriceTextView: TextView = itemView.findViewById(R.id.text_view_discounted_price)
        private val addToCartButton: ImageView = itemView.findViewById(R.id.button_add_to_cart)
        private val favoriteButton: ImageView = itemView.findViewById(R.id.button_favorite)
        private val stockStatusTextView: TextView = itemView.findViewById(R.id.text_view_stock_status)
        private val wineImageView: ImageView = itemView.findViewById(R.id.image_view_wine)

        @SuppressLint("SetTextI18n")
        fun bind(wineLine: WineLineResponse) {
            wineNameTextView.text = wineLine.TENDONG
            wineDescriptionTextView.text = wineLine.MOTA

            val originalPrice = wineLine.changeprices.firstOrNull()?.GIA ?: 0.0
            val discount = wineLine.ct_khuyenmais.firstOrNull()?.PHANTRAMGIAM ?: 0.0
            val discountedPrice = originalPrice * (1 - discount / 100)

            wineOriginalPriceTextView.text = "${originalPrice}$"

            if (discount > 0) {
                val roundedDiscountedPrice = round(discountedPrice * 100) / 100
                wineOriginalPriceTextView.paintFlags = wineOriginalPriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                wineDiscountedPriceTextView.visibility = View.VISIBLE
                wineDiscountedPriceTextView.text = "${String.format("%.2f", roundedDiscountedPrice)}$"
            } else {
                wineOriginalPriceTextView.paintFlags = wineOriginalPriceTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                wineDiscountedPriceTextView.visibility = View.GONE
            }

            stockStatusTextView.text = wineLine.TRANGTHAI
            loadImageFromAssets(itemView.context, wineImageView, wineLine.HINHANH)

            addToCartButton.setOnClickListener {
                Log.d(TAG, "click add to cart: ${wineLine.TENDONG}")
                cartViewModel.addItemToCart(wineLine)
            }
            itemView.setOnClickListener {
                clickListener.onWineLineClick(wineLine.MADONG)
            }
        }
    }

    class WineLineDiffCallback : DiffUtil.ItemCallback<WineLineResponse>() {
        override fun areItemsTheSame(oldItem: WineLineResponse, newItem: WineLineResponse): Boolean {
            return oldItem.MADONG == newItem.MADONG
        }

        override fun areContentsTheSame(oldItem: WineLineResponse, newItem: WineLineResponse): Boolean {
            return oldItem == newItem
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