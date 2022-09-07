package com.github.xsi640.mybatis.ksp.generator

import com.github.xsi640.mybatis.ksp.TableDescribe
import com.google.devtools.ksp.processing.CodeGenerator

interface MapperGenerator {
    fun generate(codeGenerator: CodeGenerator, packageName: String, className: String, tableDescribe: TableDescribe)
}

class MapperGeneratorImpl : MapperGenerator {
    override fun generate(
        codeGenerator: CodeGenerator,
        packageName: String,
        className: String,
        tableDescribe: TableDescribe
    ) {
        TODO("Not yet implemented")
    }
}