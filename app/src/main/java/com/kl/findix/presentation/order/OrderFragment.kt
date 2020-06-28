package com.kl.findix.presentation.order

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.airbnb.epoxy.EpoxyController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.google.firebase.storage.StorageReference
import com.kl.findix.R
import com.kl.findix.databinding.FragmentOrderBinding
import com.kl.findix.model.Order
import com.kl.findix.util.extension.nonNullObserve
import com.kl.findix.util.extension.viewModelProvider
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class OrderFragment : Fragment(), RewardedVideoAdListener {

    companion object {
        private const val TAG = "OrderFragment"
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navController: NavController

    private lateinit var _viewModel: OrderViewModel
    private lateinit var binding: FragmentOrderBinding
    private lateinit var rewardedVideoAd: RewardedVideoAd
    private var controller: OrderController? = null
    private var isAdRewarded = false
    private var isAdClosed = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity)
        rewardedVideoAd.rewardedVideoAdListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewModel = viewModelProvider(mViewModelFactory)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel
            swipeRefresh.setOnRefreshListener {
                _viewModel.fetchLast15Orders()
            }
            onClickAddOrder = View.OnClickListener {
                if (rewardedVideoAd.isLoaded) {
                    rewardedVideoAd.show()
                }
            }
        }

        _viewModel.fetchLast15Orders()

        initAd()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setController()
        observeState(_viewModel)
    }

    override fun onPause() {
        super.onPause()
        rewardedVideoAd.pause(activity)
    }

    override fun onResume() {
        super.onResume()
        rewardedVideoAd.resume(activity)
    }

    override fun onDestroy() {
        super.onDestroy()
        rewardedVideoAd.destroy(activity)
    }

    override fun onRewardedVideoAdClosed() {
        context?.let {
            loadMovieAd()
        }
        isAdClosed = true
        navigateToCreateOrder()
    }

    override fun onRewardedVideoAdLeftApplication() {
        // NOP
    }

    override fun onRewardedVideoAdLoaded() {
        // NOP
    }

    override fun onRewardedVideoAdOpened() {
        // NOP
    }

    override fun onRewardedVideoCompleted() {
        context?.let {
            loadMovieAd()
        }
    }

    override fun onRewarded(reward: RewardItem?) {
        isAdRewarded = true
        navigateToCreateOrder()
    }

    override fun onRewardedVideoStarted() {
        // NOP
    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {
        // NOP
    }

    private fun initAd() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        loadMovieAd()
    }

    private fun navigateToCreateOrder() {
        if (isAdClosed && isAdRewarded) {
            context?.let {
                navController.navigate(
                    OrderFragmentDirections.toCreateOrder()
                )
            }
        }
    }

    private fun loadMovieAd() {
        rewardedVideoAd.loadAd(
            getString(R.string.order_movie_ad_id),
            AdRequest.Builder().build()
        )
    }

    private fun setController() {
        controller = OrderController(
            onClickOrder = { order ->
                navController.navigate(
                    OrderFragmentDirections.toOrderDetail(order.orderId)
                )
            }
        )
        binding.recyclerView.setController(controller as EpoxyController)
    }

    private fun observeState(viewModel: OrderViewModel) {
        viewModel.run {
            this.orderListItems.nonNullObserve(viewLifecycleOwner) { orderListItems ->
                controller?.setData(orderListItems)
                if (binding.swipeRefresh.isRefreshing) {
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }
    }
}

data class OrderListItem(
    val order: Order,
    var orderPhotoRef: StorageReference? = null
)