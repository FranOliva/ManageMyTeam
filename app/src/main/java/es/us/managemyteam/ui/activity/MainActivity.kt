package es.us.managemyteam.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import es.us.managemyteam.R
import es.us.managemyteam.databinding.ActivityMainBinding
import es.us.managemyteam.ui.view.verticalnavigation.NeedCloseDrawerListener

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        navController = findNavController(R.id.main__container__nav_host)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupToolbar()
        setupBottomBar()
        setupSliderNavigationMenu()
    }

    private fun setupToolbar() {
        setSupportActionBar(getToolbar())
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false);
        }
    }

    private fun setupBottomBar() {
        getBottomBar().apply {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.action_profile ->  getNavGraph()?.navigate(R.id.action_menu_to_payment)
                    R.id.action_events -> findNavController().navigate(R.id.action_menu_to_club)
                    R.id.action_menu -> viewBinding.dashboardDrawerMain.openDrawer(
                        GravityCompat.END
                    )
                    else -> onNavigationItemSelected(it.itemId)
                }
                true
            }
        }
    }

    private fun setupSliderNavigationMenu() {
        viewBinding.dashboardDrawerMain.setScrimColor(Color.TRANSPARENT)
        viewBinding.dashboardDrawerMain.drawerElevation = 0f
        viewBinding.dashboardDrawerMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        viewBinding.mainNavigationMenu.setNeedCloseDrawerListener(object : NeedCloseDrawerListener {
            override fun onNeedClosingDrawer() {
                closeDrawer()
            }
        })
    }

    private fun closeDrawer() {
        viewBinding.dashboardDrawerMain.closeDrawers()
    }

    private fun onNavigationItemSelected(itemId: Int) {
        // TODO : Navigation of bottom menu items
        when (itemId) {
            //R.id.action_events -> getNavGraph().navigate((1))
            //R.id.action_menu_to_club -> getNavGraph().navigate((1))
           // R.id.action_chat -> getNavGraph().navigate((1))*/
            else -> Log.d("", "No action found for this id")
        }
    }

    fun getToolbar() = viewBinding.toolbar

    fun getBottomBar() = viewBinding.mainBottombarMenu

    fun getNavGraph() = navController

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}
