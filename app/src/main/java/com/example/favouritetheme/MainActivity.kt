package com.example.favouritetheme

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.favouritetheme.viewmodel.FavouriteThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: FavouriteThemeViewModel by viewModels()
    private lateinit var favouriteThemeTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavouriteThemeAdapter
    private lateinit var users: ArrayList<String>

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        favouriteThemeTextView = findViewById(R.id.textView)
        recyclerView = findViewById(R.id.recycler_view)
        users = ArrayList()

        adapter = FavouriteThemeAdapter(users)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        viewModel.getThemes(this)
        observeData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData() {
        viewModel.onFavouriteThemeUpdated().observe(this) {
            favouriteThemeTextView.text = it
        }
        viewModel.usersWhoVotedForTheFavouriteTheme().observe(this) {
            it?.let { it1 ->
                users.addAll(it1)
            }
            adapter.notifyDataSetChanged()
        }
    }
}