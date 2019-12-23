package com.traderx.ui.equipment

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
import com.traderx.api.response.EquipmentsResponse
import com.traderx.type.EquipmentType
import com.traderx.ui.search.SearchSkeletonRecyclerViewAdapter
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.FragmentTitleListeners
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.EquipmentViewModel
import io.reactivex.disposables.CompositeDisposable

class EquipmentsFragment : Fragment(), FragmentTitleEmitters {

    private var equipmentType: EquipmentType? = null
    private lateinit var equipmentViewModel: EquipmentViewModel
    private lateinit var recyclerView: RecyclerView

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            equipmentType = it.getSerializable(ARG_EQUIPMENT_TYPE) as EquipmentType
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setFragmentTitle(context, findFragmentTitle(equipmentType ?: EquipmentType.CURRENCY))

        val equipmentViewModelFactory =
            Injection.provideEquipmentViewModelFactory(context as Context)

        equipmentViewModel = ViewModelProvider(this, equipmentViewModelFactory)
            .get(EquipmentViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_equipments, container, false)

        val viewManager = LinearLayoutManager(context)

        recyclerView = root.findViewById<RecyclerView>(R.id.equipment_list).apply {
            layoutManager = viewManager
            adapter = SearchSkeletonRecyclerViewAdapter(3)
        }

        val source = when (equipmentType) {
            EquipmentType.STOCK -> equipmentViewModel.getStockEquipments()
            EquipmentType.CURRENCY -> equipmentViewModel.getCurrencyEquipments()
            else -> equipmentViewModel.getCryptoCurrencyEquipments()
        }

        disposable.add(
            source
                .compose(Helper.applyFlowableSchedulers<EquipmentsResponse>())
                .subscribe({
                    recyclerView.adapter = EquipmentRecyclerViewAdapter(it.equipments, it.base, equipmentType ?: EquipmentType.STOCK, context as Context)
                },
                    { ErrorHandler.handleError(it, context as Context) })
        )

        return root
    }

    private fun findFragmentTitle(equipmentType: EquipmentType): String {
        return when (equipmentType) {
            EquipmentType.CURRENCY -> getString(R.string.currencies)
            EquipmentType.CRYPTO_CURRENCY -> getString(R.string.crypto_currencies)
            EquipmentType.STOCK -> getString(R.string.stocks)
        }
    }

    override fun setFragmentTitle(context: Context?, title: String?) {
        if (context is FragmentTitleListeners) {
            context.showFragmentTitle(title)
        }
    }

    companion object {
        private const val ARG_EQUIPMENT_TYPE = "equipment_type"
    }
}
