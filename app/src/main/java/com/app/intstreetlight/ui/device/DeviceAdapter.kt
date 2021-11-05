package com.app.intstreetlight.ui.device

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.intstreetlight.R
import com.app.intstreetlight.StreetLightApplication.Companion.index
import com.huaweicloud.sdk.iotda.v5.model.QueryDeviceSimplify

class DeviceAdapter(
    private val context: Context,
    private val deviceList: ArrayList<QueryDeviceSimplify>
) :
    RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    inner class DeviceViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val img: ImageView = v.findViewById(R.id.status)
        val name: TextView = v.findViewById(R.id.deviceName)
        val id: TextView = v.findViewById(R.id.deviceId)

        init {
            id.setOnClickListener {
                index = adapterPosition
                val intent = Intent(context, DeviceActivity::class.java)
                context.startActivity(intent)
            }
            img.setOnClickListener{
                index = adapterPosition
                val intent = Intent(context, DeviceActivity::class.java)
                context.startActivity(intent)
            }
            name.setOnClickListener{
                index = adapterPosition
                val intent = Intent(context, DeviceActivity::class.java)
                context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.device_item, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        deviceList[position].apply {
            "设备名称：${this.deviceName}".also { holder.name.text = it }
            "设备Id：${this.deviceId}".also { holder.id.text = it }
            if (this.status == "ONLINE") {
                holder.img.setImageResource(R.mipmap.on_line)
            } else {
                holder.img.setImageResource(R.mipmap.off_line)
            }
        }
    }

    override fun getItemCount() = deviceList.size


}

