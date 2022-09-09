package com.github.xsi640.mybatis.ksp

import com.github.xsi640.mybatis.ast.ComputeExpression
import com.github.xsi640.mybatis.ast.OrderByExpression
import com.github.xsi640.mybatis.core.ForeachLanguageDriver
import com.github.xsi640.mybatis.core.Id
import com.github.xsi640.mybatis.core.Paged
import com.github.xsi640.mybatis.core.QueryProvider
import org.apache.ibatis.annotations.*
import org.apache.ibatis.annotations.Result
import kotlin.reflect.KClass

class Example(
    @Id
    val id: Long,
    val name: String
)

@Suppress("REDUNDANT_MODIFIER_FOR_TARGET")
open interface ExampleMapper {
    companion object {
        val kClass: KClass<Example> = Example::class
    }

    @Select("SELECT id,name FROM example")
    @Results(
        Result(property = "id", column = "id"),
        Result(property = "name", column = "name"),
    )
    fun list(page: Long, count: Long): Paged<Example>

    @Results(
        Result(property = "id", column = "id"),
        Result(property = "name", column = "name"),
    )
    @SelectProvider(
        type = QueryProvider::class,
        method = "listByWherePage",
    )
    fun listByWhere(
        page: Long,
        count: Long,
        `where`: ComputeExpression?,
        selects: List<String>?,
        vararg order: OrderByExpression,
    ): Paged<Example>

    @Select("SELECT id,name FROM public.Example")
    @Results(
        Result(property = "id", column = "id"),
        Result(property = "name", column = "name"),
    )
    fun listAll(): List<Example>

    @Results(
        Result(property = "id", column = "id"),
        Result(property = "name", column = "name"),
    )
    @SelectProvider(
        type = QueryProvider::class,
        method = "listAllByWhere",
    )
    fun listAllByWhere(
        `where`: ComputeExpression?,
        selects: List<String>?,
        vararg order: OrderByExpression,
    ): List<Example>

    @Results(
        Result(property = "id", column = "id"),
        Result(property = "name", column = "name"),
    )
    @SelectProvider(
        type = QueryProvider::class,
        method = "findOneByWhere",
    )
    fun findOneByWhere(
        `where`: ComputeExpression?,
        selects: List<String>?,
    ): Example?

    @Select("SELECT id,name FROM public.Example WHERE id=#{id}")
    @Results(
        Result(property = "id", column = "id"),
        Result(property = "name", column = "name"),
    )
    fun findOneById(@Param("id") id: Long): Example?

    @Select("SELECT id,name FROM public.Example WHERE id IN (#{ids})")
    @Results(
        Result(property = "id", column = "id"),
        Result(property = "name", column = "name"),
        Result(property = "lastModifier", column = "last_modifier")
    )
    @Lang(ForeachLanguageDriver::class)
    fun findAllById(@Param("ids") ids: List<Long>): List<Example>

    @Insert("INSERT INTO public.Example(id,name) VALUES(#{id}, #{name})")
    fun insert(Example: Example): Long

    @Insert("<script>INSERT INTO public.Example(id,name) VALUES <foreach collection='items' item='item' index='index' separator=','>(#{item.id}, #{item.name})</foreach></script>")
    fun inserts(@Param("items") items: List<Example>): Long

    @Update("UPDATE public.Example SET name=#{name} WHERE id=#{id}")
    fun update(Example: Example): Long

    @UpdateProvider(
        type = QueryProvider::class,
        method = "updateByWhere",
    )
    fun updateByWhere(
        `where`: ComputeExpression?,
        values: Map<String, Any?>,
    ): Long

    @Delete("DELETE FROM public.Example WHERE id=#{id}")
    fun deleteById(@Param("id") id: Long): Long

    @Delete("DELETE FROM public.Example WHERE id IN (#{ids})")
    @Lang(ForeachLanguageDriver::class)
    fun deleteByIds(@Param("ids") ids: List<Long>): Long

    fun deleteAll(): Long {
        return deleteByWhere(null)
    }

    @DeleteProvider(
        type = QueryProvider::class,
        method = "deleteByWhere",
    )
    fun deleteByWhere(`where`: ComputeExpression?): Long

    fun count(): Long {
        return countByWhere(null)
    }

    @SelectProvider(
        type = QueryProvider::class,
        method = "countByWhere",
    )
    fun countByWhere(`where`: ComputeExpression?): Long
}
