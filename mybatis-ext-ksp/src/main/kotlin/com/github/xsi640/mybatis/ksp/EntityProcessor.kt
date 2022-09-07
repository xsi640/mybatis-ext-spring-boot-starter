package com.github.xsi640.mybatis.ksp

import com.github.xsi640.mybatis.core.Table
import com.github.xsi640.mybatis.ksp.extractor.ClassDeclarationExtractorFactory
import com.github.xsi640.mybatis.ksp.generator.CodeGeneratorFactory
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate

class EntityProcessor(
    private val options: Map<String, String>,
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    val classDeclarationExtractorFactory = ClassDeclarationExtractorFactory.build()
    val codeGeneratorFactory = CodeGeneratorFactory.build()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Table::class.qualifiedName!!)
        symbols.filter {
            it is KSClassDeclaration && it.validate()
        }.forEach {
            it.accept(TableVisitor(), Unit)
        }
        return emptyList()
    }

    inner class TableVisitor : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val classDeclarationExtractor = classDeclarationExtractorFactory.create()
            val mapperCodeGenerator = codeGeneratorFactory.create()
            val tableDescribe = classDeclarationExtractor.table(classDeclaration)
            val indexDescribes = classDeclarationExtractor.index(classDeclaration)
            val name = classDeclaration.simpleName.getShortName()
            val packageName = classDeclaration.packageName.asString() + ".mapper"
            mapperCodeGenerator.generate(codeGenerator, packageName, name, tableDescribe)
        }
    }
}