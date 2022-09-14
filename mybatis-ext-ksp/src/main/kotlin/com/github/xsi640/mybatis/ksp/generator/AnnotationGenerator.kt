package com.github.xsi640.mybatis.ksp.generator

import com.github.xsi640.mybatis.ksp.TableDescribe
import com.github.xsi640.mybatis.ksp.getShortName
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ksp.toClassName
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.type.JdbcType

interface AnnotationGenerator {
    fun select(tableDescribe: TableDescribe, where: String): List<AnnotationSpec>
    fun selectElements(table: TableDescribe): AnnotationSpec
    fun count(table: TableDescribe, where: String): List<AnnotationSpec>
    fun insert(table: TableDescribe): List<AnnotationSpec>
    fun batchInsert(table: TableDescribe): List<AnnotationSpec>
    fun update(table: TableDescribe, where: String): List<AnnotationSpec>
    fun delete(table: TableDescribe, where: String): List<AnnotationSpec>
}

class AnnotationGeneratorImpl : AnnotationGenerator {
    override fun select(tableDescribe: TableDescribe, where: String): List<AnnotationSpec> {
        TODO("Not yet implemented")
    }

    override fun selectElements(table: TableDescribe): AnnotationSpec {
        TODO("Not yet implemented")
    }

    override fun count(table: TableDescribe, where: String): List<AnnotationSpec> {
        TODO("Not yet implemented")
    }

    override fun insert(table: TableDescribe): List<AnnotationSpec> {
        TODO("Not yet implemented")
    }

    override fun batchInsert(table: TableDescribe): List<AnnotationSpec> {
        TODO("Not yet implemented")
    }

    override fun update(table: TableDescribe, where: String): List<AnnotationSpec> {
        TODO("Not yet implemented")
    }

    override fun delete(table: TableDescribe, where: String): List<AnnotationSpec> {
        TODO("Not yet implemented")
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