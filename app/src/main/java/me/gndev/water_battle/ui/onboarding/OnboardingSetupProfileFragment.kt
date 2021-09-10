package me.gndev.water_battle.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
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
import me.gndev.water_battle.databinding.OnboardingSetupProfileFragmentBinding
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
            addTextChangedListener {
                if (it != null) viewModel.setName(if (it.isEmpty()) "" else it.toString())
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showSnackBar(
                    context.getString(R.string.name_description),
                    Toast.LENGTH_SHORT
                )
            }
        }
        tietAge = binding.tietAge.apply {
            addTextChangedListener {
                if (it != null) viewModel.setAge(if (it.isEmpty()) 0 else it.toString().toInt())
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showSnackBar(
                    context.getString(R.string.age_description),
                    Toast.LENGTH_SHORT
                )
            }
        }
        tietHeight = binding.tietHeight.apply {
            addTextChangedListener {
                if (it != null) viewModel.setHeight(if (it.isEmpty()) 0 else it.toString().toInt())
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showSnackBar(
                    context.getString(R.string.height_description),
                    Toast.LENGTH_SHORT
                )
            }
        }
        tietWeight = binding.tietWeight.apply {
            addTextChangedListener {
                if (it != null) viewModel.setWeight(if (it.isEmpty()) 0 else it.toString().toInt())
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showSnackBar(
                    context.getString(R.string.weight_description),
                    Toast.LENGTH_SHORT
                )
            }
        }

        btnDone = binding.btnDone
    }

    override fun subscribeObservers() {
        lifecycleScope.launchWhenResumed {
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
            val isCorrectAge = age in 7..130 // will be configurable in debug mode
            val isCorrectHeight = height in 50..260 // will be configurable in debug mode
            val isCorrectWeight = weight in 20..600 // will be configurable in debug mode
            return@combine isCorrectName and isCorrectAge and isCorrectHeight and isCorrectWeight
        }
}