package com.phone91.sdk.mvvm.base

import androidx.lifecycle.ViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import android.widget.Toast
import com.phone91.sdk.MyApplication
import com.phone91.sdk.routing.NavigationController
import com.phone91.sdk.routing.dashboard.DashboardRouteManager
import com.phone91.sdk.utils.Helper
import com.phone91.sdk.utils.NetworkUtils
import javax.inject.Inject

abstract class BaseActivity<out V : ViewModel> : AppCompatActivity(), BaseFragment.Callback {


    private lateinit var mViewModel: V
    var allowTransaction = true
    lateinit var helper: Helper
    lateinit var navigationController: NavigationController



    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        navigationController= NavigationController.getInstance(this, DashboardRouteManager(this))
        helper = Helper(this)
    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

    override fun onTransitionStarted() {
        allowTransaction = false
    }

    override fun onTransitionEnded() {
        allowTransaction = true
    }

    override fun onTransitionRepeated() {
        allowTransaction = false
    }

    override fun onResume() {
        super.onResume()

        allowTransaction = true
    }

    override fun onStop() {
        super.onStop()

        allowTransaction = false
    }

    private fun performDependencyInjection() {
//        AndroidInjection.inject(this)
        mViewModel = getViewModel()
    }

    fun isNetworkConnected(): Boolean {
        val flag = NetworkUtils.isNetworkConnected(applicationContext)
        if (!flag) {
//            showErrorToast("Internet not connected!")
        }
        return flag
    }

   /* fun isNetworkConnected(): Boolean = NetworkUtils.isNetworkConnected(applicationContext)*/

    fun hideKeyboard() {
        /*val view: View? = this.currentFocus
        val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)*/

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    fun showShortToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showNotificationDialog() {
       /* navigationController.launchNotificationDialogFragment(this)*/
    }

    fun showErrorToast(message: String) {
        Toast.makeText(this, message + "", Toast.LENGTH_SHORT).show()
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract fun getViewModel(): V

    open fun hideBottomTabs() {

    }

    open fun showBottomTabs() {

    }

}