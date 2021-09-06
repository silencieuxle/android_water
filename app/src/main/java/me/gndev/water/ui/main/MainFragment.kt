package me.gndev.water.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import dagger.hilt.android.AndroidEntryPoint
import me.gndev.water.R
import me.gndev.water.core.constant.SharePreferences
import me.gndev.water.core.constant.Weapon
import me.gndev.water.core.model.FragmentBase
import me.gndev.water.data.entity.Game
import me.gndev.water.data.entity.Turn
import me.gndev.water.data.repository.GameRepository
import me.gndev.water.data.repository.TurnRepository
import me.gndev.water.databinding.MainFragmentBinding
import me.gndev.water.util.DateTimeUtils
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : FragmentBase<MainViewModel>(R.layout.main_fragment) {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var btnHit: Button
    private lateinit var tvTotalVolume: TextView

    private lateinit var currentGame: Game

    private var defaultWeapon: Int = 0
    private var defaultVolume: Int = 0
    private var defaultGoal: Int = 0
    private var totalVolume: Int = 0
    private var maxVolume: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defaultWeapon = prefManager.getIntVal(SharePreferences.DEFAULT_WEAPON, Weapon.CUP)
        defaultVolume = prefManager.getIntVal(SharePreferences.DEFAULT_VOLUME, 100)
        defaultGoal = prefManager.getIntVal(SharePreferences.DEFAULT_GOAL, 2000)
        maxVolume = prefManager.getIntVal(SharePreferences.DEFAULT_MAX_VOLUME, 3700)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        btnHit = binding.btnHit
        tvTotalVolume = binding.tvTotalVolume

        btnHit.setOnClickListener {
            saveGameTurn()
            tvTotalVolume.text = totalVolume.toString()
        }
    }

    override fun subscribeObservers() {
        mainActivityViewModel.currentGame.observe(viewLifecycleOwner, {
            it?.let {
                currentGame = it
                totalVolume = currentGame.score
                tvTotalVolume.text = currentGame.score.toString()
            }
        })
    }

    override fun setupActionBar() {
        actionBar?.hide()
    }

    private fun saveGameTurn() {
        totalVolume += defaultVolume
        viewModel.saveTurn(Turn(volume = defaultVolume, gameId = currentGame.id))
        mainActivityViewModel.updateCurrentGame(totalVolume)

        if (totalVolume >= defaultGoal && totalVolume < defaultGoal + 200) {
            Toast.makeText(
                requireContext(),
                "You reached the goal of $defaultGoal!",
                Toast.LENGTH_LONG
            ).show()
        }
        if (totalVolume > defaultGoal + 200){
            Toast.makeText(
                requireContext(),
                "You drink too much, the goal is $defaultGoal!",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}