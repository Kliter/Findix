package com.kl.findix.presentation.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kl.findix.R
import com.kl.findix.databinding.FragmentProfileBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.navigation.ProfileNavigator
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

    private var epoxyController: ProfileController? = null
    private lateinit var _viewModel: ProfileViewModel
    private val binding: FragmentProfileBinding by lazy {
        DataBindingUtil.inflate<FragmentProfileBinding>(
            layoutInflater,
            R.layout.fragment_profile,
            container,
            false
        )
    }

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

        binding.apply {
            lifecycleOwner = this@ProfileFragment
            viewModel = _viewModel
            onClickSave = View.OnClickListener {
                _viewModel.saveProfileSettings()
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setController()
    }

    private fun setController() {
        context?.let {
            epoxyController = ProfileController(
                _viewModel.user,
                getString(R.string.hint_username),
                it.getDrawable(R.drawable.ic_user),
                getString(R.string.hint_major),
                it.getDrawable(R.drawable.ic_major),
                getString(R.string.hint_description),
                it.getDrawable(R.drawable.ic_description),
                getString(R.string.hint_website),
                it.getDrawable(R.drawable.ic_website)
            ).also {
                binding.recyclerView.setControllerAndBuildModels(it)
            }
        }
    }
}