package com.github.xsi640.mybatis.ksp

import com.github.xsi640.mybatis.core.*
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import org.apache.ibatis.type.JdbcType

class StandardClassDeclarationExtractor : ClassDeclarationExtractor {
    override fun table(classDeclaration: KSClassDeclaration): TableDescribe {
        val table = classDeclaration.getAnnotation(Table::class) ?: throw IllegalArgumentException()
        val tableName = table.getArgument("name")?.value.toString().ifEmpty {
            classDeclaration.simpleName.getShortName()
        }
        val schemaName = table.getArgument("schema")?.value.toString()
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
        TODO("Not yet implemented")
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
                column.getArgument("name")?.value.toString().ifEmpty {
                    propertyName
                }
            }
            val nullable = if (column == null) {
                true
            } else {
                column.getArgument("nullable")?.value.toString().toBoolean()
            }
            val javaType = if (converter == null) {
                null
            } else {
                val cJavaType = converter.getArgument("javaType")?.value as KSType
                if (Unit::class.simpleName == cJavaType.declaration.simpleName.getShortName() ||
                    Void::class.simpleName == cJavaType.declaration.simpleName.getShortName()
                ) {
                    null
                } else {
                    cJavaType
                }
            }
            val jdbcType = if (converter == null) {
                null
            } else {
                val cJdbcType = converter.getArgument("jdbcType")?.value as KSType
                if (JdbcType.UNDEFINED::class.simpleName == cJdbcType.declaration.simpleName.getShortName()) {
                    null
                } else {
                    val sJdbcType = cJdbcType.declaration.simpleName.getShortName()
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
                val typeHandlerType = converter.getArgument("typeHandler")?.value as KSType
                if (EmptyPropertyConverter::class.simpleName
                    == typeHandlerType.declaration.simpleName.getShortName()
                ) {
                    null
                } else {
                    typeHandlerType
                }
            }
            val primaryKeyGenerate = if (id == null) {
                null
            } else {
                id.getArgument("generate")?.value.toString().toBoolean()
            }
            val temporalType = if (temporal == null) {
                null
            } else {
                val cTemporal = temporal.getArgument("type")?.value as KSType
                val sTemporal = cTemporal.declaration.simpleName.getShortName()
                TemporalType.valueOf(sTemporal)
            }
            val embed = embedded != null
            ColumnDescribe(
                name = name,
                type = propertyDeclaration.type.resolve(),
                nullable = nullable,
                javaType = javaType,
                jdbcType = jdbcType,
                typeHandler = typeHandler,
                primaryKeyGenerate = primaryKeyGenerate,
                temporalType = temporalType,
                embedded = embed
            )
        }
    }
}