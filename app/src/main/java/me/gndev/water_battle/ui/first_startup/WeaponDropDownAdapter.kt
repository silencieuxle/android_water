package me.gndev.water_battle.ui.first_startup

import android.content.Context
import androidx.annotation.LayoutRes
import me.gndev.water_battle.core.adapter.DropdownAdapter
import me.gndev.water_battle.core.model.DropdownModelBase

class WeaponDropDownAdapter(
    context: Context,
    @LayoutRes layoutRes: Int,
    dataSet: List<WeaponDropdownModel>
) :
    DropdownAdapter<WeaponDropdownModel>(context, layoutRes, dataSet)

data class WeaponDropdownModel(val text: String, val value: String) : DropdownModelBase(text, value)