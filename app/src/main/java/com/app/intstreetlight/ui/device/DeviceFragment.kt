package com.app.intstreetlight.ui.device

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.app.intstreetlight.R
import com.app.intstreetlight.StreetLightApplication.Companion.index
import com.app.intstreetlight.databinding.FragmentDeviceBinding
import com.app.intstreetlight.getClient
import com.app.intstreetlight.logic.Repository
import com.app.intstreetlight.logic.model.Data
import com.app.intstreetlight.logic.model.Device
import com.app.intstreetlight.logic.model.DeviceViewModel
import com.app.intstreetlight.showToast
import com.app.intstreetlight.ui.chart.XAdapter
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.huaweicloud.sdk.iotda.v5.model.CreateCommandRequest
import com.huaweicloud.sdk.iotda.v5.model.DeviceCommandRequest
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread


class DeviceFragment : Fragment() {
    private val viewModel: DeviceViewModel by activityViewModels()
    private lateinit var binding: FragmentDeviceBinding
    private var id: String? = null
    private var time: Long? = null
    private val chartChange = 1

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                chartChange -> {
                    binding.lineChart.xAxis.valueFormatter = XAdapter()
                    binding.lineChart.data = msg.obj as LineData
                    binding.lineChart.axisRight.isEnabled = false
                    binding.lineChart.invalidate()
                }
            }
        }
    }

    private val task: Runnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, 15000)
            thread {
                runBlocking {
                    viewModel.setDeviceObj(Repository.getProperties(id!!, time ?: 0))
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DeviceFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(task)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeviceBinding.inflate(layoutInflater, container, false)
        viewModel.setIndex(index)
        val v = binding.root
        init()
        return v
    }

    private fun init() {
        val propertiesObserver = Observer<HashMap<*, *>?> {
            if (it != null) {
                binding.AmbientLight.text =
                    (it["AmbientLight"] as Int).toString()
                binding.LightIntensity.text =
                    (it["LightIntensity"] as Int).toString()
                binding.Raindrops.text = (it["Raindrops"] as Int).toString()
                binding.AutomaticDimming.text =
                    getBool(it["AutomaticDimming"] as Int)
                binding.Fog.text = getBool(it["Fog"] as Int)

            } else {
                binding.AmbientLight.text = "0"
                binding.LightIntensity.text = "0"
                binding.Raindrops.text = "0"
                "False".also { s -> binding.AutomaticDimming.text = s }
                "False".also { s -> binding.Fog.text = s }
            }
        }
        val deviceNameObserver = Observer<String> {
            binding.detailName.text = it
        }
        val deviceStatusObserver = Observer<String> {
            it.also { s ->
                binding.statusDetail.text = s
                if (s == "ONLINE") {
                    binding.statusDetailImg.setImageResource(R.mipmap.on_line)
                    binding.automaticDimming.isEnabled = true
                    binding.fog.isEnabled = true
                } else {
                    binding.statusDetailImg.setImageResource(R.mipmap.off_line)
                    binding.automaticDimming.isEnabled = false
                    binding.fog.isEnabled = false
                }
            }
        }
        val updateObserver = Observer<String> {
            "更新时间：$it".also { s ->
                binding.updateTime.text = s
            }
        }
        viewModel.currentDeviceProperty.observe(viewLifecycleOwner, propertiesObserver)
        viewModel.currentDeviceName.observe(viewLifecycleOwner, deviceNameObserver)
        viewModel.currentDeviceStatus.observe(viewLifecycleOwner, deviceStatusObserver)
        viewModel.currentUpdateTime.observe(viewLifecycleOwner, updateObserver)
        viewModel.currentDataList.observe(viewLifecycleOwner) {
            val dataList = it.getOrNull()
            if (dataList != null) {
                val list = dataList.data
                showChart(list)
            }
        }
        binding.automaticDimming.setOnClickListener {
            val value: Int = when (binding.AutomaticDimming.text) {
                "True" -> {
                    binding.automaticDimming.setBackgroundResource(R.mipmap.auto_off)
                    0
                }
                else -> {
                    binding.automaticDimming.setBackgroundResource(R.mipmap.auto_on)
                    1
                }
            }
            thread {
                getClient().createCommand(
                    getCommand(
                        "auto",
                        "index" to (index + 1).toString(),
                        "value" to value.toString()
                    )
                )
                viewModel.setDeviceObj(Repository.getProperties(id!!, time ?: 0))
            }
            "命令发送成功！请等待服务器反馈……".showToast()
        }
        binding.automaticDimming.isEnabled = false
        binding.fog.setOnClickListener {
            val value: Int = when (binding.Fog.text) {
                "True" -> {
                    binding.fog.setBackgroundResource(R.mipmap.fog_off)
                    0
                }
                else -> {
                    binding.fog.setBackgroundResource(R.mipmap.fog_on)
                    1
                }
            }
            thread {
                getClient().createCommand(
                    getCommand(
                        "fog",
                        "index" to (index + 1).toString(),
                        "value" to value.toString()
                    )
                )
                viewModel.setDeviceObj(Repository.getProperties(id!!, time ?: 0))
            }
            "命令发送成功！请等待服务器反馈……".showToast()
        }
        binding.fog.isEnabled = false
        handler.postDelayed(task, 5000)
        thread {
            runBlocking {
                val list = ArrayList<Device>()
                val s = Repository.getDevices()
                s.forEach {
                    list.add(Device(it, null, null))
                }
                id = list[index].deviceObj.deviceId
                viewModel.setList(list)
                viewModel.setDeviceObj(Repository.getProperties(id!!, time ?: 0))
            }
        }
    }

    private fun getCommand(
        name: String,
        vararg pairs: Pair<String, String>
    ): CreateCommandRequest {
        val request = CreateCommandRequest()
        val body = DeviceCommandRequest()
        request.withDeviceId(id!!)
        body.withCommandName(name)
        body.withServiceId("Command")
        val map = with(HashMap<String, String>()) {
            plus(pairs)
        }
        body.paras = map
        request.withBody(body)
        return request
    }

    private fun showChart(list: List<Data>) {
        thread {
            val entries1: ArrayList<Entry> = ArrayList()
            val entries2: ArrayList<Entry> = ArrayList()
            val entries3: ArrayList<Entry> = ArrayList()
            list.forEach {
                entries1.add(Entry(it.timeStamp.toFloat(), it.ambientLight.toFloat()))
                entries2.add(Entry(it.timeStamp.toFloat(), it.raindrops.toFloat()))
                entries3.add(Entry(it.timeStamp.toFloat(), it.lightIntensity.toFloat()))
            }
            val lineData = LineData(
                getDataSet(entries1, "ambientLight", Color.RED),
                getDataSet(entries2, "raindrops", Color.BLUE),
                getDataSet(entries3, "lightIntensity", Color.GRAY)
            )
            val msg = Message()
            msg.what = 1
            msg.obj = lineData
            handler.sendMessage(msg)
        }

    }

    private fun getDataSet(e: ArrayList<Entry>, l: String, c: Int): LineDataSet {
        val dataSet = LineDataSet(e, l)
        dataSet.color = c
        // 模式为贝塞尔曲线
        dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        // 是否绘制数据值
        dataSet.setDrawValues(false)
        dataSet.setDrawCircles(true)
        dataSet.setDrawCircleHole(false)
        dataSet.isHighlightEnabled = true // 隐藏点击时候的高亮线
        dataSet.highLightColor = Color.TRANSPARENT
        dataSet.setCircleColor(c)
        dataSet.circleRadius = 3.5f
        dataSet.lineWidth = 1f
        return dataSet
    }

    private fun getBool(i: Int): String {
        return if (i == 1) "True"
        else "False"
    }


}