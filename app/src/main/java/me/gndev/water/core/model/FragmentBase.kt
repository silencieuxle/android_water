package me.gndev.water.core.model

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import me.gndev.water.util.DialogUtils
import me.gndev.water.MainActivityViewModel
import me.gndev.water.data.share_preferences.PrefManager
import java.lang.reflect.ParameterizedType
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class FragmentBase<TViewModel: ViewModelBase>(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {
    @Inject
    protected lateinit var prefManager: PrefManager

    protected val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    protected var actionBar: ActionBar? = null
    protected lateinit var progressDialog: AlertDialog
    protected val viewModel by viewModel(viewModelClass())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = DialogUtils.getProgressDialog(requireContext(), layoutInflater)
        actionBar = (activity as AppCompatActivity).supportActionBar

        setupActionBar()
        setupViews()
        overrideBackButton()
        subscribeObservers()

        viewModel.loadingEvent.observe(viewLifecycleOwner, {
            if (it == true) {
                if (!progressDialog.isShowing) progressDialog.show()
            } else {
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
        })

        viewModel.navLiveEvent.observe(viewLifecycleOwner, { navDirection ->
            findNavController().navigate(navDirection)
        })
    }

    open fun overrideBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!findNavController().popBackStack()) {
                        activity?.finish()
                    }
                }
            })
    }

    abstract fun setupActionBar()
    abstract fun setupViews()
    abstract fun subscribeObservers()

    private fun viewModelClass(): KClass<TViewModel> {
        return ((javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<TViewModel>).kotlin
    }

    private fun Fragment.viewModel(
        clazz: KClass<TViewModel>,
        ownerProducer: () -> ViewModelStoreOwner = { this },
        factoryProducer: (() -> ViewModelProvider.Factory)? = null,
    ) = createViewModelLazy(clazz, { ownerProducer().viewModelStore }, factoryProducer)
}