package com.example.fernsteuerungukgv2.gui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.ToggleButton
import android.widget.SeekBar
import com.example.fernsteuerungukgv2.R
import java.net.DatagramPacket
import java.net.DatagramSocket
import android.os.StrictMode
import java.io.IOException
import java.net.InetAddress
import java.util.*



// Global
// Declaration of Port and Host-IP of the Smartphone-NodeRed-connection
class SoftOptions(host: String, port: Int ) {
    var remoteHost: String = host
    var remotePort: Int = port
}

var nodeRedSettings = SoftOptions("192.168.178.191", 2000)
//var smartphoneSettings = SoftOptions("192.168.178.173", 2000)  //would have been to be adjusted for every device, that's why we decided to delete the receive part
var myUDP:String = "" //global string buffer for sending and receiving via UDP


class MainActivity : AppCompatActivity() {

    //function that sends a UDP-String
    private fun sendUDP(messageStr: String) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            //Open a port to send the package
            val socket = DatagramSocket()
            socket.broadcast = true
            //Transform the string into a byteArray
            val sendData = messageStr.toByteArray()
            //sending the byteArray to the IP/Port of NodeRed
            val sendPacket = DatagramPacket(sendData, sendData.size, InetAddress.getByName(nodeRedSettings.remoteHost), nodeRedSettings.remotePort)
            socket.send(sendPacket)
        } catch (e: IOException) {
        // In case it didn't worked
            println("open fun sendUDP catch exception.$e")
        }
    }



    //function that receives a UDP-string
    /*private fun receiveUDP(): String {
        val buffer = ByteArray(2048)
        var socket: DatagramSocket? = null
        val packet = DatagramPacket(buffer, buffer.size)
        try {
            //Keep a socket open to listen to all the UDP traffic that is destined for this port
            socket = DatagramSocket(smartphoneSettings.remotePort, InetAddress.getByName(
                smartphoneSettings.remoteHost))
            socket.broadcast = true
            socket.receive(packet)
            //print on logcat
            println("open fun receiveUDP packet received = " + packet.data.toString())

        } catch (e: Exception) {
            //in case nothing has been received, print exception to logcat
            println("open fun receiveUDP catch exception.$e")
            e.printStackTrace()
        } finally {
            socket?.close()
        }
        //returning the string, that has been received
        return packet.data.toString()
    }*/

    //rebooting the app: the device is disconnected and in manualMode
    var enabled : Boolean = true
    var connected: Boolean = false

    //opening the App
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main_layout) //setting the Layout-Design

        //Settings for the Button "Connection"
        val toggleConnection: ToggleButton = findViewById(R.id.toggleConnection)
        toggleConnection.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                //sending UDP to NodeRed to inform about starting a connection
                myUDP = "connect"
                sendUDP(myUDP)
                connected = true
                /*waiting for response by the system
                while (!connected){

                    if (receivedString !="0"){
                        connected = true
                        Toast.makeText(this@MainActivity, receivedString, Toast.LENGTH_SHORT).show()
                    }
                }*/
            }
            else {
                //sending UDP to NodeRed to inform about disconnecting
                myUDP = "disconnect"
                sendUDP(myUDP)
                connected = false
            }
        }

        //Settings for the Button "Enable"
        val toggleMode: ToggleButton = findViewById(R.id.toggleEnable)
        toggleMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                //sending UDP to NodeRed to inform about disabling the robot
                myUDP = "enable"
                sendUDP(myUDP)
                enabled = true

            }
            else {
                //sending UDP to NodeRed to inform about enabling the robot
                myUDP = "disable"
                sendUDP(myUDP)
                enabled = false
            }
        }

        //Settings for the Velocity Bar
        val velocity: SeekBar = findViewById(R.id.velocity)
        velocity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                if (connected && enabled) {
                    //sending the value of the set velocity
                    myUDP = "velocity: $progress"
                    sendUDP(myUDP)

                    //toast, just for showing the set velocity in the app
                    Toast.makeText(this@MainActivity, "velocity: $progress", Toast.LENGTH_SHORT).show()

                }
            }
            override fun onStartTrackingTouch(seek: SeekBar) {
                // nothing, just part of the object
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // nothing, just part of the object
            }
        })
    }


    //functions for moving, only if connected and in manual mode

    fun stopMoving(view: View){
        if(connected && enabled) {
            Toast.makeText(
                this,
                "pimmel",
                Toast.LENGTH_SHORT
            ).show()
        }
        if(connected && enabled) {
            myUDP = "stop"
            sendUDP(myUDP)
        }
    }

    fun moveTopLeft(view: View){
        if(connected && enabled) {
            myUDP = "mtl"
            sendUDP(myUDP)
        }
    }
    fun moveTopRight(view:View){
        if(connected && enabled) {
            myUDP = "mtr"
            sendUDP(myUDP)
        }
    }
    fun moveBottomLeft(view:View){
        if(connected && enabled) {
            myUDP = "mbl"
            sendUDP(myUDP)
        }
    }
    fun moveBottomRight(view:View){
        if(connected && enabled) {
            myUDP = "mbr"
            sendUDP(myUDP)
        }
    }
    fun moveTopCenter(view:View){
        if(connected && enabled) {
            myUDP = "mtc"
            sendUDP(myUDP)
        }
    }
    fun moveBottomCenter(view:View){
        if(connected && enabled) {
            myUDP = "mbc"
            sendUDP(myUDP)
        }
    }
    fun moveCenterLeft(view:View){
        if(connected && enabled) {
            myUDP = "mcl"
            sendUDP(myUDP)
        }
    }
    fun moveCenterRight(view:View){
        if(connected && enabled) {
            myUDP = "mcr"
            sendUDP(myUDP)
        }
    }
    fun turnRight(view:View){
        if(connected && enabled) {
            myUDP = "tr"
            sendUDP(myUDP)
        }
    }
    fun turnLeft(view:View){
        if(connected && enabled) {
            myUDP = "tl"
            sendUDP(myUDP)
        }
    }

}
