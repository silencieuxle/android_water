package me.gndev.water.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import me.gndev.water.R
import me.gndev.water.core.base.FragmentBase
import me.gndev.water.core.base.ViewModelBase
import me.gndev.water.core.constant.Container
import me.gndev.water.core.constant.SharedPreferencesKey
import me.gndev.water.core.constant.UserSex
import me.gndev.water.databinding.OnboardingSetupGameFragmentBinding
import me.gndev.water.util.CommonExtensions.focusAndShowKeyboard
import me.gndev.water.util.DialogUtils
import me.gndev.water.util.DialogUtils.showConfirmationDialog
import me.gndev.water.util.GoalHelper
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingSetupBattleSettingsFragment :
    FragmentBase<OnboardingSetupGameViewModel>(R.layout.onboarding_setup_game_fragment) {
    private var _binding: OnboardingSetupGameFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var btnDone: Button
    private lateinit var actvContainer: AutoCompleteTextView
    private lateinit var tietVolume: TextInputEditText
    private lateinit var tietGoal: TextInputEditText

    private var container: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OnboardingSetupGameFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupActionBar() {
        actionBar?.hide()
    }

    override fun setupViews() {
        btnDone = binding.btnDone
        val sex = prefManager.getStringVal(SharedPreferencesKey.USER_SEX, UserSex.MALE)
        val age = prefManager.getIntVal(SharedPreferencesKey.USER_AGE, 9)
        val recommendedGoal = GoalHelper.getRecommendedGoal(age, sex)
        val weaponListAdapter =
            WeaponDropDownAdapter(
                requireContext(), R.layout.dropdown_item, listOf(
                    ContainerDropdownModel("Cup", Container.CUP),
                    ContainerDropdownModel(
                        "Bottle",
                        Container.BOTTLE
                    ),
                    ContainerDropdownModel("Can", Container.CAN),
                    ContainerDropdownModel("Tank", Container.TANK)
                )
            )
        actvContainer = binding.actvContainer.apply {
            addTextChangedListener {
                if (it != null) viewModel.setContainer(if (it.isEmpty()) "" else it.toString())
            }
            setText(
                prefManager.getStringVal(SharedPreferencesKey.DEFAULT_CONTAINER, Container.CUP), false
            )
            setDropDownBackgroundDrawable(
                androidx.core.content.ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bg_dropdown
                )
            )
            setOnItemClickListener { adapterView, _, i, _ ->
                val selectedItem = adapterView.getItemAtPosition(i) as ContainerDropdownModel
                container = selectedItem.value
                actvContainer.setText(selectedItem.text)
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showSnackBar(
                    null,
                    context.getString(R.string.container_description),
                    R.drawable.ic_water_drop,
                    null,
                    null,
                    Snackbar.LENGTH_SHORT
                )
            }
            setAdapter(weaponListAdapter)
        }
        tietVolume = binding.tietVolume.apply {
            addTextChangedListener {
                if (it != null) viewModel.setVolume(if (it.isEmpty()) 0 else it.toString().toInt())
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showSnackBar(
                    null,
                    context.getString(R.string.volume_description),
                    R.drawable.ic_water_drop,
                    null,
                    null,
                    Snackbar.LENGTH_SHORT
                )
            }
            setText(
                prefManager.getIntVal(SharedPreferencesKey.DEFAULT_VOLUME, 100).toString()
            )
        }
        tietGoal = binding.tietGoal.apply {
            addTextChangedListener {
                if (it != null) viewModel.setGoal(if (it.isEmpty()) 0 else it.toString().toInt())
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showSnackBar(
                    null,
                    context.getString(R.string.goal_description),
                    R.drawable.ic_water_drop,
                    null,
                    null,
                    Snackbar.LENGTH_SHORT
                )
            }

            setText(recommendedGoal.toString())
        }

        btnDone.setOnClickListener {
            when {
                actvContainer.text.isNullOrEmpty() -> {
                    showAlert(
                        getString(R.string.warning),
                        getString(R.string.alert_message_missing_container)
                    ) { actvContainer.requestFocus() }
                }
                tietVolume.text.isNullOrEmpty() -> {
                    showAlert(
                        title = getString(R.string.warning),
                        message = getString(R.string.alert_message_missing_volume)
                    ) { tietVolume.focusAndShowKeyboard() }

                }
                tietGoal.text.isNullOrEmpty() -> {
                    showAlert(
                        title = getString(R.string.warning),
                        message = getString(R.string.alert_message_missing_goal)
                    ) { tietGoal.focusAndShowKeyboard() }
                }
                else -> {
                    val volume = if (tietVolume.text.toString().isEmpty()) 100
                    else tietVolume.text.toString().toInt()

                    val goal = if (tietGoal.text.toString().isEmpty()) 2000
                    else tietGoal.text.toString().toInt()

                    if (goal > recommendedGoal) {
                        showConfirmationDialog(
                            requireContext(),
                            layoutInflater,
                            DialogUtils.ConfirmationDialogConfig(
                                title = "Warning",
                                message = "You goal is higher than the recommended goal. Are you sure?",
                                positiveButtonTitle = "Yes",
                                negativeButtonTitle = "No",
                                listener = object : DialogUtils.AlertDialogListener {
                                    override fun onPositiveButtonClick() {
                                        saveAndNavigate(volume, goal)
                                    }

                                    override fun onNegativeButtonClick() {
                                        tietGoal.setText(recommendedGoal)
                                    }

                                    override fun onDismiss() {
                                    }
                                }
                            )
                        )
                    } else {
                        saveAndNavigate(volume, goal)
                    }
                }
            }
        }
    }

    override fun subscribeObservers() {
        lifecycleScope.launchWhenResumed {
            if (actvContainer.text != null) viewModel.setContainer(
                if (actvContainer.text!!.isEmpty()) "" else actvContainer.text.toString()
            )
            if (tietVolume.text != null) viewModel.setVolume(
                if (tietVolume.text!!.isEmpty()) 0 else tietVolume.text.toString().toInt()
            )
            if (tietGoal.text != null) viewModel.setGoal(
                if (tietGoal.text!!.isEmpty()) 0 else tietGoal.text.toString().toInt()
            )
            viewModel.isSubmitEnabled.collect {
                btnDone.isEnabled = it
            }
        }
    }

    private fun saveAndNavigate(volume: Int, goal: Int) {
        prefManager.setVal(
            SharedPreferencesKey.DEFAULT_CONTAINER,
            container
        )
        prefManager.setVal(
            SharedPreferencesKey.DEFAULT_VOLUME,
            volume
        )
        prefManager.setVal(SharedPreferencesKey.DEFAULT_GOAL, goal)
        prefManager.setVal(
            SharedPreferencesKey.IS_FIRST_STARTUP,
            false
        )
        // val extras = FragmentNavigatorExtras(tvAppName to "shared_element_container")
        parentFragment?.findNavController()?.navigate(
            OnboardingFragmentDirections.actionOnboardingFragmentToMainFragment()/*, extras*/
        )
    }

    private fun showAlert(title: String, message: String, callBack: () -> Any) {
        DialogUtils.showAlertDialog(
            requireContext(),
            layoutInflater,
            icon = R.drawable.ic_warning,
            title = title,
            message = message,
            btnTitle = getString(R.string.got_it),
            callBack = callBack
        )
    }
}

@HiltViewModel
class OnboardingSetupGameViewModel @Inject constructor() : ViewModelBase() {
    private val _weapon: MutableStateFlow<String> = MutableStateFlow("")
    private val _volume: MutableStateFlow<Int> = MutableStateFlow(0)
    private val _goal: MutableStateFlow<Int> = MutableStateFlow(0)

    fun setContainer(weapon: String) {
        _weapon.value = weapon
    }

    fun setVolume(volume: Int) {
        _volume.value = volume
    }

    fun setGoal(goal: Int) {
        _goal.value = goal
    }

    val isSubmitEnabled: Flow<Boolean> =
        combine(_weapon, _volume, _goal) { weapon, volume, goal ->
            val isCorrectWeapon = weapon.isNotEmpty()
            val isCorrectVolume = volume in listOf(
                100, 200, 300, 400, 500,
                600, 700, 800, 900, 1000
            ) // will be configurable in debug mode
            val isCorrectGoal = goal in 1000..6000

            return@combine isCorrectWeapon and isCorrectVolume and isCorrectGoal
        }
}