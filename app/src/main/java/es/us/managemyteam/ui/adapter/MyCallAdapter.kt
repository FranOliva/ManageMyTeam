package es.us.managemyteam.ui.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import es.us.managemyteam.contract.AcceptListener
import es.us.managemyteam.data.model.CallStatus
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.databinding.RowAcceptPlayerBinding

class MyCallAdapter(
    private val acceptListener: AcceptListener,
    private val status: CallStatus
) :
    BaseAdapter<EventBo, RowAcceptPlayerBinding, MyCallAdapter.AcceptCallViewHolder>() {

    override fun onCreate(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): AcceptCallViewHolder {
        return AcceptCallViewHolder(
            RowAcceptPlayerBinding.inflate(inflater, parent, false),
            acceptListener,
            status
        )
    }

    override fun onBind(item: EventBo, position: Int, holder: AcceptCallViewHolder) {
        holder.setup(holder.getViewBinding(), item)
    }

    class AcceptCallViewHolder(
        viewBinding: RowAcceptPlayerBinding,
        private val acceptListener: AcceptListener,
        private val status: CallStatus
    ) :
        BaseAdapter.BaseViewHolder<EventBo, RowAcceptPlayerBinding>(viewBinding) {
        override fun setup(viewBinding: RowAcceptPlayerBinding, item: EventBo) {
            viewBinding.rowAcceptPlayerLabelName.text = item.title
            viewBinding.rowAcceptPlayerBtnAccept.setOnClickListener {
                acceptListener.onAccepted(item.uuid ?: "")
            }
            viewBinding.rowAcceptPlayerBtnRefuse.setOnClickListener {
                acceptListener.onRefused(item.uuid ?: "")
            }
            when(status) {
                CallStatus.PENDING -> {
                    viewBinding.rowAcceptPlayerBtnAccept.visibility = VISIBLE
                    viewBinding.rowAcceptPlayerBtnRefuse.visibility = VISIBLE
                }
                CallStatus.ACCEPTED -> {
                    viewBinding.rowAcceptPlayerBtnAccept.visibility = GONE
                    viewBinding.rowAcceptPlayerBtnRefuse.visibility = VISIBLE
                }
                else -> {
                    viewBinding.rowAcceptPlayerBtnAccept.visibility = VISIBLE
                    viewBinding.rowAcceptPlayerBtnRefuse.visibility = GONE
                }
            }
        }

    }

}