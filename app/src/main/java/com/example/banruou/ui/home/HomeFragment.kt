package com.example.banruou.ui.home

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.children
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.banruou.R
import com.example.banruou.data.model.WineLineResponse
import com.example.banruou.data.network.RetrofitInstance
import com.example.banruou.ui.adapter.WineLineSearchAdapter
import com.example.banruou.ui.cart.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(),OnWineLineClickListener  {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val homeViewModel: HomeViewModel by viewModels()
    val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var popupWindow: PopupWindow
    private lateinit var searchResultsAdapter: WineLineSearchAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.listWire)
        recyclerView.layoutManager = GridLayoutManager(context, 1) // 2 columns
        val adapter = WineLineAdapter(cartViewModel,this)
        recyclerView.adapter = adapter
        val searchView: SearchView = root.findViewById(R.id.searchView)
        val nestedScrollView: NestedScrollView = root.findViewById(R.id.homeLayout)
        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
            hideKeyboard(nestedScrollView)
        })
        homeViewModel.wineLines.observe(viewLifecycleOwner, Observer { wineLines ->
            Log.d("HomeFragment", "wireline: $wineLines")
            adapter.submitList(wineLines)
        })
        val horizontalScrollView: HorizontalScrollView = root.findViewById(R.id.horizontalScrollView)
        val linearLayout: LinearLayout = root.findViewById(R.id.categoriesLinearLayout)

        val categories = listOf("Hàng Hot" to homeViewModel::fetchWineLinesHot, "Hàng Khuyến mãi" to homeViewModel::fetchWineLinesPromo)

        categories.forEach { (name, fetchAction) ->
            val categoryLayout = inflater.inflate(R.layout.category_item_layout, linearLayout, false)
            val categoryTextView: TextView = categoryLayout.findViewById(R.id.categoryTextView)
            categoryTextView.text = name
            categoryLayout.setOnClickListener {
                Log.d("HomeFragment", "click: $name")
                fetchAction.invoke()
                // Set selected background for the clicked category
                categoryLayout.setBackgroundResource(R.drawable.selected_category_background)

                // Reset background for other categories
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

        // Select "Hàng Hot" by default
        linearLayout.getChildAt(0)?.performClick()
        setupCartBadge()
        setupSearchView(root)
        return root // Return the root view instead of inflating layout again
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

        // Inflate the dropdown layout and set up the RecyclerView
        val inflater = LayoutInflater.from(requireContext())
        val popupView = inflater.inflate(R.layout.dropdown_search_results, null)
        val searchResultsRecyclerView: RecyclerView = popupView.findViewById(R.id.searchResultsRecyclerView)
        searchResultsRecyclerView.layoutManager = GridLayoutManager(context, 1)
        searchResultsAdapter = WineLineSearchAdapter(this)
        searchResultsRecyclerView.adapter = searchResultsAdapter

        // Configure PopupWindow
        popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        popupWindow.elevation = 8.0f
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE)) // Ensure the background is white

        // Ensure PopupWindow does not steal focus
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = false

        homeViewModel.searchResult.observe(viewLifecycleOwner, Observer { searchResults ->
            Log.d("HomeFragment", "search results: $searchResults")
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
                    Log.d("HomeFragment", "text $newText")
                    homeViewModel.fetchWineLinesByName(newText.orEmpty())
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun hideKeyboard(view: View) {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onWineLineClick(wineLineId: String) {
        popupWindow.dismiss()
        val bundle = Bundle().apply {
            putString("wineLineId", wineLineId)
        }
        findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, bundle)
    }

}