package com.redmadrobot.samplekotlincompiler

/**
 * Date: 12-Jan-16 Time: 15:09

 * @author Alexander Blinov
 */
internal data class ClassGeneratingParams(var name: String,
                                          var body: String,
                                          var packageNme: String) {

    fun getSimpleFileName() = name + KOTLIN_FILE_EXTENSION

    companion object {

        val KOTLIN_FILE_EXTENSION = ".kt"
    }
}
