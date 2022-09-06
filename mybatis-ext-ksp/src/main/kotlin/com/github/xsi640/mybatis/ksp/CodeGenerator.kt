package com.github.xsi640.mybatis.ksp

import com.google.devtools.ksp.processing.CodeGenerator

interface CodeGenerator {
    fun generate(codeGenerator: CodeGenerator, packageName: String, className: String, tableDescribe: TableDescribe)
}