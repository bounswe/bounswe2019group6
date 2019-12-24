package com.traderx.ui.prediction


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
import com.traderx.ui.search.SearchSkeletonRecyclerViewAdapter
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.EquipmentViewModel
import io.reactivex.disposables.CompositeDisposable

class PredictionsFragment : Fragment(), FragmentTitleEmitters {

    private var username: String = ""
    private lateinit var recyclerView: RecyclerView
    private lateinit var equipmentViewModel: EquipmentViewModel

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            username = it.getString(ARG_USERNAME) as String
        }

        val equipmentViewModelFactory =
            Injection.provideEquipmentViewModelFactory(context as Context)

        equipmentViewModel = ViewModelProvider(this, equipmentViewModelFactory)
            .get(EquipmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setFragmentTitle(context, getString(R.string.title_predictions))
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_predictions, container, false)

        val viewManager = LinearLayoutManager(context as Context)
        recyclerView = view.findViewById<RecyclerView>(R.id.prediction_list).apply {
            layoutManager = viewManager
            adapter = SearchSkeletonRecyclerViewAdapter(5)
        }

        disposable.add(
            equipmentViewModel.getPredictions(username)
                .compose(Helper.applySingleSchedulers())
                .subscribe({
                    recyclerView.adapter = PredictionRecyclerViewAdapter(it.predictions, context as Context)
                }, { ErrorHandler.handleError(it, context as Context) })
        )

        return view
    }

    companion object {
        const val ARG_USERNAME = "username"
    }
}
