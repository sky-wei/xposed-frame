/*
 * Copyright (c) 2020 The sky Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sky.xposed.compiler;

import com.google.auto.service.AutoService;
import com.sky.xposed.annotations.AConfig;
import com.sky.xposed.annotations.APlugin;
import com.sky.xposed.compiler.generate.GenerateStore;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by sky on 2020-03-12.
 */
@AutoService(Processor.class)
public class XAnnotationProcessor extends AbstractProcessor {

    private Messager mMessager;
    private Elements mElements;
    private Filer mFiler;

    private List<TypeElement> mConfigElements;
    private List<TypeElement> mPluginElements;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnv.getMessager();
        mElements = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        if (set == null || set.isEmpty()) {
            return false;
        }

        // 开始处理注解
        annotationProcessor(roundEnvironment);

        // 生成相应的文件
        generateConfigFiles();
        return true;
    }

    private void annotationProcessor(RoundEnvironment environment) {

        mConfigElements = handlerElements(environment.getElementsAnnotatedWith(AConfig.class));
        mPluginElements = handlerElements(environment.getElementsAnnotatedWith(APlugin.class));
    }

    private void generateConfigFiles() {

        ClassName storeClassName = ClassName.get("", "XStore");
        GenerateStore generateStore = new GenerateStore(processingEnv);
        generateFile("com.sky.xposed.core",
                generateStore.generate(storeClassName, mConfigElements, mPluginElements));
    }

    private List<TypeElement> handlerElements(Set<? extends Element> elements) {

        if (elements == null || elements.isEmpty()) {
            return new ArrayList<>();
        }

        Iterator iterator = elements.iterator();
        List<TypeElement> value = new ArrayList<>();

        while (iterator.hasNext()) {

            TypeElement element = (TypeElement) iterator.next();

            if (element.getKind().isClass()) {

                value.add(element);
            }
        }
        return value;
    }

    private void generateFile(String packageName, TypeSpec typeSpec) {

        try {
            JavaFile.builder(packageName, typeSpec)
                    .build()
                    .writeTo(mFiler);
        } catch (IOException e) {
            printError("生成文件异常!");
        }
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(AConfig.class.getCanonicalName());
        set.add(APlugin.class.getCanonicalName());
        return set;
    }

    public void printError(Element e, String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

    public void printError(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    public void printInfo(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }
}
