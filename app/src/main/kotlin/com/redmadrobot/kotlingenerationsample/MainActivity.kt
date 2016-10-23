package com.redmadrobot.kotlingenerationsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.redmadrobot.samplelib.SampleAnnotation
import com.redmadrobot.samplelib.SampleInterface
import kotlinx.android.synthetic.main.activity_main.*

@SampleAnnotation("sampleValue")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainActivityGeneratedClass = Class.forName("com.redmadrobot.kotlingenerationsample.MainActivity_Generated").newInstance() as SampleInterface

        activity_main_text_view.text = mainActivityGeneratedClass.getSampleString()

    }
}
