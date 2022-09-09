package com.github.xsi640.mybatis.ast

import java.lang.invoke.VarHandle

abstract class Expression {
}

data class OrderByExpression(
    val type: OrderType,
    val expression: Expression,
) : Expression()

data class QueryExpression(
    val orderBy: List<OrderByExpression>,
    val offset: Int?,
    val limit: Int?,
    val alias: String?
)

abstract class ComputeExpression() : Expression()

data class UnaryExpression(
    val type: UnaryType,
    val expression: Expression
) : ComputeExpression()

data class BinaryExpression(
    val type: BinaryType,
    val left: Expression,
    val right: Expression
) : ComputeExpression()

data class BetweenExpression(
    val expression: Expression,
    val lower: Expression,
    val upper: Expression,
    val notBetween: Boolean = false
) : ComputeExpression()

data class AggregateExpression(
    val type: AggregateType,
    val argument: Expression?,
    val isDistinct: Boolean = false
) : ComputeExpression()

data class ArgumentExpression(
    val value: Any?
) : ComputeExpression()

data class FunctionExpression(
    val functionName: String,
    val arguments: List<Expression>,
) : ComputeExpression()

data class CastingExpression(
    val expression: Expression,
) : ComputeExpression()


data class ColumnExpression(
    val name: String
) : ComputeExpression()

data class ColumnDeclaringExpression(
    val expression: Expression,
    val name: String,
) : ComputeExpression()

data class ExistsExpression(
    val query: Expression,
    val notExists: Boolean = false,
) : ComputeExpression()

data class ILikeExpression(
    val left: Expression,
    val right: Expression,
) : ComputeExpression()

data class InListExpression(
    val left: Expression,
    val values: List<Expression>,
    val notIn: Boolean = false
) : ComputeExpression()


enum class AggregateType(val value: String) {
    MIN("MIN"),
    MAX("MAX"),
    AVG("AVG"),
    SUM("SUM"),
    COUNT("COUNT");
}

enum class BinaryType(val value: String) {
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    DIV("/"),
    REM("%"),
    LIKE("like"),
    NOT_LIKE("not like"),
    AND("and"),
    OR("or"),
    XOR("xor"),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL("<="),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL(">="),
    EQUAL("="),
    NOT_EQUAL("<>");
}

enum class UnaryType(val value: String) {
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL"),
    MINUS("-"),
    PLUS("+"),
    NOT("NOT");
}

enum class OrderType(val value: String) {
    ASC("ASC"),
    DESC("DESC");
}

fun Expression.isNull(): UnaryExpression {
    return UnaryExpression(UnaryType.IS_NULL, this)
}

fun Expression.isNotNull(): UnaryExpression {
    return UnaryExpression(UnaryType.IS_NOT_NULL, this)
}

fun Expression.unaryMinus(): UnaryExpression {
    return UnaryExpression(UnaryType.MINUS, this)
}

fun Expression.unaryPlus(): UnaryExpression {
    return UnaryExpression(UnaryType.PLUS, this)
}

fun Expression.not(): UnaryExpression {
    return UnaryExpression(UnaryType.NOT, this)
}

infix operator fun Expression.plus(expr: Expression): BinaryExpression {
    return BinaryExpression(BinaryType.PLUS, this, expr)
}

infix operator fun Expression.plus(value: Any): BinaryExpression {
    return this + ArgumentExpression(value)
}

infix operator fun Expression.minus(expr: Expression): BinaryExpression {
    return BinaryExpression(BinaryType.MINUS, this, expr)
}

infix operator fun Expression.minus(value: Any): BinaryExpression {
    return this - ArgumentExpression(value)
}

infix operator fun Expression.times(expr: Expression): BinaryExpression {
    return BinaryExpression(BinaryType.TIMES, this, expr)
}

infix operator fun Expression.times(value: Any): BinaryExpression {
    return this * ArgumentExpression(value)
}

infix operator fun Expression.div(expr: Expression): BinaryExpression {
    return BinaryExpression(BinaryType.DIV, this, expr)
}

infix operator fun Expression.div(value: Any): BinaryExpression {
    return this / ArgumentExpression(value)
}

