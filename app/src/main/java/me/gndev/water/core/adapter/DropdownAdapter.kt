package me.gndev.water.core.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import me.gndev.water.R
import me.gndev.water.core.base.DataSelectModelBase

abstract class DropdownAdapter<T : DataSelectModelBase>(
    private val context: Context,
    @LayoutRes val layout: Int,
    private val dataSet: List<T>
) : ArrayAdapter<T>(context, layout, dataSet) {

    override fun getCount(): Int = dataSet.size

    override fun getItem(position: Int): T = dataSet[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(layout, parent, false)
        view.findViewById<TextView>(R.id.tv_dropdown_item_text).text = dataSet[position].t
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(layout, parent, false)
        view.findViewById<TextView>(R.id.tv_dropdown_item_text).text = dataSet[position].t
        return view
    }
}