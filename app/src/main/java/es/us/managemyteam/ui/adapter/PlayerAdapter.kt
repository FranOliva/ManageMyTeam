package es.us.managemyteam.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import es.us.managemyteam.data.model.UserCalledBo
import es.us.managemyteam.databinding.RowPlayerBinding

class PlayerAdapter :
    BaseAdapter<UserCalledBo, RowPlayerBinding, PlayerAdapter.PlayerViewHolder>() {

    override fun onCreate(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): PlayerViewHolder {
        return PlayerViewHolder(RowPlayerBinding.inflate(inflater, parent, false))
    }

    override fun onBind(item: UserCalledBo, position: Int, holder: PlayerViewHolder) {
        holder.setup(item = item)
    }

    class PlayerViewHolder(binding: RowPlayerBinding) :
        BaseAdapter.BaseViewHolder<UserCalledBo, RowPlayerBinding>(binding) {
        override fun setup(viewBinding: RowPlayerBinding, item: UserCalledBo) {
            val number = adapterPosition + 1
            viewBinding.rowPlayerLabelName.text = ("$number.").plus(" " + item.userName)
        }

    }

}