infix operator fun Expression.rem(expr: Expression): BinaryExpression {
    return BinaryExpression(BinaryType.REM, this, expr)
}

infix operator fun Expression.rem(value: Any): BinaryExpression {
    return this % ArgumentExpression(value)
}

infix fun Expression.like(expr: Expression): BinaryExpression {
    return BinaryExpression(BinaryType.LIKE, this, expr)
}

infix fun Expression.like(value: Any): BinaryExpression {
    return this like ArgumentExpression(value)
}

infix fun Expression.notLike(expr: Expression): BinaryExpression {
    return BinaryExpression(BinaryType.NOT_LIKE, this, expr)
}

infix fun Expression.notLike(value: Any): BinaryExpression {
    return this notLike ArgumentExpression(value)
}

infix fun Expression.and(expr: Expression): BinaryExpression {
    return BinaryExpression(BinaryType.AND, this, expr)
}

infix fun Expression.and(value: Boolean): BinaryExpression {
    return this and ArgumentExpression(value)
}

infix fun Expression.or(expr: Expression): BinaryExpression {
    return BinaryExpression(BinaryType.OR, this, expr)
}

infix fun Expression.or(value: Boolean): BinaryExpression {
    return this or ArgumentExpression(value)
}

infix fun Expression.xor(expr: Expression): BinaryExpression {
    return BinaryExpression(BinaryType.XOR, this, expr)
}

infix fun Expression.xor(value: Any): BinaryExpression {
    return this xor ArgumentExpression(value)
}

infix fun Expression.lt(expr: Expression): BinaryExpression {
    return BinaryExpression(BinaryType.LESS_THAN, this, expr)
}

infix fun Expression.lt(value: Any): BinaryExpression {
    return this lt ArgumentExpression(value)
}

infix fun Expression.lte(expr: Expression): BinaryExpression {
    return BinaryExpression(BinaryType.LESS_THAN_OR_EQUAL, this, expr)
}

infix fun Expression.lte(value: Any): BinaryExpression {
    return this lte ArgumentExpression(value)
}

infix fun Expression.gt(expr: Expression): BinaryExpression {
    return BinaryExpression(BinaryType.GREATER_THAN, this, expr)
}

infix fun Expression.gt(value: Any): BinaryExpression {
    return this gt ArgumentExpression(value)
}

infix fun Expression.gte(expr: Expression): BinaryExpression {
    return BinaryExpression(BinaryType.GREATER_THAN_OR_EQUAL, this, expr)
}

infix fun Expression.gte(value: Any): BinaryExpression {
    return this gte ArgumentExpression(value)
}

infix fun Expression.eq(expr: Expression): BinaryExpression {
    return BinaryExpression(BinaryType.EQUAL, this, expr)
}

infix fun Expression.eq(value: Any): BinaryExpression {
    return this eq ArgumentExpression(value)
}

infix fun Expression.neq(expr: Expression): BinaryExpression {
    return BinaryExpression(BinaryType.NOT_EQUAL, this, expr)
}

infix fun Expression.neq(value: Any): BinaryExpression {
    return this neq ArgumentExpression(value)
}

infix fun Expression.between(range: ClosedRange<*>): BetweenExpression {
    return BetweenExpression(this, ArgumentExpression(range.start), ArgumentExpression(range.endInclusive))
}

infix fun Expression.notBetween(range: ClosedRange<*>): BetweenExpression {
    return BetweenExpression(this, ArgumentExpression(range.start), ArgumentExpression(range.endInclusive), true)
}

fun Expression.inList(vararg list: Any): InListExpression {
    return InListExpression(this, list.map { ArgumentExpression(it) })
}

infix fun Expression.inList(list: List<*>): InListExpression {
    return InListExpression(this, list.map { ArgumentExpression(it) })
}

fun Expression.notInList(vararg list: Any): InListExpression {
    return InListExpression(this, list.map { ArgumentExpression(it) }, true)
}

infix fun Expression.notInList(list: List<*>): InListExpression {
    return InListExpression(this, list.map { ArgumentExpression(it) }, true)
}