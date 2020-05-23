package es.us.managemyteam.ui.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import es.us.managemyteam.contract.BaseAdapterClickListener
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.databinding.RowEventBinding
import es.us.managemyteam.util.DateUtil

class EventsAdapter(private val itemListener: BaseAdapterClickListener<EventBo>) :
    BaseAdapter<EventBo, RowEventBinding, EventsAdapter.EventsViewHolder>() {

    override fun onCreate(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): EventsViewHolder {
        return EventsViewHolder(RowEventBinding.inflate(inflater, parent, false), itemListener)
    }

    override fun onBind(item: EventBo, position: Int, holder: EventsViewHolder) {
        holder.setup(holder.getViewBinding(), data[position])
    }

    class EventsViewHolder(
        viewBinding: RowEventBinding,
        private val itemClickListener: BaseAdapterClickListener<EventBo>
    ) : BaseViewHolder<EventBo, RowEventBinding>(viewBinding) {

        override fun setup(viewBinding: RowEventBinding, item: EventBo) {
            viewBinding.apply {
                rowEventLabelTitle.text = item.title
                rowEventLabelEventType.text = item.eventType
                if (item.date != null) {
                    rowEventLabelDay.text = DateUtil.getDayFromMillis(item.date!!.time).toString()
                    rowEventLabelMonth.text = DateUtil.getMonthLabelFromMillis(item.date!!.time)
                } else {
                    rowEventContainerDate.visibility = GONE
                }
                root.setOnClickListener {
                    itemClickListener.onAdapterItemClicked(item, adapterPosition)
                }
            }
        }

    }

}