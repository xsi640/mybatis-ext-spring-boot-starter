package com.github.xsi640.mybatis.ksp.generator

interface CodeGeneratorFactory {
    fun create(): CodeGenerator

    companion object {
        fun build(): CodeGeneratorFactory {
            return CodeGeneratorFactoryImpl()
        }
    }
}

class CodeGeneratorFactoryImpl : CodeGeneratorFactory {
    override fun create(): CodeGenerator {
        val sqlGenerator = SqlGeneratorImpl()
        val annotationGenerator = AnnotationGeneratorImpl(sqlGenerator)
        return StandardCodeGenerator(sqlGenerator, annotationGenerator)
    }
}