package com.github.xsi640.mybatis.ksp

import ch.qos.logback.classic.pattern.PropertyConverter
import com.github.xsi640.mybatis.core.TemporalType
import com.google.devtools.ksp.symbol.KSClassDeclaration
import org.apache.ibatis.type.JdbcType
import kotlin.reflect.KClass

data class TableDescribe(
    val schema: String,
    val name: String,
    val columns: List<ColumnDescribe>,
    val classDeclaration: KSClassDeclaration
)

data class ColumnDescribe(
    val name: String,
    val nullable: Boolean,
    val javaType: KClass<*>,
    val jdbcType: JdbcType,
    val typeHandler: KClass<out PropertyConverter>,
    val primaryKeyGenerate: Boolean?,
    val ignore: Boolean,
    val temporalType: TemporalType?,
    val embedded: Boolean
)