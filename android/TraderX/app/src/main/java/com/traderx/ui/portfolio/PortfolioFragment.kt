package com.traderx.ui.portfolio

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.api.response.EquipmentResponse
import com.traderx.api.response.EquipmentsResponse
import com.traderx.api.response.PortfolioEquipment
import com.traderx.ui.search.UserSearchSkeletonRecyclerViewAdapter
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.FragmentTitleListeners
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.EquipmentViewModel
import com.traderx.viewmodel.PortfolioViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.layout_portfolio_modal.view.*


class PortfolioFragment : Fragment(), FragmentTitleEmitters {
    private lateinit var name: String

    private lateinit var recyclerView: RecyclerView
    private val disposable = CompositeDisposable()
    private lateinit var portfolioViewModel: PortfolioViewModel
    private lateinit var equipmentViewModel: EquipmentViewModel
    private lateinit var addEquipmentButton: ImageButton
    private lateinit var expandableListView: ExpandableListView

    private var equipments =  HashMap<String, List<PortfolioEquipment>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString("portfolio_name") as String
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setFragmentTitle(context as Context, "Portfolio: $name")

        val portfolioViewModelFactory =
            Injection.provideEquipmentViewModelFactory(context as Context)
        val equipmentViewModelFactory =
            Injection.provideEquipmentViewModelFactory(context as Context)
        equipmentViewModel = ViewModelProvider(this, equipmentViewModelFactory)
            .get(EquipmentViewModel::class.java)
        portfolioViewModel = ViewModelProvider(this, portfolioViewModelFactory)
            .get(PortfolioViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_portfolio, container, false)
        addEquipmentButton = root.findViewById(R.id.portfolio_equipment_add)

        recyclerView = root.findViewById<RecyclerView>(R.id.portfolio_equipment_list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = UserSearchSkeletonRecyclerViewAdapter(5)
        }


        equipmentViewModel.let { viewModel ->
            disposable.addAll(
                viewModel.getCryptoCurrencyEquipments().compose(Helper.applyFlowableSchedulers<EquipmentsResponse>())
                    .subscribe({
                        equipments["Currency"] = it.equipments.map { e -> PortfolioEquipment(e.code, e.data, false) }
                    }, { ErrorHandler.handleError(it, context as Context) }),
                viewModel.getStockEquipments().compose(Helper.applyFlowableSchedulers<EquipmentsResponse>())
                    .subscribe({
                        equipments["Stock"] =  it.equipments.map { e -> PortfolioEquipment(e.code, e.data, false) }
                    }, { ErrorHandler.handleError(it, context as Context) }),
                viewModel.getCurrencyEquipments().compose(Helper.applyFlowableSchedulers<EquipmentsResponse>())
                    .subscribe({
                        equipments["Crypto Currency"] =  it.equipments.map { e -> PortfolioEquipment(e.code, e.data, false) }
                    }, { ErrorHandler.handleError(it, context as Context) })
            )
        }

        setEquipments(disposable)

        addEquipmentButton.setOnClickListener {
            val pDialogView = LayoutInflater.from(context as Context)
                .inflate(R.layout.layout_equipment_modal,null)
            expandableListView = pDialogView.findViewById(R.id.portfolio_expandable_equipments)

            expandableListView.setAdapter(
                PortfolioExpendableAdapter(
                    context as Context,  equipments.keys.toList(),
                    equipments
                )
            )
             expandableListView.setOnGroupExpandListener{
                for (i in 0 until expandableListView.expandableListAdapter.groupCount)
                    if (i != it){
                        expandableListView.collapseGroup(i)
                    }
            }


            val builder = AlertDialog.Builder(context as Context)
                .setView(pDialogView).setTitle("Add Equipments")
            val pAlertDialog = builder.show()


            pDialogView.dialog_add_portfolio.setOnClickListener {
                addEquipments((expandableListView.expandableListAdapter as PortfolioExpendableAdapter).getSelecteds(), onComplete = {
                     setEquipments(disposable)
                     pAlertDialog.dismiss()

                })

            }

            pDialogView.dialog_cancel_btn.setOnClickListener {
                pAlertDialog.dismiss()
            }
        }



        return root
    }

    private fun setEquipments( disposable: CompositeDisposable){
        disposable.add(portfolioViewModel.getPortfolio(name)
            .compose(Helper.applySingleSchedulers<ArrayList<EquipmentResponse.Equipment>>())
            .subscribe({
                recyclerView.adapter =
                    PortfolioDetailRecyclerViewAdapter(
                        it
                    ) { code, onComplete -> deleteEquipment( code,  onComplete) }
            }, {}))
    }

    private fun addEquipments(codes : List<String> , onComplete: () -> Unit){
        disposable.add(
            portfolioViewModel.addToPortfolio(name,codes)
                .compose(Helper.applyCompletableSchedulers())
                .doFinally {
                    onComplete()
                }
                .subscribe({
                    Snackbar.make(
                        requireView(),
                        "Equipments are added to portfolio.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }, {
                    ErrorHandler.handleError(it, context as Context)
                })
        )
    }

    private fun deleteEquipment( code : String,  onComplete: () -> Unit) {

        disposable.add(
            portfolioViewModel.deleteFromPortfolio(name,code)
                .compose(Helper.applyCompletableSchedulers())
                .doFinally {
                    onComplete()
                }
                .subscribe({
                    Snackbar.make(
                        requireView(),
                        "$code is removed from portfolio $name",
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