package com.sky.xposed.compiler.generate;

import com.squareup.javapoet.ClassName;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by sky on 2020-02-16.
 */
public abstract class BaseGenerate {

    ProcessingEnvironment mProcessing;
    Messager mMessager;
    Elements mElements;

    public BaseGenerate(ProcessingEnvironment processing) {
        mProcessing = processing;
        mMessager = mProcessing.getMessager();
        mElements = mProcessing.getElementUtils();
    }

    void printError(Element e, String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

    void printError(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    void printInfo(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }

    ClassName className(String className) {
        TypeElement element = findTypeElement(className);
        return element != null ? ClassName.get(element) : null;
    }

    TypeElement findTypeElement(String className) {
        return mElements.getTypeElement(className);
    }
}
