package com.example.banruou.ui.cart

import PhieuDat
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.banruou.R
import com.example.banruou.data.model.CartItem
import com.example.banruou.data.network.RetrofitInstance
import com.example.banruou.data.singleton.UserSession
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.round

data class PhieuDatCart(
    val CTPDS: List<CTPhieuDat>,
    val DIACHINN: String,
    val GHICHU: String,
    val HONN: String,
    val MAKH: String,
    val MANVD: String,
    val MANVGH: String,
    val MAPD: String,
    val NGAYDAT: String,
    val SDTNN: String,
    val TENNN: String,
    val TRANGTHAI: String
)

data class CTPhieuDat(
    val GIA: Double,
    val MADONG: String,
    val MAPD: String,
    val SOLUONG: Int
)

class CartFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_cart, container, false)
        val editTextName: EditText = root.findViewById(R.id.editTextName)
        val editTextAddress: EditText = root.findViewById(R.id.editTextAddress)
        val editTextPhoneNumber: EditText = root.findViewById(R.id.editTextPhoneNumber)
        val totalPriceTextView: TextView = root.findViewById(R.id.totalPriceTextView) // TextView mới

        val recyclerView: RecyclerView = root.findViewById(R.id.cartRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = CartAdapter(
            onQuantityChanged = { cartItem ->
                cartViewModel.updateCartItem(cartItem)
                updateTotalPrice(totalPriceTextView)
            },
            onItemDeleted = { cartItem ->
                removeCartItem(cartItem)
                updateTotalPrice(totalPriceTextView)
            }
        )
        recyclerView.adapter = adapter

        cartViewModel.cartItems.observe(viewLifecycleOwner, Observer { cartItems ->
            Log.d("CartFragment", "cartItems: $cartItems")
            adapter.submitList(cartItems.toList())
            updateCartBadge(cartItems.size)
            updateTotalPrice(totalPriceTextView)
        })

        val userResponse = UserSession.userResponse
        if (userResponse != null) {
            editTextName.setText("${userResponse.ho} ${userResponse.ten}")
            editTextAddress.setText(userResponse.diaChi)
            editTextPhoneNumber.setText(userResponse.sdt)
        }

        val btnOrder: Button = root.findViewById(R.id.btnOrder)
        btnOrder.setOnClickListener {
            placeOrder()
        }

        return root
    }

    private fun placeOrder() {
        if (cartViewModel.cartItems.value.isNullOrEmpty()) {
            showDialog("Giỏ hàng trống", "Không có sản phẩm nào trong giỏ hàng")
            return
        }

        val phieuDat = createPhieuDatObject()
        val apiService = RetrofitInstance.apiService
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = apiService.createPhieuDat(phieuDat)
                if (response.isSuccessful) {
                    showDialog("Đặt hàng thành công", "Vui lòng xem trong chi tiết đơn hàng")
                    clearCartItems()
                } else {
                    showDialog("Đặt hàng không thành công", "Vui lòng thử lại sau")
                }
            } catch (e: Exception) {
                showDialog("Lỗi", "Lỗi: ${e.message}")
            }
        }
    }

    private fun createPhieuDatObject(): PhieuDatCart {
        val editTextName: EditText = requireView().findViewById(R.id.editTextName)
        val editTextAddress: EditText = requireView().findViewById(R.id.editTextAddress)
        val editTextPhoneNumber: EditText = requireView().findViewById(R.id.editTextPhoneNumber)
        val editTextNote: EditText = requireView().findViewById(R.id.editTextNote)

        val hoNN = editTextName.text.toString().split(" ")[0]
        val tenNN = editTextName.text.toString().split(" ")[1]
        val diaChiNN = editTextAddress.text.toString()
        val sdtNN = editTextPhoneNumber.text.toString()
        val ghiChu = editTextNote.text.toString()

        val maKH = UserSession.userResponse?.maKH ?: ""
        val ngayDat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val cartItems = cartViewModel.cartItems.value ?: emptyList()
        val ctpds = cartItems.map { cartItem ->
            val originalPrice = cartItem.wineLine.changeprices?.firstOrNull()?.GIA ?: 0.0
            val discount = cartItem.wineLine.ct_khuyenmais?.firstOrNull()?.PHANTRAMGIAM ?: 0.0
            val finalPrice = if (discount > 0) {
                val discountedPrice = originalPrice * (1 - discount / 100)
                round(discountedPrice * 100) / 100
            } else {
                originalPrice
            }
            CTPhieuDat(finalPrice, cartItem.wineLine.MADONG, "12312sssd", cartItem.quantity)
        }

        return PhieuDatCart(ctpds, diaChiNN, ghiChu, hoNN, maKH, "", "", "", ngayDat, sdtNN, tenNN, "Chưa Duyệt")
    }


    private fun removeCartItem(cartItem: CartItem) {
        val currentList = cartViewModel.cartItems.value?.toMutableList()
        currentList?.remove(cartItem)
        cartViewModel.updateCartItems(currentList?.toList() ?: emptyList())
    }

    private fun clearCartItems() {
        cartViewModel.updateCartItems(emptyList())
    }

    private fun updateCartBadge(count: Int) {
        val bottomNavigationView: BottomNavigationView? = activity?.findViewById(R.id.nav_view)
        bottomNavigationView?.let {
            val badge = it.getOrCreateBadge(R.id.cartFragment)
            badge.isVisible = count > 0
            badge.number = count
        }
    }

    private fun updateTotalPrice(totalPriceTextView: TextView) {
        val totalPrice = cartViewModel.cartItems.value?.sumOf { cartItem ->
            val originalPrice = cartItem.wineLine.changeprices?.firstOrNull()?.GIA ?: 0.0
            val discount = cartItem.wineLine.ct_khuyenmais?.firstOrNull()?.PHANTRAMGIAM ?: 0.0
            val finalPrice = if (discount > 0) {
                val discountedPrice = originalPrice * (1 - discount / 100)
                round(discountedPrice * 100) / 100
            } else {
                originalPrice
            }
            finalPrice * cartItem.quantity
        } ?: 0.0
        totalPriceTextView.text = "Tổng giá trị đơn hàng: ${String.format("%.2f", totalPrice)} $"
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
