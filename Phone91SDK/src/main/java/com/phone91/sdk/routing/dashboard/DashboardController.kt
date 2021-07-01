package com.phone91.sdk.routing.dashboard

import android.content.Context
import com.phone91.sdk.mvvm.dashboard.home.HomeFragment
import com.phone91.sdk.mvvm.dashboard.teams.TeamFragment

//import com.mbaka.ui.mvvm.dashboard.changepassword.ChangePasswordFragment
//import com.mbaka.ui.mvvm.dashboard.chat.ChatFragment
//import com.mbaka.ui.mvvm.dashboard.customvideoplayer.CustomYTPlayerFragment
//import com.mbaka.ui.mvvm.dashboard.donation.ProjectListFragment
//import com.mbaka.ui.mvvm.dashboard.donation.projectdetail.ProjectDetailFragment
//import com.mbaka.ui.mvvm.dashboard.editprofile.EditProfileFragment
//import com.mbaka.ui.mvvm.dashboard.gospelmusic.list.MusicListFragment
//import com.mbaka.ui.mvvm.dashboard.gospelmusic.music.MusicFragment
//import com.mbaka.ui.mvvm.dashboard.home.HomeFragment
//import com.mbaka.ui.mvvm.dashboard.monthlysubscription.SubscriptionFragment
//import com.mbaka.ui.mvvm.dashboard.notification.NotificationListFragment
//import com.mbaka.ui.mvvm.dashboard.postdetails.PostDetailsFragment
//import com.mbaka.ui.mvvm.dashboard.referralcode.ReferralCodeFragment
//import com.mbaka.ui.mvvm.dashboard.settingsfragment.SettingsFragment
//import com.mbaka.ui.mvvm.dashboard.tabParentFragment.TabParentFragment
//import com.mbaka.ui.mvvm.dashboard.tnc.TNCFragment
//import com.mbaka.ui.mvvm.dashboard.userprofile.ProfileFragment
//import com.mbaka.ui.mvvm.dashboard.videoplayer.VideoPlayerFragment


interface DashboardController {

    val homeFragmentTag: String
    val teamFragmentTag: String



    fun openHomeFragment(context: Context, containerId: Int, homeFragment: HomeFragment)
    fun openHomeFragmentR(context: Context, containerId: Int, homeFragment: HomeFragment)
    fun openTeamFragment(context: Context, containerId: Int, teamFragment: TeamFragment)

}