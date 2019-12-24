package com.traderx.ui.profile



import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.api.response.AssetsResponse
import com.traderx.api.response.EquipmentResponse
import com.traderx.api.response.FollowerResponse
import com.traderx.ui.equipment.EquipmentsFragmentDirections
import com.traderx.ui.portfolio.PortfolioDetailRecyclerViewAdapter
import com.traderx.ui.search.SearchSkeletonRecyclerViewAdapter
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.FragmentTitleListeners
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.AuthUserViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.item_alert.*
import retrofit2.HttpException
import java.time.temporal.TemporalAmount

class AssetsFragment : Fragment(), FragmentTitleEmitters {

    private lateinit var userViewModel: AuthUserViewModel
    private lateinit var recyclerView: RecyclerView
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setFragmentTitle(context as Context, "My Assets")

        val userViewModelFactory = Injection.provideAuthUserViewModelFactory(context as Context)
        userViewModel =
            ViewModelProvider(this, userViewModelFactory).get(AuthUserViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_assets, container, false)

        recyclerView = root.findViewById<RecyclerView>(R.id.assets_list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SearchSkeletonRecyclerViewAdapter(5)
        }

        getAssets(disposable)



        return root
    }

    private fun getAssets(disposable: CompositeDisposable){
        disposable.add(
            userViewModel.getAssets()
                .compose(Helper.applySingleSchedulers<ArrayList<AssetsResponse>>())
                .subscribe({
                    recyclerView.adapter = AssetRecyclerViewAdapter(it,
                        context as Context,
                        { code, amount,  onComplete -> sellEquipment( code,amount,   onComplete) },
                        { code -> AssetsFragmentDirections.actionNavigationAssetsToDetails(code) })
                }, {
                    ErrorHandler.handleError(it, context as Context)
                })
        )
    }

    private fun sellEquipment( code : String, amount: Double,   onComplete: () -> Unit) {

        disposable.add(
            userViewModel.sellAsset(code,amount)
                .compose(Helper.applyCompletableSchedulers())
                .doFinally {
                    getAssets(disposable)
                    onComplete()
                }
                .subscribe({
                    Snackbar.make(
                        requireView(),
                        "$amount $code is sold",
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




    override fun setFragmentTitle(context: Context?, title: String?) {

        if (context is FragmentTitleListeners) {
            context.showFragmentTitle(title)
        }
    }
}
