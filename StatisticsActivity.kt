package com.example.btmonitork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.security.KeyStore

//взял код у индуса


class StatisticsActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        init()
    }

    fun init(){
        lineChart=findViewById(R.id.lineChart)
        setLineChartData()
    }

    fun setLineChartData(){
        val xValue = ArrayList<String>()
        xValue.add("11.00AM")
        xValue.add("12.00AM")
        xValue.add("13.00AM")
        xValue.add("14.00AM")
        xValue.add("15.00AM")


        val lineEntry = ArrayList<Entry>();
        lineEntry.add(Entry(40f,0))
        lineEntry.add(Entry(30f,1))
        lineEntry.add(Entry(35f,2))
        lineEntry.add(Entry(25f,3))
        lineEntry.add(Entry(30f,4))

        val lineDataSet= LineDataSet(lineEntry,"First")
        lineDataSet.color=resources.getColor(R.color.purple_500)


        lineDataSet.circleRadius = 0f
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor=resources.getColor(com.google.android.material.R.color.design_default_color_primary_dark)
        lineDataSet.fillAlpha = 30


        val data = LineData(xValue,lineDataSet)

        lineChart.data= data
        lineChart.setBackgroundColor(resources.getColor(R.color.white))
        lineChart.animateXY(1000,1000)



    }

}