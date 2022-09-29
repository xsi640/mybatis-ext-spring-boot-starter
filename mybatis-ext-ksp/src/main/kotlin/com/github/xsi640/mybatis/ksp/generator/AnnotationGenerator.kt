package com.github.xsi640.mybatis.ksp.generator

import com.github.xsi640.mybatis.ksp.TableDescribe
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ksp.toClassName
import org.apache.ibatis.annotations.*
import org.apache.ibatis.type.JdbcType

interface AnnotationGenerator {
    fun select(table: TableDescribe, where: String): List<AnnotationSpec>
    fun selectElements(table: TableDescribe): AnnotationSpec
    fun count(table: TableDescribe, where: String): List<AnnotationSpec>
    fun insert(table: TableDescribe): List<AnnotationSpec>
    fun inserts(table: TableDescribe): List<AnnotationSpec>
    fun update(table: TableDescribe, where: String): List<AnnotationSpec>
    fun delete(table: TableDescribe, where: String): List<AnnotationSpec>
}

class AnnotationGeneratorImpl(
    val generator: SqlGenerator
) : AnnotationGenerator {
    override fun select(table: TableDescribe, where: String): List<AnnotationSpec> {
        val result = mutableListOf<AnnotationSpec>()
        result.add(
            AnnotationSpec.builder(Select::class)
                .addMember("%S", generator.select(table, where))
                .build()
        )
        result.add(selectElements(table))
        return result
    }

    override fun selectElements(table: TableDescribe): AnnotationSpec {
        return buildSelectResults(table)
    }

    override fun count(table: TableDescribe, where: String): List<AnnotationSpec> {
        val result = mutableListOf<AnnotationSpec>()
        result.add(
            AnnotationSpec.builder(Select::class)
                .addMember("%S", generator.count(table, where))
                .build()
        )
        return result
    }

    override fun insert(table: TableDescribe): List<AnnotationSpec> {
        val result = mutableListOf<AnnotationSpec>()
        val primaryKey = table.columns.firstOrNull { it.primaryKeyGenerate != null }
            ?: throw IllegalArgumentException("primary key not empty.")
        result.add(
            AnnotationSpec.builder(Insert::class)
                .addMember("%S", generator.insert(table))
                .build()
        )
        if (primaryKey.primaryKeyGenerate == true) {
            result.add(
                AnnotationSpec.builder(Options::class)
                    .addMember("useGeneratedKeys = true")
                    .addMember("keyProperty = %S", primaryKey.propertyName)
                    .addMember("keyColumn = %S", primaryKey.name)
                    .build()
            )
        }
        return result
    }

    override fun inserts(table: TableDescribe): List<AnnotationSpec> {
        val result = mutableListOf<AnnotationSpec>()
        val primaryKey = table.columns.firstOrNull { it.primaryKeyGenerate != null }
            ?: throw IllegalArgumentException("primary key not empty.")
        result.add(
            AnnotationSpec.builder(Insert::class)
                .addMember("%S", generator.batchInsert(table))
                .build()
        )
        if (primaryKey.primaryKeyGenerate == true) {
            result.add(
                AnnotationSpec.builder(Options::class)
                    .addMember("useGeneratedKeys = true")
                    .addMember("keyProperty = %S", primaryKey.propertyName)
                    .addMember("keyColumn = %S", primaryKey.name)
                    .build()
            )
        }
        return result
    }

    override fun update(table: TableDescribe, where: String): List<AnnotationSpec> {
        val result = mutableListOf<AnnotationSpec>()
        result.add(
            AnnotationSpec.builder(Update::class)
                .addMember("%S", generator.update(table, where))
                .build()
        )
        return result
    }

    override fun delete(table: TableDescribe, where: String): List<AnnotationSpec> {
        val result = mutableListOf<AnnotationSpec>()
        result.add(
            AnnotationSpec.builder(Delete::class)
                .addMember("%S", generator.delete(table, where))
                .build()
        )
        return result
    }

    private fun buildSelectResults(table: TableDescribe): AnnotationSpec {
        val columns = mutableListOf<AnnotationSpec>()
        val sb = StringBuilder()
        table.columns.forEachIndexed { i, column ->
            val builder = AnnotationSpec.builder(Result::class)
                .addMember("property = %S", column.propertyName)
                .addMember("column = %S", column.name)
            if (column.jdbcType != null && column.jdbcType != JdbcType.UNDEFINED) {
                builder.addMember("jdbcType = %T.%L", JdbcType::class, column.jdbcType.name)
            }
            if (column.javaType != null) {
                builder.addMember("javaType = %L::class", column.type.toClassName())//需要转java类型
            }
            if (column.typeHandler != null) {
                builder.addMember("typeHandler = %L::class", column.typeHandler.toClassName())
            }
            columns.add(builder.build())
            sb.append("%L")
            if (i != table.columns.size - 1) {
                sb.append(", \n")
            }
        }
        return AnnotationSpec.builder(Results::class)
            .addMember(sb.toString(), *columns.toTypedArray())
            .build()
    }
}