package com.traderx.ui.portfolio


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
import com.traderx.ui.search.UserSearchSkeletonRecyclerViewAdapter
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.FragmentTitleListeners
import com.traderx.util.Injection
import com.traderx.viewmodel.AuthUserViewModel

class PortfoliosFragment : Fragment(), FragmentTitleEmitters {

    private lateinit var userViewModel: AuthUserViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setFragmentTitle(context as Context, "My Portfolio")

        val userViewModelFactory = Injection.provideAuthUserViewModelFactory(context as Context)
        userViewModel =
            ViewModelProvider(this, userViewModelFactory).get(AuthUserViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_portfolio, container, false)

        recyclerView = root.findViewById<RecyclerView>(R.id.portfolio_list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = UserSearchSkeletonRecyclerViewAdapter(5)
        }

        return root
    }


    override fun setFragmentTitle(context: Context?, title: String?) {

        if (context is FragmentTitleListeners) {
            context.showFragmentTitle(title)
        }
    }


}
