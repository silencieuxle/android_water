package me.gndev.water.service

import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import me.gndev.water.R
import me.gndev.water.core.constant.SharedPreferencesKey
import me.gndev.water.data.share_preferences.PrefManager
import me.gndev.water.util.DialogUtils
import java.util.concurrent.Executor
import javax.inject.Inject

class BiometricService @Inject constructor(
    private val prefManager: PrefManager
) {
    fun getPromptInfo(
        title: String,
        subTitle: String? = null,
        description: String? = null,
        negativeButtonText: String
    ): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subTitle)
            .setDescription(description)
            .setNegativeButtonText(negativeButtonText)
            .build()
    }

    fun getBiometricPrompt(
        executor: Executor,
        fragment: Fragment,
        listener: BiometricListener? = null
    ): BiometricPrompt {
        return BiometricPrompt(
            fragment,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    listener?.onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    listener?.onAuthenticationSucceeded(result)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    listener?.onAuthenticationFailed()
                }
            })

    }

    fun showEnableBioAuthDialog(
        context: Context,
        executor: Executor,
        fragment: Fragment
    ) {
        val bm = BiometricManager.from(context)
        val canAuthenticate = bm.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        val isHardwareAvailableAndEnrolled =
            !(canAuthenticate == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ||
                    canAuthenticate == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE) &&
                    (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS)

        if (isHardwareAvailableAndEnrolled) {
            DialogUtils.showAlert(
                context,
                context.getString(R.string.dialog_enable_biometric_title),
                context.getString(R.string.dialog_enable_biometric_message),
                object : DialogUtils.AlertDialogListener {
                    override fun onDismiss() {
                        // Do nothing
                    }

                    override fun onPositiveButtonClick() {
                        val promptInfo = getPromptInfo(
                            title = context.getString(R.string.bio_prompt_title),
                            negativeButtonText = context.getString(R.string.cancel)
                        )
                        getBiometricPrompt(executor, fragment, object : BiometricListener {
                            override fun onAuthenticationError(
                                errorCode: Int,
                                errString: CharSequence
                            ) {
                                Toast.makeText(context, R.string.bio_auth_error, Toast.LENGTH_SHORT)
                                    .show()
                                prefManager.setVal(SharedPreferencesKey.USE_BIO, false)
                            }

                            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                Toast.makeText(
                                    context,
                                    R.string.bio_auth_success,
                                    Toast.LENGTH_SHORT
                                ).show()
                                prefManager.setVal(SharedPreferencesKey.USE_BIO, true)
                            }

                            override fun onAuthenticationFailed() {
                                Toast.makeText(
                                    context,
                                    R.string.bio_auth_failed,
                                    Toast.LENGTH_SHORT
                                ).show()
                                prefManager.setVal(SharedPreferencesKey.USE_BIO, false)
                            }
                        }).authenticate(promptInfo)
                    }

                    override fun onNegativeButtonClick() {
                        prefManager.setVal(SharedPreferencesKey.USE_BIO, false)
                    }
                })
        } else {
            Toast.makeText(context, R.string.bio_auth_no_hardware, Toast.LENGTH_SHORT).show()
        }
    }

    interface BiometricListener {
        fun onAuthenticationError(errorCode: Int, errString: CharSequence)
        fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult)
        fun onAuthenticationFailed()
    }
}
