package com.github.xsi640.mybatis.ksp

import com.google.devtools.ksp.symbol.*
import kotlin.reflect.KClass

fun KSClassDeclaration.getAnnotation(kClass: KClass<*>): KSAnnotation? {
    return this.annotations.firstOrNull { it.shortName.getShortName() == kClass.simpleName }
}

fun KSClassDeclaration.getAnnotations(kClass: KClass<*>): List<KSAnnotation> {
    return this.annotations.filter { it.shortName.getShortName() == kClass.simpleName }.toList()
}

fun KSClassDeclaration.getShortName(): String {
    return this.simpleName.getShortName()
}

fun KSAnnotation.getArgument(name: String): KSValueArgument? {
    return this.arguments.firstOrNull { it.name?.getShortName() == name }
}

fun KSAnnotation.getArgumentType(name: String): KSType? {
    val annotation = this.getArgument(name)
    return if (annotation != null) {
        annotation.value as KSType
    } else {
        null
    }
}

fun KSType.getShortName(): String {
    return this.declaration.simpleName.getShortName()
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