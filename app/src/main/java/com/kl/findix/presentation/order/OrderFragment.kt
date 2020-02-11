package com.kl.findix.presentation.order

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyController
import com.kl.findix.R
import com.kl.findix.databinding.FragmentOrderBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.navigation.OrderNavigator
import com.kl.findix.util.nonNullObserve
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class OrderFragment : Fragment() {

    companion object {
        private const val TAG = "OrderFragment"
    }

    @Inject
    lateinit var navigator: OrderNavigator
    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var _viewModel: OrderViewModel
    private lateinit var binding: FragmentOrderBinding
    private var controller: OrderController? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewModel = ViewModelProviders.of(this, mViewModelFactory).get(OrderViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel
            swipeRefresh.setOnRefreshListener {
                _viewModel.fetchLast15Orders()
            }
            onClickAddOrder = View.OnClickListener {
                navigator.toCreateOrderFragment()
            }
            onClickSearch = View.OnClickListener {
                navigator.toSearchFragment()
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setController()
        observeState(_viewModel)
//        observeEvent(_viewModel)
    }

    private fun setController() {
        controller = OrderController(
            onClickOrder = { order ->
                navigator.toOrderDetailFragment(order.orderId)
            }
        )
        binding.recyclerView?.let {
            it.setController(controller as EpoxyController)
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observeState(viewModel: OrderViewModel) {
        viewModel.run {
            this.orders.nonNullObserve(viewLifecycleOwner) { orders ->
                controller?.setData(orders)
                if (binding.swipeRefresh.isRefreshing) {
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun observeEvent(viewModel: OrderViewModel) {
    }
}