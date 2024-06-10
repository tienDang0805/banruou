package com.example.banruou.ui.setting.orderdetail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.banruou.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderDetailFragment : Fragment() {

    private val orderDetailViewModel: OrderDetailViewModel by viewModels()
    val TAG = "OrderDetailFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "OrderDetailFragment ")

        return inflater.inflate(R.layout.fragment_order_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setTitle("Chi tiết đơn")
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val textOrderNumber: TextView = view.findViewById(R.id.text_order_number)
        val textOrderDate: TextView = view.findViewById(R.id.text_order_date)
        val textCustomerName: TextView = view.findViewById(R.id.text_customer_name)
        val textCustomerAddress: TextView = view.findViewById(R.id.text_customer_address)
        val textCustomerPhone: TextView = view.findViewById(R.id.text_customer_phone)
        val textOrderStatus: TextView = view.findViewById(R.id.text_order_status)
        val textOrderNote: TextView = view.findViewById(R.id.text_order_note)
        val recyclerViewOrderItems: RecyclerView = view.findViewById(R.id.recycler_view_order_items)

        recyclerViewOrderItems.layoutManager = LinearLayoutManager(requireContext())

        val orderId = arguments?.getString("orderId") ?: return
        Log.d(TAG, "orderId $orderId")

        orderDetailViewModel.fetchOrderDetail(orderId)
        orderDetailViewModel.orderDetail.observe(viewLifecycleOwner, Observer { order ->
            order?.let {
                textOrderNumber.text = "${order.MAPD}"
                textOrderDate.text = "${order.NGAYDAT}"
                textCustomerName.text = "${order.HONN} ${order.TENNN}"
                textCustomerAddress.text = "${order.DIACHINN}"
                textCustomerPhone.text = "${order.SDTNN}"
                textOrderStatus.text = "${order.TRANGTHAI}"
                textOrderNote.text = "${order.GHICHU}"

                // Set text gravity to right
                textOrderNumber.gravity = Gravity.END
                textOrderDate.gravity = Gravity.END
                textCustomerName.gravity = Gravity.END
                textCustomerAddress.gravity = Gravity.END
                textCustomerPhone.gravity = Gravity.END
                textOrderStatus.gravity = Gravity.END
                textOrderNote.gravity = Gravity.END

                // Calculate total price
                var totalPrice = 0.0
                for (item in order.ct_phieudats) {
                    totalPrice += item.SOLUONG * item.GIA
                }

                // Add TextView for Total Price
                val textTotalPrice: TextView = view.findViewById(R.id.text_total_price)
                textTotalPrice.text = totalPrice.toString() + "$"
                textTotalPrice.gravity = Gravity.END

                val adapter = OrderProductsAdapter(requireContext(), lifecycleScope, order.ct_phieudats)
                recyclerViewOrderItems.adapter = adapter
            }
        })

    }
}