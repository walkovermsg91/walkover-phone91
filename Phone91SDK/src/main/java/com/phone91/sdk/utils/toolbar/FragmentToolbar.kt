package com.phone91.sdk.utils.toolbar

import androidx.annotation.BoolRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes

class FragmentToolbar
private constructor(@IdRes val resId: Int,
                    @StringRes val tvTitle: String?,
                    @StringRes val tvDes: String?,
                    @BoolRes val isClose: Boolean
) {

    companion object {
        @JvmField
        val NO_TOOLBAR = -1
        val NO_NOTIFICATION = -1
        val NO_FILTER = -1
    }


    class Builder {
        private var resId: Int = -1
        private var tvTitle: String = ""
        private var tvDes: String = ""
        private var isClose: Boolean = false



        /***
         * Pass FragmentToolbar.NO_TOOLBAR as ID if no toolbar required.
         * */

        fun withId(@IdRes resId: Int) = apply { this.resId = resId }

        fun withTitle(title: String) = apply { this.tvTitle = title }
        fun withDescription(description: String) = apply { this.tvDes = description }

        fun withClose() = apply { this.isClose = true }



        fun build() = FragmentToolbar(resId, tvTitle,tvDes, isClose)
    }

}