package com.github.xsi640.mybatis.ksp

interface ClassDeclarationExtractorFactory {
    fun create(): ClassDeclarationExtractor

    companion object {
        fun build(): ClassDeclarationExtractorFactory {
            return ClassDeclarationExtractorFactoryImpl()
        }
    }
}

class ClassDeclarationExtractorFactoryImpl : ClassDeclarationExtractorFactory {
    override fun create(): ClassDeclarationExtractor {
        return StandardClassDeclarationExtractor()
    }
}