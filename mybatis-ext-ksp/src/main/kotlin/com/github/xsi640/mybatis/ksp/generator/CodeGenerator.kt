package com.github.xsi640.mybatis.ksp.generator

import com.github.xsi640.mybatis.ksp.ColumnDescribe
import com.github.xsi640.mybatis.ksp.TableDescribe
import com.google.devtools.ksp.processing.CodeGenerator

interface CodeGenerator {
    fun generate(codeGenerator: CodeGenerator, packageName: String, className: String, tableDescribe: TableDescribe)
}

interface SqlGenerator {
    fun select(tableDescribe: TableDescribe, where: String): String
    fun count(tableDescribe: TableDescribe, where: String): String
    fun insert(tableDescribe: TableDescribe): String
    fun batchInsert(tableDescribe: TableDescribe): String
    fun update(tableDescribe: TableDescribe, where: String): String
    fun delete(tableDescribe: TableDescribe, where: String): String
    fun column(columnDescribe: ColumnDescribe, aliasTableName: String): String
}