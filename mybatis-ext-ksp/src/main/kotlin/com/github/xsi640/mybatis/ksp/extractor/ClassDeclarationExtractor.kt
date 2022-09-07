package com.github.xsi640.mybatis.ksp.extractor

import com.github.xsi640.mybatis.ksp.IndexDescribe
import com.github.xsi640.mybatis.ksp.TableDescribe
import com.google.devtools.ksp.symbol.KSClassDeclaration

interface ClassDeclarationExtractor {
    fun table(classDeclaration: KSClassDeclaration): TableDescribe
    fun index(classDeclaration: KSClassDeclaration): List<IndexDescribe>
}