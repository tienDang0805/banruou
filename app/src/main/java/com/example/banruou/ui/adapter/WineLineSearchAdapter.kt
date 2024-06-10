package com.example.banruou.ui.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.banruou.R
import com.example.banruou.data.model.WineLineResponse
import com.example.banruou.ui.home.OnWineLineClickListener
import java.io.IOException

class WineLineSearchAdapter(private val clickListener: OnWineLineClickListener) : RecyclerView.Adapter<WineLineSearchAdapter.WineLineSearchViewHolder>() {

    private var wineLines: List<WineLineResponse> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WineLineSearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false)
        return WineLineSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: WineLineSearchViewHolder, position: Int) {
        val wineLine = wineLines[position]
        holder.wineNameTextView.text = wineLine.TENDONG
        // Assuming you have a method to load images from a URL
        // loadImage(holder.wineImageView, wineLine.imageUrl)
        loadImageFromAssets(holder.itemView.context, holder.wineImageView, wineLine.HINHANH)

        holder.itemView.setOnClickListener {
            clickListener.onWineLineClick(wineLine.MADONG)
        }
    }

    override fun getItemCount(): Int = wineLines.size

    fun submitList(list: List<WineLineResponse>) {
        wineLines = list
        notifyDataSetChanged()
    }

    class WineLineSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wineImageView: ImageView = itemView.findViewById(R.id.wineImageView)
        val wineNameTextView: TextView = itemView.findViewById(R.id.wineNameTextView)
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
