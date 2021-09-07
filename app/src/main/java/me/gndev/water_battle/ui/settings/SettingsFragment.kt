package me.gndev.water_battle.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import me.gndev.water_battle.R
import me.gndev.water_battle.core.model.EmptyViewModel
import me.gndev.water_battle.core.model.FragmentBase
import me.gndev.water_battle.databinding.SettingsFragmentBinding

@AndroidEntryPoint
class SettingsFragment : FragmentBase<EmptyViewModel>(R.layout.settings_fragment) {
    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupActionBar() {

    }

    override fun setupViews() {
    }

    override fun subscribeObservers() {
    }
}