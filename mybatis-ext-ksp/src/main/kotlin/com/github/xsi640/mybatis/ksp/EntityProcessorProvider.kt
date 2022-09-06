package com.github.xsi640.mybatis.ksp

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class EntityProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return EntityProcessor(
            options = environment.options,
            codeGenerator = environment.codeGenerator,
            logger = environment.logger
        )
    }
}