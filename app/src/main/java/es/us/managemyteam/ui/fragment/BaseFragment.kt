package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import es.us.managemyteam.ui.activity.MainActivity
import es.us.managemyteam.ui.view.CustomToolbar

abstract class BaseFragment<Any : ViewBinding> : Fragment() {

    companion object {
        const val ANIMATION_DELAYER_TIME = 200L
    }

    lateinit var viewBinding: Any

    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): Any

    abstract fun setupToolbar(toolbar: CustomToolbar)

    //abstract fun setupBottomBar(bottomNavigationView: CommonBottomNavigationView)

    //fun getToolbar() = (activity as MainActivity).getCustomToolbar()

    /*fun notifyVerticalMenuChanged() {
        (activity as MainActivity).getVerticalNavigation().notifyUserChanged()
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        setupMenuViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = inflateViewBinding(inflater, container)
        return viewBinding.root
    }


    private fun setupMenuViews() {
        if (activity is MainActivity) {
            //setupToolbar((activity as MainActivity).getCustomToolbar().apply { setNavAction { } })
            //setupBottomBar((activity as MainActivity).getBottomBar())
        }
    }

}