package es.us.managemyteam.ui.view.verticalnavigation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import es.us.managemyteam.R
import es.us.managemyteam.contract.BaseAdapterClickListener
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.databinding.ViewVerticalMenuBinding
import es.us.managemyteam.extension.getBaseActivity
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.activity.MainActivity
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
    private var userIsAdmin = false

    override fun onFinishInflate() {
        super.onFinishInflate()
        setupUserIsAdminObserver()
    }

    private fun setupUserIsAdminObserver() {
        getBaseActivity()?.let {
            menuViewModel.getUserData().observe(it,
                object : ResourceObserver<UserBo>() {
                    override fun onSuccess(response: UserBo?) {
                        response?.let { user ->
                            userIsAdmin = user.isAdmin()
                            setupMenuList(userIsAdmin, user.isStaff())
                        }
                    }

                    override fun onError(error: es.us.managemyteam.repository.util.Error) {
                        super.onError(error)
                        userIsAdmin = false
                        setupMenuList(userIsAdmin, false)
                    }
                })
            menuViewModel.getUser()
        }

    }

    private fun setupMenuList(userIsAdmin: Boolean, userIsStaff: Boolean) {
        viewBinding.verticalMenuListOption.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.verticalMenuListOption.adapter =
            VerticalMenuAdapter(VerticalMenuVO.getDefaultMenu(context, userIsAdmin, userIsStaff)).apply {
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
            VerticalMenuId.ADMINISTRATION_ID -> setupAdministrationClick()
            VerticalMenuId.MY_TEAM_ID -> setupMyTeamClick()
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

    private fun setupAdministrationClick() {
        (getBaseActivity() as MainActivity).getNavGraph()
            .navigate(R.id.action_menu_to_accept_players)
    }

    private fun setupMyTeamClick() {
        (getBaseActivity() as MainActivity).getNavGraph()
            .navigate(R.id.action_menu_to_my_team)
    }

}