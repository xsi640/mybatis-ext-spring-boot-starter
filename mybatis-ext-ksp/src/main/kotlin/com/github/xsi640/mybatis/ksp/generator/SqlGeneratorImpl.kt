package com.github.xsi640.mybatis.ksp.generator

import com.github.xsi640.mybatis.ksp.ColumnDescribe
import com.github.xsi640.mybatis.ksp.TableDescribe
import org.apache.ibatis.type.JdbcType
import kotlin.text.StringBuilder

class SqlGeneratorImpl : SqlGenerator {
    override fun select(tableDescribe: TableDescribe, where: String): String {
        val sb = StringBuilder()
        sb.append("SELECT ")
        columnNames(tableDescribe.columns, sb)
        sb.append(" FROM ")
        if (tableDescribe.schema.isNotEmpty()) {
            sb.append(tableDescribe.schema).append(".")
        }
        sb.append(tableDescribe.name)
        if (where.isNotEmpty()) {
            sb.append(" WHERE ").append(where)
        }
        return sb.toString()
    }

    override fun count(tableDescribe: TableDescribe, where: String): String {
        val sb = StringBuilder()
        sb.append("SELECT COUNT(*) FROM ")
        if (tableDescribe.schema.isNotEmpty()) {
            sb.append(tableDescribe.schema).append(".")
        }
        sb.append(tableDescribe.name)
        if (where.isNotEmpty()) {
            sb.append(" WHERE ").append(where)
        }
        return sb.toString()
    }

    override fun insert(tableDescribe: TableDescribe): String {
        val sb = StringBuilder()
        sb.append("INSERT INTO ")
        if (tableDescribe.schema.isNotEmpty()) {
            sb.append(tableDescribe.schema).append(".")
        }
        sb.append(tableDescribe.name)
        sb.append("(")
        val columns = tableDescribe.columns.filter { it.primaryKeyGenerate == null }
        columnNames(columns, sb)
        sb.append(") VALUES(")
        columnValues(columns, "", sb)
        sb.append(")")
        return sb.toString()
    }

    override fun batchInsert(tableDescribe: TableDescribe): String {
        val sb = StringBuilder()
        sb.append("<script>INSERT INTO ")
        if (tableDescribe.schema.isNotEmpty()) {
            sb.append(tableDescribe.schema).append(".")
        }
        sb.append(tableDescribe.name)
        sb.append("(")
        val columns = tableDescribe.columns.filter { it.primaryKeyGenerate == null }
        columnNames(columns, sb)
        sb.append(") VALUES ")
        sb.append("<foreach collection='elements' item='element' index='i' separator=','>(")
        columnValues(columns, "element", sb)
        sb.append(")</foreach></script>")
        return sb.toString()
    }

    override fun update(tableDescribe: TableDescribe, where: String): String {
        val sb = StringBuilder()
        sb.append("UPDATE ")
        if (tableDescribe.schema.isNotEmpty()) {
            sb.append(tableDescribe.schema).append(".")
        }
        sb.append(tableDescribe.name)
        sb.append(" SET ")
        val columns = tableDescribe.columns.filter { it.primaryKeyGenerate == null }
        columns.forEachIndexed { i, column ->
            sb.append(column.name).append("=")
            columnValue(column, "", sb)
            if (i != columns.size) {
                sb.append(", ")
            }
        }
        if (where.isNotEmpty()) {
            sb.append(" WHERE ").append(where)
        }
        return sb.toString()
    }

    override fun delete(tableDescribe: TableDescribe, where: String): String {
        val sb = StringBuilder()
        sb.append("DELETE FROM ")
        if (tableDescribe.schema.isNotEmpty()) {
            sb.append(tableDescribe.schema).append(".")
        }
        sb.append(tableDescribe.name)
        if (where.isNotEmpty()) {
            sb.append(" WHERE ").append(where)
        }
        return sb.toString()
    }

    override fun column(columnDescribe: ColumnDescribe, aliasTableName: String): String {
        val sb = StringBuilder()
        columnValue(columnDescribe, aliasTableName, sb)
        return sb.toString()
    }

    private fun columnNames(columns: List<ColumnDescribe>, sb: StringBuilder) {
        columns.forEachIndexed { i, column ->
            sb.append(column.name)
            if (i != columns.size) {
                sb.append(", ")
            }
        }
    }

    private fun columnValues(columns: List<ColumnDescribe>, aliasTableName: String, sb: StringBuilder) {
        columns.forEachIndexed { i, column ->
            columnValue(column, aliasTableName, sb)
            if (i != columns.size) {
                sb.append(", ")
            }
        }
    }

    private fun columnValue(column: ColumnDescribe, aliasTableName: String, sb: StringBuilder) {
        sb.append("#{")
        if (aliasTableName.isNotEmpty()) {
            sb.append(aliasTableName).append(".")
        }
        sb.append(column.name)
        if (column.jdbcType != null && column.jdbcType != JdbcType.UNDEFINED) {
            sb.append(", jdbcType=").append(column.jdbcType.name)
        }
        if (column.javaType != null) {
            sb.append(", javaType=").append(column.javaType.declaration.qualifiedName!!.asString().asJavaType())
        }
        if (column.typeHandler != null) {
            sb.append(", typeHandler=").append(column.typeHandler.declaration.qualifiedName!!.asString())
        }
        sb.append("}")
    }

    private fun String.asJavaType(): String {
        if (this == "kotlin.Byte") {
            return "java.lang.Byte"
        } else if (this == "kotlin.Short") {
            return "java.lang.Short"
        } else if (this == "kotlin.Int") {
            return "java.lang.Integer"
        } else if (this == "kotlin.Long") {
            return "java.lang.Long"
        } else if (this == "kotlin.Float") {
            return "java.lang.Float"
        } else if (this == "kotlin.Double") {
            return "java.lang.Double"
        } else if (this == "kotlin.String") {
            return "java.lang.String"
        }
        return this
    }
}