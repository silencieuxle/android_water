package me.gndev.water.component

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import me.gndev.water.R

class RadioImageButton : RelativeLayout {
    constructor(context: Context) : super(context, null) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) :
            super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private var _text: String? = ""
    var text: String?
        get() = _text
        set(value) {
            _text = value
        }

    private var _stringValue: String? = null
    var stringValue: String?
        get() = _stringValue
        set(value) {
            _stringValue = value
        }

    private var _integerValue: Int? = null
    var integerValue: Int?
        get() = _integerValue
        set(value) {
            _integerValue = value
        }

    private var _booleanValue: Boolean? = null
    var booleanValue: Boolean?
        get() = _booleanValue
        set(value) {
            _booleanValue = value
        }

    private var _hideDivider: Boolean? = null
    var hideDivider: Boolean?
        get() = _hideDivider
        set(value) {
            _hideDivider = value
        }

    private var _listener: RadioImageButtonListener? = null
    var listener: RadioImageButtonListener?
        get() = _listener
        set(value) {
            _listener = value
        }

    private lateinit var tvText: TextView
    private lateinit var ivRadio: ImageView
    // private lateinit var viewBreakLine: View

    fun check() {
        ivRadio.setImageDrawable(
            ContextCompat.getDrawable(context, R.drawable.ic_radio_button_checked)
        )
    }

    fun uncheck() {
        ivRadio.setImageDrawable(
            ContextCompat.getDrawable(context, R.drawable.ic_radio_button_unchecked)
        )
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val view = inflate(context, R.layout.radio_image_button, this)

        val attributes =
            context.obtainStyledAttributes(attrs, R.styleable.RadioImageButton, defStyle, 0)

        _text = attributes.getString(R.styleable.RadioImageButton_text)
        _booleanValue = attributes.getBoolean(R.styleable.RadioImageButton_booleanValue, false)
        _stringValue = attributes.getString(R.styleable.RadioImageButton_stringValue)
        _integerValue =
            attributes.getInteger(R.styleable.RadioImageButton_integerValue, Int.MIN_VALUE)
        _hideDivider = attributes.getBoolean(R.styleable.RadioImageButton_hideDivider, false)
        attributes.recycle()

        tvText = view.findViewById(R.id.tv_display_text)
        ivRadio = view.findViewById(R.id.iv_radio)

        tvText.text = _text
        view.setOnClickListener {
            if (!_stringValue.isNullOrEmpty()) {
                _listener?.onClick(_stringValue)
            } else if (_integerValue != null) {
                _listener?.onClick(_integerValue)
            } else if (_booleanValue != null) {
                _listener?.onClick(_booleanValue)
            }
        }
        ivRadio.setImageDrawable(
            ContextCompat.getDrawable(context, R.drawable.ic_radio_button_unchecked)
        )
        // viewBreakLine.visibility = if (_hideDivider == true) View.GONE else View.VISIBLE
    }

    interface RadioImageButtonListener {
        fun onClick(value: Any?)
    }
}