package me.gndev.water.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
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
import me.gndev.water.core.constant.SharedPreferencesKey
import me.gndev.water.core.constant.UserSex
import me.gndev.water.databinding.OnboardingSetupProfileFragmentBinding
import me.gndev.water.util.DialogUtils
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingSetupProfileFragment @Inject constructor() :
    FragmentBase<OnboardingSetupProfileViewModel>(R.layout.onboarding_setup_profile_fragment) {
    private var _binding: OnboardingSetupProfileFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var tietName: TextInputEditText
    private lateinit var tietAge: TextInputEditText
    private lateinit var tietHeight: TextInputEditText
    private lateinit var tietWeight: TextInputEditText
    private lateinit var btnDone: MaterialButton
    private lateinit var rbMale: RadioButton
    private lateinit var btnSexHelp: MaterialButton

    private var minAge: Int = 0
    private var minHeight: Int = 0
    private var minWeight: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OnboardingSetupProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupActionBar() {
    }

    override fun setupViews() {
        tietName = binding.tietName.apply {
            setText(prefManager.getStringVal(SharedPreferencesKey.USER_NAME, ""))
            addTextChangedListener {
                if (it != null) viewModel.setName(if (it.isEmpty()) "" else it.toString())
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showSnackBar(
                    null,
                    context.getString(R.string.name_description),
                    R.drawable.ic_water_drop,
                    null,
                    null,
                    Snackbar.LENGTH_SHORT
                )
            }
        }
        tietAge = binding.tietAge.apply {
            setText(prefManager.getIntVal(SharedPreferencesKey.USER_AGE, 9).toString())
            addTextChangedListener {
                if (it != null) viewModel.setAge(if (it.isEmpty()) 0 else it.toString().toInt())
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showSnackBar(
                    null,
                    context.getString(R.string.age_description),
                    R.drawable.ic_water_drop,
                    null,
                    null,
                    Snackbar.LENGTH_SHORT
                )
            }
        }
        tietHeight = binding.tietHeight.apply {
            setText(prefManager.getIntVal(SharedPreferencesKey.USER_HEIGHT, 50).toString())
            addTextChangedListener {
                if (it != null) viewModel.setHeight(if (it.isEmpty()) 0 else it.toString().toInt())
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showSnackBar(
                    null,
                    context.getString(R.string.height_description),
                    R.drawable.ic_water_drop,
                    null,
                    null,
                    Snackbar.LENGTH_SHORT
                )
            }
        }
        tietWeight = binding.tietWeight.apply {
            setText(prefManager.getIntVal(SharedPreferencesKey.USER_WEIGHT, 20).toString())
            addTextChangedListener {
                if (it != null) viewModel.setWeight(if (it.isEmpty()) 0 else it.toString().toInt())
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showSnackBar(
                    null,
                    context.getString(R.string.weight_description),
                    R.drawable.ic_water_drop,
                    null,
                    null,
                    Snackbar.LENGTH_SHORT
                )
            }
        }
        rbMale = binding.rbMale
        btnDone = binding.btnDone.apply {
            setOnClickListener {
                prefManager.setVal(SharedPreferencesKey.USER_NAME, tietName.text.toString())
                prefManager.setVal(
                    SharedPreferencesKey.USER_SEX,
                    if (rbMale.isChecked) UserSex.MALE else UserSex.FEMALE
                )
                prefManager.setVal(SharedPreferencesKey.USER_AGE, tietAge.text.toString().toInt())
                prefManager.setVal(
                    SharedPreferencesKey.USER_HEIGHT,
                    tietHeight.text.toString().toInt()
                )
                prefManager.setVal(
                    SharedPreferencesKey.USER_WEIGHT,
                    tietWeight.text.toString().toInt()
                )

                (parentFragment as OnboardingFragment).updateViewPager(2)
            }
        }

        btnSexHelp = binding.btnSexHelp.apply {
            setOnClickListener {
                val view = layoutInflater.inflate(R.layout.information_bottom_sheet, null)
                view.findViewById<TextView>(R.id.tv_title).text =
                    context.getString(R.string.sex_help_title)
                view.findViewById<TextView>(R.id.tv_message).text =
                    context.getString(R.string.sex_help_summary)
                DialogUtils.getBottomDialog(
                    requireContext(),
                    view
                ).show()
            }
        }
    }

    override fun subscribeObservers() {
        lifecycleScope.launchWhenResumed {
            if (tietName.text != null) viewModel.setName(
                if (tietName.text!!.isEmpty()) "" else tietName.text.toString()
            )
            if (tietAge.text != null) viewModel.setAge(
                if (tietAge.text!!.isEmpty()) 0 else tietAge.text.toString().toInt()
            )
            if (tietHeight.text != null) viewModel.setHeight(
                if (tietHeight.text!!.isEmpty()) 0 else tietHeight.text.toString().toInt()
            )
            if (tietWeight.text != null) viewModel.setWeight(
                if (tietWeight.text!!.isEmpty()) 0 else tietWeight.text.toString().toInt()
            )
            viewModel.isSubmitEnabled.collect {
                btnDone.isEnabled = it
            }
        }
    }
}

@HiltViewModel
class OnboardingSetupProfileViewModel @Inject constructor() : ViewModelBase() {
    private val _name: MutableStateFlow<String> = MutableStateFlow("")
    private val _age: MutableStateFlow<Int> = MutableStateFlow(0)
    private val _height: MutableStateFlow<Int> = MutableStateFlow(0)
    private val _weight: MutableStateFlow<Int> = MutableStateFlow(0)

    fun setName(name: String) {
        _name.value = name
    }

    fun setAge(age: Int) {
        _age.value = age
    }

    fun setHeight(height: Int) {
        _height.value = height
    }

    fun setWeight(weight: Int) {
        _weight.value = weight
    }

    val isSubmitEnabled: Flow<Boolean> =
        combine(_name, _age, _height, _weight) { name, age, height, weight ->
            val regexName = "[A-Za-z\\s]+" // name contain alphabet characters and spaces only
            val isCorrectName = name.matches(regexName.toRegex())
            val isCorrectAge = age in 9..130 // will be configurable in debug mode
            val isCorrectHeight = height in 50..260 // will be configurable in debug mode
            val isCorrectWeight = weight in 20..600 // will be configurable in debug mode
            return@combine isCorrectName and isCorrectAge and isCorrectHeight and isCorrectWeight
        }
}