package es.us.managemyteam.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import es.us.managemyteam.contract.UserSelectedListener
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.databinding.RowSelectPlayerBinding

class UnableUsersAdapter :
    BaseAdapter<UserBo, RowSelectPlayerBinding, UnableUsersAdapter.UnableUserViewHolder>(),
    UserSelectedListener {

    override fun onCreate(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): UnableUserViewHolder {
        return UnableUserViewHolder(RowSelectPlayerBinding.inflate(inflater, parent, false), this)
    }

    override fun onBind(item: UserBo, position: Int, holder: UnableUserViewHolder) {
        holder.setup(holder.getViewBinding(), data[position])
    }

    class UnableUserViewHolder(
        viewBinding: RowSelectPlayerBinding,
        private val playerSelectedListener: UserSelectedListener
    ) :
        BaseAdapter.BaseViewHolder<UserBo, RowSelectPlayerBinding>(viewBinding) {
        override fun setup(viewBinding: RowSelectPlayerBinding, item: UserBo) {
            viewBinding.rowSelectPlayerLabelName.text = item.getFullName()
            viewBinding.rowSelectPlayerSwitcher.apply {
                setChecked(item.enable ?: true)
                setOnCheckedChangeListener {
                    setChecked(it)
                    playerSelectedListener.onUserSelected(adapterPosition, it)
                }
            }
        }
    }

    override fun onUserSelected(position: Int, enable: Boolean) {
        data[position].enable = enable
    }

}