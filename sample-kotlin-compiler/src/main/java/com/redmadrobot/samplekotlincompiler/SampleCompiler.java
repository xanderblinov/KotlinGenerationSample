package com.redmadrobot.samplekotlincompiler;

import com.google.auto.service.AutoService;

import com.redmadrobot.samplelib.SampleAnnotation;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static javax.lang.model.SourceVersion.latestSupported;
import static javax.tools.StandardLocation.SOURCE_OUTPUT;

@SuppressWarnings("unused")
@AutoService(Processor.class)
public class SampleCompiler extends AbstractProcessor {

    private Messager messager;

    private Types typeUtils;

    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedAnnotationTypes = new HashSet<>();
        Collections.addAll(supportedAnnotationTypes, SampleAnnotation.class.getCanonicalName());
        return supportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        processInjectors(roundEnv, SampleAnnotation.class, ElementKind.CLASS,
                new SampleClassGenerator());

        return true;
    }

    private void processInjectors(final RoundEnvironment roundEnv,
            Class<? extends Annotation> clazz, ElementKind kind, ClassGenerator classGenerator) {
        for (Element annotatedElements : roundEnv.getElementsAnnotatedWith(clazz)) {
            if (annotatedElements.getKind() != kind) {
                throw new RuntimeException(
                        annotatedElements + " must be " + kind.name() + ", or not mark it as @"
                                + clazz.getSimpleName());
            }

            generateCode(kind, classGenerator, annotatedElements);
        }
    }

    private void generateCode(ElementKind kind, ClassGenerator classGenerator, Element element) {
        if (element.getKind() != kind) {
            throw new RuntimeException(element + " must be " + kind.name());
        }

        List<ClassGeneratingParams> classGeneratingParamsList = new ArrayList<>();

        //noinspection unchecked
        final boolean generated = classGenerator.generate(element, classGeneratingParamsList);

        if (!generated) {
            return;
        }

        for (ClassGeneratingParams classGeneratingParams : classGeneratingParamsList) {
            createSourceFile(classGeneratingParams);
        }
    }

    private void createSourceFile(ClassGeneratingParams classGeneratingParams) {

        Map<String, String> options = processingEnv.getOptions();
        String generatedPath = options.get("kapt.kotlin.generated");
        String path = generatedPath
                .replaceAll("(.*)tmp(/kapt/debug/)kotlinGenerated",
                        "$1generated/source$2");

        String packageName = generatedPath
                .replaceAll("(.*)tmp(/kapt/debug/)kotlinGenerated",
                        "$1generated/source$2");


				/*File srcFile = new File(path + classGeneratingParams.getName()
                                                .replace(".", "/") + ".java");
				*/

        String destFileFullClass = path + classGeneratingParams.getName()
                .replace(".", "/") + ".kt";
        File destFile = new File(destFileFullClass);

        try {
            Writer w = processingEnv.getFiler()
                    .createResource(SOURCE_OUTPUT, classGeneratingParams.getPackageNme(),
                            classGeneratingParams.getSimpleFileName())
                    .openWriter();

            w.write(classGeneratingParams.getBody());
            w.flush();
            w.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
