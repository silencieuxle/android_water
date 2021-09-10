package me.gndev.water_battle.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import me.gndev.water_battle.R
import me.gndev.water_battle.core.base.FragmentBase
import me.gndev.water_battle.core.base.ViewModelBase
import me.gndev.water_battle.core.constant.SharedPreferencesKey
import me.gndev.water_battle.core.constant.Weapon
import me.gndev.water_battle.databinding.OnboardingSetupBattleSettingsFragmentBinding
import me.gndev.water_battle.util.CommonExtensions.focusAndShowKeyboard
import me.gndev.water_battle.util.DialogUtils
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingSetupBattleSettingsFragment :
    FragmentBase<OnboardingSetupBattleSettingsViewModel>(R.layout.onboarding_setup_battle_settings_fragment) {
    private var _binding: OnboardingSetupBattleSettingsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var btnDone: Button
    private lateinit var actvWeapon: AutoCompleteTextView
    private lateinit var tietVolume: TextInputEditText
    private lateinit var tietGoal: TextInputEditText

    private var weapon: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OnboardingSetupBattleSettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupActionBar() {
        actionBar?.hide()
    }

    override fun setupViews() {
        btnDone = binding.btnDone

        val weaponListAdapter =
            WeaponDropDownAdapter(
                requireContext(), R.layout.dropdown_item, kotlin.collections.listOf(
                    WeaponDropdownModel("Cup", Weapon.CUP),
                    WeaponDropdownModel(
                        "Bottle",
                        Weapon.BOTTLE
                    ),
                    WeaponDropdownModel("Can", Weapon.CAN),
                    WeaponDropdownModel("Tank", Weapon.TANK)
                )
            )
        actvWeapon = binding.actvWeapon.apply {
            addTextChangedListener {
                if (it != null) viewModel.setWeapon(if (it.isEmpty()) "" else it.toString())
            }
            setText(
                prefManager.getStringVal(SharedPreferencesKey.DEFAULT_WEAPON, Weapon.CUP), false
            )
            setDropDownBackgroundDrawable(
                androidx.core.content.ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bg_dropdown
                )
            )
            setOnItemClickListener { adapterView, _, i, _ ->
                val selectedItem = adapterView.getItemAtPosition(i) as WeaponDropdownModel
                weapon = selectedItem.value
                actvWeapon.setText(selectedItem.text)
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showSnackBar(
                    context.getString(R.string.weapon_description),
                    Toast.LENGTH_SHORT
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
                    context.getString(R.string.volume_description),
                    Toast.LENGTH_SHORT
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
                    context.getString(R.string.goal_description),
                    Toast.LENGTH_SHORT
                )
            }
        }

        btnDone.setOnClickListener {
            when {
                actvWeapon.text.isNullOrEmpty() -> {
                    showAlert(
                        getString(R.string.warning),
                        getString(R.string.alert_message_missing_weapon)
                    ) { actvWeapon.requestFocus() }
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

                    if (goal > 3700) {
                        //showConfirmationDialog()
                    }

                    prefManager.setVal(SharedPreferencesKey.DEFAULT_WEAPON, weapon)
                    prefManager.setVal(SharedPreferencesKey.DEFAULT_VOLUME, volume)
                    prefManager.setVal(SharedPreferencesKey.DEFAULT_GOAL, goal)
                    prefManager.setVal(SharedPreferencesKey.IS_FIRST_STARTUP, false)
                    // val extras = FragmentNavigatorExtras(tvAppName to "shared_element_container")
                    findNavController().navigate(
                        OnboardingFragmentDirections.actionOnboardingFragmentToMainFragment()/*, extras*/
                    )
                }
            }
        }
    }

    override fun subscribeObservers() {
        lifecycleScope.launchWhenResumed {
            viewModel.isSubmitEnabled.collect {
                btnDone.isEnabled = it
            }
        }
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
class OnboardingSetupBattleSettingsViewModel @Inject constructor() : ViewModelBase() {
    private val _weapon: MutableStateFlow<String> = MutableStateFlow("")
    private val _volume: MutableStateFlow<Int> = MutableStateFlow(0)
    private val _goal: MutableStateFlow<Int> = MutableStateFlow(0)

    fun setWeapon(weapon: String) {
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
            val isCorrectGoal = goal in listOf(
                1000, 2000, 3000,
                4000, 5000, 6000
            ) // will be configurable in debug mode
            return@combine isCorrectWeapon and isCorrectVolume and isCorrectGoal
        }
}