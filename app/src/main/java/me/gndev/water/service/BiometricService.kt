package me.gndev.water.service

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import me.gndev.water.R
import me.gndev.water.data.share_preferences.PrefManager
import java.util.concurrent.Executor
import javax.inject.Inject

class BiometricService @Inject constructor(private val prefManager: PrefManager) {
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

    fun showBioAuthDialog(context: Context, executor: Executor, fragment: Fragment): Int {
        val bm = BiometricManager.from(context)
        return when (val canAuthenticate =
            bm.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                var authResult = 101
                val promptInfo = getPromptInfo(
                    title = context.getString(R.string.bio_prompt_title),
                    negativeButtonText = context.getString(R.string.cancel)
                )
                BiometricPrompt(
                    fragment,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            super.onAuthenticationError(errorCode, errString)
                            authResult = if (errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
                                BioAuthResult.CANCELED
                            } else {
                                BioAuthResult.ERROR
                            }
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            authResult = BioAuthResult.SUCCEEDED
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            authResult = BioAuthResult.FAILED
                        }
                    }).authenticate(promptInfo)
                return authResult
            }
            else -> canAuthenticate
        }
    }

    fun returnData(result: Int) {

    }

    interface BiometricListener {
        fun onAuthenticationError(errorCode: Int, errString: CharSequence)
        fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult)
        fun onAuthenticationFailed()
    }

    object BioAuthResult {
        const val SUCCEEDED = 101
        const val FAILED = 102
        const val ERROR = 103
        const val CANCELED = 104
    }
}
