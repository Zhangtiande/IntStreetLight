package com.app.intstreetlight.ui.device

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.intstreetlight.R
import com.app.intstreetlight.StreetLightApplication.Companion.deviceList
import com.app.intstreetlight.StreetLightApplication.Companion.index


class DeviceFragment : Fragment() {
    private lateinit var ambientLight: TextView
    private lateinit var automaticDimming: TextView
    private lateinit var lightIntensity: TextView
    private lateinit var fog: TextView
    private lateinit var raindrops: TextView
    private lateinit var statusText: TextView
    private lateinit var statusImg: ImageView

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_device, container, false)
        ambientLight = v.findViewById(R.id.AmbientLight)
        lightIntensity = v.findViewById(R.id.LightIntensity)
        raindrops = v.findViewById(R.id.Raindrops)
        automaticDimming = v.findViewById(R.id.Automatic_Dimming)
        fog = v.findViewById(R.id.Fog)
        statusText = v.findViewById(R.id.status_detail)
        statusImg = v.findViewById(R.id.status_detail_img)
        with(deviceList[index]) {
            v.findViewById<TextView>(R.id.detail_name).text = this.deviceObj.deviceName
            if (this.properties != null) {
                ambientLight.text = (this.properties!!["AmbientLight"] as Int).toString()
                lightIntensity.text = (this.properties!!["LightIntensity"] as Int).toString()
                raindrops.text = (this.properties!!["Raindrops"] as Int).toString()
                automaticDimming.text = getBool(this.properties!!["AutomaticDimming"] as Int)
                fog.text = getBool(this.properties!!["Fog"] as Int)
                this.deviceObj.status.also {
                    statusText.text = it
                    if (it == "ONLINE") {
                        statusImg.setImageResource(R.mipmap.on_line)
                    }else{
                        statusImg.setImageResource(R.mipmap.off_line)
                    }
                }
            }else{
                ambientLight.text = "0"
                lightIntensity.text = "0"
                raindrops.text = "0"
                automaticDimming.text = "False"
                fog.text = "False"
                this.deviceObj.status.also {
                    statusText.text = it
                    if (it == "ONLINE") {
                        statusImg.setImageResource(R.mipmap.on_line)
                    }else{
                        statusImg.setImageResource(R.mipmap.off_line)
                    }
                }
            }
        }
        return v
    }

    private fun getBool(i: Int): String{
        return if (i==1) "True"
        else "False"
    }

    companion object {
        @JvmStatic
        fun newInstance() = DeviceFragment()
    }
}