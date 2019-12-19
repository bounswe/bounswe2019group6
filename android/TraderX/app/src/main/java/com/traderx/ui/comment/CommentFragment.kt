package com.traderx.ui.comment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.api.response.CommentResponse
import com.traderx.type.VoteType
import com.traderx.ui.search.UserSearchSkeletonRecyclerViewAdapter
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.AuthUserViewModel
import com.traderx.viewmodel.CommentableViewModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class CommentFragment(
    private val commentsSingle: Single<ArrayList<CommentResponse>>,
    private val deleteComp: (id: Int) -> Completable,
    private val editComp: (id: Int, message: String) -> Completable,
    private val createComp: (message: String) -> Single<CommentResponse>,
    private val voteComp: (id: Int, vote: VoteType) -> Completable,
    private val revokeComp: (id: Int) -> Completable
) : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var commentEditText: EditText
    private lateinit var authUserViewModel: AuthUserViewModel
    private lateinit var editFrameLayout: FrameLayout
    private var isCommenting = false
    private var isEditting = false

    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = context as Context
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_comment, container, false)

        commentEditText = view.findViewById(R.id.comment)
        editFrameLayout = view.findViewById(R.id.edit_frame)

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
                                    CommentRecyclerViewAdapter(
                                        it,
                                        user.username,
                                        { id, doOnSuccess -> deleteComment(id, doOnSuccess) },
                                        { id, message, doOnSuccess ->
                                            editComment(id, message, doOnSuccess)
                                        },
                                        { comment, vote, doOnVote ->
                                            voteComment(comment, vote, doOnVote)
                                        }
                                    )
                            }, {
                                ErrorHandler.handleError(it, context as Context)
                            })
                    )
                }, {})
        )
    }

    private fun voteComment(comment: CommentResponse, vote: VoteType, doOnVote: () -> Unit) {
        if (!isCommenting) {
            isCommenting = true

            if (comment.status == vote) {
                disposable.add(
                    revokeComp(comment.id)
                        .compose(Helper.applyCompletableSchedulers())
                        .doOnComplete(doOnVote)
                        .subscribe({
                            Snackbar.make(
                                requireView(),
                                getString(R.string.comment_revoke),
                                Snackbar.LENGTH_SHORT
                            ).show()

                            isCommenting = false

                            when (vote) {
                                VoteType.LIKED -> comment.likes--
                                VoteType.DISLIKED -> comment.dislikes--
                            }

                            comment.status = VoteType.NOT_COMMENTED
                        }, { ErrorHandler.handleError(it, context as Context) })
                )

            } else if (comment.status == VoteType.NOT_COMMENTED) {
                disposable.add(
                    voteComp(comment.id, vote)
                        .compose(Helper.applyCompletableSchedulers())
                        .doOnComplete(doOnVote)
                        .subscribe({
                            Snackbar.make(
                                requireView(),
                                getString(if (vote == VoteType.LIKED) R.string.comment_like else R.string.comment_dislike),
                                Snackbar.LENGTH_SHORT
                            ).show()

                            isCommenting = false

                            when (vote) {
                                VoteType.LIKED -> comment.likes++
                                VoteType.DISLIKED -> comment.dislikes++
                            }

                            comment.status = vote

                        }, { ErrorHandler.handleError(it, context as Context) })
                )
            } else {
                Snackbar.make(
                    requireView(),
                    getString(R.string.comment_already_voted),
                    Snackbar.LENGTH_SHORT
                ).show()

                isCommenting = false
            }
        }
    }

    private fun deleteComment(id: Int, doOnSuccess: () -> Unit) {
        if (!isCommenting) {
            isCommenting = true

            disposable.add(
                deleteComp(id)
                    .compose(Helper.applyCompletableSchedulers())
                    .doOnComplete(doOnSuccess)
                    .subscribe({
                        Snackbar.make(
                            requireView(),
                            getString(R.string.comment_delete_success),
                            Snackbar.LENGTH_SHORT
                        ).show()

                        isCommenting = false

                    }, { ErrorHandler.handleError(it, context as Context) })
            )
        }
    }

    private fun editComment(
        id: Int,
        message: String,
        doOnSuccess: (id: Int, message: String) -> Unit
    ) {
        if (!isEditting) {

            isEditting = true

            val editFragment = CommentEditFragment(message, {
                val transaction = (fragmentManager as FragmentManager).beginTransaction()
                transaction.remove(it)
                transaction.commit()
                isEditting = false
            }) { mes, doOnSave ->
                if (!isCommenting) {
                    isCommenting = true
                    disposable.add(
                        editComp(id, message)
                            .compose(Helper.applyCompletableSchedulers())
                            .doOnComplete {
                                doOnSave()
                                isCommenting = false
                            }
                            .subscribe({
                                Snackbar.make(
                                    requireView(),
                                    getString(R.string.comment_edit_success),
                                    Snackbar.LENGTH_SHORT
                                ).show()

                                doOnSave()
                                doOnSuccess(id, mes)

                            }, { ErrorHandler.handleError(it, context as Context) })
                    )
                }
            }

            val transaction = (fragmentManager as FragmentManager).beginTransaction()
            transaction.add(editFrameLayout.id, editFragment, EDIT_TAG)
            transaction.commit()
        }

    }

    private fun createComment() {
        if (!isCommenting) {
            isCommenting = true

            if (commentEditText.text.isEmpty() && commentEditText.text.length > 1000) {
                return
            }

            disposable.add(
                createComp(commentEditText.text.toString())
                    .compose(Helper.applySingleSchedulers())
                    .doFinally {
                        isCommenting = false
                    }.subscribe({
                        Snackbar.make(
                            requireView(),
                            getString(R.string.comment_create_success),
                            Snackbar.LENGTH_SHORT
                        ).show()

                        commentEditText.setText("")
                        isCommenting = false

                        val adapter = recyclerView.adapter
                        if(adapter is CommentRecyclerViewAdapter) {
                            adapter.addItem(it)
                        }
                    }, { ErrorHandler.handleError(it, context as Context) })
            )
        }
    }

    class CommentEditFragment(
        private val message: String,
        private val onCancel: (fragment: Fragment) -> Unit,
        private val onSave: (message: String, doOnSuccess: () -> Unit) -> Unit
    ) : Fragment() {
        private lateinit var messageEdit: EditText

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_comment_edit, container, false)

            messageEdit = view.findViewById(R.id.message)

            setMessage(message)

            view.findViewById<Button>(R.id.save_action)?.let {
                it.setOnClickListener {
                    onSave(getMessage()) {
                        onCancel(this)
                    }
                }
            }

            view.findViewById<Button>(R.id.cancel_action)?.let {
                it.setOnClickListener {
                    onCancel(this)
                }
            }

            return view
        }

        private fun setMessage(message: String) {
            messageEdit.setText(message)
        }

        private fun getMessage(): String {
            return messageEdit.text.toString()
        }
    }

    companion object {
        const val TAG = "CommentFragment"
        private const val EDIT_TAG = "CommentEditFragment"
    }
}
