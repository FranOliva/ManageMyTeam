package es.us.managemyteam.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import es.us.managemyteam.contract.AcceptPlayerListener
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.databinding.RowAcceptPlayerBinding

class AcceptPlayersAdapter(private val acceptPlayerListener: AcceptPlayerListener) :
    BaseAdapter<UserBo, RowAcceptPlayerBinding, AcceptPlayersAdapter.AcceptPlayersViewHolder>() {

    override fun onCreate(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): AcceptPlayersViewHolder {
        return AcceptPlayersViewHolder(
            RowAcceptPlayerBinding.inflate(inflater, parent, false),
            acceptPlayerListener
        )
    }

    override fun onBind(item: UserBo, position: Int, holder: AcceptPlayersViewHolder) {
        holder.setup(holder.getViewBinding(), data[position])
    }

    class AcceptPlayersViewHolder(
        viewBinding: RowAcceptPlayerBinding,
        private val acceptPlayerListener: AcceptPlayerListener
    ) :
        BaseAdapter.BaseViewHolder<UserBo, RowAcceptPlayerBinding>(viewBinding) {
        override fun setup(viewBinding: RowAcceptPlayerBinding, item: UserBo) {
            viewBinding.rowAcceptPlayerLabelName.text = item.name.plus(" ").plus(item.surname)
            viewBinding.rowAcceptPlayerBtnAccept.setOnClickListener {
                acceptPlayerListener.onPlayerAccepted(item)
            }
            viewBinding.rowAcceptPlayerBtnRefuse.setOnClickListener {
                acceptPlayerListener.onPlayerRefused(item)
            }
        }

    }

}