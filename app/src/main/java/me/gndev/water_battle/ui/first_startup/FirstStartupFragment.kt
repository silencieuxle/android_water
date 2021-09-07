package me.gndev.water_battle.ui.first_startup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import me.gndev.water_battle.R
import me.gndev.water_battle.core.constant.SharedPreferencesKey
import me.gndev.water_battle.core.constant.Weapon
import me.gndev.water_battle.core.model.FragmentBase
import me.gndev.water_battle.databinding.FirstStartupFragmentBinding
import javax.inject.Inject

@AndroidEntryPoint
class FirstStartupFragment @Inject constructor() :
    FragmentBase<FirstStartupViewModel>(R.layout.first_startup_fragment) {
    private var _binding: FirstStartupFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var btnDone: Button
    private lateinit var actvDefaultWeapon: AutoCompleteTextView
    private lateinit var tietDefaultVolume: TextInputEditText
    private lateinit var tietDefaultGoal: TextInputEditText
    private lateinit var tvAppName: TextView

    private var selectedWeapon: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialElevationScale(/* growing= */ false)
        reenterTransition = MaterialElevationScale(/* growing= */ true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FirstStartupFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupActionBar() {
        actionBar?.hide()
    }

    override fun setupViews() {
        tvAppName = binding.tvAppName
        btnDone = binding.btnDone
        tietDefaultVolume = binding.tietDefaultVolume
        tietDefaultGoal = binding.tietDefaultGoal
        actvDefaultWeapon = binding.actvDefaultWeapon

        tietDefaultGoal.doAfterTextChanged {
            if (!it.isNullOrEmpty()) {
                if (it.toString().toInt() > 3700) {
                    Toast.makeText(
                        requireContext(),
                        "Woah, slow down, that's $it and it's too much. Are you sure?",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        actvDefaultWeapon.setOnItemClickListener { adapterView, _, i, _ ->
            val weapon = adapterView.getItemAtPosition(i) as WeaponDropdownModel
            selectedWeapon = weapon.value
            actvDefaultWeapon.setText(weapon.text)
        }

        val weaponListAdapter =
            WeaponDropDownAdapter(
                requireContext(), R.layout.dropdown_item, listOf(
                    WeaponDropdownModel("Cup", Weapon.CUP),
                    WeaponDropdownModel("Bottle", Weapon.BOTTLE),
                    WeaponDropdownModel("Can", Weapon.CAN),
                    WeaponDropdownModel("Tank", Weapon.TANK)
                )
            )
        actvDefaultWeapon.setAdapter(weaponListAdapter)
        actvDefaultWeapon.setText(Weapon.CUP, false)

        btnDone.setOnClickListener {
            val defaultVolume = if (tietDefaultVolume.text.toString().isEmpty()) 100
            else tietDefaultVolume.text.toString().toInt()

            val defaultGoal = if (tietDefaultGoal.text.toString().isEmpty()) 2000
            else tietDefaultGoal.text.toString().toInt()

            prefManager.setVal(SharedPreferencesKey.DEFAULT_WEAPON, selectedWeapon)
            prefManager.setVal(SharedPreferencesKey.DEFAULT_VOLUME, defaultVolume)
            prefManager.setVal(SharedPreferencesKey.DEFAULT_GOAL, defaultGoal)
            prefManager.setVal(SharedPreferencesKey.IS_FIRST_STARTUP, false)
            // val extras = FragmentNavigatorExtras(tvAppName to "shared_element_container")
            findNavController().navigate(
                FirstStartupFragmentDirections.actionFirstStartupFragmentToMainFragment()/*, extras*/
            )
        }
    }

    override fun subscribeObservers() {

    }
}