package com.traderx.ui.search

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.util.Injection
import com.traderx.viewmodel.UserViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class UserSearchFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel

    private lateinit var searchEditText: EditText
    private lateinit var skeletonAdapter: UserSearchSkeletonRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private var loading = false
    private lateinit var viewManager: LinearLayoutManager
    private val subject: BehaviorSubject<String> = BehaviorSubject.create()
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val userViewModelFactory = Injection.provideUserViewModelFactory(context as Context)
        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_search, container, false)

        viewManager = LinearLayoutManager(context)
        recyclerView = view.findViewById<RecyclerView>(R.id.user_list).apply {
            layoutManager = viewManager
        }

        skeletonAdapter = UserSearchSkeletonRecyclerViewAdapter(3)

        searchEditText = view.findViewById(R.id.search_text)

        view.findViewById<ImageButton>(R.id.search_button)?.let {
            it.setOnClickListener {
                subject.onNext(searchEditText.text.toString())
            }
        }

        searchEditText.setOnKeyListener { _, keyId, keyEvent ->
            if (keyEvent.keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) {
                subject.onNext(searchEditText.text.toString())
            }

            true
        }

        disposable.add(
            subject.switchMap {
                if (!loading) {
                    recyclerView.adapter = skeletonAdapter

                    loading = true
                }

                userViewModel.allUsers().toObservable()
            }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    loading = false
                }
                .subscribe({ recyclerView.adapter = UserSearchRecyclerViewAdapter(it) },
                    { context?.let { context -> ErrorHandler.handleError(it, context) } })
        )

        return view
    }

    override fun onDetach() {
        super.onDetach()
        disposable.clear()
    }
}
