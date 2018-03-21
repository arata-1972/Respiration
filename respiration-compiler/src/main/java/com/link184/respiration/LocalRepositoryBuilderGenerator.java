package com.link184.respiration;

import com.link184.respiration.base.BaseBuilderGenerator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/**
 * Created by eugeniu on 3/6/18.
 */

public class LocalRepositoryBuilderGenerator extends BaseBuilderGenerator {

    @Override
    public MethodSpec generateMethodSingleInit(Map.Entry<Element, String> entry) {
        ClassName configurationClass = ClassName.get("com.link184.respiration.repository.local", "LocalConfiguration");

        LocalRepository annotation = entry.getKey().getAnnotation(LocalRepository.class);
        TypeName modelTypeName = GenerationUtils.extractTypeName(annotation);
        MethodSpec.Builder createMethod = MethodSpec
                .methodBuilder(METHOD_CREATE_INSTANCE)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(TypeName.get(entry.getKey().asType()))
                .addStatement("$T<$T> configuration = new $T<>($T.class)",
                        configurationClass, modelTypeName, configurationClass, modelTypeName)
                .addStatement("configuration.setDbName($L)", annotation.dataBaseName())
                .addStatement("configuration.setAssetDbFilePath($L)", annotation.dataBaseAssetPath());
        if (annotation.children().length > 0 && !annotation.children()[0].isEmpty()) {
            createMethod.addStatement("configuration.setDatabaseChildren($L)", GenerationUtils.generateChildrenArray(annotation));
        }
        createMethod.addStatement("return new $T($N)", entry.getKey(), "configuration");
        return createMethod.build();
    }
}
