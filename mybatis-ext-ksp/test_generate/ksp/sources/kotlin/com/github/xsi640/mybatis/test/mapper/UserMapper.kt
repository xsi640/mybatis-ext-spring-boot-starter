package com.github.xsi640.mybatis.test.mapper

import com.github.xsi640.mybatis.ast.ComputeExpression
import com.github.xsi640.mybatis.ast.OrderByExpression
import com.github.xsi640.mybatis.core.ForeachLanguageDriver
import com.github.xsi640.mybatis.core.Paged
import com.github.xsi640.mybatis.core.QueryProvider
import com.github.xsi640.mybatis.test.User
import kotlin.Any
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.reflect.KClass
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.DeleteProvider
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Lang
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.SelectProvider
import org.apache.ibatis.annotations.Update
import org.apache.ibatis.annotations.UpdateProvider
import org.apache.ibatis.type.JdbcType

@Suppress("REDUNDANT_MODIFIER_FOR_TARGET")
public open interface UserMapper {
  @Select("SELECT id, name, sex FROM public.users")
  @Results(Result(property = "id", column = "id"), 
  Result(property = "name", column = "name"), 
  Result(property = "sex", column = "sex", jdbcType = JdbcType.VARCHAR, javaType =
        kotlin.Int::class, typeHandler = com.github.xsi640.mybatis.test.SexConverter::class))
  public fun list(page: Long, count: Long): Paged<User>

  @Results(Result(property = "id", column = "id"), 
  Result(property = "name", column = "name"), 
  Result(property = "sex", column = "sex", jdbcType = JdbcType.VARCHAR, javaType =
        kotlin.Int::class, typeHandler = com.github.xsi640.mybatis.test.SexConverter::class))
  @SelectProvider(
    type = QueryProvider::class,
    method = "listByWherePage",
  )
  public fun listByWhere(
    page: Long,
    count: Long,
    `where`: ComputeExpression?,
    selects: List<String>?,
    vararg order: OrderByExpression,
  ): Paged<User>

  @Select("SELECT id, name, sex FROM public.users")
  @Results(Result(property = "id", column = "id"), 
  Result(property = "name", column = "name"), 
  Result(property = "sex", column = "sex", jdbcType = JdbcType.VARCHAR, javaType =
        kotlin.Int::class, typeHandler = com.github.xsi640.mybatis.test.SexConverter::class))
  public fun listAll(): List<User>

  @Results(Result(property = "id", column = "id"), 
  Result(property = "name", column = "name"), 
  Result(property = "sex", column = "sex", jdbcType = JdbcType.VARCHAR, javaType =
        kotlin.Int::class, typeHandler = com.github.xsi640.mybatis.test.SexConverter::class))
  @SelectProvider(
    type = QueryProvider::class,
    method = "listAllByWhere",
  )
  public fun listAllByWhere(
    `where`: ComputeExpression?,
    selects: List<String>?,
    vararg order: OrderByExpression,
  ): List<User>

  @Results(Result(property = "id", column = "id"), 
  Result(property = "name", column = "name"), 
  Result(property = "sex", column = "sex", jdbcType = JdbcType.VARCHAR, javaType =
        kotlin.Int::class, typeHandler = com.github.xsi640.mybatis.test.SexConverter::class))
  @SelectProvider(
    type = QueryProvider::class,
    method = "findOneByWhere",
  )
  public fun findOneByWhere(`where`: ComputeExpression?, selects: List<String>?): User

  @Select("SELECT id, name, sex FROM public.users WHERE id=#{id}")
  @Results(Result(property = "id", column = "id"), 
  Result(property = "name", column = "name"), 
  Result(property = "sex", column = "sex", jdbcType = JdbcType.VARCHAR, javaType =
        kotlin.Int::class, typeHandler = com.github.xsi640.mybatis.test.SexConverter::class))
  public fun findOneById(@Param("id") id: Long): Unit

  @Select("SELECT id, name, sex FROM public.users WHERE id IN (#{ids})")
  @Results(Result(property = "id", column = "id"), 
  Result(property = "name", column = "name"), 
  Result(property = "sex", column = "sex", jdbcType = JdbcType.VARCHAR, javaType =
        kotlin.Int::class, typeHandler = com.github.xsi640.mybatis.test.SexConverter::class))
  @Lang(ForeachLanguageDriver::class)
  public fun findAllById(@Param("ids") ids: List<Long>): List<User>

  @Insert("INSERT INTO public.users(name, sex) VALUES(#{name}, #{sex, jdbcType=VARCHAR, javaType=java.lang.Integer, typeHandler=com.github.xsi640.mybatis.test.SexConverter})")
  public fun insert(users: User): Long

  @Insert("<script>INSERT INTO public.users(name, sex) VALUES <foreach collection='elements' item='element' index='i' separator=','>(#{element.name}, #{element.sex, jdbcType=VARCHAR, javaType=java.lang.Integer, typeHandler=com.github.xsi640.mybatis.test.SexConverter})</foreach></script>")
  public fun inserts(@Param("items") items: List<User>): Long

  @Update("UPDATE public.users SET name=#{name}, sex=#{sex, jdbcType=VARCHAR, javaType=java.lang.Integer, typeHandler=com.github.xsi640.mybatis.test.SexConverter} WHERE id=#{id}")
  public fun update(users: User): Long

  @UpdateProvider(
    type = QueryProvider::class,
    method = "updateByWhere",
  )
  public fun updateByWhere(
    `where`: ComputeExpression?,
    parameters: Map<String, Any?>?,
    values: Map<String, Any?>,
  ): Long

  @Delete("DELETE FROM public.users WHERE id=#{id}")
  public fun deleteById(@Param("id") id: Long): Long

  @Delete("DELETE FROM public.users WHERE id IN (#{ids})")
  @Lang(ForeachLanguageDriver::class)
  public fun deleteByIds(@Param("ids") ids: List<Long>): Long

  public fun deleteAll(): Long = deleteByWhere(null)

  @DeleteProvider(
    type = QueryProvider::class,
    method = "deleteByWhere",
  )
  public fun deleteByWhere(`where`: ComputeExpression?, parameters: Map<String, Any?>?): Long

  @SelectProvider(
    type = QueryProvider::class,
    method = "countByWhere",
  )
  public fun countByWhere(`where`: ComputeExpression?): Long {
  }

  public fun count(): Long = countByWhere(null)

  public companion object {
    public val kClass: KClass<User> = User::class
  }
}
