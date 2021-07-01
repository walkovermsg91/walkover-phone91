package com.phone91.sdk.mvvm.dashboard.teams

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.phone91.sdk.R
import com.phone91.sdk.model.TeamObject
import kotlinx.android.synthetic.main.item_team.view.*

//import kotlinx.android.synthetic.main.item_post.view.*


class TeamAdapter(var context: Context?, public var teamList: ArrayList<TeamObject>) :
    RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

    private lateinit var onItemSelectionListener: OnItemSelectionListener
//    private  var receipentListFiltered=receipent

    fun setOnItemSelectionListener(listener: OnItemSelectionListener) {
        this.onItemSelectionListener = listener
    }

    private var mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(mInflater.inflate(R.layout.item_team, parent, false))
    }

    override fun getItemCount(): Int = teamList.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvname.text=teamList.get(position).name.toString()
        holder.itemView.setOnClickListener {
            onItemSelectionListener.onTeamSelected(position)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvname = view.textTeamame!!
    }


    interface OnItemSelectionListener {
        fun onTeamSelected(pos: Int)

    }

}