package com.github.xsi640.mybatis.ksp

import com.google.devtools.ksp.symbol.KSClassDeclaration

data class IndexDescribe(
    val name: String,
    @Suppress("ArrayInDataClass")
    val properties: Array<String>,
    val unique: Boolean,
    val classDeclaration: KSClassDeclaration
)