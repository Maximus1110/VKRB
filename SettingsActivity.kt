package com.example.btmonitork

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {


    private lateinit var btSwitch:Switch
    private lateinit var editTextView:EditText
    private lateinit var text_d:TextView

    var pref : SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        btSwitch = findViewById(R.id.switch1)
        editTextView = findViewById(R.id.editTextNumberSigned)
        text_d=findViewById(R.id.textView_d)
        memory_init()

        editTextView.visibility = View.INVISIBLE

        btSwitch.setOnClickListener {

            if (btSwitch.isChecked) {
                Toast.makeText(this, "+", Toast.LENGTH_SHORT).show()
                editTextView.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "-", Toast.LENGTH_SHORT).show()
                editTextView.visibility = View.INVISIBLE

            }
        }

        editTextView.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // if the event is a key down event on the enter button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    val dia_str=editTextView.text.toString()
                    if(dia_str.toInt()<200 || dia_str.toInt()>1000){
                        Toast.makeText(this@SettingsActivity,"Введите корректный диаметр",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@SettingsActivity,"Результат сохранён",Toast.LENGTH_SHORT).show()
                        // clear focus and hide cursor from edit text
                        saveData(dia_str.toInt())
                        finish()

                    }


                    return true
                }
                return false
            }
        })
    }

    private fun memory_init(){
        pref=getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        text_d.setText("Диаметр: "+pref?.getInt("diametr",600).toString()+"мм")
    }

    fun saveData(res: Int){
        val editor = pref?.edit()
        editor?.putInt("diametr",res)
        editor?.apply()
    }
}