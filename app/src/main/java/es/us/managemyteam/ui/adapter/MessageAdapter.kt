package es.us.managemyteam.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import es.us.managemyteam.data.model.MessageBo
import es.us.managemyteam.databinding.RowChatIncomingBinding
import es.us.managemyteam.databinding.RowChatOutgoingBinding
import es.us.managemyteam.util.DateUtil

private const val VIEW_TYPE_INCOME = 0
private const val VIEW_TYPE_OUTGOING = 1

class MessageAdapter :
    BaseAdapter<MessageBo, ViewBinding, MessageAdapter.MessageViewHolder>() {

    private var currentUserId: String = ""

    fun setCurrentUserId(userId: String) {
        currentUserId = userId
    }

    fun getCurrentUserId() = currentUserId

    override fun getItemViewType(position: Int): Int {
        return if (currentUserId.isNotBlank() &&
            currentUserId == data[position].fromId
        ) {
            VIEW_TYPE_OUTGOING
        } else {
            VIEW_TYPE_INCOME
        }
    }

    override fun onCreate(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder {
        return if (viewType == VIEW_TYPE_INCOME) {
            IncomingMessageViewHolder(RowChatIncomingBinding.inflate(inflater, parent, false))
        } else {
            OutgoingMessageViewHolder(RowChatOutgoingBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBind(item: MessageBo, position: Int, holder: MessageViewHolder) {
        holder.setup(holder.getViewBinding(), data[position])
    }

    abstract class MessageViewHolder(
        viewBinding: ViewBinding
    ) : BaseViewHolder<MessageBo, ViewBinding>(viewBinding) {

        override fun setup(viewBinding: ViewBinding, item: MessageBo) {}

    }

    class IncomingMessageViewHolder(
        viewBinding: RowChatIncomingBinding
    ) : MessageViewHolder(viewBinding) {

        override fun setup(viewBinding: ViewBinding, item: MessageBo) {
            (viewBinding as RowChatIncomingBinding).apply {
                rowChatIncomingLabelMessage.text = item.message
                rowChatIncomingLabelDate.text = DateUtil.format(item.date)
            }
        }

    }

    class OutgoingMessageViewHolder(
        viewBinding: RowChatOutgoingBinding
    ) : MessageViewHolder(viewBinding) {

        override fun setup(viewBinding: ViewBinding, item: MessageBo) {
            (viewBinding as RowChatOutgoingBinding).apply {
                rowChatOutgoingLabelMessage.text = item.message
                rowChatOutgoingLabelDate.text = DateUtil.format(item.date)
                    .plus(" - ")
                    .plus(item.fromName)
            }
        }

    }

}