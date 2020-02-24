package com.kl.findix.presentation.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyController
import com.bumptech.glide.Glide
import com.kl.findix.R
import com.kl.findix.databinding.FragmentProfileBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.navigation.ProfileNavigator
import com.kl.findix.util.nonNullObserve
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileFragment : Fragment() {

    companion object {
        private const val TAG = "MapsFragment"
    }

    @Inject
    lateinit var navigator: ProfileNavigator
    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var _viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private var controller: ProfileController? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewModel =
            ViewModelProviders.of(this, mViewModelFactory).get(ProfileViewModel::class.java)

        binding = DataBindingUtil.inflate<FragmentProfileBinding>(
            layoutInflater,
            R.layout.fragment_profile,
            container,
            false
        ).apply {
            lifecycleOwner = this@ProfileFragment
            viewModel = _viewModel
            toolbar.setTitle(R.string.action_profile)
        }
        lifecycle.addObserver(_viewModel)

        _viewModel.setProfileIcon()
        _viewModel.fetchUserInfo()
        _viewModel.fetchOwnOrder()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setController()
        observeState(_viewModel)
        observeEvent(_viewModel)
    }

    private fun setController() {
        controller = ProfileController(
            onClickMenu = {

            }
        )
        binding.recyclerView?.let {
            it.setController(controller as EpoxyController)
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            it.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    return true
                }
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
            it.setOnTouchListener { _, motionEvent ->
                (container as ViewGroup).onTouchEvent(motionEvent)
                true
            }
        }
    }

    private fun observeEvent(viewModel: ProfileViewModel) {
        viewModel.run {
            setProfileIconCommand.nonNullObserve(viewLifecycleOwner) { storageReference ->
                Glide.with(requireContext())
                    .load(storageReference)
                    .into(binding.profilePhoto)
            }
        }
    }

    private fun observeState(viewModel: ProfileViewModel) {
        viewModel.run {
            profileIconBitmap.nonNullObserve(viewLifecycleOwner) { profileIconBitmap ->
                binding.profileIconSrc = profileIconBitmap
            }
            orders.nonNullObserve(viewLifecycleOwner) { orders ->
                controller?.setData(orders)
            }
        }
    }
}