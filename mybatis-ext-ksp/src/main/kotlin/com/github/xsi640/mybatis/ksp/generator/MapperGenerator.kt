package com.github.xsi640.mybatis.ksp.generator

import com.github.xsi640.mybatis.ksp.TableDescribe
import com.github.xsi640.mybatis.ksp.asKClassTypeName
import com.github.xsi640.mybatis.ksp.asPageTypeName
import com.github.xsi640.mybatis.ksp.asTypeName
import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.writeTo

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

        TODO("generate functions")
    }
}