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
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import me.gndev.water_battle.R
import me.gndev.water_battle.core.constant.SharePreferences
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

    private var selectedWeapon: Int = 0

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
            selectedWeapon = weapon.value.toInt()
            actvDefaultWeapon.setText(weapon.text)
        }

        val weaponListAdapter =
            WeaponDropDownAdapter(
                requireContext(), R.layout.dropdown_item, listOf(
                    WeaponDropdownModel("Cup", Weapon.CUP.toString()),
                    WeaponDropdownModel("Bottle", Weapon.BOTTLE.toString()),
                    WeaponDropdownModel("Can", Weapon.CAN.toString()),
                    WeaponDropdownModel("Tank", Weapon.TANK.toString())
                )
            )
        actvDefaultWeapon.setAdapter(weaponListAdapter)
        actvDefaultWeapon.setSelection(0)

        btnDone.setOnClickListener {
            val defaultVolume = if (tietDefaultVolume.text.toString().isNullOrEmpty()) 100
            else tietDefaultVolume.text.toString().toInt()

            val defaultGoal = if (tietDefaultGoal.text.toString().isNullOrEmpty()) 2000
            else tietDefaultGoal.text.toString().toInt()

            prefManager.setVal(SharePreferences.DEFAULT_WEAPON, selectedWeapon)
            prefManager.setVal(SharePreferences.DEFAULT_VOLUME, defaultVolume)
            prefManager.setVal(SharePreferences.DEFAULT_GOAL, defaultGoal)

            val extras = FragmentNavigatorExtras(tvAppName to "shared_element_container")
            findNavController().navigate(
                FirstStartupFragmentDirections.actionFirstStartupFragmentToMainFragment(), extras
            )
        }
    }

    override fun subscribeObservers() {

    }
}