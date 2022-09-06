val vers = rootProject.extra.get("vers") as Map<String, String>
val kspVersion: String by project

plugins {
    id("com.google.devtools.ksp")
    id("kotlin-noarg")
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
    implementation("com.squareup:kotlinpoet:${vers["kotlinpoet"]}")
    implementation("com.squareup:kotlinpoet-ksp:${vers["kotlinpoet"]}")

    api(project(":mybatis-ext-core"))

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.4.32")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.4.8")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.4.8")
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