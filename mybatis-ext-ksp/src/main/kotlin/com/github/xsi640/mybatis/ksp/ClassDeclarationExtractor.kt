package com.github.xsi640.mybatis.ksp

import com.google.devtools.ksp.symbol.KSClassDeclaration

interface ClassDeclarationExtractor {
    fun table(classDeclaration: KSClassDeclaration): TableDescribe
    fun index(classDeclaration: KSClassDeclaration): List<IndexDescribe>
}