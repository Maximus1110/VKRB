package com.example.btmonitork

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.time.LocalDate
import java.time.Period
import kotlin.collections.ArrayList

//взял код у индуса


class StatisticsActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    var dist=1f
    lateinit var dateNow : LocalDate
    lateinit var dateBuff : LocalDate
    var pref : SharedPreferences?=null
    lateinit var dateList:ArrayList<LocalDate>
    lateinit var dateValue:ArrayList<Float>
    lateinit var dateName:ArrayList<String>
    var sum_of_other=0f

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        dist=intent.getFloatExtra("DIST",0f)

        init()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun init(){
        dateList=ArrayList<LocalDate>()
        dateValue=ArrayList()
        dateName=ArrayList<String>()
        dateNow=LocalDate.now()

        lineChart=findViewById(R.id.lineChart)
        pref=getSharedPreferences("TABLE_DATE", Context.MODE_PRIVATE)
        initData()
        //memory_init()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initData(){

        //dateList=ArrayList<LocalDate>()S
        //delete_All_data()
//        for( i in 0..6){
//            saveDataFloat(0f,("day_"+i.toString()+"_float").toString())
//            saveDataString("2022-01-01","day_"+i.toString()+"_string")
//        }
//        saveDataFloat(5f,("day_"+3.toString()+"_float").toString())


        Log.d("MyLog","Pass1")
        Log.d("MyLog",dateValue.toString())

//        for( i in 0..6){
//            dateList.add(dateNow.minusDays(i.toLong()))
//            dateValue.add(i.toFloat())
//            dateName.add( "2022-01-01")
//        }
        for( i in 0..6){
            dateList.add(dateNow.minusDays(i.toLong()))
            dateValue.add( pref?.getFloat("day_"+i.toString()+"_float",0f)!! )
            dateName.add( pref?.getString("day_"+i.toString()+"_string",dateList[i].toString())!! )
            Log.d("MyLog","Mem1: "+dateValue[i].toString()+" "+ i.toString())
        }

        val day_between_last_and_now=Period.between(dateNow,dateList[0])

        //Log.d("MyLog","Betwstroka: "+ dateBuff.toString())
        //Log.d("MyLog","Betw: "+day_between_last_and_now.days.toString())

        pushDate(day_between_last_and_now.days)
        setLineChartData()
    }

    fun pushDate(x:Int){
        sum_of_other =pref?.getFloat("sum_of_other",0f)!! // читаем из памяти старую сумму

        for(i in 1..x){ //добавляем к этой сумме вылетевшие элементы
            if(7-i>=0){
                sum_of_other=sum_of_other+dateValue[7-i]
                saveDataFloat(sum_of_other,"sum_of_other")
            }
        }

        var buffValue= ArrayList<Float>() // буферные листы
        var buffName= ArrayList<String>()

        for(i in 0..6){ // обнуляем лмсты и ставим корректную дату
            buffValue.add(0f)
            buffName.add(dateList[i].toString())
        }

        for (i in 0..6){ // меняем значения
            if(i+x<7){
                buffValue[i+x]=dateValue[i]
            }
        }
        for ( i in 0..6){ // перезаписываем
            dateName[i]=buffName[i]
            dateValue[i]=buffValue[i]
            saveDataString(buffName[i],"day_"+i.toString()+"_string")
            saveDataFloat(buffValue[i],"day_"+i.toString()+"_float")
        }
        dateValue[0]=dist-sum_of_other-dateValue.sum()+dateValue[0]

    }


    fun saveDataFloat(res: Float, label:String){
        val editor = pref?.edit()
        editor?.putFloat(label,res)
        editor?.apply()
    }
    fun saveDataString(res: String,label:String){
        val editor = pref?.edit()
        editor?.putString(label,res)
        editor?.apply()
    }

    fun delete_All_data(){
        val editor = pref?.edit()
        editor?.clear()
        editor?.apply()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setLineChartData(){
        //Thread.sleep(1000)
        val xValue = ArrayList<String>()
        val lineEntry = ArrayList<Entry>();

        for ( i in 0..6){
            xValue.add(dateList[i].dayOfWeek.toString()) //dateList[i].dayOfWeek.toString()
            Log.d("MyLog",dateValue[i].toString())
            var b =dateValue[i]
            lineEntry.add(Entry(b,i))
        }

         //dateList[i].dayOfWeek.toString()



//        xValue.add("10.06")
//        xValue.add("11.06")
//        xValue.add("12.06")
//        xValue.add("13.06")
//        xValue.add("14.06")
//        xValue.add("15.06")
//        xValue.add("16.06")
//
//        lineEntry.add(Entry(4f,0))
//        lineEntry.add(Entry(12.2f,1))
//        lineEntry.add(Entry(11.8f,2))
//        lineEntry.add(Entry(0f,3))
//        lineEntry.add(Entry(9.3f,4))
//        lineEntry.add(Entry(0f,5))
//        lineEntry.add(Entry(dist,6))

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