package es.us.managemyteam.ui.view.verticalnavigation

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import es.us.managemyteam.R
import es.us.managemyteam.contract.BaseAdapterClickListener
import es.us.managemyteam.databinding.ViewVerticalMenuBinding
import es.us.managemyteam.extension.getBaseActivity
import es.us.managemyteam.ui.activity.MainActivity
import es.us.managemyteam.ui.viewmodel.ClubViewModel
import es.us.managemyteam.ui.viewmodel.MenuViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class VerticalMenuView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), VerticalMenuClickListener,
    BaseAdapterClickListener<VerticalMenuVO> {

    private var needClosingDrawerListener: NeedCloseDrawerListener? = null
    private var viewBinding =
        ViewVerticalMenuBinding.inflate(LayoutInflater.from(context), this, true)

    private val menuViewModel: MenuViewModel by (context as AppCompatActivity).viewModel()
    private var userIsLogged = false

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (!isInEditMode) {
            setupMenuList()
            Handler().postDelayed({
                //setupUserLoggedObserver()
            }, 3000)
        }
    }

    /*private fun setupUserLoggedObserver() {
        getBaseActivity()?.let {
            menuViewModel.getUserLoggedData().observe(it,
                object : ResourceObserver<UserBo>() {
                    override fun onSuccess(response: UserBo?) {
                        userIsLogged = response != null
                    }

                    override fun onError(error: Error) {
                        super.onError(error)
                        userIsLogged = false
                    }
                })
            menuViewModel.getUser()
        }

    }

    fun notifyUserChanged() {
        menuViewModel.getUser()
    }*/

    private fun setupMenuList() {
        viewBinding.verticalMenuListOption.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.verticalMenuListOption.adapter =
            VerticalMenuAdapter(VerticalMenuVO.getDefaultMenu(context)).apply {
                setItemClickListener(this@VerticalMenuView)
            }
    }

    //region VerticalMenuClickListener

    override fun onAdapterItemClicked(item: VerticalMenuVO, position: Int) {
        onVerticalMenuClicked(item.id)
    }

    override fun onVerticalMenuClicked(menuId: VerticalMenuId) {
        when (menuId) {
            VerticalMenuId.LOGOUT_ID -> setupLogoutClick()
            VerticalMenuId.MY_CLUB_ID -> setupClubClick()
        }
        needClosingDrawerListener?.onNeedClosingDrawer()
    }

    fun setNeedCloseDrawerListener(needListener: NeedCloseDrawerListener) {
        needClosingDrawerListener = needListener
    }
    //endregion

    private fun setupLogoutClick() {
        menuViewModel.logout()
        (getBaseActivity() as MainActivity).getNavGraph().navigate(R.id.action_menu_to_login)
    }

    private fun setupClubClick() {
        (getBaseActivity() as MainActivity).getNavGraph().navigate(R.id.action_menu_to_club)
    }

    private fun setupMyAccountNavigation(): Int {
        return 0
        /*return if (userIsLogged) {
            R.id.action_menu_to_user
        } else {
            R.id.action_menu_to_login
        }*/
    }

}