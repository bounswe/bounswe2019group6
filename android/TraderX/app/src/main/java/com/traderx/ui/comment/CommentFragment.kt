package com.traderx.ui.comment


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.api.response.CommentResponse
import com.traderx.db.User
import com.traderx.ui.search.UserSearchSkeletonRecyclerViewAdapter
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.AuthUserViewModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class CommentFragment(
    private val commentsSingle: Single<ArrayList<CommentResponse>>,
    private val onDeleteAction: (id: Int, doOnSuccess: () -> Unit) -> Unit,
    private val onCreateAction: (comment: String, doOnSuccess: () -> Unit) -> Unit,
    private val onRateAction: () -> Unit
) : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var commentEditText: EditText
    private lateinit var authUserViewModel: AuthUserViewModel

    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = context as Context
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_comment, container, false)

        commentEditText = view.findViewById(R.id.comment)

        view.findViewById<FloatingActionButton>(R.id.create_comment_action)?.let {
            it.setOnClickListener {
                createComment()
            }
        }

        val authUserViewModelFactory = Injection.provideAuthUserViewModelFactory(context)
        authUserViewModel =
            ViewModelProvider(this, authUserViewModelFactory).get(AuthUserViewModel::class.java)

        val viewManager = LinearLayoutManager(context)
        recyclerView = view.findViewById<RecyclerView>(R.id.comment_list).apply {
            layoutManager = viewManager
            adapter = UserSearchSkeletonRecyclerViewAdapter(3)
        }

        refreshComments()

        return view
    }

    override fun onStop() {
        super.onStop()

        disposable.clear()
    }

    private fun refreshComments() {
        disposable.add(
            authUserViewModel.userOrNew()
                .compose(Helper.applySingleSchedulers())
                .subscribe({ user ->
                disposable.add(
                    commentsSingle
                        .compose(Helper.applySingleSchedulers())
                        .subscribe({
                        recyclerView.adapter =
                            CommentRecyclerViewAdapter(it, user.username, onDeleteAction)
                    }, {
                        ErrorHandler.handleError(it, context as Context)
                    })
                )
            }, {})
        )
    }

    private fun createComment() {
        if (commentEditText.text.isEmpty() && commentEditText.text.length > 1000) {
            return
        }

        onCreateAction(commentEditText.text.toString()) {
            commentEditText.setText("")

            refreshComments()
        }
    }
}
