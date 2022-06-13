package com.example.btmonitork

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.btmonitork.databinding.ActivityControlBinding
import java.io.IOException

class ControlActivity : AppCompatActivity(), RecevieThread.Listener {

    private lateinit var binding: ActivityControlBinding
    private lateinit var actListLauncher: ActivityResultLauncher<Intent>
    lateinit var btConnection: BtConnection
    lateinit var btAdapterDef: BluetoothAdapter
    var REQUEST_CODE_ENABLE_BLUETOOTH = 10
    private var buff = ""
    private var gl_dist=0f

    private var listItem: ListItem? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBtListResult()
        init()

    }


    private fun init() {
        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = btManager.adapter
        btConnection = BtConnection(btAdapter, this)
        btAdapterDef = BluetoothAdapter.getDefaultAdapter()
        binding.bA.setOnClickListener { connect_click() }

    }



    private fun set_visible(first_v: Boolean, but_v: Boolean) {
        binding.apply {
            if (!first_v) {
                textVMainLabel.visibility = View.GONE
                texVSpeed.visibility = View.VISIBLE
                textVDist.visibility = View.VISIBLE
                textVDistLabel.visibility = View.VISIBLE
                textVSpeedLabel.visibility = View.VISIBLE

            } else {
                textVMainLabel.visibility = View.VISIBLE

                texVSpeed.visibility = View.GONE
                textVDist.visibility = View.GONE
                textVDistLabel.visibility = View.GONE
                textVSpeedLabel.visibility = View.GONE
            }
            if (but_v) {
                bA.visibility = View.VISIBLE
            } else {
                bA.visibility = View.GONE
            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun connect_click() {
        if (btAdapterDef.isEnabled) {

            if (listItem?.mac.isNullOrEmpty()) {
                Toast.makeText(this, "Выберете устройстов для подключения", Toast.LENGTH_LONG)
                    .show()
                actListLauncher.launch(Intent(this, BtListActivity::class.java))
            } else {
                Toast.makeText(this, "Попытка подключения", Toast.LENGTH_LONG).show()
                listItem.let {
                    btConnection.connect(it?.mac!!)
                }
            }
        } else {
            var intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(intent, REQUEST_CODE_ENABLE_BLUETOOTH)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.control_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("MissingPermission")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.id_list) {

            actListLauncher.launch(Intent(this, BtListActivity::class.java))

        } else if (item.itemId == R.id.id_connect) {

            connect_click()


        } else if (item.itemId == R.id.id_settings) {

            actListLauncher.launch(Intent(this, SettingsActivity::class.java))

        } else if (item.itemId == R.id.id_statistics) {


            var intent=Intent(this, StatisticsActivity::class.java)
            intent.putExtra("DIST",gl_dist)

            actListLauncher.launch(intent)

        }



        return super.onOptionsItemSelected(item)
    }

    private fun onBtListResult() {
        actListLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                listItem = it.data?.getSerializableExtra(BtListActivity.DEVICE_KEY) as ListItem
                //Log.d("MyLog","Name ${(it.data?.getSerializableExtra(BtListActivity.DEVICE_KEY) as ListItem).name}")
            }

        }
    }


    override fun onReceive(message: String) {


        runOnUiThread {
            Log.d("MyLog","M: "+message)
                Log.d("MyLog","B: "+buff)
                if (message == "Connecting...") {
                    binding.textVMainLabel.setText("Подключение...")

                } else if (message == "Can not connect to device") {
                    binding.textVMainLabel.setText("Не подключено")
                    set_visible(true, true)
                } else if (message == "Connected") {
                    binding.textVMainLabel.setText("Подключено")
                } else {
                    buff = buff + message
                    var list = buff.split("end")
                    if (list.size > 1 && list[0].split(",")[1].toString()[0] == 'V') {

                        var sp_str = list[0].split(",")[1].split(":")[1].toString()
                        var dis_str = list[0].split(",")[2].split(":")[1].toString()

                        gl_dist= dis_str.toFloat()
                        var sp_doub = sp_str.toDouble()
                        sp_doub = sp_doub * 600 / 600
                        var sp_int = (sp_doub * 10).toInt()

                        if (sp_str == "0.0") {
                            set_visible(true, false)
                            binding.textVMainLabel.setText("Не двигаемся")
                        } else {
                            set_visible(false, false)
                            binding.texVSpeed.setText((sp_int / 10).toString() + "." + (sp_int % 10).toString() + " км/ч")
                            binding.textVDist.setText(dis_str+" км")
                        }

                        buff = list[1].toString()
                    }

                }


        }

    }


}