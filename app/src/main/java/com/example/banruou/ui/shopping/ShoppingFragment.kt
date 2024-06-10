package com.example.banruou.ui.shopping

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.banruou.R
import com.example.banruou.ui.adapter.WineLineSearchAdapter
import com.example.banruou.ui.cart.CartViewModel
import com.example.banruou.ui.home.OnWineLineClickListener
import com.example.banruou.ui.home.WineLineAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShoppingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShoppingFragment : Fragment() , OnWineLineClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private val shoppingViewModel: ShoppingViewModel by viewModels()
    val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var popupWindow: PopupWindow
    private lateinit var searchResultsAdapter: WineLineSearchAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_shopping, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.listWire)
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        val adapter = WineLineAdapter(cartViewModel, this)
        recyclerView.adapter = adapter

        shoppingViewModel.wineLines.observe(viewLifecycleOwner, Observer { wineLines ->
            Log.d("ShoppingFragment", "wineLines: $wineLines")
            adapter.submitList(wineLines)
        })

        val linearLayout: LinearLayout = root.findViewById(R.id.categoriesLinearLayout)

        shoppingViewModel.wineTypes.observe(viewLifecycleOwner, Observer { wineTypes ->
            if (wineTypes.isNotEmpty()) {
                val firstCategory = wineTypes.first()
                shoppingViewModel.fetchWineLinesByCategory(firstCategory.MALOAI)

                wineTypes.forEach { wineType ->
                    val categoryLayout = inflater.inflate(R.layout.category_item_layout, linearLayout, false)
                    val categoryTextView: TextView = categoryLayout.findViewById(R.id.categoryTextView)
                    categoryTextView.text = wineType.TENLOAI
                    categoryLayout.setOnClickListener {
                        shoppingViewModel.fetchWineLinesByCategory(wineType.MALOAI)

                        categoryLayout.setBackgroundResource(R.drawable.selected_category_background)
                        linearLayout.children.forEach { view ->
                            if (view != categoryLayout) {
                                view.setBackgroundResource(R.drawable.category_item_background)
                                (view.findViewById<TextView>(R.id.categoryTextView)).setTextColor(Color.BLACK)
                            }
                        }
                        categoryTextView.setTextColor(Color.WHITE)
                    }
                    linearLayout.addView(categoryLayout)
                }

                linearLayout.getChildAt(0)?.performClick()
            }
        })

        setupCartBadge()
        setupSearchView(root)
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

    private fun setupSearchView(root: View) {
        val searchView: SearchView = root.findViewById(R.id.searchView)
        val inflater = LayoutInflater.from(requireContext())
        val popupView = inflater.inflate(R.layout.dropdown_search_results, null)
        val searchResultsRecyclerView: RecyclerView = popupView.findViewById(R.id.searchResultsRecyclerView)
        searchResultsRecyclerView.layoutManager = GridLayoutManager(context, 1)
        searchResultsAdapter = WineLineSearchAdapter(this)
        searchResultsRecyclerView.adapter = searchResultsAdapter

        popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        popupWindow.elevation = 8.0f
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = false

        shoppingViewModel.searchResult.observe(viewLifecycleOwner, Observer { searchResults ->
            Log.d("ShoppingFragment", "search results: $searchResults")
            searchResultsAdapter.submitList(searchResults)
            if (searchResults.isNotEmpty()) {
                if (!popupWindow.isShowing) {
                    popupWindow.showAsDropDown(searchView, 0, 0)
                }
            } else {
                popupWindow.dismiss()
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.length ?: 0 >= 2) {
                    Log.d("ShoppingFragment", "text $newText")
                    shoppingViewModel.fetchWineLinesByName(newText.orEmpty())
                } else {
                    popupWindow.dismiss()
                }
                return true
            }
        })

        searchView.setOnCloseListener {
            popupWindow.dismiss()
            false
        }
    }

    override fun onWineLineClick(wineLineId: String) {
        popupWindow.dismiss()
        val bundle = Bundle().apply {
            putString("wineLineId", wineLineId)
        }
        findNavController().navigate(R.id.action_billFragment_to_productDetailFragment, bundle)
    }
}