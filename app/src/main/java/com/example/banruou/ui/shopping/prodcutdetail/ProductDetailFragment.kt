package com.example.banruou.ui.shopping.prodcutdetail

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.banruou.R
import com.example.banruou.ui.cart.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductDetailFragment : Fragment() {

    private val productDetailViewModel: ProductDetailViewModel by viewModels()
    val cartViewModel: CartViewModel by activityViewModels()

    private lateinit var imageViewWine: ImageView
    private lateinit var textViewWineName: TextView
    private lateinit var textViewWinePrice: TextView
    private lateinit var textViewWineStock: TextView
    private lateinit var textViewWineDescription: TextView
    private lateinit var buttonAddToCart: Button
    private lateinit var textWineInfo: TextView
    private lateinit var textViewOriginalPrice: TextView
    private lateinit var textViewDiscountedPrice: TextView

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_product_detail, container, false)
        val toolbar: Toolbar = root.findViewById(R.id.toolbar)

        imageViewWine = root.findViewById(R.id.imageViewWine)
        textViewWineName = root.findViewById(R.id.textViewWineName)
        textViewWinePrice = root.findViewById(R.id.textViewWinePrice)
        textViewWineStock = root.findViewById(R.id.textViewWineStock)
        textWineInfo = root.findViewById(R.id.textViewInfo)
        buttonAddToCart = root.findViewById(R.id.buttonAddToCart)
        textViewWineDescription = root.findViewById(R.id.textViewWineDescription)
        textViewOriginalPrice = root.findViewById(R.id.textViewOriginalPrice)
        textViewDiscountedPrice = root.findViewById(R.id.textViewDiscountedPrice)

        val wineLineId = arguments?.getString("wineLineId")
        wineLineId?.let {
            productDetailViewModel.fetchWineLinesById(it)
        }

        productDetailViewModel.wineDetail.observe(viewLifecycleOwner, Observer { wineLine ->
            wineLine?.let {
                textViewWineName.text = it.TENDONG
                if(it.TRANGTHAI!=""){
                    textViewWineStock.visibility=View.VISIBLE
                    textViewWineStock.text = it.TRANGTHAI
                    textViewWineStock.setTextColor(Color.RED)
                }
                else{
                    textViewWineStock.visibility=View.GONE

                }
                textWineInfo.text = "Mô Tả: "+it.MOTA
                textViewWineStock.text = "Tồn Kho : ${it.SOLUONGTON}"
                textViewWineDescription.text = if (it.CHITIET.isNullOrEmpty()) {
                    "Đang cập nhật chi tiết"
                } else {
                    it.CHITIET
                }

                val originalPrice = it.changeprices?.firstOrNull()?.GIA ?: 0.0
                val discount = it.ct_khuyenmais?.firstOrNull()?.PHANTRAMGIAM ?: 0.0
                val discountedPrice = originalPrice * (1 - discount / 100)

                textViewOriginalPrice.text = "${originalPrice}$"
                if (discount > 0) {
                    val roundedDiscountedPrice = kotlin.math.round(discountedPrice * 100) / 100
                    textViewOriginalPrice.paintFlags = textViewOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    textViewDiscountedPrice.visibility = View.VISIBLE
                    textViewDiscountedPrice.text = "${String.format("%.2f", roundedDiscountedPrice)}$"
                } else {
                    textViewOriginalPrice.paintFlags = textViewOriginalPrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    textViewDiscountedPrice.visibility = View.GONE
                }

                buttonAddToCart.setOnClickListener {
                    cartViewModel.addItemToCart(wineLine)
                }
                loadImageFromAssets(requireContext(), imageViewWine, it.HINHANH)
            }
        })

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        setupCartBadge()
        return root
    }

    private fun setupCartBadge() {
        val bottomNavigationView: BottomNavigationView? = activity?.findViewById(R.id.nav_view)
        cartViewModel.cartItems.observe(viewLifecycleOwner, Observer { cartItems ->
            if (bottomNavigationView != null) {
                val badge = bottomNavigationView.getOrCreateBadge(R.id.cartFragment)
                badge.isVisible = cartItems.isNotEmpty()
                badge.number = cartItems.size
            }
        })
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