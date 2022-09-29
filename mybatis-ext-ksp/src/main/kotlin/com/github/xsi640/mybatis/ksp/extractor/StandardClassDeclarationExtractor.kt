package com.github.xsi640.mybatis.ksp.extractor

import com.github.xsi640.mybatis.core.*
import com.github.xsi640.mybatis.ksp.*
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import org.apache.ibatis.type.JdbcType

class StandardClassDeclarationExtractor : ClassDeclarationExtractor {
    override fun table(classDeclaration: KSClassDeclaration): TableDescribe {
        val table = classDeclaration.getAnnotation(Table::class) ?: throw IllegalArgumentException()
        val tableName = table.getArgumentValue("name")?.toString()!!.ifEmpty {
            classDeclaration.getShortName()
        }
        val schemaName = table.getArgumentValue("schema")?.toString()!!
        val columns = mutableListOf<ColumnDescribe>()
        columns(classDeclaration, columns)
        var current = classDeclaration.getSupperClassDeclaration()
        while (current != null) {
            columns(classDeclaration, columns)
            current = current.getSupperClassDeclaration()
        }
        return TableDescribe(
            schema = schemaName,
            name = tableName,
            columns = columns,
            classDeclaration = classDeclaration
        )
    }

    override fun index(classDeclaration: KSClassDeclaration): List<IndexDescribe> {
        val indexes = classDeclaration.getAnnotations(Index::class)
        return indexes.map {
            val name = it.getArgumentValue("name")?.toString()!!
            @Suppress("UNCHECKED_CAST")
            val properties = it.getArgumentValue("properties") as List<String>
            val unique = it.getArgumentValue("unique")!!.toString().toBoolean()
            IndexDescribe(
                name = name,
                properties = emptyArray(),
                unique = unique,
                classDeclaration = classDeclaration
            )
        }
    }

    private fun columns(classDeclaration: KSClassDeclaration, columns: MutableList<ColumnDescribe>) {
        classDeclaration.getDeclaredProperties().forEach { propertyDeclaration ->
            if (propertyDeclaration.getAnnotation(Ignore::class) != null) {
                return@forEach
            }

            val propertyName = propertyDeclaration.simpleName.getShortName()
            if (columns.any { it.name == propertyName }) {
                return@forEach
            }
            val column = propertyDeclaration.getAnnotation(Column::class)
            val id = propertyDeclaration.getAnnotation(Id::class)
            val converter = propertyDeclaration.getAnnotation(Converter::class)
            val temporal = propertyDeclaration.getAnnotation(Temporal::class)
            val embedded = propertyDeclaration.getAnnotation(Embedded::class)
            val name = if (column == null) {
                propertyName
            } else {
                column.getArgumentValue("name")?.toString()!!.ifEmpty {
                    propertyName
                }
            }
            val nullable = if (column == null) {
                true
            } else {
                column.getArgumentValue("nullable")?.toString().toBoolean()
            }
            val javaType = if (converter == null) {
                null
            } else {
                val cJavaType = converter.getArgumentValue("javaType") as KSType
                if (Unit::class.simpleName == cJavaType.getShortName() ||
                    Void::class.simpleName == cJavaType.getShortName()
                ) {
                    null
                } else {
                    cJavaType
                }
            }
            val jdbcType = if (converter == null) {
                null
            } else {
                val cJdbcType = converter.getArgumentValue("jdbcType") as KSType
                if (JdbcType.UNDEFINED::class.simpleName == cJdbcType.getShortName()) {
                    null
                } else {
                    val sJdbcType = cJdbcType.getShortName()
                    val v = JdbcType.valueOf(sJdbcType)
                    if (v == JdbcType.UNDEFINED)
                        null
                    else
                        v
                }
            }
            val typeHandler = if (converter == null) {
                null
            } else {
                val typeHandlerType = converter.getArgumentValue("typeHandler") as KSType
                if (EmptyTypeHandler::class.simpleName
                    == typeHandlerType.getShortName()
                ) {
                    null
                } else {
                    typeHandlerType
                }
            }
            val primaryKeyGenerate = if (id == null) {
                null
            } else {
                id.getArgumentValue("generate")?.toString().toBoolean()
            }
            val temporalType = if (temporal == null) {
                null
            } else {
                val cTemporal = temporal.getArgumentValue("type") as KSType
                val sTemporal = cTemporal.getShortName()
                TemporalType.valueOf(sTemporal)
            }
            val embed = embedded != null
            columns.add(
                ColumnDescribe(
                    name = name,
                    propertyName = propertyName,
                    type = propertyDeclaration.type.resolve(),
                    nullable = nullable,
                    javaType = javaType,
                    jdbcType = jdbcType,
                    typeHandler = typeHandler,
                    primaryKeyGenerate = primaryKeyGenerate,
                    temporalType = temporalType,
                    embedded = embed
                )
            )
        }
    }
}