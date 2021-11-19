package com.app.intstreetlight.ui.load


import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.app.intstreetlight.R


@SuppressLint("ViewConstructor")
class LoadingView :LinearLayout{

    constructor(context: Context) : super(context){
        init()
    }

    constructor(context: Context, attr: AttributeSet) : super(context,attr){
        init()
    }

    constructor(context: Context, attr: AttributeSet, num: Int): super(context,attr,num){
        init()
    }


    private lateinit var progressBar: ProgressBar
    private lateinit var tv: TextView
    private lateinit var  iv: ImageView


    private fun showLoading() {
        iv.visibility = GONE
        progressBar.visibility = VISIBLE
        tv.text = "正在加载……"
    }

    fun showSuccess() {
        iv.setImageResource(R.mipmap.load_success)
        iv.visibility = VISIBLE
        progressBar.visibility = GONE
        tv.text = "加载成功！"
    }

    fun showFail() {
        iv.setImageResource(R.mipmap.load_fail)
        iv.visibility = VISIBLE
        progressBar.visibility = GONE
    }


    private fun init() {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.loading_view, this, true)
        progressBar = view.findViewById(R.id.progressBar)
        tv = findViewById(R.id.tv)
        iv = findViewById(R.id.iv)
        showLoading()
    }


}