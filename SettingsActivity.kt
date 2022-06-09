package com.example.btmonitork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {


    private lateinit var btSwitch:Switch
    private lateinit var editTextView:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        btSwitch = findViewById(R.id.switch1)
        editTextView = findViewById(R.id.editTextNumberSigned)
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
    }
}