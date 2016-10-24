package com.redmadrobot.samplekotlincompiler

import com.redmadrobot.samplelib.SampleInterface

import javax.lang.model.element.TypeElement

/**
 * Date: 22/10/16

 * @author Alexander Blinov
 */

internal class SampleClassGenerator : ClassGenerator<TypeElement>() {

    override fun generate(element: TypeElement, classGeneratingParamsList: MutableList<ClassGeneratingParams>): Boolean {
        val parentClassName = element.toString()

        val viewClassName = parentClassName.substring(parentClassName.lastIndexOf(".") + 1)

        val packageName = parentClassName.substring(0, parentClassName.lastIndexOf("."))

        createGeneratingParams(classGeneratingParamsList, viewClassName, packageName)

        return true

    }


    private fun createGeneratingParams(classGeneratingParamsList: MutableList<ClassGeneratingParams>,
                                       viewClassName: String, packageName: String) {
        val generatedFileName = viewClassName + GENERATED_FILE_SUFFIX

        val body = "package $packageName\n\n" +
                "import $IMPL_IMPORT\n\n" +
                "class $generatedFileName : $IMPL_NAME {\n" +
                "   override fun getSampleString() = \"Generated using $viewClassName\"\n" +
                "}"

        val params = ClassGeneratingParams(generatedFileName, body, packageName)
        classGeneratingParamsList.add(params)
    }

    companion object {

        val GENERATED_FILE_SUFFIX = "_Generated"

        val IMPL_NAME = SampleInterface::class.java.simpleName!!

        val IMPL_IMPORT = SampleInterface::class.java.canonicalName!!
    }
}
