package com.github.xsi640.mybatis.ksp

import com.github.xsi640.mybatis.core.TemporalType
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import org.apache.ibatis.type.JdbcType

data class TableDescribe(
    val schema: String,
    val name: String,
    val columns: List<ColumnDescribe>,
    val classDeclaration: KSClassDeclaration
)

data class ColumnDescribe(
    val name: String,
    val propertyName: String,
    val type: KSType,
    val nullable: Boolean,
    val javaType: KSType?,
    val jdbcType: JdbcType?,
    val typeHandler: KSType?,
    val primaryKeyGenerate: Boolean?,
    val temporalType: TemporalType?,
    val embedded: Boolean
)