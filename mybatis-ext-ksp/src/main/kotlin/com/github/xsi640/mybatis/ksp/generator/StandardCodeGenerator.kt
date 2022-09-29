package com.github.xsi640.mybatis.ksp.generator

import com.github.xsi640.mybatis.ksp.TableDescribe

class StandardCodeGenerator(
    sqlGenerator: SqlGenerator,
    annotationGenerator: AnnotationGenerator
) : CodeGenerator {

    private val mapperGenerator = MapperGeneratorImpl(sqlGenerator, annotationGenerator)

    override fun generate(
        codeGenerator: com.google.devtools.ksp.processing.CodeGenerator,
        packageName: String,
        className: String,
        tableDescribe: TableDescribe
    ) {
        mapperGenerator.generate(codeGenerator, packageName, className, tableDescribe)
    }
}

