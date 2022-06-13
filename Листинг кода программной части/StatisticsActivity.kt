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
    var dist=1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        dist=intent.getFloatExtra("DIST",0f)
        init()
    }

    fun init(){
        lineChart=findViewById(R.id.lineChart)
        setLineChartData()
    }

    fun setLineChartData(){
        val xValue = ArrayList<String>()
        xValue.add("10.06")
        xValue.add("11.06")
        xValue.add("12.06")
        xValue.add("13.06")
        xValue.add("14.06")
        xValue.add("15.06")
        xValue.add("16.06")



        val lineEntry = ArrayList<Entry>();
        lineEntry.add(Entry(4f,0))
        lineEntry.add(Entry(12.2f,1))
        lineEntry.add(Entry(11.8f,2))
        lineEntry.add(Entry(0f,3))
        lineEntry.add(Entry(9.3f,4))
        lineEntry.add(Entry(0f,5))
        lineEntry.add(Entry(dist,6))

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