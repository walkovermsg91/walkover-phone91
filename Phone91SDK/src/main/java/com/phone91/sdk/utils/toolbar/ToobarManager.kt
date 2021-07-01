package com.phone91.sdk.utils.toolbar

//import com.canary.mvvm.home.notification.NotificationPopup
import android.content.Context
import androidx.core.view.GravityCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View
import com.phone91.sdk.mvvm.dashboard.DashboardActivity
//import com.mbaka.ui.mvvm.dashboard.DashboardActivity
//import com.titan.ui.mvvm.dashboard.DashboardActivity
import com.phone91.sdk.routing.NavigationController
import kotlinx.android.synthetic.main.layout_toolbar.view.*

//import kotlinx.android.synthetic.main.activity_dashboard.*
//import kotlinx.android.synthetic.main.layout_toolbar.view.*

//import com.titan.ui.dashboard.DashboardActivity
//import kotlinx.android.synthetic.main.activity_dashboard.*
//import kotlinx.android.synthetic.main.layout_toolbar.view.*


//import com.labo.kaji.relativepopupwindow.RelativePopupWindow
//import kotlinx.android.synthetic.main.layout_toolbar.view.*

class ToolbarManager constructor(
        private var builder: FragmentToolbar,
        private var container: View) {


    /***
     * Choose FragmentToolbar.NO_TOOLBAR as ID, if no toolbar required.
     * */
    fun prepareToolbar(context: Context, navigationController: NavigationController) {
        if (builder.resId != FragmentToolbar.NO_TOOLBAR) {

            val fragmentToolbar = container.findViewById(builder.resId) as Toolbar

            if (builder.tvTitle != "") {
                fragmentToolbar.tvTitle.text = builder.tvTitle
            }

            if (builder.tvDes != null) {
                fragmentToolbar.tvDes.text = builder.tvDes
            }


            if (builder.isClose) {
                fragmentToolbar.ivClose.visibility = View.VISIBLE
                fragmentToolbar.ivClose.setOnClickListener {
                    (context as DashboardActivity).close()
//                    (context as DashboardActivity).drawer_layout.openDrawer(GravityCompat.START)
                }

            } else {
               fragmentToolbar.ivClose.visibility = View.INVISIBLE
            }
        }
    }
}

