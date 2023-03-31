rootProject.name = "mybatis-ext"

pluginManagement {
    val kotlinVersion: String by settings
    val kspVersion: String by settings
    val springBootVersion: String by settings
    val springBootManagementVersion: String by settings

    plugins {
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springBootManagementVersion
        id("java")

        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.noarg") version kotlinVersion
        kotlin("plugin.allopen") version kotlinVersion
    }

    repositories {
        mavenLocal()
        maven {
            url = uri("https://maven.aliyun.com/nexus/content/groups/public/")
        }
        mavenCentral()
    }
}

fun defineSubProject(name: String, path: String) {
    include(name)
    project(":$name").projectDir = file(path)
}

defineSubProject("mybatis-ext-core", "mybatis-ext-core")
defineSubProject("mybatis-ext-dialect", "mybatis-ext-dialect")
defineSubProject("mybatis-ext-ast", "mybatis-ext-ast")