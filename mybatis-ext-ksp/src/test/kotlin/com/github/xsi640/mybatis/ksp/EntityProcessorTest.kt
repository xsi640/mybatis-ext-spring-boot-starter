package com.github.xsi640.mybatis.ksp

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class EntityProcessorTest {
    @Test
    fun test() {
        val source = SourceFile.kotlin(
            "User.kt", """
    package com.github.xsi640.mybatis.ksp.example

    import com.github.xsi640.mybatis.core.*import org.apache.ibatis.type.JdbcType

    @Table(
        schema = "public",
        name = "users"
    )       
    @Index(
        name = "idx_user_name",
        properties = ["name"],
        unique = false
    )
    class User(
        @Id
        var id:Long,
        @Column(name = "name", nullable = false)
        var name:String,
        @Converter(javaType = Int::class, jdbcType = JdbcType.VARCHAR, typeHandler = SexConverter::class)
        var sex:Int
    )

    class SexConverter:PropertyConverter<Int,String>(){
        
    }
""".trimIndent()
        )

        val result = compile(source)
        Assertions.assertEquals(true, result.exitCode == KotlinCompilation.ExitCode.OK)
    }

    private fun compile(vararg source: SourceFile) = KotlinCompilation().apply {
        sources = source.toList()
        symbolProcessorProviders = listOf(EntityProcessorProvider())
        workingDir = File("./build/tmp")
        inheritClassPath = true
        verbose = true
        messageOutputStream = System.out
    }.compile()
}