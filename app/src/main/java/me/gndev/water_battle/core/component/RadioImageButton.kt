package me.gndev.water_battle.core.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import me.gndev.water_battle.R

class RadioImageButton : RelativeLayout {
    constructor(context: Context) : super(context, null) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private var text: String? = ""
    private var stringValue: String? = null
    private var integerValue: Int? = null
    private var booleanValue: Boolean? = null
    private var hideDivider: Boolean? = null
    private var _listener: RadioImageButtonListener? = null

    private lateinit var tvText: TextView
    private lateinit var ivRadio: ImageView
    private lateinit var viewBreakLine: View

    fun getStringVal(): String? {
        return stringValue
    }

    fun getBooleanVal(): Boolean? {
        return booleanValue
    }

    fun getIntegerVal(): Int? {
        return integerValue
    }

    fun setListener(listener: RadioImageButtonListener) {
        _listener = listener
    }

    fun select() {
        ivRadio.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_radio_button_checked
            )
        )
    }

    fun unselect() {
        ivRadio.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_radio_button_unchecked
            )
        )
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val view = inflate(context, R.layout.radio_image_button, this)

        val attributes =
            context.obtainStyledAttributes(attrs, R.styleable.RadioImageButton, defStyle, 0)

        text = attributes.getString(R.styleable.RadioImageButton_text)
        booleanValue = attributes.getBoolean(R.styleable.RadioImageButton_booleanValue, false)
        stringValue = attributes.getString(R.styleable.RadioImageButton_stringValue)
        integerValue =
            attributes.getInteger(R.styleable.RadioImageButton_integerValue, Int.MIN_VALUE)
        hideDivider = attributes.getBoolean(R.styleable.RadioImageButton_hideDivider, false)
        attributes.recycle()

        tvText = view.findViewById(R.id.tv_display_text)
        ivRadio = view.findViewById(R.id.iv_radio)

        tvText.text = text
        tvText.setOnClickListener {
            _listener?.onClick(stringValue)
        }
        ivRadio.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_radio_button_unchecked
            )
        )
        viewBreakLine.visibility = if (hideDivider == true) View.GONE else View.VISIBLE
    }

    interface RadioImageButtonListener {
        fun onClick(value: String?)
    }
}