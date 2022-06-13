package com.example.btmonitork

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*

@SuppressLint("MissingPermission")
class ConnectThread(private var device: BluetoothDevice, private val listener: RecevieThread.Listener) : Thread() {

    val uuid = "00001101-0000-1000-8000-00805F9B34FB"
    var mSocket: BluetoothSocket? = null
    lateinit var rThread: RecevieThread


    init {
        try {
            mSocket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(uuid))

        } catch (i: IOException) {

        }

    }

    override fun run() {
        try {
            Log.d("MyLog", "Connecting...")
            listener.onReceive("Connecting...")
            mSocket?.connect()
            Log.d("MyLog", "Connected")
            listener.onReceive("Connected")
            rThread = RecevieThread(mSocket!!, listener)
            rThread.start()

        } catch (i: IOException) {
            Log.d("MyLog", "Can not connect to device")
            listener.onReceive("Can not connect to device")
            closeConnection()

        }
    }

    fun closeConnection() {
        try {
            mSocket?.close()

        } catch (i: IOException) {


        }

    }


}