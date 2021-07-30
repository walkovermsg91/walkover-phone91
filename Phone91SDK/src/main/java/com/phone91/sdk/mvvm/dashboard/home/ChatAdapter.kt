package com.phone91.sdk.mvvm.dashboard.home

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.media.Image
import android.provider.ContactsContract
import android.text.format.DateUtils
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.phone91.sdk.R
import com.phone91.sdk.model.CallConnectionSignal
import com.phone91.sdk.model.CallStatusSignal
import com.phone91.sdk.model.ChatObject
import com.phone91.sdk.mvvm.dashboard.DashboardActivity
import com.phone91.sdk.utils.TimeUtil
import kotlinx.android.synthetic.main.item_chat.view.*


//import kotlinx.android.synthetic.main.item_post.view.*


class ChatAdapter(
    var context: Activity?,
    public var teamList: ArrayList<CallConnectionSignal>,
    var callbackClient: CallbackClient
) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    //      private var callbackClient:CallbackClient=context as CallbackClient
    var requestOptions: RequestOptions = RequestOptions()
        .placeholder(R.drawable.ic_image_placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(R.drawable.ic_image_placeholder)

    private var mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(mInflater.inflate(R.layout.item_chat, parent, false))
    }


    override fun getItemCount(): Int = teamList.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var chatObject = teamList.get(position)
        Log.e("onBindViewHolder", Gson().toJson(chatObject))
        // holder.txtMe.text=chatObject.content

