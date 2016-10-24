package com.redmadrobot.samplekotlincompiler

import javax.lang.model.element.Element

/**
 * Date: 12-Jan-16
 * Time: 10:35

 * @author Alexander Blinov
 */
internal abstract class ClassGenerator<in ElementType> where ElementType : Element {
    abstract fun generate(element: ElementType, classGeneratingParamsList: MutableList<ClassGeneratingParams>): Boolean
}
