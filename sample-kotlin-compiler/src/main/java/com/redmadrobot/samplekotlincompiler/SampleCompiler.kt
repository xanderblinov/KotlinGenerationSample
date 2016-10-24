package com.redmadrobot.samplekotlincompiler

import com.redmadrobot.samplelib.SampleAnnotation
import java.io.File
import java.io.IOException
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.SourceVersion.latestSupported
import javax.lang.model.element.*
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.StandardLocation.SOURCE_OUTPUT


class SampleCompiler : Processor {
    private lateinit var processingEnvironment: ProcessingEnvironment

    override fun getSupportedOptions(): MutableSet<String> {
        return mutableSetOf()
    }

    override fun getCompletions(element: Element?, annotation: AnnotationMirror?, member: ExecutableElement?, userText: String?): MutableIterable<Completion> {
        return mutableListOf()
    }

    private var messager: Messager? = null

    private var typeUtils: Types? = null

    private var elementUtils: Elements? = null

    @Synchronized override fun init(processingEnv: ProcessingEnvironment) {
        processingEnvironment = processingEnv
        messager = processingEnvironment.messager
        typeUtils = processingEnvironment.typeUtils
        elementUtils = processingEnvironment.elementUtils
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        val supportedAnnotationTypes = HashSet<String>()
        Collections.addAll(supportedAnnotationTypes, SampleAnnotation::class.java.canonicalName)
        return supportedAnnotationTypes
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return latestSupported()
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        processInjectors(roundEnv, SampleAnnotation::class.java, ElementKind.CLASS,
                SampleClassGenerator())

        return true
    }

    private fun processInjectors(roundEnv: RoundEnvironment,
                                 clazz: Class<out Annotation>, kind: ElementKind, classGenerator: ClassGenerator<TypeElement>) {
        for (annotatedElements in roundEnv.getElementsAnnotatedWith(clazz)) {
            if (annotatedElements.kind != kind) {
                throw RuntimeException(
                        "$annotatedElements  must be  ${kind.name}  or not mark it as @${clazz.simpleName}")
            }


            generateCode(kind, classGenerator, annotatedElements as TypeElement)
        }
    }

    private fun generateCode(kind: ElementKind, classGenerator: ClassGenerator<TypeElement>, element: TypeElement) {
        if (element.kind != kind) {
            throw RuntimeException("$element  must be ${kind.name}")
        }

        val classGeneratingParamsList = ArrayList<ClassGeneratingParams>()

        //noinspection unchecked
        val generated = classGenerator.generate(element, classGeneratingParamsList)

        if (!generated) {
            return
        }

        for (classGeneratingParams in classGeneratingParamsList) {
            createSourceFile(classGeneratingParams)
        }
    }

    private fun createSourceFile(classGeneratingParams: ClassGeneratingParams) {

        val options = processingEnvironment.options
        val GENERATED_CLASS_PATH_OPTION = "kapt.kotlin.generated"

        val generatedPath = options[GENERATED_CLASS_PATH_OPTION] ?: throw RuntimeException("compatible with kapt only. kapt2 is not supported now")

        val path = generatedPath.replace("(.*)tmp(/kapt/debug/)kotlinGenerated".toRegex(), "$1generated/source$2")

        val packageName = generatedPath.replace("(.*)tmp(/kapt/debug/)kotlinGenerated".toRegex(), "$1generated/source$2")


        /*File srcFile = new File(path + classGeneratingParams.getName()
                                                .replace(".", "/") + ".java");
				*/

        val destFileFullClass = path + classGeneratingParams.name.replace(".", "/") + ".kt"
        val destFile = File(destFileFullClass)

        try {
            val w = processingEnvironment.filer.createResource(SOURCE_OUTPUT, classGeneratingParams.packageNme,
                    classGeneratingParams.getSimpleFileName()).openWriter()

            w.write(classGeneratingParams.body)
            w.flush()
            w.close()


        } catch (e: IOException) {
            e.printStackTrace()
        }


    }
}
