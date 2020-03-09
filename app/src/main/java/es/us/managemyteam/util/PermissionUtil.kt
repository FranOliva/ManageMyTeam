package es.us.managemyteam.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import es.us.managemyteam.constant.Permission

class PermissionUtil {

    companion object {

        @JvmStatic
        fun needPermission(context: Context, vararg permissions: String): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissions.forEach {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            it
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return true
                    }
                }
            }

            return false
        }

        @JvmStatic
        fun requestPermission(fragment: Fragment, vararg permissions: String) {
            fragment.requestPermissions(permissions, Permission.REQUEST_CODE)
        }

        @JvmStatic
        fun requestPermission(activity: FragmentActivity, vararg permissions: String) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(permissions, Permission.REQUEST_CODE)
            }
        }

        @JvmStatic
        fun isPermissionGranted(requestCode: Int, responses: IntArray): Boolean {
            return requestCode == Permission.REQUEST_CODE &&
                    !responses.contains(PackageManager.PERMISSION_DENIED)
        }

    }

}