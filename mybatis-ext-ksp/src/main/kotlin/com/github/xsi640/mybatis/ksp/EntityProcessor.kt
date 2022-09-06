package com.github.xsi640.mybatis.ksp

import com.github.xsi640.mybatis.core.Table
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

    }
}