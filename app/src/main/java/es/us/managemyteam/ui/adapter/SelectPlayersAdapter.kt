package es.us.managemyteam.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import es.us.managemyteam.contract.PlayerSelectedListener
import es.us.managemyteam.data.model.UserCalledBo
import es.us.managemyteam.databinding.RowSelectPlayerBinding

class SelectPlayersAdapter :
    BaseAdapter<UserCalledBo, RowSelectPlayerBinding, SelectPlayersAdapter.SelectPlayerViewHolder>(),
    PlayerSelectedListener {

    override fun onCreate(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): SelectPlayerViewHolder {
        return SelectPlayerViewHolder(RowSelectPlayerBinding.inflate(inflater, parent, false), this)
    }

    override fun onBind(item: UserCalledBo, position: Int, holder: SelectPlayerViewHolder) {
        holder.setup(holder.getViewBinding(), data[position])
    }

    fun getPlayers(selected: Boolean) = data.filter { it.enable == selected }

    class SelectPlayerViewHolder(
        viewBinding: RowSelectPlayerBinding,
        private val playerSelectedListener: PlayerSelectedListener
    ) :
        BaseAdapter.BaseViewHolder<UserCalledBo, RowSelectPlayerBinding>(viewBinding) {
        override fun setup(viewBinding: RowSelectPlayerBinding, item: UserCalledBo) {
            viewBinding.rowSelectPlayerLabelName.text = item.userName
            viewBinding.rowSelectPlayerSwitcher.apply {
                setChecked(item.enable)
                setOnCheckedChangeListener {
                    setChecked(it)
                    playerSelectedListener.onPlayerSelected(adapterPosition, it)
                }
            }
        }
    }

    override fun onPlayerSelected(position: Int, enable: Boolean) {
        data[position].enable = enable
    }

}