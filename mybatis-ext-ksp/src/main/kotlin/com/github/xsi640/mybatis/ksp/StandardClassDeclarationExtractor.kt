package com.github.xsi640.mybatis.ksp

import com.google.devtools.ksp.symbol.KSClassDeclaration

class StandardClassDeclarationExtractor : ClassDeclarationExtractor {
    override fun table(classDeclaration: KSClassDeclaration): TableDescribe {
        TODO("Not yet implemented")
    }

    override fun index(classDeclaration: KSClassDeclaration): List<IndexDescribe> {
        TODO("Not yet implemented")
    }

}