//        holder.txtMe.visibility=View.GONE
//        holder.llMe.visibility=View.GONE
//        holder.llYou.visibility=View.GONE
//        holder.imgMe.visibility=View.GONE
//        holder.txtYou.visibility=View.GONE
//        holder.imgYou.visibility=View.GONE

        if ((context as DashboardActivity).email == null)
            Log.d("email is null? ", "  yes")
        if (chatObject.attachment_url == null && chatObject.content == null && chatObject.CallSignal == null && (context as DashboardActivity).email == null) {
            holder.llYou.visibility = View.GONE
            holder.llMe.visibility = View.GONE
            holder.llDetail.visibility = View.VISIBLE
            holder.llCallMsg.visibility = View.GONE
            holder.bind()
//            holder.edName.setOnKeyListener(this)
//            holder.edEmail.setOnKeyListener(this)
//            holder.edName.setOnKeyListener(this)


        } else if (chatObject.CallSignal != null) {
            holder.llYou.visibility = View.GONE
            holder.llMe.visibility = View.GONE
            holder.llDetail.visibility = View.GONE
            holder.llCallMsg.visibility = View.VISIBLE
            holder.txtSignal.text = chatObject.msg
            holder.txtTime.text = TimeUtil.covertTimeToText(chatObject.time)

        }else if (chatObject.type!=null&&chatObject.type.toString().equals("feedback")){
            holder.llYou.visibility = View.GONE
            holder.llMe.visibility = View.GONE
            holder.llDetail.visibility = View.GONE
            holder.llCallMsg.visibility = View.GONE
        } else {
            holder.llYou.visibility = View.VISIBLE
            holder.llMe.visibility = View.VISIBLE
            holder.llDetail.visibility = View.GONE
            holder.llCallMsg.visibility = View.GONE
            if (chatObject.sender_id == null) {
                holder.llMe.visibility == View.VISIBLE
                holder.txMeTime.visibility == View.VISIBLE
                if (chatObject.time != null && !chatObject.time.toString().equals("-1")) {

                        holder.txMeTime.text =
                            TimeUtil.covertTimeToText(chatObject.time)//.toDuration(chatObject.time)
                } else {
                    holder.txMeTime.visibility == View.GONE
                }

                if (chatObject.content == null)
                    holder.txtMe.visibility = View.GONE
                else {
                    holder.txtMe.text = HtmlCompat.fromHtml(
                        chatObject.content!!.toString(),
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    ).toString().trim()
                    holder.txtMe.visibility = View.VISIBLE
                }
                holder.llYou.visibility = View.GONE
                if (chatObject.attachment_url != null && !chatObject.attachment_url.equals("")) {

                    holder.imgMe.visibility = View.VISIBLE
                    if (chatObject.mime_type == null || chatObject.mime_type?.replace("\"", "")
                            .toString().contains("image/") == true
                    ) {
                        Glide.with(context!!)
                            .load(chatObject.attachment_url)
                            .apply(requestOptions)
                            .into(holder.imgMe)
                    } else
                        holder.imgMe.setImageDrawable(
                            ContextCompat.getDrawable(
                                context!!,
                                R.drawable.txt
                            )
                        );


                } else
                    holder.imgMe.visibility = View.GONE

            }
            else {
                holder.llYou.visibility == View.VISIBLE
                holder.txtYouTime.visibility == View.VISIBLE
                holder.txtYouTime.text =
                    TimeUtil.covertTimeToText(chatObject.time)//TimeUtil.toDuration(chatObject.time)
                if (chatObject.content == null)
                    holder.txtYou.visibility = View.GONE
                else {
                    holder.txtYou.text = HtmlCompat.fromHtml(
                        chatObject.content!!.toString(),
                        HtmlCompat.FROM_HTML_MODE_COMPACT
                    ).toString().trim()
                    holder.txtYou.visibility = View.VISIBLE
                }
                holder.llMe.visibility = View.GONE

                if (chatObject.attachment_url != null && !chatObject.attachment_url.equals("")) {
                    holder.imgYou.visibility = View.VISIBLE
                    if (chatObject.mime_type == null || chatObject.mime_type?.replace("\"", "")
                            .toString().contains("image/") == true
                    ) {
                        Glide.with(context!!)
                            .load(chatObject.attachment_url)
                            .apply(requestOptions)
                            .into(holder.imgYou)
                    } else
                        holder.imgYou.setImageDrawable(
                            ContextCompat.getDrawable(
                                context!!,
                                R.drawable.txt
                            )
                        );
                } else
                    holder.imgYou.visibility = View.GONE
            }
        }

    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var llMe = view.llMe!!
        var llYou = view.llYou!!
        var txtMe = view.txtMe!!
        var txtYou = view.txtYou!!
        var imgYou = view.imgYou!!
        var imgMe = view.imgMe!!
        var llDetail = view.llDetail
        var edName = view.edName
        var edEmail = view.edEmail
        var edPhone = view.edPhone
        var txMeTime = view.txMeTime
        var txtYouTime = view.txtYouTime
        var llName = view.llName
        var llEmail = view.llEmail
        var llPhone = view.llPhone
        var btnName = view.btnName
        var btnEmail = view.btnEmail
        var btnPhone = view.btnPhone
        var txtName = view.txtName
        var txtEmail = view.txtEmail
        var txtNumber = view.txtNumber
        var llCallMsg = view.llCallMsg
        var txtSignal = view.txtSignal
        var txtTime = view.txtTime
        public fun bind() {
            btnEmail.setOnClickListener(this)
            btnName.setOnClickListener(this)
            btnPhone.setOnClickListener(this)

            edName.requestFocus()
            edName.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (event.action === KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (edName.text.toString().trim().equals("")) {
                        Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT)
                            .show()
                        return@OnKeyListener true
                    }
                    llEmail.visibility = View.VISIBLE
                    txtEmail.visibility = View.VISIBLE
                    edEmail.requestFocus()
                }
                return@OnKeyListener true
            })

            edEmail.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (event.action === KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (edEmail.text.toString().trim()
                            .equals("") || !Patterns.EMAIL_ADDRESS.matcher(
                            edEmail.text.toString().trim()
                        ).matches()
                    ) {
                        Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT)
                            .show()
                        return@OnKeyListener true
                    }
                    llPhone.visibility = View.VISIBLE
                    txtNumber.visibility = View.VISIBLE
                    edPhone.requestFocus()
                }
                return@OnKeyListener true
            })

            edPhone.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (event.action === KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (edName.text.toString().trim().equals("")) {
                        Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT)
                            .show()
                        return@OnKeyListener true
                    }
                    if (edEmail.text.toString().trim()
                            .equals("") || !Patterns.EMAIL_ADDRESS.matcher(
                            edEmail.text.toString().trim()
                        ).matches()
                    ) {
                        Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT)
                            .show()
                        return@OnKeyListener true
                    }
                    if (edPhone.text.toString().trim()
                            .equals("") || !Patterns.PHONE.matcher(edPhone.text.toString().trim())
                            .matches()
                    ) {
                        Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT)
                            .show()
                        return@OnKeyListener true
                    }
                    llDetail.visibility = View.GONE
                    teamList.removeAt(1)
                    callbackClient.callList(
                        edName.text.toString().trim(),
                        edEmail.text.toString().trim(),
                        edPhone.text.toString().trim()
                    )
