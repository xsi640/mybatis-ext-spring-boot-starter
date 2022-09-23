package com.github.xsi640.mybatis.ksp.generator

import com.github.xsi640.mybatis.ast.ComputeExpression
import com.github.xsi640.mybatis.ast.OrderByExpression
import com.github.xsi640.mybatis.core.QueryProvider
import com.github.xsi640.mybatis.ksp.*
import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.writeTo
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.SelectProvider

interface MapperGenerator {

    val sqlGenerator: SqlGenerator
    val annotationGenerator: AnnotationGenerator
    fun generate(codeGenerator: CodeGenerator, packageName: String, className: String, tableDescribe: TableDescribe)
}

class MapperGeneratorImpl(
    override val sqlGenerator: SqlGenerator,
    override val annotationGenerator: AnnotationGenerator,
) : MapperGenerator {

    override fun generate(
        codeGenerator: CodeGenerator,
        packageName: String,
        className: String,
        tableDescribe: TableDescribe
    ) {
        val companion = TypeSpec.companionObjectBuilder()
            .addProperty(
                PropertySpec.builder("kClass", tableDescribe.classDeclaration.asKClassTypeName())
                    .initializer("%T::class", tableDescribe.classDeclaration.asTypeName())
                    .build()
            ).build()

        val typeSpec = TypeSpec.interfaceBuilder("${className}Mapper")
            .addAnnotation(
                AnnotationSpec.builder(Suppress::class).addMember("%S", "REDUNDANT_MODIFIER_FOR_TARGET").build()
            )
            .addType(companion)
            .addFunctions(generateFunctions(tableDescribe))
            .addModifiers(KModifier.PUBLIC, KModifier.OPEN)
            .build()
        val fileSpec = FileSpec.builder(packageName, typeSpec.name!!)
            .addType(typeSpec)
            .build()
        fileSpec.writeTo(codeGenerator, false)
    }

    private fun generateFunctions(tableDescribe: TableDescribe): Iterable<FunSpec> {
        val result = mutableListOf<FunSpec>()
        val primaryKey = tableDescribe.columns.firstOrNull {
            it.primaryKeyGenerate != null
        } ?: throw IllegalArgumentException("The primary key not found.")
        result.add(
            FunSpec.builder("list")
                .addModifiers(KModifier.ABSTRACT)
                .addAnnotations(annotationGenerator.select(tableDescribe, ""))
                .addParameter("page", Long::class.asTypeName())
                .addParameter("count", Long::class.asTypeName())
                .returns(tableDescribe.classDeclaration.asPageTypeName())
                .build()
        )
        result.add(
            FunSpec.builder("listByWhere")
                .addModifiers(KModifier.ABSTRACT)
                .addAnnotation(annotationGenerator.selectElements(tableDescribe))
                .addAnnotation(providerAnnotation("listByWherePage"))
                .addParameter("page", Long::class.asTypeName())
                .addParameter("count", Long::class.asTypeName())
                .addParameter("where", ComputeExpression::class.asTypeName().copy(true))
                .addParameter("selects", List::class.parameterizedBy(String::class).copy(true))
                .addParameter("order", OrderByExpression::class.asTypeName(), KModifier.VARARG)
                .returns(tableDescribe.classDeclaration.asPageTypeName())
                .build()
        )
        result.add(
            FunSpec.builder("listAll")
                .addModifiers(KModifier.ABSTRACT)
                .addAnnotations(annotationGenerator.select(tableDescribe, ""))
                .returns(tableDescribe.classDeclaration.asListTypeName())
                .build()
        )
        result.add(
            FunSpec.builder("listAllByWhere")
                .addAnnotation(annotationGenerator.selectElements(tableDescribe))
                .addAnnotation(providerAnnotation("listAllByWhere"))
                .addParameter("where", ComputeExpression::class.asTypeName().copy(true))
                .addParameter("selects", List::class.parameterizedBy(String::class).copy(true))
                .addParameter("order", OrderByExpression::class.asTypeName(), KModifier.VARARG)
                .returns(tableDescribe.classDeclaration.asListTypeName())
                .build()
        )
        result.add(
            FunSpec.builder("findOneByWhere")
                .addAnnotation(annotationGenerator.selectElements(tableDescribe))
                .addAnnotation(providerAnnotation("findOneByWhere"))
                .addParameter("where", ComputeExpression::class.asTypeName().copy(true))
                .addParameter("selects", List::class.parameterizedBy(String::class).copy(true))
                .returns(tableDescribe.classDeclaration.asTypeName())
                .build()
        )
        result.add(
            FunSpec.builder("findOneById")
                .addAnnotations(
                    annotationGenerator.select(
                        tableDescribe,
                        "${primaryKey.name}=${sqlGenerator.column(primaryKey, "")}"
                    )
                )
                .addParameter(
                    ParameterSpec.builder("id", primaryKey.type.asTypeName())
                        .param(primaryKey.name)
                        .build()
                )
                .build()
        )
        TODO("generate functions")
    }

    private fun providerAnnotation(method: String): AnnotationSpec {
        return AnnotationSpec.builder(SelectProvider::class)
            .addMember("type = %T::class", QueryProvider::class)
            .addMember("method = %S", method)
            .build()
    }
}

fun ParameterSpec.Builder.param(s: String): ParameterSpec.Builder {
    return this.addAnnotation(
        AnnotationSpec.builder(Param::class)
            .addMember("%S", s)
            .build()
    )
}