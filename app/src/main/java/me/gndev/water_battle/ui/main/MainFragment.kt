package me.gndev.water_battle.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import dagger.hilt.android.AndroidEntryPoint
import me.gndev.water_battle.R
import me.gndev.water_battle.core.constant.SharePreferences
import me.gndev.water_battle.core.constant.Weapon
import me.gndev.water_battle.core.model.FragmentBase
import me.gndev.water_battle.data.entity.Game
import me.gndev.water_battle.data.entity.Turn
import me.gndev.water_battle.databinding.MainFragmentBinding

@AndroidEntryPoint
class MainFragment : FragmentBase<MainViewModel>(R.layout.main_fragment) {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var btnHit: Button
    private lateinit var tvTotalVolume: TextView

    private lateinit var currentGame: Game

    private var weapon: Int = 0
    private var volume: Int = 0
    private var goal: Int = 0
    private var score: Int = 0
    private var maxVolume: Int = 0
    private var isFirstStartup: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weapon = prefManager.getIntVal(SharePreferences.DEFAULT_WEAPON, Weapon.CUP)
        volume = prefManager.getIntVal(SharePreferences.DEFAULT_VOLUME, 100)
        goal = prefManager.getIntVal(SharePreferences.DEFAULT_GOAL, 2000)
        maxVolume = prefManager.getIntVal(SharePreferences.DEFAULT_MAX_VOLUME, 3700)
        isFirstStartup = prefManager.getBooleanVal(SharePreferences.IS_FIRST_STARTUP, true)
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
        if (!isFirstStartup) {
            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        requireActivity().finish()
                    }
                })
        }
        super.overrideBackButton()
    }

    override fun setupViews() {
        btnHit = binding.btnHit
        tvTotalVolume = binding.tvTotalVolume

        btnHit.setOnClickListener {
            if (isFirstStartup) {
                prefManager.setVal(SharePreferences.IS_FIRST_STARTUP, false)
            }
            saveGameTurn()
            tvTotalVolume.text = score.toString()
        }
    }

    override fun subscribeObservers() {
        mainActivityViewModel.currentGame.observe(viewLifecycleOwner, {
            it?.let {
                currentGame = it
                score = currentGame.score
                tvTotalVolume.text = if (score > 0) currentGame.score.toString() else getString(
                    R.string.no_score_message
                )
            }
        })
    }

    override fun setupActionBar() {
        actionBar?.hide()
    }

    private fun saveGameTurn() {
        score += volume
        viewModel.saveTurn(Turn(weapon = weapon, volume = volume, gameId = currentGame.id))
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
}