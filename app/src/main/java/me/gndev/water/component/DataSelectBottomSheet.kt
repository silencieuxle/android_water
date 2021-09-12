package me.gndev.water.component

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.gndev.water.R
import me.gndev.water.core.base.DataSelectModelBase

class DataSelectBottomSheet(private val dataSet: List<SelectorItem>) :
    BottomSheetDialogFragment() {
    private lateinit var rvContainer: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.container_selector_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvContainer = view.findViewById(R.id.rv_container)
        rvContainer.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = DataSelectRvAdapter(
                dataSet,
                object : DataSelectRvAdapter.RecyclerViewListener {
                    override fun onItemSelected(id: String) {
                        setFragmentResult(
                            CONTAINER_SELECTED,
                            bundleOf(SELECTED_CONTAINER_ID to id)
                        )
                        this@DataSelectBottomSheet.dismiss()
                    }
                }
            )
        }

        ViewCompat.setNestedScrollingEnabled(rvContainer, true)
        (view.parent as ViewGroup).background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_bottom_sheet)

        val layout = view.findViewById<View>(R.id.ll_bottom_sheet)
        BottomSheetBehavior.from(layout!!).apply {
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, R.style.BottomSheetDialog)
        return super.onCreateDialog(savedInstanceState)
    }

    companion object {
        const val CONTAINER_SELECTED = "CONTAINER_SELECTED"
        const val SELECTED_CONTAINER_ID = "SELECTED_CONTAINER_ID"
    }

    data class SelectorItem(val text: String, val value: String, var isChecked: Boolean) :
        DataSelectModelBase(text, value)
}

class DataSelectRvAdapter(
    private var dataSet: List<DataSelectBottomSheet.SelectorItem>,
    private val listener: RecyclerViewListener
) : RecyclerView.Adapter<DataSelectRvAdapter.ViewHolder>() {
    private lateinit var context: Context

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val radioButtonView: RadioImageButton = v as RadioImageButton
        fun getView(): RadioImageButton {
            return radioButtonView
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        context = viewGroup.context
        val itemView = RadioImageButton(context)
        itemView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return ViewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.getView().findViewById<TextView>(R.id.tv_display_text).text =
            dataSet[position].text
        viewHolder.getView().stringValue = dataSet[position].value
        val item = dataSet[position]
        if (item.isChecked) {
            viewHolder.getView().check()
        } else {
            viewHolder.getView().uncheck()
        }

        viewHolder.getView().setOnClickListener {
            dataSet.forEach { x -> x.isChecked = false }
            notifyDataSetChanged()
            dataSet[position].isChecked = true
            Handler(Looper.getMainLooper()).postDelayed({
                listener.onItemSelected(dataSet[position].value)
            }, 150)
        }
    }

    override fun getItemCount() = dataSet.size

    interface RecyclerViewListener {
        fun onItemSelected(id: String)
    }
}
