package com.traderx.ui.alert

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.api.response.AlertResponse
import com.traderx.ui.search.UserSearchSkeletonRecyclerViewAdapter
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.EquipmentViewModel
import io.reactivex.disposables.CompositeDisposable

class AlertsFragment : Fragment(), FragmentTitleEmitters {

    private lateinit var equipmentViewModel: EquipmentViewModel

    private lateinit var recyclerView: RecyclerView

    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = context as Context

        setFragmentTitle(context, getString(R.string.my_alerts))

        equipmentViewModel = ViewModelProvider(this, Injection.provideEquipmentViewModelFactory(context)).get(EquipmentViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_alerts, container, false)

        recyclerView = root.findViewById<RecyclerView>(R.id.alert_list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = UserSearchSkeletonRecyclerViewAdapter(5)
        }

        disposable.add(
            equipmentViewModel.getAlerts()
                .compose(Helper.applySingleSchedulers<ArrayList<AlertResponse>>())
                .subscribe({
                    recyclerView.adapter = AlertRecyclerViewAdapter(it)
                }, {
                    ErrorHandler.handleError(it, context)
                })
        )

        return root
    }

}
