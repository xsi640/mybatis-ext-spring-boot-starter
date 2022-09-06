val kspVersion: String by project
val springBootMybatisVersion: String by project

plugins {
    id("kotlin-noarg")
}

dependencies {
    api("org.mybatis.spring.boot:mybatis-spring-boot-starter:$springBootMybatisVersion")
}

noArg {
    annotation("com.github.xsi640.mybatis.core.NoArg")
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}