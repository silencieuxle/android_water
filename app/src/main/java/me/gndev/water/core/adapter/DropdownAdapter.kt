package me.gndev.water.core.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import me.gndev.water.R
import me.gndev.water.core.base.DropdownModelBase

abstract class DropdownAdapter<T : DropdownModelBase>(
    private val c: Context,
    @LayoutRes val rl: Int,
    private val ds: List<T>
) : ArrayAdapter<T>(c, rl, ds) {

    override fun getCount(): Int = ds.size

    override fun getItem(position: Int): T = ds[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(c).inflate(rl, parent, false)
        view.findViewById<TextView>(R.id.tv_dropdown_item_text).text = ds[position].t
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(c).inflate(rl, parent, false)
        view.findViewById<TextView>(R.id.tv_dropdown_item_text).text = ds[position].t
        return view
    }
}