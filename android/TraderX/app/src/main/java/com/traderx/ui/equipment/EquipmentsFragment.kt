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
import com.traderx.util.Injection
import com.traderx.viewmodel.EquipmentViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class EquipmentsFragment : Fragment() {

    private lateinit var equipmentViewModel: EquipmentViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: LinearLayoutManager

    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val equipmentViewModelFactory =
            Injection.provideTraderEquipmentViewModelFactory(context as Context)

        equipmentViewModel = ViewModelProvider(
            this,
            equipmentViewModelFactory
        ).get(EquipmentViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_equipments, container, false)

        viewManager = LinearLayoutManager(context)
        recyclerView = root.findViewById<RecyclerView>(R.id.equipment_list).apply {
            layoutManager = viewManager
        }

        disposable.add(
            equipmentViewModel.getEquipments().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    recyclerView.swapAdapter(EquipmentRecyclerViewAdapter(it), false)
                },
                    { ErrorHandler.handleError(it, context as Context) })
        )

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
