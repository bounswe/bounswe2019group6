package com.traderx.ui.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.ApiService
import com.traderx.api.RequestService
import com.traderx.api.ResponseHandler
import io.reactivex.disposables.CompositeDisposable

class UserSearchActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var requestService: RequestService
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_search)

        requestService = ApiService.getInstance()

        viewManager = LinearLayoutManager(this)

        userRecyclerView = findViewById<RecyclerView>(R.id.search_users_recycler).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager
        }


        disposable.add(
            requestService.usersGetAll().subscribe({
                userRecyclerView.adapter = UserSearchAdapter(it)
            }, {
                ResponseHandler.handleError(it, this)
            })
        )
    }

    override fun onStop() {
        super.onStop()

        disposable.clear()
    }
}
