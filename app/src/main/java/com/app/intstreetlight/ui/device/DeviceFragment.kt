package com.app.intstreetlight.ui.device

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.intstreetlight.StreetLightApplication.Companion.deviceList
import com.app.intstreetlight.StreetLightApplication.Companion.index
import com.app.intstreetlight.ui.chart.XAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class DeviceFragment : Fragment() {
    private lateinit var ambientLight: TextView
    private lateinit var automaticDimming: TextView
    private lateinit var lightIntensity: TextView
    private lateinit var fog: TextView
    private lateinit var raindrops: TextView
    private lateinit var statusText: TextView
    private lateinit var statusImg: ImageView
    private lateinit var lineChart: LineChart

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(com.app.intstreetlight.R.layout.fragment_device, container, false)
        ambientLight = v.findViewById(com.app.intstreetlight.R.id.AmbientLight)
        lightIntensity = v.findViewById(com.app.intstreetlight.R.id.LightIntensity)
        raindrops = v.findViewById(com.app.intstreetlight.R.id.Raindrops)
        automaticDimming = v.findViewById(com.app.intstreetlight.R.id.Automatic_Dimming)
        fog = v.findViewById(com.app.intstreetlight.R.id.Fog)
        statusText = v.findViewById(com.app.intstreetlight.R.id.status_detail)
        statusImg = v.findViewById(com.app.intstreetlight.R.id.status_detail_img)
        lineChart = v.findViewById(com.app.intstreetlight.R.id.lineChart)
        with(deviceList[index]) {
            v.findViewById<TextView>(com.app.intstreetlight.R.id.detail_name).text =
                this.deviceObj.deviceName
            if (this.properties != null) {
                ambientLight.text = (this.properties!!["AmbientLight"] as Int).toString()
                lightIntensity.text = (this.properties!!["LightIntensity"] as Int).toString()
                raindrops.text = (this.properties!!["Raindrops"] as Int).toString()
                automaticDimming.text = getBool(this.properties!!["AutomaticDimming"] as Int)
                fog.text = getBool(this.properties!!["Fog"] as Int)
                this.deviceObj.status.also {
                    statusText.text = it
                    if (it == "ONLINE") {
                        statusImg.setImageResource(com.app.intstreetlight.R.mipmap.on_line)
                    }else{
                        statusImg.setImageResource(com.app.intstreetlight.R.mipmap.off_line)
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
                        statusImg.setImageResource(com.app.intstreetlight.R.mipmap.on_line)
                    } else {
                        statusImg.setImageResource(com.app.intstreetlight.R.mipmap.off_line)
                    }
                }
            }
        }
        chartDraw()
        return v
    }

    private fun getBool(i: Int): String {
        return if (i == 1) "True"
        else "False"
    }

    companion object {
        @JvmStatic
        fun newInstance() = DeviceFragment()
    }


    private fun chartDraw() {
        val entries: ArrayList<Entry> = ArrayList()
        var yIterator = deviceList[index].datList!!.ambientLight.iterator()
        var xIterator = deviceList[index].datList!!.time.iterator()
        while (xIterator.hasNext()) {
            entries.add(Entry(xIterator.next().toFloat(), yIterator.next().toFloat()))
        }
        val dataSet = LineDataSet(entries, "ambientLight") // 图表绑定数据，设置图表折现备注
        val entries2: ArrayList<Entry> = ArrayList()
        yIterator = deviceList[index].datList!!.lightIntensity.iterator()
        xIterator = deviceList[index].datList!!.time.iterator()
        while (xIterator.hasNext()) {
            entries2.add(Entry(xIterator.next().toFloat(), yIterator.next().toFloat()))
        }
        val dataSet2 = LineDataSet(entries2, "lightIntensity") // 图表绑定数据，设置图表折现备注
        val entries3: ArrayList<Entry> = ArrayList()
        yIterator = deviceList[index].datList!!.raindrops.iterator()
        xIterator = deviceList[index].datList!!.time.iterator()
        while (xIterator.hasNext()) {
            entries3.add(Entry(xIterator.next().toFloat(), yIterator.next().toFloat()))
        }
        val dataSet3 = LineDataSet(entries3, "raindrops") // 图表绑定数据，设置图表折现备注
        dataSet.color = Color.RED
        dataSet2.color = Color.GREEN
        val lineData = LineData(dataSet, dataSet2, dataSet3)
        lineChart.xAxis.valueFormatter = XAdapter()
        lineChart.data = lineData // 图表绑定数据值
        lineChart.invalidate() // 刷新图表
    }
}