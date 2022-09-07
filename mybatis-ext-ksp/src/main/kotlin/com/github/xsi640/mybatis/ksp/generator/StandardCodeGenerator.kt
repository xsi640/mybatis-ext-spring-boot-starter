package com.github.xsi640.mybatis.ksp.generator

import com.github.xsi640.mybatis.ksp.TableDescribe

class StandardCodeGenerator : CodeGenerator {

    private val mapperGenerator = MapperGeneratorImpl()

    override fun generate(
        codeGenerator: com.google.devtools.ksp.processing.CodeGenerator,
        packageName: String,
        className: String,
        tableDescribe: TableDescribe
    ) {
        mapperGenerator.generate(codeGenerator, packageName, className, tableDescribe)
    }
}

