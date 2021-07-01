package com.phone91.sdk.routing.dashboard

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.phone91.sdk.R
import com.phone91.sdk.mvvm.base.BaseFragment
import com.phone91.sdk.mvvm.dashboard.home.HomeFragment
import com.phone91.sdk.mvvm.dashboard.teams.TeamFragment
//import com.mbaka.ui.R
//import com.mbaka.ui.mvvm.base.BaseFragment
//import com.mbaka.ui.mvvm.createpost.CreatePostActivity
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
//import com.mbaka.ui.mvvm.dashboard.videoactivity.IncomingVideoCallActivity
//import com.mbaka.ui.mvvm.dashboard.videoactivity.VideoActivity
//import com.mbaka.ui.mvvm.dashboard.videoplayer.VideoPlayerFragment
import com.phone91.sdk.utils.*
import javax.inject.Inject

class DashboardRouteManager

constructor(val context: Context) : DashboardController {



    override val homeFragmentTag: String
        get() = HOME_FRAGMENT_TAG
    override val teamFragmentTag: String
        get() = TEAM_FRAGMENT_TAG



    override fun openHomeFragment(context: Context, containerId: Int, homeFragment: HomeFragment) {
        val activity = context as FragmentActivity
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.beginTransaction()
            ?.replace(containerId, homeFragment, homeFragmentTag)?.commit()
    }

    override fun openTeamFragment(context: Context, containerId: Int, teamFragment: TeamFragment) {
        val activity = context as FragmentActivity
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.beginTransaction()
            ?.replace(containerId, teamFragment, teamFragmentTag)?.commit()
    }

    override fun openHomeFragmentR(context: Context, containerId: Int, homeFragment: HomeFragment) {
        replaceFragment(
            context,
            homeFragment,
            containerId,
            true,
            true,
            homeFragmentTag
        )
    }
//
//
//    override fun openTabParentFragment(
//        context: Context,
//        containerId: Int,
//        homeFragment: TabParentFragment
//    ) {
//        val activity = context as FragmentActivity
//        val fragmentManager = activity.supportFragmentManager
//        fragmentManager.beginTransaction()
//            ?.replace(containerId, homeFragment, tabHomeFragmentTag)?.commitNow()
//    }
//
//    override fun openChangePasswordFragment(
//        context: Context,
//        containerId: Int,
//        changePasswordFragment: ChangePasswordFragment
//    ) {
//        replaceFragment(
//            context,
//            changePasswordFragment,
//            containerId,
//            true,
//            true,
//            changePasswordFragmentTag
//        )
//    }
//
//    override fun openProfileFragment(
//        context: Context,
//        containerId: Int,
//        profileFragment: ProfileFragment
//    ) {
//        replaceFragment(context, profileFragment, containerId, true, true, profileFragmentTag)
//    }
//
//    override fun openSubscriptionFragment(
//        context: Context,
//        containerId: Int,
//        subscriptionFragment: SubscriptionFragment
//    ) {
//        replaceFragment(
//            context,
//            subscriptionFragment,
//            containerId,
//            true,
//            true,
//            subscriptionFragmentTag
//        )
//    }
//
//    override fun openReferralCodeFragment(
//        context: Context,
//        containerId: Int,
//        referralCodeFragment: ReferralCodeFragment
//    ) {
//        replaceFragment(
//            context,
//            referralCodeFragment,
//            containerId,
//            true,
//            true,
//            referralCodeFragmentTag
//        )
//    }
//
//    override fun openMusicFragment(
//        context: Context,
//        containerId: Int,
//        musicFragment: MusicFragment
//    ) {
//        replaceFragment(context, musicFragment, containerId, true, true, musicFragmentTag)
//    }
//
//    override fun openEditProfileFragment(
//        context: Context,
//        containerId: Int,
//        editProfileFragment: EditProfileFragment
//    ) {
//        replaceFragment(
//            context,
//            editProfileFragment,
//            containerId,
//            true,
//            true,
//            editProfileFragmentTag
//        )
//    }
//
//    override fun openMusicListFragment(
//        context: Context,
//        containerId: Int,
//        musicListFragment: MusicListFragment
//    ) {
//        replaceFragment(context, musicListFragment, containerId, true, true, musicListFragmentTag)
//    }
//
//    override fun openProjectListFragment(
//        context: Context,
//        containerId: Int,
//        projectListFragment: ProjectListFragment
//    ) {
//        replaceFragment(
//            context,
//            projectListFragment,
//            containerId,
//            true,
//            true,
//            projectListFragmentTag
//        )
//    }
//
//    override fun openProjectDetailFragment(
//        context: Context,
//        containerId: Int,
//        projectDetailFragment: ProjectDetailFragment
//    ) {
//        replaceFragment(
//            context,
//            projectDetailFragment,
//            containerId,
//            true,
//            true,
//            projectDetailFragmentTag
//        )
//    }
//
//    override fun openNotificationListFragment(
//        context: Context,
//        containerId: Int,
//        notificationListFragment: NotificationListFragment
//    ) {
//        replaceFragment(
//            context,
//            notificationListFragment,
//            containerId,
//            true,
//            true,
//            notificationListFragmentTag
//        )
//    }
//
//    override fun openSettingsFragment(
//        context: Context,
//        containerId: Int,
//        settingsFragment: SettingsFragment
//    ) {
//        replaceFragment(context, settingsFragment, containerId, true, true, settingsFragmentTag)
//    }
//
//    override fun openTNCFragment(context: Context, containerId: Int, tNCFragment: TNCFragment) {
//        replaceFragment(context, tNCFragment, containerId, true, true, tNCFragmentTag)
//    }
//
//
//    override fun openVideoFragment(
//        context: Context,
//        containerId: Int,
//        videoPlayerFragment: VideoPlayerFragment
//    ) {
//        replaceFragment(
//            context,
//            videoPlayerFragment,
//            containerId,
//            true,
//            true,
//            videoPlayerFragmentTag
//        )
//    }
//
//    override fun openCustomYTPlayerFragment(
//        context: Context,
//        containerId: Int,
//        customYTPlayerFragment: CustomYTPlayerFragment
//    ) {
//        replaceFragment(
//            context,
//            customYTPlayerFragment,
//            containerId,
//            true,
//            true,
//            customYTPlayerFragmentTag
//        )
//    }
//
//    override fun openChatFragment(context: Context, containerId: Int, chatFragment: ChatFragment){
//        replaceFragment(context, chatFragment, containerId, true, true, chatFragmentTag)
//    }
//
//    override fun openVideoActivity(bundle: String) {
//        context.apply {
//            val intent = Intent(context, VideoActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            intent.putExtra("url", bundle)
//            startActivity(intent)
//        }
//    }
//
//    override fun openCreatePostActivity() {
//        context.apply {
//            val intent = Intent(context, CreatePostActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
//        }
//    }
//
//    //
//
//
//    override fun openIncomingVideoCallActivity(bundle: String) {
//        context.apply {
//            val intent = Intent(context, IncomingVideoCallActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            intent.putExtra("videofile", "https://adorationministry.com/public/videos/video-notifications/1579168392_20-01-16_878194_video_file.mp4")
//            startActivity(intent)
//        }
//    }
//
    private fun replaceFragment(
    context: Context?,
    fragment: BaseFragment<ViewModel>,
    containerId: Int,
    isAddToBackStack: Boolean = false,
    withAnimation: Boolean = true,
    tag: String
    ) {
        val activity = context as AppCompatActivity
        val transaction = activity.supportFragmentManager.beginTransaction()
        if (isAddToBackStack) {
            transaction?.addToBackStack(tag)
        }

        if (withAnimation) {
            transaction?.setCustomAnimations(
                R.anim.trans_left_in,
                R.anim.trans_left_out,
                R.anim.trans_right_in,
                R.anim.trans_right_out
            )
        }
        transaction?.replace(containerId, fragment, fragment.getFragmentTAG().name)
        transaction?.commit()
    }

}
