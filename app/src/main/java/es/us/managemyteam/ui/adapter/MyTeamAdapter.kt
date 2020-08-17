package es.us.managemyteam.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.databinding.RowMyTeamUserBinding

class MyTeamAdapter : BaseAdapter<UserBo, RowMyTeamUserBinding, MyTeamAdapter.MyTeamViewHolder>() {

    override fun onCreate(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): MyTeamViewHolder {
        return MyTeamViewHolder(RowMyTeamUserBinding.inflate(inflater, parent, false))
    }

    override fun onBind(item: UserBo, position: Int, holder: MyTeamViewHolder) {
        holder.setup(item = item)
    }

    class MyTeamViewHolder(binding: RowMyTeamUserBinding) :
        BaseAdapter.BaseViewHolder<UserBo, RowMyTeamUserBinding>(binding) {
        override fun setup(viewBinding: RowMyTeamUserBinding, item: UserBo) {
            viewBinding.rowMyTeamUserLabelName.text = item.getFullName()
        }

    }

}