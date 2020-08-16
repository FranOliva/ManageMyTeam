package es.us.managemyteam.ui.adapter

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import es.us.managemyteam.data.model.CallStatus
import es.us.managemyteam.ui.fragment.StateCallFragment

class CallPagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                StateCallFragment.newInstance(CallStatus.PENDING)
            }
            1 -> {
                StateCallFragment.newInstance(CallStatus.ACCEPTED)
            }
            else -> {
                StateCallFragment.newInstance(CallStatus.DENIED)
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun saveState(): Parcelable? {
        return null
    }
}