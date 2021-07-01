package com.phone91.sdk.mvvm.dashboard.teams


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phone91.sdk.R

import com.phone91.sdk.model.WidgetObject
import com.phone91.sdk.mvvm.base.BaseFragment
import com.phone91.sdk.mvvm.dashboard.DashboardActivity
//import com.phone91.sdk.mvvm.dashboard.postdetails.PostDetailsFragment
import com.phone91.sdk.utils.toolbar.FragmentToolbar
import kotlinx.android.synthetic.main.fragment_team.*

import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
class TeamFragment : BaseFragment<TeamViewModel>(), TeamAdapter.OnItemSelectionListener {


    private lateinit var progressDialog: Dialog



    private lateinit var teamViewModel: TeamViewModel


    companion object {



        var widgetObject: WidgetObject? = null
        //
        @JvmStatic
        fun newInstance(widgetObject: WidgetObject): TeamFragment {
            this.widgetObject = widgetObject
            var fragment = TeamFragment()
            return fragment
        }

    }

    override fun builder(): FragmentToolbar? {

        return FragmentToolbar.Builder()
            .withId(R.id.toolBarL)
            .withTitle(widgetObject?.name!!)
            .withDescription(widgetObject?.tagline!!)
            .withClose()
            .build()
    }



    override fun getFragmentTAG(): FragmentTAG = FragmentTAG.TEAM_FRAGMENT

    override fun getViewModel(): TeamViewModel {
        teamViewModel = ViewModelProviders.of(this@TeamFragment).get(TeamViewModel::class.java)/*ViewModelProviders.of(this@TeamFragment, appViewModelFactory)
            .get(TeamViewModel::class.java)*/

        return teamViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_team, container, false)
    }

    override fun onResume() {
        super.onResume()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = Dialog(activity!!)
        progressDialog.setCancelable(false)
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog.setContentView(R.layout.dialog_progress)

        var teamAdapter=TeamAdapter(activity, widgetObject?.teams!!)
        teamAdapter.setOnItemSelectionListener(this)
        var layoutManager = LinearLayoutManager(activity as DashboardActivity, RecyclerView.VERTICAL, false)
        rvPosts.layoutManager=layoutManager
        rvPosts.apply {
            adapter = teamAdapter
        }
    }



    override fun onTeamSelected(pos: Int) {
        (activity as DashboardActivity).pos=pos
        (activity as DashboardActivity).callTeamChat()
        }
}