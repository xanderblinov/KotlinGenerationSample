package com.redmadrobot.samplekotlincompiler;

import com.redmadrobot.samplelib.SampleInterface;

import java.util.List;

import javax.lang.model.element.TypeElement;

/**
 * Date: 22/10/16
 *
 * @author Alexander Blinov
 */

class SampleClassGenerator extends ClassGenerator<TypeElement> {

    public static final String GENERATED_FILE_SUFFIX = "_Generated";

    public static final String IMPL_NAME = SampleInterface.class.getSimpleName();

    public static final String IMPL_IMPORT = SampleInterface.class.getCanonicalName();

    @Override
    boolean generate(final TypeElement typeElement,
            final List<ClassGeneratingParams> classGeneratingParamsList) {

        String parentClassName = typeElement.toString();

        final String viewClassName = parentClassName
                .substring(parentClassName.lastIndexOf(".") + 1);

        String packageName = parentClassName.substring(0, parentClassName.lastIndexOf("."));

        createGeneratingParams(classGeneratingParamsList, viewClassName, packageName);

        return true;
    }

    private void createGeneratingParams(final List<ClassGeneratingParams> classGeneratingParamsList,
            final String viewClassName, final String packageName) {
        final String generatedFileName = viewClassName + GENERATED_FILE_SUFFIX;

        final String body = "package " + packageName + "\n"
                + "\n"
                + "import " + IMPL_IMPORT + "\n"
                + "\n" +
                "class " + generatedFileName + " : " + IMPL_NAME +
                "{\n"
                + "   override fun getSampleString() = " + "\"Generated using " + viewClassName
                + "\""
                + "\n"
                + "}";

        ClassGeneratingParams params = new ClassGeneratingParams();
        params.setName(generatedFileName);
        params.setBody(body);
        params.setPackage(packageName);
        classGeneratingParamsList.add(params);
    }
}
