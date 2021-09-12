package me.gndev.water.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint
import me.gndev.water.R
import me.gndev.water.core.base.FragmentBase
import me.gndev.water.core.constant.Container
import me.gndev.water.core.constant.SharedPreferencesKey
import me.gndev.water.core.model.EmptyViewModel
import me.gndev.water.databinding.SettingsFragmentBinding
import me.gndev.water.service.BiometricService
import java.util.concurrent.Executor
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment @Inject constructor(
    private val biometricService: BiometricService
) : FragmentBase<EmptyViewModel>(R.layout.settings_fragment) {
    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var swUseBio: SwitchMaterial
    private lateinit var swSync: SwitchMaterial
    private lateinit var tvUseBioSummary: TextView
    private lateinit var tvSyncSummary: TextView
    private lateinit var clContainer: View
    private lateinit var clVolume: View
    private lateinit var tvContainerSummary: TextView
    private lateinit var tvVolumeSummary: TextView

    private var useBio: Boolean = false
    private var syncEnabled: Boolean = false
    private var container: String = Container.CUP
    private var volume: Int = 100

    @Inject
    lateinit var executor: Executor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        setVars()
        return binding.root
    }

    override fun setupActionBar() {

    }

    override fun setupViews() {
        swUseBio = binding.swAppSettingUseBio.apply {
            isChecked = useBio
            setOnClickListener {
                if (swUseBio.isChecked) {
                    Toast.makeText(requireContext(), "swUseBio", Toast.LENGTH_SHORT).show()
                    biometricService.showEnableBioAuthDialog(
                        requireContext(),
                        ContextCompat.getMainExecutor(requireContext()),
                        this@SettingsFragment
                    )
                    tvUseBioSummary.text = getString(R.string.setting_on)
                } else {
                    Toast.makeText(requireContext(), "swUseBio", Toast.LENGTH_SHORT).show()
                    tvUseBioSummary.text = getString(R.string.setting_off)
                    prefManager.setVal(SharedPreferencesKey.USE_BIO, true)
                }
            }
        }
        swSync = binding.swAppSettingSync.apply {
            isChecked = syncEnabled
            setOnClickListener {
                if (swSync.isChecked) {
                    Toast.makeText(requireContext(), "swSync", Toast.LENGTH_SHORT).show()
                    tvSyncSummary.text = getString(R.string.setting_on)
                } else {
                    Toast.makeText(requireContext(), "swSync", Toast.LENGTH_SHORT).show()
                    tvSyncSummary.text = getString(R.string.setting_off)
                }
            }
        }
        tvUseBioSummary = binding.tvAppSettingsUseBioSummary.apply {
            text = if (useBio) getString(R.string.setting_on) else getString(R.string.setting_off)
        }
        tvSyncSummary = binding.tvAppSettingsSyncSummary.apply {
            text =
                if (syncEnabled) getString(R.string.setting_on) else getString(R.string.setting_off)
        }
        clContainer = binding.clGameSettingsContainer.apply {
            setOnClickListener {
                Toast.makeText(requireContext(), "clContainer", Toast.LENGTH_SHORT).show()
            }
        }
        clVolume = binding.clGameSettingsVolume.apply {
            setOnClickListener {
                Toast.makeText(requireContext(), "clVolume", Toast.LENGTH_SHORT).show()
            }
        }
        tvContainerSummary = binding.tvGameSettingsContainerSummary.apply {
            text = container
        }
        tvVolumeSummary = binding.tvGameSettingsVolumeSummary.apply {
            text = volume.toString()
        }
    }

    override fun subscribeObservers() {
    }

    private fun setVars() {
        useBio = prefManager.getBooleanVal(SharedPreferencesKey.USE_BIO, false)
        syncEnabled = prefManager.getBooleanVal(SharedPreferencesKey.SYNC_ENABLED, false)
        container = prefManager.getStringVal(SharedPreferencesKey.DEFAULT_CONTAINER, Container.CUP)
        volume = prefManager.getIntVal(SharedPreferencesKey.DEFAULT_VOLUME, 100)
    }
}