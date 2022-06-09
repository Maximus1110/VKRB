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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.btmonitork.databinding.ActivityControlBinding

class ControlActivity : AppCompatActivity(),RecevieThread.Listener {

    private lateinit var binding: ActivityControlBinding
    private lateinit var actListLauncher: ActivityResultLauncher<Intent>
    lateinit var btConnection: BtConnection
    lateinit var btAdapterDef: BluetoothAdapter
    var REQUEST_CODE_ENABLE_BLUETOOTH= 10


    private var listItem: ListItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBtListResult()
        init()
        binding.apply {
            bA.setOnClickListener{
                btConnection.sendMessage("A")
            }
            bB.setOnClickListener{
                btConnection.sendMessage("B")
            }
            textView.setText("15,2 КМ/Ч")

        }
    }

    private fun init() {
        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = btManager.adapter
        btConnection = BtConnection(btAdapter, this)
        btAdapterDef=BluetoothAdapter.getDefaultAdapter()

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

            if(btAdapterDef.isEnabled){
                Toast.makeText(this,"Уже лучше",Toast.LENGTH_LONG).show()
                listItem.let {
                btConnection.connect(it?.mac!!)
                }
            }else{
                //Toast.makeText(this,"Влючи БТ !!!",Toast.LENGTH_LONG).show()
                var intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent,REQUEST_CODE_ENABLE_BLUETOOTH)
            }


//            listItem.let {
//                btConnection.connect(it?.mac!!)
//            }
        } else if (item.itemId == R.id.id_settings){

            actListLauncher.launch(Intent(this, SettingsActivity::class.java))

        }else if (item.itemId == R.id.id_statistics){

            actListLauncher.launch(Intent(this, StatisticsActivity::class.java))

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
        runOnUiThread{ // режим работы со второстепенным потоком
            binding.textView.text=message
        }
    }


}