package com.traderx.ui.event

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
import com.traderx.ui.search.UserSearchSkeletonRecyclerViewAdapter
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.EventViewModel
import io.reactivex.disposables.CompositeDisposable

class EventFragment : Fragment(), FragmentTitleEmitters {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventViewModel: EventViewModel

    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = context as Context

        setFragmentTitle(context, getString(R.string.title_events))

        val eventViewModelFactory = Injection.provideEventViewModelFactory(context)
        eventViewModel =
            ViewModelProvider(this, eventViewModelFactory).get(EventViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_event, container, false)

        val viewManager = LinearLayoutManager(context)
        recyclerView = view.findViewById<RecyclerView>(R.id.event_list).apply {
            layoutManager = viewManager
            adapter = UserSearchSkeletonRecyclerViewAdapter(5)
        }

        disposable.add(
            eventViewModel.getEvents()
                .compose(Helper.applySingleSchedulers())
                .subscribe({
                    recyclerView.adapter = EventRecyclerViewAdapter(it)
                }, { ErrorHandler.handleError(it, context)})
        )

        return view
    }
}