//                    callbackClient.onsetClient(edName.text.toString().trim(),edEmail.text.toString().trim(),edPhone.text.toString().trim())
                }
                return@OnKeyListener false
            })

        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.btnName ->
                    if (edName.text.toString().trim().equals("")) {
                        Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        llEmail.visibility = View.VISIBLE
                        txtEmail.visibility = View.VISIBLE
                        edEmail.requestFocus()
                    }

                R.id.btnEmail ->

                    if (edEmail.text.toString().trim()
                            .equals("") || !Patterns.EMAIL_ADDRESS.matcher(
                            edEmail.text.toString().trim()
                        ).matches()
                    ) {
                        Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        llPhone.visibility = View.VISIBLE
                        txtNumber.visibility = View.VISIBLE
                        edPhone.requestFocus()
                    }
                R.id.btnPhone ->
                    if (edName.text.toString().trim().equals("")) {
                        Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT)
                            .show()

                    } else if (edEmail.text.toString().trim()
                            .equals("") || !Patterns.EMAIL_ADDRESS.matcher(
                            edEmail.text.toString().trim()
                        ).matches()
                    ) {
                        Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT)
                            .show()

                    } else
                        if (edPhone.text.toString().trim().equals("") || !Patterns.PHONE.matcher(
                                edPhone.text.toString().trim()
                            ).matches()
                        ) {
                            Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT)
                                .show()

                        } else {
                            llDetail.visibility = View.GONE
                            teamList.removeAt(1)
//                        callbackClient.onsetClient(
//                            edName.text.toString().trim(),
//                            edEmail.text.toString().trim(),
//                            edPhone.text.toString().trim()
//                        )

                            callbackClient.callList(
                                edName.text.toString().trim(),
                                edEmail.text.toString().trim(),
                                edPhone.text.toString().trim()
                            )
                        }
            }
        }
    }

    fun add(message: CallConnectionSignal) {
        teamList.add(message)
        Log.d("shadga", teamList.toString())
        if (context != null && itemCount == 1 && (context as DashboardActivity).email == null)
            teamList.add(CallConnectionSignal())
//        else if(itemCount==1)
//             callbackClient.callList()

        (context as Activity?)!!.runOnUiThread {

            notifyDataSetChanged()


        }


    }

    interface CallbackClient {
        public fun onsetClient(name: String, email: String, phone: String)
        public fun callList(name: String, email: String, phone: String)
        public fun callButtonStatusListener(callStatusSignal: CallStatusSignal)
        public fun callConnectionListener(callConnectionSignal: CallConnectionSignal)
        public fun connectionErrorListener(massage: String?)
    }

    public fun callButtonStatus(callStatusSignal: CallStatusSignal) {
        callbackClient.callButtonStatusListener(callStatusSignal)
    }


    public fun errorMessge(massage: String?) {
        callbackClient.connectionErrorListener(massage)
    }

    public fun callConnectionStatus(callConnectionSignal: CallConnectionSignal) {
        callbackClient.callConnectionListener(callConnectionSignal)
    }


}
