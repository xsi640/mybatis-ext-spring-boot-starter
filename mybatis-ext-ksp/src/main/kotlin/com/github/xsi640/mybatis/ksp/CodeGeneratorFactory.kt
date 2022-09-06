package com.github.xsi640.mybatis.ksp

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
        return StandardCodeGenerator()
    }
}