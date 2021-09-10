package me.gndev.water_battle.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.gndev.water_battle.R

object DialogUtils {
    fun showAlert(
        context: Context,
        config: AlertDialogConfig
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(config.title)
        builder.setMessage(config.message)

        if (config.positiveButtonConfig.isVisible) {
            builder.setPositiveButton(config.positiveButtonConfig.title) { _, _ ->
                config.positiveButtonConfig.onClick()
            }
        }

        if (config.negativeButtonConfig.isVisible) {
            builder.setNegativeButton(config.negativeButtonConfig.title) { _, _ ->
                config.negativeButtonConfig.onClick()
            }
        }
        builder.show()
    }

    fun showAlert(
        context: Context,
        title: String,
        message: String,
        listener: AlertDialogListener? = null
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            listener?.onPositiveButtonClick()
        }

        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
            listener?.onNegativeButtonClick()
        }
        builder.show()
    }

    fun showAlertDialog(
        context: Context,
        layoutInflater: LayoutInflater,
        @DrawableRes icon: Int? = null,
        title: String,
        message: String,
        btnTitle: String? = null,
        callBack: () -> Any?
    ) {
        val view = layoutInflater.inflate(R.layout.alert_dialog, null)
        val dialog = getAlert(context, view)
        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val tvContent = view.findViewById<TextView>(R.id.tv_message)
        val btnContinue = view.findViewById<TextView>(R.id.btn_continue)
        if (btnTitle != null) {
            btnContinue.text = btnTitle
        }

        if (icon != null) {
            tvTitle.setCompoundDrawables(ContextCompat.getDrawable(context, icon), null, null, null)
        }

        tvTitle.text = title
        tvContent.text = message
        btnContinue.setOnClickListener {
            dialog.dismiss()
            callBack()
        }
        dialog.show()
    }

    fun getAlert(context: Context, view: View): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        val alertDialog: AlertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return alertDialog
    }

    fun getProgressDialog(context: Context, inflater: LayoutInflater): AlertDialog {
        val builder = AlertDialog.Builder(context)

        val dialogView: View = inflater.inflate(R.layout.loading_dialog, null)
        builder.setView(dialogView)

        val alertDialog: AlertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)

        return alertDialog
    }

    fun showConfirmationDialog(
        context: Context,
        inflater: LayoutInflater,
        config: ConfirmationDialogConfig
    ) {
        val builder = AlertDialog.Builder(context)

        val dialogView: View = inflater.inflate(R.layout.confirmation_dialog, null)
        builder.setView(dialogView)

        val btnPositive = dialogView.findViewById<TextView>(R.id.btn_positive)
        val btnNegative = dialogView.findViewById<TextView>(R.id.btn_negative)
        dialogView.findViewById<TextView>(R.id.tv_title).text = config.title
        dialogView.findViewById<TextView>(R.id.tv_message).text = config.message

        btnPositive.text = config.positiveButtonTitle
        btnNegative.text = config.negativeButtonTitle
        btnPositive.setTextColor(
            context.resources.getColor(
                config.positiveButtonColor ?: R.color.purpleDarker, null
            )
        )
        btnNegative.setTextColor(
            context.resources.getColor(
                config.negativeButtonColor ?: R.color.redSecondary, null
            )
        )

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnPositive.setOnClickListener {
            config.listener?.onPositiveButtonClick()
            alertDialog.dismiss()
        }
        btnNegative.setOnClickListener {
            config.listener?.onNegativeButtonClick()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    fun getBottomDialog(
        context: Context,
        @LayoutRes layoutId: Int,
        inflater: LayoutInflater
    ): BottomSheetDialog {
        val dialog = BottomSheetDialog(context)
        val customView = inflater.inflate(layoutId, null)
        dialog.setContentView(customView)
        dialog.setOnShowListener {
            (customView.parent as ViewGroup).background = ColorDrawable(Color.TRANSPARENT)
        }
        return dialog
    }

    /*fun showImagePickerDialog(
        context: Context,
        isPickLogo: Boolean,
        isUsingLogoAsAvatar: Boolean?,
        isAvatarEmpty: Boolean,
        isLogoEmpty: Boolean,
        inflater: LayoutInflater,
        listener: DialogImagePickerListener
    ) {
        val dialog = getBottomDialog(context, R.layout.dialog_image_picker, inflater)
        val tvDialogTitle = dialog.findViewById<TextView>(R.id.tvDialogTitle)
        val ctrAvatarPickImage = dialog.findViewById<ConstraintLayout>(R.id.ctrAvatarPickImage)
        val ctrLogoPickImage = dialog.findViewById<ConstraintLayout>(R.id.ctrLogoPickImage)
        val ctrUseLogoAsAvatar = dialog.findViewById<ConstraintLayout>(R.id.ctrUseLogoAsAvatar)
        val ctrUnUseLogoAsAvatar = dialog.findViewById<ConstraintLayout>(R.id.ctrUnUseLogoAsAvatar)
        val ctrDelete = dialog.findViewById<ConstraintLayout>(R.id.ctrDelete)
        val tvDelete = dialog.findViewById<TextView>(R.id.tvDelete)
        val tvCamera = dialog.findViewById<TextView>(R.id.tvCamera)
        val tvGallery = dialog.findViewById<TextView>(R.id.tvGallery)


        tvDialogTitle?.text = context.getString(if (isPickLogo) R.string.logo else R.string.profibild)

        ctrAvatarPickImage?.visibility = if (isPickLogo) View.GONE else View.VISIBLE
        ctrLogoPickImage?.visibility = if (isPickLogo) View.VISIBLE else View.GONE

        if (isPickLogo) {
            ctrUseLogoAsAvatar?.visibility = View.GONE
            ctrUnUseLogoAsAvatar?.visibility = View.GONE
            ctrDelete?.visibility = if (isLogoEmpty) View.GONE else View.VISIBLE
            tvDelete?.text = context.getString(R.string.delete_logo)
        } else {
            if (isAvatarEmpty && isLogoEmpty) {
                ctrUseLogoAsAvatar?.visibility = View.GONE
                ctrUnUseLogoAsAvatar?.visibility = View.GONE
                ctrDelete?.visibility = View.GONE
            } else {
                ctrUseLogoAsAvatar?.visibility = if (!isLogoEmpty && isUsingLogoAsAvatar == false) View.VISIBLE else View.GONE
                ctrUnUseLogoAsAvatar?.visibility = if (!isAvatarEmpty && isUsingLogoAsAvatar == true) View.VISIBLE else View.GONE

                ctrDelete?.visibility = if (!isAvatarEmpty && isUsingLogoAsAvatar == false) View.VISIBLE else View.GONE
                tvDelete?.text = context.getString(R.string.delete_profile_picture)
            }
        }

        tvCamera?.setOnClickListener {
            dialog.dismiss()
            listener.onCameraClicked()
        }
        tvGallery?.setOnClickListener {
            dialog.dismiss()
            listener.onGalleryClicked()
        }
        ctrLogoPickImage?.setOnClickListener {
            dialog.dismiss()
            listener.onGalleryClicked()
        }
        ctrUseLogoAsAvatar?.setOnClickListener {
            dialog.dismiss()
            listener.onUseLogoAsAvatarClicked()
        }
        ctrUnUseLogoAsAvatar?.setOnClickListener {
            dialog.dismiss()
            listener.ctrUnUseLogoAsAvatar()
        }
        ctrDelete?.setOnClickListener {
            dialog.dismiss()
            listener.onDeleteClicked()
        }

        dialog.show()
    }

    interface DialogImagePickerListener {
        fun onCameraClicked()
        fun onGalleryClicked()
        fun onUseLogoAsAvatarClicked()
        fun onDeleteClicked()
        fun ctrUnUseLogoAsAvatar()
    }*/

    interface AlertDialogListener {
        fun onPositiveButtonClick()
        fun onNegativeButtonClick()
        fun onDismiss()
    }

    interface AlertDialogButtonConfig {
        var title: String
        var isVisible: Boolean
        fun onClick()
    }

    data class AlertDialogConfig(
        var title: String,
        var message: String,
        var positiveButtonConfig: AlertDialogButtonConfig,
        var negativeButtonConfig: AlertDialogButtonConfig
    )

    open class SingleConfirmDialogConfig(
        open var title: String? = null,
        open var message: String? = null,
        open var positiveButtonTitle: String? = null,
        @ColorRes
        open var positiveButtonColor: Int? = null,
        open val listener: AlertDialogListener? = null
    )

    data class ConfirmationDialogConfig(
        override var title: String? = null,
        override var message: String? = null,
        override var positiveButtonTitle: String? = null,
        @ColorRes
        override var positiveButtonColor: Int? = null,
        val negativeButtonTitle: String? = null,
        @ColorRes val negativeButtonColor: Int? = null,
        override val listener: AlertDialogListener? = null
    ) : SingleConfirmDialogConfig(
        title,
        message,
        positiveButtonTitle,
        positiveButtonColor,
        listener
    )
}
