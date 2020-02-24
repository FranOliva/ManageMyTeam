package es.us.managemyteam.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import es.us.managemyteam.R
import es.us.managemyteam.databinding.ActivityMainBinding

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
    }

    private fun setupToolbar() {
        setSupportActionBar(getToolbar())
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    //region BaseActivity

    fun getToolbar() = viewBinding.toolbar

    fun getBottomBar() = viewBinding.mainBottombarMenu

    fun getNavGraph() = navController

    //endregion
}
