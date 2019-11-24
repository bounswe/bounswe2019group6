package com.traderx.ui.transaction

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.api.response.TransactionsResponse
import com.traderx.ui.profile.PendingRequestRecyclerViewAdapter
import com.traderx.ui.search.UserSearchSkeletonRecyclerViewAdapter
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.FragmentTitleListeners
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.AuthUserViewModel
import com.traderx.viewmodel.TransactionViewModel
import io.reactivex.disposables.CompositeDisposable

class TransactionsFragment : Fragment(), FragmentTitleEmitters {

    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var authUserViewModel: AuthUserViewModel
    private lateinit var recyclerView: RecyclerView

    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setFragmentTitle(context, getString(R.string.title_my_transactions))

        val context = context as Context

        val root = inflater.inflate(R.layout.fragment_transactions, container, false)

        val viewManager = LinearLayoutManager(context)

        recyclerView = root.findViewById<RecyclerView>(R.id.transaction_list).apply {
            layoutManager = viewManager
            adapter = UserSearchSkeletonRecyclerViewAdapter(3)
        }

        transactionViewModel =
            ViewModelProvider(this, Injection.provideTransactionViewModelFactory(context)).get(
                TransactionViewModel::class.java
            )

        authUserViewModel =
            ViewModelProvider(this, Injection.provideAuthUserViewModelFactory(context)).get(
                AuthUserViewModel::class.java
            )

        disposable.add(
            authUserViewModel.user(context).flatMap { transactionViewModel.getTransactions(it.username) }
                .compose(Helper.applySingleSchedulers<List<TransactionsResponse>>())
                .subscribe({
                    recyclerView.adapter = TransactionRecyclerViewAdapter(it)
                }, {
                    ErrorHandler.handleError(it, context)
                })
        )

        return root
    }
}
