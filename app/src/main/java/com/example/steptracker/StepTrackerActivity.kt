package com.example.steptracker

import android.hardware.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import kotlin.math.sqrt

class StepTrackerActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var txtSteps: TextView
    private lateinit var btnStartTracker: Button
    private lateinit var txtStatus: TextView
    private var magnitudePrevious = 0f
    val context = this@StepTrackerActivity
    var status = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_tracker)
        progressBar=findViewById(R.id.progressBar)
        txtSteps=findViewById(R.id.txtSteps)
        btnStartTracker=findViewById(R.id.btnStartTracker)
        txtStatus=findViewById(R.id.txtStatus)
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        var currentSteps = UserObject.steps%1000
        progressBar.progress = currentSteps/10
        txtSteps.text = "${currentSteps}/1000"
        val stepDetector: SensorEventListener = object: SensorEventListener{
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null){
                    var x_acceleration = event.values[0]
                    var z_acceleration = event.values[2]

                    var magnitude: Float = sqrt(x_acceleration*x_acceleration+z_acceleration*z_acceleration)
                    var magnitudeDelta: Float = magnitude-magnitudePrevious
                    magnitudePrevious = magnitude

                    if (magnitudeDelta > 6){
                        UserObject.steps++
                        currentSteps = UserObject.steps%1000
                        progressBar.progress = currentSteps/10
                        txtSteps.text = "${currentSteps}/1000"
                    }

                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

        }

        btnStartTracker.setOnClickListener {
            if (!status){
                sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL)
                Toast.makeText(context, "Step tracker enabled.", Toast.LENGTH_SHORT).show()
                status=true
            }else{
                sensorManager.unregisterListener(stepDetector)
                Toast.makeText(context, "Step tracker disabled.", Toast.LENGTH_SHORT).show()
                status=false
            }
            txtStatus.text = "Status: ${if (status) "ON" else "OFF"}"
            txtStatus.setTextColor(resources.getColor(if (status) R.color.teal_200 else R.color.c4))
        }
    }

    override fun onPause() {
        super.onPause()

        val dbHelper = DatabaseHelper(context)
        dbHelper.updateSteps(UserObject.ID, UserObject.steps)
    }

    override fun onStop() {
        super.onStop()

        val dbHelper = DatabaseHelper(context)
        dbHelper.updateSteps(UserObject.ID, UserObject.steps)
    }

    override fun onResume() {
        super.onResume()
        var currentSteps = UserObject.steps%1000
        progressBar.progress = currentSteps/10
        txtSteps.text = "${currentSteps}/1000"
    }
}