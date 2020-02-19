package es.us.managemyteam.ui.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.ui.view.CustomToolbar

abstract class BaseActivity : AppCompatActivity() {

    abstract fun getToolbar(): CustomToolbar

    abstract fun getBottomBar(): BottomNavigationView

//    abstract fun getVerticalNavigation(): CommonVerticalNavigationView

    abstract fun getNavGraph(): NavController

}