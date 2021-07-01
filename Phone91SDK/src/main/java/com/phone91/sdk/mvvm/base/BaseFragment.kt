package com.phone91.sdk.mvvm.base

import androidx.lifecycle.ViewModel
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.phone91.sdk.data.AppDataManager
import com.phone91.sdk.routing.NavigationController
import com.phone91.sdk.routing.dashboard.DashboardRouteManager
import com.phone91.sdk.utils.Helper
import com.phone91.sdk.utils.toolbar.FragmentToolbar
import com.phone91.sdk.utils.toolbar.ToolbarManager
//import dagger.android.support.AndroidSupportInjection
//import javax.inject.Inject

abstract class BaseFragment<out V : ViewModel> : Fragment() {

    private var mActivity: BaseActivity<*>? = null
    private var mViewModel: V? = null
    protected var root: ViewGroup? = null
    var isFragmentLoaded = false

//    @Inject
    lateinit var navigationController: NavigationController
     var appDataManager: AppDataManager?=null
    lateinit var helper: Helper
    lateinit var toolbar: ToolbarManager

    val keyboardLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        // navigation bar height
        var navigationBarHeight = 0
        var resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId)
        }

        // status bar height
        var statusBarHeight = 0
        resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }

        // display window size for the app layout
        val rect = Rect()
        activity?.getWindow()?.getDecorView()?.getWindowVisibleDisplayFrame(rect)

        // screen height - (user app height + status + nav) ..... if non-zero, then there is a soft keyboard
        val keyboardHeight = root?.getRootView()?.getHeight()!! - (statusBarHeight + navigationBarHeight + rect.height())

        if (keyboardHeight <= 0) {
            onKeyboardClose()
        } else {
            //onShowKeyboard(keyboardHeight)
            onKeyboardOpen()
        }
    }



    open fun onKeyboardOpen() {

    }

    open fun onKeyboardClose() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()

        navigationController= NavigationController.getInstance(context!!, DashboardRouteManager(context!!))
        setHasOptionsMenu(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //var mActivity: DashboardActivity = activity as DashboardActivity
        if (this.builder() != null) {
            toolbar = ToolbarManager(this.builder()!!, view)
            toolbar.prepareToolbar(mActivity!!, navigationController)
        }
//
        isFragmentLoaded = true
    }

    protected abstract fun builder(): FragmentToolbar?



    override fun onAttach(context: Context) {
//        performDependencyInjection()
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            val activity = context as BaseActivity<*>?
            this.mActivity = activity
            activity!!.onFragmentAttached()
            appDataManager= AppDataManager.getInstance(context.application)
            mActivity!!.helper = Helper(activity)
            helper = mActivity!!.helper
        }
    }

    override fun onResume() {
        super.onResume()

        hideKeyboard()
    }

    override fun onStop() {
        super.onStop()

        hideKeyboard()
    }

    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    /*override fun onDestroyView() {
        if (root?.getParent() != null) {
            (root?.getParent() as ViewGroup).removeView(root)
        }
        super.onDestroyView()
    }*/

    override fun onDestroy() {
        super.onDestroy()

        isFragmentLoaded = false
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        var animation: Animation? = super.onCreateAnimation(transit, enter, nextAnim)

        if (animation == null && nextAnim != 0) {
            animation = AnimationUtils.loadAnimation(activity, nextAnim)
        }

        if (animation != null) {
            view!!.setLayerType(View.LAYER_TYPE_HARDWARE, null)

            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    mActivity?.onTransitionStarted()
                }

                override fun onAnimationEnd(animation: Animation) {
                    if (view != null) {
                        view!!.setLayerType(View.LAYER_TYPE_NONE, null)
                    }
                    mActivity?.onTransitionEnded()
                }

                override fun onAnimationRepeat(animation: Animation) {
                    mActivity?.onTransitionRepeated()
                }
                // ...other AnimationListener methods go here...
            })
        }
        return animation
    }

    /**
     * @return the identifying enum for the current fragment.
     */

    fun getBaseActivity(): BaseActivity<*>? {
        return mActivity
    }


    fun isNetworkConnected(): Boolean {
        return mActivity != null && mActivity!!.isNetworkConnected()
    }

    fun hideKeyboard() {
        if (mActivity != null) {
            mActivity!!.hideKeyboard()
        }
    }

    fun getPrice() {
        if (mActivity != null) {
            mActivity!!.hideKeyboard()
        }
    }

    fun showShortToast(message: String) {
        if (mActivity != null) {
            mActivity!!.showShortToast(message)
        }
    }

    fun showNotificationDialog() {
        if (mActivity != null) {
            mActivity!!.showNotificationDialog()
        }
    }

    fun hideBottomTabs() {
        if (mActivity != null) {
            mActivity!!.hideBottomTabs()
        }
    }

    fun showBottomTabs() {
        if (mActivity != null) {
            mActivity!!.showBottomTabs()
        }
    }

    /*fun openActivityOnTokenExpire() {
          if (mActivity != null) {
              mActivity!!.openActivityOnTokenExpire()
          }
      }*/
    abstract fun getFragmentTAG(): FragmentTAG

    /**
     * An enumerator of TAGs for Fragments.
     */
    enum class FragmentTAG {
        HOME_FRAGMENT,TEAM_FRAGMENT
    }

//    private fun performDependencyInjection() {
//        AndroidSupportInjection.inject(this)
//    }

    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)

        fun onTransitionStarted()

        fun onTransitionEnded()

        fun onTransitionRepeated()
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract fun getViewModel(): V



}