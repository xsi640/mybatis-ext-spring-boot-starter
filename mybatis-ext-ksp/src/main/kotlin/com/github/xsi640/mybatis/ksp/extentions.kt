package com.github.xsi640.mybatis.ksp

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSValueArgument
import kotlin.reflect.KClass

fun KSClassDeclaration.getAnnotation(kClass: KClass<*>): KSAnnotation? {
    return this.annotations.firstOrNull { it.shortName.getShortName() == kClass.simpleName }
}

fun KSAnnotation.getArgument(name: String): KSValueArgument? {
    return this.arguments.firstOrNull { it.name?.getShortName() == name }
}

fun KSClassDeclaration.getSupperClassDeclaration(): KSClassDeclaration? {
    val list = this.superTypes.toList()
    if (list.isNotEmpty()) {
        for (ksTypeReference in list) {
            val type = ksTypeReference.resolve()
            if (type.declaration is KSClassDeclaration && this != type.declaration) {
                return type.declaration as KSClassDeclaration
            }
        }
    }
    return null
}

fun KSPropertyDeclaration.getAnnotation(kClass: KClass<*>): KSAnnotation? {
    return this.annotations.firstOrNull { it.shortName.getShortName() == kClass.simpleName }
}

fun KSPropertyDeclaration.getAnnotations(kClass: KClass<*>): List<KSAnnotation> {
    return this.annotations.filter { it.shortName.getShortName() == kClass.simpleName }.toList()
}