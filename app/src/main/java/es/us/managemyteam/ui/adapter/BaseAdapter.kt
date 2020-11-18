package es.us.managemyteam.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import es.us.managemyteam.contract.BaseAdapterClickListener

abstract class BaseAdapter<T, VB : ViewBinding, VH : BaseAdapter.BaseViewHolder<T, VB>>
    (val data: MutableList<T> = arrayListOf()) : RecyclerView.Adapter<VH>() {

    private var itemClickListener: BaseAdapterClickListener<T>? = null

    abstract fun onCreate(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): VH

    abstract fun onBind(item: T, position: Int, holder: VH)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return onCreate(LayoutInflater.from(parent.context), parent, viewType)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = data[position]
        holder.getRoot().setOnClickListener {
            itemClickListener?.onAdapterItemClicked(item, position)
        }
        return onBind(item, position, holder)
    }

    open fun setData(vararg newData: T) {
        setData(*newData)
    }

    open fun setData(newData: List<T>) {
        data.clear()
        data.addAll(newData)
    }

    open fun addData(newData: List<T>, notify: Boolean = true) {
        data.addAll(newData)
        if (notify && newData.isNotEmpty()) {
            notifyItemRangeInserted(data.size - newData.size, data.size)
        }
    }

    fun setItemClickListener(itemClickListener: BaseAdapterClickListener<T>) {
        this.itemClickListener = itemClickListener
    }

    fun getItemClickListener(): BaseAdapterClickListener<T>? {
        return itemClickListener
    }

    abstract class BaseViewHolder<T, VB : ViewBinding>(private val itemViewBinding: VB) :
        RecyclerView.ViewHolder(itemViewBinding.root) {

        fun getRoot() = itemViewBinding.root

        fun getViewBinding() = itemViewBinding

        abstract fun setup(viewBinding: VB = getViewBinding(), item: T)
    }

}