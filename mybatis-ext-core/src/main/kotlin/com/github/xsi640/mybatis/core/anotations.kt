package com.github.xsi640.mybatis.core

import org.apache.ibatis.type.JdbcType
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Table(
    val schema: String = "",
    val name: String = ""
)

@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Column(
    val name: String = "",
    val nullable: Boolean = true,
)

@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Converter(
    val javaType: KClass<*> = Void::class,
    val jdbcType: JdbcType = JdbcType.UNDEFINED,
    val typeHandler: KClass<out PropertyConverter<*, *>> = EmptyPropertyConverter::class
)

@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Id(
    val generate: Boolean = false
)

@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Index(
    val name: String = "",
    val properties: Array<String> = [],
    val unique: Boolean = false
)

@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Ignore


@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Temporal(
    val type: TemporalType = TemporalType.TIMESTAMP
)

@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Embedded

@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class CreatedBy

@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class CreatedDate

@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class LastModifiedBy

@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class LastModifiedDate

enum class TemporalType {
    DATE,
    TIME,
    TIMESTAMP
}

annotation class NoArg