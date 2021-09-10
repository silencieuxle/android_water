package me.gndev.water.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import me.gndev.water.R
import me.gndev.water.core.model.EmptyViewModel
import me.gndev.water.core.base.FragmentBase
import me.gndev.water.databinding.OnboardingFragmentBinding

@AndroidEntryPoint
class OnboardingFragment :
    FragmentBase<EmptyViewModel>(R.layout.onboarding_fragment) {
    private var _binding: OnboardingFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewAdapter: OnboardingFragmentStateAdapter
    private lateinit var vpOnboarding: ViewPager2
    /*private lateinit var ivFirstStep: ImageView
    private lateinit var ivSecondStep: ImageView
    private lateinit var ivThirdStep: ImageView*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OnboardingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val welcomeFragment = OnboardingWelcomeFragment()
        val setupProfileFragment = OnboardingSetupProfileFragment()
        val setupBattleSettingsFragment = OnboardingSetupBattleSettingsFragment()
        val childFragments = listOf(
            welcomeFragment,
            setupProfileFragment,
            setupBattleSettingsFragment
        )

        val viewAdapter = OnboardingFragmentStateAdapter(this, childFragments)
        vpOnboarding = view.findViewById(R.id.vp_onboarding)
        vpOnboarding.apply {
            adapter = viewAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    /*updateStepper(position)*/
                    // Disable swipe if navigated to step 2
                    if (position == 1) isUserInputEnabled = false
                }
            })
            // isUserInputEnabled = false;
            // setPageTransformer(FadePageTransformer())
            // Setting the overscroll mode in the viewpager xml won't disable the overscroll effect
            // Refer to this https://stackoverflow.com/a/56942231/4138362
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
    }

    override fun setupActionBar() {
        actionBar?.hide()
    }

    override fun setupViews() {
        /*ivFirstStep = binding.ivFirstStep
        ivSecondStep = binding.ivSecondStep
        ivThirdStep = binding.ivThirdStep*/
    }

    override fun subscribeObservers() {
    }

    fun updateViewPager(position: Int) {
        vpOnboarding.currentItem = position
    }

    /*private fun updateStepper(index: Int) {
        when (index) {
            0 -> {
                ivFirstStep.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.purpleDarker)
                )
                ivSecondStep.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                ivThirdStep.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
            }
            1 -> {
                ivSecondStep.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.purpleDarker)
                )
                ivFirstStep.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                ivThirdStep.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
            }
            2 -> {
                ivThirdStep.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.purpleDarker)
                )
                ivFirstStep.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                ivSecondStep.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
            }
        }
    }*/
}

class OnboardingFragmentStateAdapter(
    fragment: Fragment,
    private val childFragments: List<Fragment>
) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = childFragments.count()

    override fun createFragment(position: Int): Fragment {
        return childFragments[position]
    }
}

/*
class FadePageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        if (position <= -1.0F || position >= 1.0F) {
            view.translationX = view.width * position;
            view.alpha = 0.0F;
        } else if (position == 0.0F) {
            view.translationX = view.width * position;
            view.alpha = 1.0F;
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            view.translationX = view.width * -position;
            view.alpha = 1.0F - abs(position);
        }
    }
}*/
