package com.kl.findix.presentation.order

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.kl.findix.R
import com.kl.findix.databinding.FragmentOrderDetailBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.navigation.OrderDetailNavigator
import com.kl.findix.util.getDateTimeText
import com.kl.findix.util.nonNullObserve
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class OrderDetailFragment: Fragment() {

    companion object {
        private const val TAG = "OrderDetailFragment"
    }


    @Inject
    lateinit var mViewModelFactory: ViewModelFactory
    @Inject
    lateinit var navigator: OrderDetailNavigator

    private lateinit var _viewModel: OrderDetailViewModel
    private lateinit var binding: FragmentOrderDetailBinding

    private val args: OrderDetailFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewModel = ViewModelProviders.of(this, mViewModelFactory).get(OrderDetailViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_detail, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            lifecycle.addObserver(_viewModel)
            onClickUserName = View.OnClickListener {
                _viewModel.toProfileDetailFragment()
            }
            onClickBack = View.OnClickListener {
                navigator.toPrev()
            }
        }

        _viewModel.fetchOrderDetail(args.orderId)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeEvent(_viewModel)
        observeState(_viewModel)
    }

    private fun observeState(viewModel: OrderDetailViewModel) {
        viewModel.run {
            this._order.nonNullObserve(viewLifecycleOwner) { order ->
                binding.order = order
                order.timeStamp?.let { date ->
                    binding.dateTime = getDateTimeText(date)
                }
            }
        }
    }

    private fun observeEvent(viewModel: OrderDetailViewModel) {
        viewModel.run {
            this.toProfileDetailCommand.nonNullObserve(viewLifecycleOwner) { userId ->
                userId?.let {
                    navigator.toProfileDetailFragment(it)
                }
            }
        }
    }
}