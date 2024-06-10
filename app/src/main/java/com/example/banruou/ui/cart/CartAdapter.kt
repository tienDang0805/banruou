package com.example.banruou.ui.cart

import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.banruou.R
import com.example.banruou.data.model.CartItem
import java.io.IOException
import kotlin.math.round


class CartAdapter(
    private val onQuantityChanged: (CartItem) -> Unit,
    private val onItemDeleted: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wineImageView: ImageView = itemView.findViewById(R.id.image_view_wine)
        private val wineNameTextView: TextView = itemView.findViewById(R.id.text_view_wine_name)
        private val wineDescriptionTextView: TextView = itemView.findViewById(R.id.text_view_wine_description)
        private val wineOriginalPriceTextView: TextView = itemView.findViewById(R.id.text_view_original_price)
        private val wineDiscountedPriceTextView: TextView = itemView.findViewById(R.id.text_view_discounted_price)
        private val quantityTextView: TextView = itemView.findViewById(R.id.text_view_quantity)
        private val addButton: ImageView = itemView.findViewById(R.id.btn_increase_quantity)
        private val minusButton: ImageView = itemView.findViewById(R.id.btn_decrease_quantity)
        private val deleteButton: ImageView = itemView.findViewById(R.id.btn_delete_item)

        fun bind(cartItem: CartItem) {
            wineNameTextView.text = cartItem.wineLine.TENDONG
            wineDescriptionTextView.text = cartItem.wineLine.MOTA
            quantityTextView.text = cartItem.quantity.toString()

            val originalPrice = cartItem.wineLine.changeprices?.firstOrNull()?.GIA ?: 0.0
            val discount = cartItem.wineLine.ct_khuyenmais?.firstOrNull()?.PHANTRAMGIAM ?: 0.0
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

            updatePriceAndQuantity(cartItem)
            loadImageFromAssets(itemView.context, wineImageView, cartItem.wineLine.HINHANH)

            addButton.setOnClickListener {
                if (cartItem.quantity < cartItem.wineLine.SOLUONGTON) {
                    cartItem.quantity++
                    updatePriceAndQuantity(cartItem)
                } else {
                    showAlert(itemView.context, cartItem.wineLine.SOLUONGTON)
                }
            }

            minusButton.setOnClickListener {
                if (cartItem.quantity > 1) {
                    cartItem.quantity--
                    updatePriceAndQuantity(cartItem)
                }
            }

            deleteButton.setOnClickListener {
                onItemDeleted(cartItem)
            }
        }

        private fun showAlert(context: Context, stockQuantity: Int) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Số lượng vượt quá tồn kho")
            builder.setMessage("Số sản phẩm còn lại là: $stockQuantity. Không thể tăng thêm nữa.")
            builder.setPositiveButton("OK", null)
            builder.show()
        }

        private fun calculateTotalPrice(cartItem: CartItem): Double {
            val originalPrice = cartItem.wineLine.changeprices?.firstOrNull()?.GIA ?: 0.0
            val discount = cartItem.wineLine.ct_khuyenmais?.firstOrNull()?.PHANTRAMGIAM ?: 0.0
            val finalPrice = if (discount > 0) {
                val discountedPrice = originalPrice * (1 - discount / 100)
                round(discountedPrice * 100) / 100
            } else {
                originalPrice
            }
            return finalPrice * cartItem.quantity
        }

        private fun updatePriceAndQuantity(cartItem: CartItem) {
            quantityTextView.text = cartItem.quantity.toString()
            val totalPrice = calculateTotalPrice(cartItem)
            wineDiscountedPriceTextView.text = if (cartItem.wineLine.ct_khuyenmais?.firstOrNull()?.PHANTRAMGIAM ?: 0.0 > 0) {
                "${String.format("%.2f", totalPrice)}$"
            } else {
                wineOriginalPriceTextView.text = "${totalPrice}$"
                ""
            }
            onQuantityChanged(cartItem)
        }

        private fun loadImageFromAssets(context: Context, imageView: ImageView, imagePath: String) {
            try {
                context.assets.open(imagePath).use { inputStream ->
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    imageView.setImageBitmap(bitmap)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.wineLine.MADONG == newItem.wineLine.MADONG
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}