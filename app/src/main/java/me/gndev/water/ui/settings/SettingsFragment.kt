package me.gndev.water.ui.settings

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
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
import me.gndev.water.util.DialogUtils
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : FragmentBase<EmptyViewModel>(R.layout.settings_fragment) {
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

    @Inject
    lateinit var biometricService: BiometricService

    private var useBio: Boolean = false
    private var syncEnabled: Boolean = false
    private var container: String = Container.CUP
    private var volume: Int = 100

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
                    DialogUtils.showConfirmationDialog(
                        context, layoutInflater,
                        DialogUtils.ConfirmationDialogConfig(
                            title = getString(R.string.dialog_enable_biometric_title),
                            message = getString(R.string.dialog_enable_biometric_message),
                            positiveButtonTitle = getString(R.string.yes),
                            negativeButtonTitle = getString(R.string.no),
                            listener = object : DialogUtils.AlertDialogListener {
                                override fun onDismiss() {

                                }

                                override fun onPositiveButtonClick() {
                                    val bm = BiometricManager.from(context)
                                    when (val canAuthenticate =
                                        bm.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
                                        BiometricManager.BIOMETRIC_SUCCESS -> {
                                            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                                                .setTitle(getString(R.string.bio_prompt_title))
                                                .setNegativeButtonText(getString(R.string.cancel))
                                                .build()
                                            BiometricPrompt(
                                                this@SettingsFragment,
                                                ContextCompat.getMainExecutor(context),
                                                object : BiometricPrompt.AuthenticationCallback() {
                                                    override fun onAuthenticationError(
                                                        errorCode: Int,
                                                        errString: CharSequence
                                                    ) {
                                                        super.onAuthenticationError(
                                                            errorCode,
                                                            errString
                                                        )
                                                        swUseBio.isChecked = false
                                                    }

                                                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                                        super.onAuthenticationSucceeded(result)
                                                        tvUseBioSummary.text =
                                                            getString(R.string.setting_on)
                                                        prefManager.setVal(
                                                            SharedPreferencesKey.USE_BIO,
                                                            true
                                                        )
                                                    }

                                                    override fun onAuthenticationFailed() {
                                                        super.onAuthenticationFailed()
                                                        swUseBio.isChecked = false
                                                    }
                                                }).authenticate(promptInfo)
                                        }
                                    }
                                }

                                override fun onNegativeButtonClick() {
                                    tvUseBioSummary.text = getString(R.string.setting_off)
                                    prefManager.setVal(SharedPreferencesKey.USE_BIO, false)
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        swUseBio.isChecked = false
                                    }, 50)
                                }
                            }
                        )
                    )
                } else {
                    if (prefManager.getBooleanVal(SharedPreferencesKey.USE_BIO, true)) {
                        val bm = BiometricManager.from(context)
                        when (val canAuthenticate =
                            bm.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
                            BiometricManager.BIOMETRIC_SUCCESS -> {
                                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                                    .setTitle(getString(R.string.bio_prompt_title))
                                    .setNegativeButtonText(getString(R.string.cancel))
                                    .build()
                                BiometricPrompt(
                                    this@SettingsFragment,
                                    ContextCompat.getMainExecutor(context),
                                    object : BiometricPrompt.AuthenticationCallback() {
                                        override fun onAuthenticationError(
                                            errorCode: Int,
                                            errString: CharSequence
                                        ) {
                                            super.onAuthenticationError(
                                                errorCode,
                                                errString
                                            )
                                            swUseBio.isChecked = true
                                        }

                                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                            super.onAuthenticationSucceeded(result)
                                            tvUseBioSummary.text = getString(R.string.setting_off)
                                            prefManager.setVal(SharedPreferencesKey.USE_BIO, false)
                                        }

                                        override fun onAuthenticationFailed() {
                                            super.onAuthenticationFailed()
                                            swUseBio.isChecked = true
                                        }
                                    }).authenticate(promptInfo)
                            }
                        }
                    }
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