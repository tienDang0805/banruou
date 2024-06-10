package com.example.banruou.ui.setting.order

import PhieuDat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.banruou.R
import java.math.BigDecimal
import java.math.RoundingMode

class OrderAdapter(private val orders: List<PhieuDat?>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    // Inner class for holding the view references
    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMAPD: TextView = itemView.findViewById(R.id.textOrderNumber)
        val textNGAYDAT: TextView = itemView.findViewById(R.id.textOrderDate)
//        val textHONN: TextView = itemView.findViewById(R.id.textHONN)
//        val textTENNN: TextView = itemView.findViewById(R.id.textTENNN)
//        val textDIACHINN: TextView = itemView.findViewById(R.id.textDIACHINN)
//        val textSDTNN: TextView = itemView.findViewById(R.id.textSDTNN)
//        val textGHICHU: TextView = itemView.findViewById(R.id.textGHICHU)
        val textTongTien: TextView= itemView.findViewById(R.id.textTotalAmount)
        val textTRANGTHAI: TextView = itemView.findViewById(R.id.textStatus)
        val textHovaTen : TextView = itemView.findViewById(R.id.textNameCus)
        val textDiaChi: TextView =itemView.findViewById(R.id.textAddress)
        val textSDT : TextView=itemView.findViewById(R.id.textSDT)
        val btnDetail : Button =itemView.findViewById((R.id.buttonDetails))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        var totalAmount = 0.0
        if(order !=null){
            val totalAmount = order.ct_phieudats.sumOf { it.SOLUONG * it.GIA }
            val roundedTotalAmount = BigDecimal(totalAmount).setScale(2, RoundingMode.HALF_EVEN).toDouble()
            holder.textMAPD.text = "Mã: "+order.MAPD
            holder.textNGAYDAT.text = order.NGAYDAT
            holder.textTongTien.text = "Tổng Tiền: ${roundedTotalAmount}$"
            holder.textTRANGTHAI.text = order.TRANGTHAI
            holder.textHovaTen.text = "Người đặt: ${order.HONN +" "+order.TENNN}"
            holder.textDiaChi.text="Địa Chỉ: ${order.DIACHINN}"
            holder.textSDT.text="SDT: ${order.SDTNN}"
            holder.btnDetail.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("orderId", order.MAPD)
                }
                it.findNavController().navigate(R.id.action_orderFragment_to_orderDetailFragment, bundle)

            }
        }

    }

    override fun getItemCount() = orders.size
}
