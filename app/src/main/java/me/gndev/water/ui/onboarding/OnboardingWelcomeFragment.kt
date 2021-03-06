package me.gndev.water.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.gndev.water.R
import me.gndev.water.core.model.EmptyViewModel
import me.gndev.water.core.base.FragmentBase
import me.gndev.water.databinding.OnboardingWelcomeFragmentBinding

class OnboardingWelcomeFragment : FragmentBase<EmptyViewModel>(R.layout.onboarding_welcome_fragment) {
    private var _binding: OnboardingWelcomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OnboardingWelcomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupActionBar() {
    }

    override fun setupViews() {
    }

    override fun subscribeObservers() {
    }
}