package com.traderx.ui.portfolio


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.api.response.PortfolioResponse
import com.traderx.ui.search.UserSearchSkeletonRecyclerViewAdapter
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.FragmentTitleListeners
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.PortfolioViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.layout_portfolio_modal.view.*
import retrofit2.HttpException

class PortfoliosFragment : Fragment(), FragmentTitleEmitters {

    private lateinit var recyclerView: RecyclerView
    private val disposable = CompositeDisposable()
    private lateinit var portfolioViewModel: PortfolioViewModel
    private lateinit var addPortfolioButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setFragmentTitle(context as Context, "My Portfolio")


         val portfolioViewModelFactory =
            Injection.provideEquipmentViewModelFactory(context as Context)


        portfolioViewModel = ViewModelProvider(this, portfolioViewModelFactory)
            .get(PortfolioViewModel::class.java)


        val root = inflater.inflate(R.layout.fragment_portfolios, container, false)

        addPortfolioButton = root.findViewById(R.id.portfolio_add_action)

        recyclerView = root.findViewById<RecyclerView>(R.id.portfolio_list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = UserSearchSkeletonRecyclerViewAdapter(5)
        }

        addPortfolioButton.setOnClickListener {
            val pDialogView = LayoutInflater.from(context as Context)
                .inflate(R.layout.layout_portfolio_modal,null)

            val builder = AlertDialog.Builder(context as Context)
                .setView(pDialogView).setTitle("Create Portfolio")
            val pAlertDialog = builder.show()

            pDialogView.dialog_add_portfolio.setOnClickListener {
                val portfolioName = pDialogView.dialog_port_name.text.toString()
                addPortfolio(portfolioName, onComplete = {
                    pAlertDialog.dismiss()

                })

            }

            pDialogView.dialog_cancel_btn.setOnClickListener {
                pAlertDialog.dismiss()
            }


        }

        disposable.add(
            portfolioViewModel.getPortfolios()
                .compose(Helper.applySingleSchedulers<ArrayList<PortfolioResponse>>())
                .subscribe({
                    recyclerView.adapter =
                        PortfolioRecyclerViewAdapter(
                            it,
                            { name, onComplete -> deletePortfolio(name, onComplete) },
                            { name ->
                                PortfoliosFragmentDirections.actionNavigationPortfolioToDetails(name)
                            }
                        )
                }, {})
        )

        return root
    }


    private fun addPortfolio(name: String, onComplete: () -> Unit) {
        disposable.add(
            portfolioViewModel.addPortfolio(name)
                .compose(Helper.applyCompletableSchedulers())
                .doFinally {
                    onComplete()
                }
                .subscribe({
                    (recyclerView.adapter as PortfolioRecyclerViewAdapter).addData(PortfolioResponse(name))
                    Snackbar.make(
                        requireView(),
                        "Added $name",
                        Snackbar.LENGTH_SHORT
                    ).show()

                }, {
                    if (!ErrorHandler.handleConnectException(it, context as Context) && it is HttpException) {
                        val errorResponse = ErrorHandler.parseErrorMessage(
                            it.response().errorBody()?.string() ?: ""
                        )
                        Snackbar.make(
                            requireView(),
                            errorResponse.message,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                })
        )
    }

    private fun deletePortfolio(name: String, onComplete: () -> Unit) {

        disposable.add(
            portfolioViewModel.deletePortfolio(name)
                .compose(Helper.applyCompletableSchedulers())
                .doFinally {
                    onComplete()
                }
                .subscribe({
                    Snackbar.make(
                        requireView(),
                        getString(R.string.delete_portfolio),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }, {
                    ErrorHandler.handleError(it, context as Context)
                })
        )
    }


    override fun setFragmentTitle(context: Context?, title: String?) {

        if (context is FragmentTitleListeners) {
            context.showFragmentTitle(title)
        }
    }


}
