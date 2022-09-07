package com.github.xsi640.mybatis.ksp.generator

import com.github.xsi640.mybatis.ksp.TableDescribe
import com.google.devtools.ksp.processing.CodeGenerator

interface CodeGenerator {
    fun generate(codeGenerator: CodeGenerator, packageName: String, className: String, tableDescribe: TableDescribe)
}