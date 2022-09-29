package com.github.xsi640.mybatis.ksp.generator

import com.github.xsi640.mybatis.ast.ComputeExpression
import com.github.xsi640.mybatis.ast.OrderByExpression
import com.github.xsi640.mybatis.core.ForeachLanguageDriver
import com.github.xsi640.mybatis.core.QueryProvider
import com.github.xsi640.mybatis.ksp.*
import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.writeTo
import org.apache.ibatis.annotations.*
import kotlin.reflect.KClass

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
                .addAnnotation(providerAnnotation(SelectProvider::class, "listByWherePage"))
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
                .addModifiers(KModifier.ABSTRACT)
                .addAnnotation(annotationGenerator.selectElements(tableDescribe))
                .addAnnotation(providerAnnotation(SelectProvider::class, "listAllByWhere"))
                .addParameter("where", ComputeExpression::class.asTypeName().copy(true))
                .addParameter("selects", List::class.parameterizedBy(String::class).copy(true))
                .addParameter("order", OrderByExpression::class.asTypeName(), KModifier.VARARG)
                .returns(tableDescribe.classDeclaration.asListTypeName())
                .build()
        )
        result.add(
            FunSpec.builder("findOneByWhere")
                .addModifiers(KModifier.ABSTRACT)
                .addAnnotation(annotationGenerator.selectElements(tableDescribe))
                .addAnnotation(providerAnnotation(SelectProvider::class, "findOneByWhere"))
                .addParameter("where", ComputeExpression::class.asTypeName().copy(true))
                .addParameter("selects", List::class.parameterizedBy(String::class).copy(true))
                .returns(tableDescribe.classDeclaration.asTypeName())
                .build()
        )
        result.add(
            FunSpec.builder("findOneById")
                .addModifiers(KModifier.ABSTRACT)
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
        result.add(
            FunSpec.builder("findAllById")
                .addModifiers(KModifier.ABSTRACT)
                .addAnnotations(
                    annotationGenerator.select(
                        tableDescribe,
                        "${primaryKey.name} IN (#{ids})"
                    )
                )
                .addAnnotation(
                    AnnotationSpec.builder(Lang::class)
                        .addMember("%T::class", ForeachLanguageDriver::class).build()
                )
                .addParameter(
                    ParameterSpec.builder("ids", primaryKey.type.asListTypeName())
                        .param("ids")
                        .build()
                )
                .returns(tableDescribe.classDeclaration.asListTypeName())
                .build()
        )
        result.add(
            FunSpec.builder("insert")
                .addModifiers(KModifier.ABSTRACT)
                .addAnnotations(annotationGenerator.insert(tableDescribe))
                .addParameter(tableDescribe.name.camelName(), tableDescribe.classDeclaration.asTypeName())
                .returns(Long::class.asTypeName())
                .build()
        )
        result.add(
            FunSpec.builder("inserts")
                .addModifiers(KModifier.ABSTRACT)
                .addAnnotations(annotationGenerator.inserts(tableDescribe))
                .addParameter(
                    ParameterSpec.builder("items", tableDescribe.classDeclaration.asListTypeName())
                        .param("items").build()
                )
                .returns(Long::class.asTypeName())
                .build()
        )
        result.add(
            FunSpec.builder("update")
                .addModifiers(KModifier.ABSTRACT)
                .addAnnotations(
                    annotationGenerator.update(
                        tableDescribe,
                        "${primaryKey.name}=#{${primaryKey.propertyName}}"
                    )
                )
                .addParameter(tableDescribe.name.camelName(), tableDescribe.classDeclaration.asTypeName())
                .returns(Long::class.asTypeName())
                .build()
        )
        result.add(
            FunSpec.builder("updateByWhere")
                .addModifiers(KModifier.ABSTRACT)
                .addAnnotation(providerAnnotation(UpdateProvider::class, "updateByWhere"))
                .addParameter("where", ComputeExpression::class.asTypeName().copy(true))
                .addParameter("parameters", parameterAsTypeName.copy(true))
                .addParameter("values", parameterAsTypeName)
                .returns(Long::class.asTypeName())
                .build()
        )
        result.add(
            FunSpec.builder("deleteById")
                .addModifiers(KModifier.ABSTRACT)
                .addAnnotations(
                    annotationGenerator.delete(
                        tableDescribe,
                        "${primaryKey.propertyName}=${sqlGenerator.column(primaryKey, "")}"
                    )
                )
                .addParameter(
                    ParameterSpec.builder("id", primaryKey.type.asTypeName())
                        .param(primaryKey.propertyName)
                        .build()
                )
                .returns(Long::class.asTypeName())
                .build()
        )
        result.add(
            FunSpec.builder("deleteByIds")
                .addModifiers(KModifier.ABSTRACT)
                .addAnnotations(
                    annotationGenerator.delete(
                        tableDescribe,
                        "${primaryKey.name} IN (#{ids})"
                    )
                )
                .addAnnotation(
                    AnnotationSpec.builder(Lang::class)
                        .addMember("%T::class", ForeachLanguageDriver::class).build()
                )
                .addParameter(
                    ParameterSpec.builder("ids", primaryKey.type.asListTypeName())
                        .param("ids")
                        .build()
                )
                .returns(Long::class.asTypeName())
                .build()
        )
        result.add(
            FunSpec.builder("deleteAll")
                .returns(Long::class.asTypeName())
                .addStatement("return deleteByWhere(null)")
                .build()
        )
        result.add(
            FunSpec.builder("deleteByWhere")
                .addModifiers(KModifier.ABSTRACT)
                .addAnnotation(providerAnnotation(DeleteProvider::class, "deleteByWhere"))
                .addParameter("where", ComputeExpression::class.asTypeName().copy(true))
                .addParameter("parameters", parameterAsTypeName.copy(true))
                .returns(Long::class.asTypeName())
                .build()
        )
        result.add(
            FunSpec.builder("countByWhere")
                .addAnnotation(providerAnnotation(SelectProvider::class, "countByWhere"))
                .addParameter("where", ComputeExpression::class.asTypeName().copy(true))
                .returns(Long::class.asTypeName())
                .build()
        )
        result.add(
            FunSpec.builder("count")
                .returns(Long::class.asTypeName())
                .addStatement("return countByWhere(null)")
                .build()
        )
        return result
    }

    private fun providerAnnotation(providerClass: KClass<out Annotation>, method: String): AnnotationSpec {
        return AnnotationSpec.builder(providerClass)
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