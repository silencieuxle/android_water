package me.gndev.water_battle.core.component

import android.text.InputFilter
import android.text.Spanned

class InputFilterMinMax : InputFilter {
    private var min: Double
    private var max: Double

    constructor(min: Double, max: Double) {
        this.min = min
        this.max = max
    }

    constructor(min: String, max: String) {
        this.min = min.toDouble()
        this.max = max.toDouble()
    }

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            val lastVal = dest.toString()
            val newVal =
                lastVal.substring(0, dstart) + source.toString() + lastVal.substring(dstart)
            val strInput = source.toString()
            val input: Double = if (strInput == "-" && (lastVal.isEmpty() || lastVal == "0")) {
                return null
            } else {
                newVal.toDouble()
            }
            if (isInRange(min, max, input)) {
                try {
                    if (lastVal == "0" && strInput == "0" && strInput != ".") {
                        return ""
                    } else if (strInput == "0") {
                        if (dstart == 0) {
                            if (lastVal.substring(0, 1) == "0") {
                                return ""
                            } else if (lastVal.substring(0, 1) != ".") {
                                return ""
                            }
                        } else {
                            if (lastVal.substring(0, 1) == "0" && dstart == 1) {
                                return ""
                            } else if (lastVal.substring(0, 1) == "-") {
                                if (lastVal.toDouble() == 0.0) {
                                    if (!lastVal.contains(".")) {
                                        return ""
                                    } else {
                                        if (dstart <= lastVal.indexOf(".")) {
                                            return ""
                                        }
                                    }
                                } else {
                                    if (lastVal.indexOf("0") == 1 && (dstart == 1 || dstart == 2)) {
                                        return ""
                                    } else if (lastVal.substring(
                                            1,
                                            2
                                        ) != "0" && lastVal.substring(1, 2) != "." && dstart == 1
                                    ) {
                                        return ""
                                    }
                                }
                            }
                        }
                    }

                    if (strInput == "." && lastVal.substring(0, 1) == "-" &&
                        lastVal.toDouble() == min && dstart == lastVal.length
                    ) {
                        return ""
                    }
                } catch (e: Exception) {
                }
                return null
            }
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
        return ""
    }

    private fun isInRange(a: Double, b: Double, c: Double): Boolean {
        return if (b > a) c in a..b else c in b..a
    }
}