package es.us.managemyteam.ui.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import es.us.managemyteam.data.model.PaymentBo
import es.us.managemyteam.databinding.RowPaymentBinding
import es.us.managemyteam.util.DateUtil

class MyPaymentsAdapter :
    BaseAdapter<PaymentBo, RowPaymentBinding, MyPaymentsAdapter.MyPaymentsViewHolder>() {

    override fun onCreate(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): MyPaymentsViewHolder {
        return MyPaymentsViewHolder(RowPaymentBinding.inflate(inflater, parent, false))
    }

    override fun onBind(item: PaymentBo, position: Int, holder: MyPaymentsViewHolder) {
        holder.setup(item = item)
    }

    class MyPaymentsViewHolder(
        viewBinding: RowPaymentBinding
    ) :
        BaseAdapter.BaseViewHolder<PaymentBo, RowPaymentBinding>(viewBinding) {

        override fun setup(viewBinding: RowPaymentBinding, item: PaymentBo) {
            viewBinding.rowPaymentLabelConcept.text = item.concept ?: ""
            viewBinding.rowPaymentLabelQuantity.text = item.quantity ?: "0 EUR"
            viewBinding.rowPaymentLabelDate.apply {
                if (item.moment != null) {
                    this.visibility = VISIBLE
                    this.text = DateUtil.format(item.moment!!)
                } else {
                    this.visibility = GONE
                }
            }
        }

    }

}