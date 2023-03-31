val kspVersion: String by project
val kotlinPoetVersion: String by project
val kotlinTestJunitVersion: String by project
val kotlinCompileTestingVersion: String by project

plugins {
    id("kotlin-noarg")
}

dependencies {
    api(project(":mybatis-ext-core"))
    api(project(":mybatis-ext-ast"))

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinTestJunitVersion")
}

noArg {
    annotation("com.github.xsi640.mybatis.core.kotlin.NoArg")
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
        kotlin.srcDir("build/tmp/ksp/sources/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}