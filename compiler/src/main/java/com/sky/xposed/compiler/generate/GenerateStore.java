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

package com.sky.xposed.compiler.generate;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Created by sky on 2020-02-16.
 */
public class GenerateStore extends BaseGenerate {

    public GenerateStore(ProcessingEnvironment processing) {
        super(processing);
    }

    public TypeSpec generate(ClassName className,
                             List<TypeElement> aConfigInfos, List<TypeElement> aPluginInfos) {

        // 生成相应的类
        TypeSpec.Builder builderStore = TypeSpec.classBuilder(className);
        builderStore.addModifiers(Modifier.PUBLIC);

        builderStore.addMethod(generateConfigMethod(aConfigInfos));
        builderStore.addMethod(generatePluginMethod(aPluginInfos));

        return builderStore.build();
    }

    private MethodSpec generateConfigMethod(List<TypeElement> aConfigInfos) {

        ClassName className = className("com.sky.xposed.core.interfaces.XConfig");

        CodeBlock.Builder builder = CodeBlock.builder()
                .addStatement("$T<Class<? extends $T>> list = new $T<>()", List.class, className, ArrayList.class);

        for (TypeElement element : aConfigInfos) {
            builder.addStatement("list.add($T.class)", ClassName.get(element));
        }

        builder.addStatement("return list").build();

        return MethodSpec.methodBuilder("getConfigClass")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addCode(builder.build())
                .returns(TypeVariableName.get("List<Class<? extends XConfig>>"))
                .build();
    }

    private MethodSpec generatePluginMethod(List<TypeElement> aPluginInfos) {

        ClassName className = className("com.sky.xposed.core.interfaces.XPlugin");

        CodeBlock.Builder builder = CodeBlock.builder()
                .addStatement("$T<Class<? extends $T>> list = new $T<>()", List.class, className, ArrayList.class);


        for (TypeElement element : aPluginInfos) {
            builder.addStatement("list.add($T.class)", ClassName.get(element));
        }

        builder.addStatement("return list").build();

        return MethodSpec.methodBuilder("getPluginClass")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addCode(builder.build())
                .returns(TypeVariableName.get("List<Class<? extends XPlugin>>"))
                .build();
    }
}
