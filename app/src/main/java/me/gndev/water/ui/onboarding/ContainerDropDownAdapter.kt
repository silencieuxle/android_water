package me.gndev.water.ui.onboarding

import android.content.Context
import androidx.annotation.LayoutRes
import me.gndev.water.core.adapter.DropdownAdapter
import me.gndev.water.core.base.DropdownModelBase

class WeaponDropDownAdapter(
    context: Context,
    @LayoutRes layoutRes: Int,
    dataSet: List<ContainerDropdownModel>
) : DropdownAdapter<ContainerDropdownModel>(context, layoutRes, dataSet)

data class ContainerDropdownModel(val text: String, val value: String) : DropdownModelBase(text, value)