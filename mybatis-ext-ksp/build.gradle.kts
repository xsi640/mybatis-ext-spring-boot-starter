val kspVersion: String by project
val kotlinPoetVersion: String by project
val kotlinTestJunitVersion: String by project
val kotlinCompileTestingVersion: String by project

plugins {
    id("com.google.devtools.ksp")
    id("kotlin-noarg")
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
    implementation("com.squareup:kotlinpoet:$kotlinPoetVersion")
    implementation("com.squareup:kotlinpoet-ksp:$kotlinPoetVersion")

    api(project(":mybatis-ext-core"))
    api(project(":mybatis-ext-ast"))

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinTestJunitVersion")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:$kotlinCompileTestingVersion")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:$kotlinCompileTestingVersion")
}

noArg {
    annotation("com.github.xsi640.mybatis.core.kotlin.NoArg")
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}