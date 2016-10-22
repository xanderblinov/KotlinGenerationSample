package com.redmadrobot.kotlingenerationsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.redmadrobot.samplelib.SampleAnnotation

@SampleAnnotation("sampleValue")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
