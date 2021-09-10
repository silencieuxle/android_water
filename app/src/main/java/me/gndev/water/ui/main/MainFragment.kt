package me.gndev.water.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import me.gndev.water.R
import me.gndev.water.core.constant.ActiveLevel
import me.gndev.water.core.constant.SharedPreferencesKey
import me.gndev.water.core.constant.UserSex
import me.gndev.water.core.constant.Container
import me.gndev.water.core.base.FragmentBase
import me.gndev.water.data.entity.Game
import me.gndev.water.data.entity.Turn
import me.gndev.water.databinding.MainFragmentBinding
import me.gndev.water.ui.onboarding.WeaponDropDownAdapter
import me.gndev.water.ui.onboarding.ContainerDropdownModel

@AndroidEntryPoint
class MainFragment : FragmentBase<MainViewModel>(R.layout.main_fragment) {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var btnTakeAShot: MaterialButton
    private lateinit var btnSettings: MaterialButton
    private lateinit var actvContainer: AutoCompleteTextView
    private lateinit var tietVolume: TextInputEditText
    private lateinit var tvScore: TextView
    private lateinit var currentGame: Game

    private var container: String = Container.CUP
    private var volume: Int = 0
    private var goal: Int = 0
    private var score: Int = 0
    private var sex: String = UserSex.MALE
    private var activeLevel: String = ActiveLevel.MODERATE
    private var maxVolume: Int = 0
    private var isFirstStartup: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setVarValue()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Force close the activity on MainFragment
    override fun overrideBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }

    override fun setupViews() {
        btnTakeAShot = binding.btnTakeAShot
        btnSettings = binding.btnSettings
        tvScore = binding.tvScore
        tietVolume = binding.tietVolume
        actvContainer = binding.actvContainer

        val weaponListAdapter =
            WeaponDropDownAdapter(
                requireContext(), R.layout.dropdown_item, listOf(
                    ContainerDropdownModel(Container.CUP, Container.CUP),
                    ContainerDropdownModel(Container.BOTTLE, Container.BOTTLE),
                    ContainerDropdownModel(Container.CAN, Container.CAN),
                    ContainerDropdownModel(Container.TANK, Container.TANK)
                )
            )
        actvContainer.setAdapter(weaponListAdapter)
        actvContainer.setText(container, false)
        actvContainer.setDropDownBackgroundDrawable(
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_dropdown)
        )
        actvContainer.setOnItemClickListener { adapterView, _, i, _ ->
            val selectedItem = adapterView.getItemAtPosition(i) as ContainerDropdownModel
            container = selectedItem.value
            actvContainer.setText(selectedItem.text)
        }
        tietVolume.setText(volume.toString()) // passing Int to setText will cause the view to find the resource Id

        btnSettings.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToSettingsFragment())
        }

        btnTakeAShot.setOnClickListener {
            saveGameTurn()
            tvScore.text = score.toString()
            prefManager.setVal(
                SharedPreferencesKey.LAST_HIT_VOLUME,
                tietVolume.text.toString().toInt()
            )
        }
    }

    override fun subscribeObservers() {
        mainActivityViewModel.currentGame.observe(viewLifecycleOwner, {
            it?.let {
                currentGame = it
                score = currentGame.score
                tvScore.text = if (score > 0) currentGame.score.toString() else getString(
                    R.string.no_score_message
                )
            }
        })

        prefManager.registerOnChangeListener { _, _ ->
            setVarValue()
        }
    }

    override fun setupActionBar() {
        actionBar?.hide()
    }

    private fun saveGameTurn() {
        score += volume
        viewModel.saveTurn(Turn(weapon = container, volume = volume, gameId = currentGame.id))
        mainActivityViewModel.updateCurrentGame(score)

        if (score >= goal && score < goal + 200) {
            Toast.makeText(
                requireContext(),
                "You reached the goal of $goal!",
                Toast.LENGTH_LONG
            ).show()
        }
        if (score > goal + 200) {
            Toast.makeText(
                requireContext(),
                "You drink too much, the goal is $goal!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setVarValue() {
        container = prefManager.getStringVal(SharedPreferencesKey.DEFAULT_CONTAINER, Container.CUP)
        volume = prefManager.getIntVal(SharedPreferencesKey.DEFAULT_VOLUME, 100)
        goal = prefManager.getIntVal(SharedPreferencesKey.RECOMMENDED_GOAL, 2000)
        sex = prefManager.getStringVal(SharedPreferencesKey.USER_SEX, UserSex.MALE)
        activeLevel = prefManager.getStringVal(SharedPreferencesKey.ACTIVE_LEVEL, UserSex.MALE)
        isFirstStartup = prefManager.getBooleanVal(SharedPreferencesKey.IS_FIRST_STARTUP, true)
    }

    private fun getSuggestedMaxVolume(): Int {
        return if (sex == UserSex.MALE) {
            1
        } else {
            2
        }
    }

}