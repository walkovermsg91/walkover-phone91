package com.phone91.sdk.utils

import android.animation.ValueAnimator
import android.app.*
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Point
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class Helper(activity: Activity) {

    private var screenWidth = 0
    private var screenHeight = 0

    private var activity: Activity? = null
    private var progressDialog: ProgressDialog? = null
    private var dialog: Dialog? = null
    private var anim: RotateAnimation? = null
    private var prog: ImageView? = null
    internal var tvLoading: TextView? = null


    init {
        this.activity = activity
//        createFullScreenProgress(activity);

    }

    companion object {
        fun isHebrewLanguage(): Boolean {
            return Locale.getDefault().toString().equals("iw_IL")
        }

        fun updateToHebrew(view: View) {
            view.scaleX = -1F
        }

        fun getDaysLeft(endDate: String?): String? {
            var myFormat = SimpleDateFormat("yyyy-MM-dd")
            var inputString1: String? = endDate
            var days: String? = null
            var currDate: Date = Calendar.getInstance().time

            try {
                var date2: Date = myFormat.parse(inputString1)
                var diff: Long = date2.time - currDate.time
                days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toString()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return days
        }

        fun getPathFromUri(uri: Uri, activity: Activity): String {
            var cursor = activity.contentResolver.query(uri, null, null, null, null)
            cursor!!.moveToFirst()
            var document_id = cursor.getString(0)
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
            cursor.close()
            cursor = activity.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", arrayOf(document_id), null)
            cursor!!.moveToFirst()
            val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            cursor.close()

            return path
        }

        fun getUnit(quantity: Float?, type: Int): String? {
            var units = ""
            when (type) {
                1 -> units = "" + quantity!! + " Lt."
                2 -> units = "" + quantity!! + " Kg."
                3 -> units = "" + quantity!! + " Pc."
            }
            return units

        }

        fun getPrice(toString: Float?): String? {
            return "$ $toString"

        }

        fun setImage(file: File?) {
            if(file!=null)
            this.file=file
        }

        var file: File? = null
        fun getImage() : File? {
          return this.file
        }

        fun getoneDecimal(quantity: Float?): Float {
            var mNumberFormat = NumberFormat.getInstance()
            mNumberFormat.minimumFractionDigits = 0
            mNumberFormat.maximumFractionDigits = 1
//            val df = DecimalFormat("#.##")
//            df.roundingMode = RoundingMode.CEILING
            return NumberFormat.getNumberInstance().parse(mNumberFormat.format(quantity)).toFloat()

        }
        fun gettwoDecimal(quantity: Double?): Float {
            var mNumberFormat = NumberFormat.getInstance()
            mNumberFormat.minimumFractionDigits = 0
            mNumberFormat.maximumFractionDigits = 2
//            val df = DecimalFormat("#.##")
//            df.roundingMode = RoundingMode.CEILING
            return NumberFormat.getNumberInstance().parse(mNumberFormat.format(quantity)).toFloat()

        }

        fun getFormattedDateFromTimestamp(timestampInMilliSeconds: Long, strFormat: String, blnIsAMPM: Boolean): String {
            val date = Date()
            //date.setTime(timestampInMilliSeconds * 1000L);
            date.time = timestampInMilliSeconds

            var strFormattedDate = SimpleDateFormat(strFormat).format(date)

            if (blnIsAMPM) {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = timestampInMilliSeconds
                val intTimeAMPM = calendar.get(Calendar.AM_PM)

                if (intTimeAMPM == 0) {
                    strFormattedDate += " AM"
                } else if (intTimeAMPM == 1) {
                    strFormattedDate += " PM"
                }
            }
            return strFormattedDate

        }
        fun getImageUri(context: Context,inImage: Bitmap): Uri {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(
                context!!.getContentResolver(),
                inImage,
                "Title",
                null
            )
            return Uri.parse(path)
        }

        fun isBoolean(value: String): Boolean {
            if (value.toLowerCase() == "true" || value.toLowerCase() == "false")
                return true
            return false
        }

        fun isValidString(value: String): Boolean {
            if (!TextUtils.isEmpty(value))
                return true
            return false
        }

        fun isSoldUnitValue(value: String): Boolean {
            if (!TextUtils.isEmpty(value)){
                if (isOnlyNumericValue(value)){
                    if (value.toInt() == 1 || value.toInt() == 2 ||value.toInt() == 3)
                        return true

                }
            }

            return false
        }

        fun isOnlyNumericValue(value: String): Boolean {
            val strRegEx = "[0-9]+"
            if (!TextUtils.isEmpty(value)) {
                if (value.matches("[0-9]+".toRegex())) {
                    return true
                }
            }

            return false
        }

        fun isOnlyDecimalValue(value: String): Boolean {
            val decimalRegEx = "^[1-9]\\d*(\\.\\d+)?\$"
            if (!TextUtils.isEmpty(value)) {
                if (value.matches(decimalRegEx.toRegex())) {
                    return true
                }
            }

            return false
        }


    }

//    fun createFullScreenProgress(ctx : Context)
//    {
//        dialog = Dialog(ctx)
//        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog!!.window.setBackgroundDrawable(ColorDrawable(0))
//        dialog!!.setContentView(R.layout.dialog_progress)
//        prog = dialog!!.findViewById(R.id.progressBarImageView)
//
//        anim =  RotateAnimation(0f,360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        anim!!.interpolator = LinearInterpolator();
//        anim!!.repeatCount = Animation.INFINITE;
//        anim!!.duration = 1000;
//    }


    fun getScreenHeight(c: Context): Int {
        if (screenHeight == 0) {
            val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            screenHeight = size.y
        }

        return screenHeight
    }

    fun getScreenWidth(c: Context?): Int {
        if (screenWidth == 0) {
            val wm = c?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            screenWidth = size.x
        }

        return screenWidth
    }

    fun isAndroid5(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    fun isAndroid6(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    fun showFullScreenProgress(cancellable: Boolean) {
        if (activity != null) {
            activity!!.runOnUiThread {
                dialog!!.setCancelable(cancellable)
                prog!!.startAnimation(anim)
                dialog!!.show()
            }
        }
    }

    fun dismissFullScreenProgress() {
        if (activity != null && dialog != null && dialog!!.isShowing) {
            prog!!.animation = null
            dialog!!.dismiss()
        }
    }

    fun showProgressDialog(title: String, message: String) {
        if (activity != null) {
            activity!!.runOnUiThread { progressDialog = ProgressDialog.show(activity, title, message) }
        }
    }

    fun isProgressActive(): Boolean {
        return if (progressDialog != null) {
            progressDialog!!.isShowing
        } else {
            false
        }
    }

    fun dismissProgressDialog() {
        if (activity != null && progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(activity).setTitle(title).setMessage(message).setPositiveButton("OK"
        ) { dialog, id -> dialog.dismiss() }.create().show()
    }

    fun showAlertDialogOnUiThread(title: String, message: String) {
        if (activity != null) {
            activity!!.runOnUiThread {
                AlertDialog.Builder(activity).setTitle(title).setMessage(message).setPositiveButton("OK"
                ) { dialog, id -> dialog.dismiss() }.create().show()
            }
        }
    }

    fun showErrorFinishAppDialogOnGuiThread(errorMessage: String) {
        if (activity != null) {
            activity!!.runOnUiThread {
                AlertDialog.Builder(activity).setMessage(errorMessage).setTitle("message").setPositiveButton("OK") { dialog, id ->
                    dialog.dismiss()
                    System.runFinalizersOnExit(true)
                    System.exit(0)
                }.create().show()
            }
        }
    }

    fun showErrorFinishAppDialog(errorMessage: String) {
        if (activity != null) {
            AlertDialog.Builder(activity).setMessage(errorMessage).setTitle("message").setPositiveButton("OK") { dialog, id ->
                dialog.dismiss()
                System.runFinalizersOnExit(true)
                System.exit(0)
            }.create().show()
        }
    }

    fun showErrorToast(message: String) {
//        ColorToast.makeText(activity, message, ColorToast.LENGTH_SHORT, ColorToast.ERROR, false).show()
    }

    fun showInfoToast(message: String?) {
//        if (message != null)
//        ColorToast.makeText(activity, message, ColorToast.LENGTH_SHORT, ColorToast.INFO, false).show()
    }


    fun showWarningToast(message: String?) {
//        if (message != null)
//        ColorToast.makeText(activity, message, ColorToast.LENGTH_SHORT, ColorToast.WARNING, false).show()
    }

    fun showSuccessToast(message: String?) {
//        if (message != null)
//        ColorToast.makeText(activity, message, ColorToast.LENGTH_SHORT, ColorToast.SUCCESS, false).show()
    }

    fun showShortToast(msg: String) {
        if (activity != null) {
            activity!!.runOnUiThread { Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show() }
        }
    }

    fun showLongToast(msg: String) {
        if (activity != null) {
            activity!!.runOnUiThread { Toast.makeText(activity, msg, Toast.LENGTH_LONG).show() }
        }
    }

    fun hideKeyboard() {
        val inputMethodManager = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity!!.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideWindowKeyboard() {
        activity?.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    fun showKeyboard() {
        val inputMethodManager = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity!!.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        //inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }


    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if (componentInfo?.packageName == context.packageName) {
                isInBackground = false
            }
        }

        return isInBackground
    }

    fun dpToPx(dp: Float): Float {
        return (dp * Resources.getSystem().getDisplayMetrics().density)
    }

    fun pxToDp(px: Float): Float {
        return (px / Resources.getSystem().getDisplayMetrics().density)
    }

    /*fun dpToPx(dp: Int): Int {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp.toFloat(), activity!!.resources.displayMetrics).toInt()
    }*/

    fun getAppVersionName(): String {
        val pm = activity!!.packageManager
        var pInfo: PackageInfo? = null

        try {
            pInfo = pm.getPackageInfo(activity!!.packageName, 0)
        } catch (e1: PackageManager.NameNotFoundException) {
            e1.printStackTrace()
        }

        return pInfo!!.versionName
    }

    fun getAppVersionCode(): Int {
        val pm = activity!!.packageManager
        var pInfo: PackageInfo? = null

        try {
            pInfo = pm.getPackageInfo(activity!!.packageName, 0)
        } catch (e1: PackageManager.NameNotFoundException) {
            e1.printStackTrace()
        }

        return pInfo!!.versionCode
    }

    /**
     * Returns the unique serial number of device
     */
    fun getSerialNumber(): String {
        return Build.SERIAL
    }

    fun getDeviceId(): String {
        return Settings.Secure.getString(activity!!.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getFormattedDateFromTimestamp(timestampInMilliSeconds: Long, strFormat: String, blnIsAMPM: Boolean): String {
        val date = Date()
        //date.setTime(timestampInMilliSeconds * 1000L);
        date.time = timestampInMilliSeconds

        var strFormattedDate = SimpleDateFormat(strFormat).format(date)

        if (blnIsAMPM) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestampInMilliSeconds
            val intTimeAMPM = calendar.get(Calendar.AM_PM)

            if (intTimeAMPM == 0) {
                strFormattedDate += " AM"
            } else if (intTimeAMPM == 1) {
                strFormattedDate += " PM"
            }
        }
        return strFormattedDate

    }


    fun getAddressFromLatLong(location: Location, context: Context): ArrayList<String> {
        val addressList = ArrayList<String>()
        try {
            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(context, Locale.getDefault())

            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            val address = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            val city = addresses[0].locality
            val state = addresses[0].adminArea
            val country = addresses[0].countryName
            val postalCode = addresses[0].postalCode
            val knownName = addresses[0].featureName // Only if available else return NULL

            addressList.add(city)
            addressList.add(state)
            addressList.add(country)
            addressList.add(address)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return addressList
    }

    private fun getDate(time: Long): Date {
        val cal = Calendar.getInstance()
        val tz = cal.timeZone//get your local time zone.
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a")
        sdf.timeZone = tz//set time zone.
        val localTime = sdf.format(Date(time * 1000))
        var date = Date()
        try {
            date = sdf.parse(localTime)//get local date
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return date
    }

    fun textAnimation(textView: TextView, value: Float) {
        val animator = ValueAnimator.ofFloat(0F, value)
        animator.duration = 1000
        animator.addUpdateListener { animation -> textView.text = "Total :" + animation.animatedValue.toString() }
        animator.start()
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)

        // Save a file: path for use with ACTION_VIEW intents
        return File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
    }


}