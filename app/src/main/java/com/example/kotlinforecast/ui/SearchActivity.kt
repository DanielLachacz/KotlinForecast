package com.example.kotlinforecast.ui


import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinforecast.R
import com.example.kotlinforecast.adapter.SearchAdapter
import com.example.kotlinforecast.data.model.city.City
import kotlinx.android.synthetic.main.activity_search.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


class SearchActivity : AppCompatActivity(), KodeinAware, SearchAdapter.OnCityListener {

    override val kodein by closestKodein()

    private val searchViewModelFactory: SearchViewModelFactory by instance()

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var viewAdapter: SearchAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var deleteIcon: Drawable
    private var cities: List<City> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        searchViewModel = ViewModelProviders.of(this, searchViewModelFactory).get(SearchViewModel::class.java)

        viewAdapter = SearchAdapter(this, cities, this)
        viewManager = LinearLayoutManager(this)

        search_recycler_view.apply {
            setHasFixedSize(true)
            adapter = viewAdapter
            layoutManager = viewManager
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

        searchViewModel.getAllCities().observe(this, Observer { list ->
            list?.let { viewAdapter.updateList(it as ArrayList<City>) }
        })

        deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete_white_24dp)!!

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewAdapter.removeItem(viewHolder)
                searchViewModel.deleteCity(viewAdapter.getCityPosition(viewHolder.layoutPosition))
            }


            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val swipeBackground = ColorDrawable(Color.RED)
                val itemView = viewHolder.itemView

                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

                if (dX > 0) {
                    swipeBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                    deleteIcon.setBounds(itemView.left + iconMargin, itemView.top + iconMargin, itemView.left
                    + iconMargin + deleteIcon.intrinsicWidth, itemView.bottom - iconMargin)
                } else {
                    swipeBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    deleteIcon.setBounds(itemView.right - iconMargin - deleteIcon.intrinsicWidth, itemView.top + iconMargin, itemView.right
                            - iconMargin, itemView.bottom - iconMargin)
                }

                swipeBackground.draw(c)

                c.save()

                if (dX > 0) {
                    c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                } else {
                    c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                }
                deleteIcon.draw(c)

                c.restore()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(search_recycler_view)

        return_search_button.setOnClickListener {
            onBackPressed()
        }
        add_search_button.setOnClickListener {
            if (search_edit_text.text.toString().trim().isNotEmpty()) {
                val city = City(search_edit_text.text.toString())
                searchViewModel.addCity(city)
                search_edit_text.setText("")
            }
            else {
                Toast.makeText(this, "Field is empty!", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCityClick(position: Int) {
        searchViewModel.getAllCities().observe(this, Observer { list ->
            list?.let { cities == list }
            list.get(position)

            val intent = Intent(this, CityActivity::class.java)
            intent.putExtra("CITY",list[position].name)
            startActivity(intent)
        })

    }
}