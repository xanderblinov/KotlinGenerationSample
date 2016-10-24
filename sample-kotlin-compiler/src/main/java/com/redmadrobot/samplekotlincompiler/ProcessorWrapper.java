package com.redmadrobot.samplekotlincompiler;

import com.google.auto.service.AutoService;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * Date: 24/10/16
 *
 * @author Alexander Blinov
 */

@SuppressWarnings("unused")
@AutoService(Processor.class)
public class ProcessorWrapper extends AbstractProcessor {

    private SampleCompiler mSampleCompiler;

    public ProcessorWrapper() {
        mSampleCompiler = new SampleCompiler();
    }

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        mSampleCompiler.init(processingEnv);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return mSampleCompiler.getSupportedAnnotationTypes();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return mSampleCompiler.getSupportedSourceVersion();
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {
        return mSampleCompiler.process(annotations, roundEnv);
    }
}
