package com.github.xsi640.mybatis.ksp.example

import com.github.xsi640.mybatis.core.*
import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

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
    var id: Long,
    @Column(name = "name", nullable = false)
    var name: String,
    @Converter(javaType = Int::class, jdbcType = JdbcType.VARCHAR, typeHandler = SexConverter::class)
    var sex: Int
)

class SexConverter : BaseTypeHandler<Int>() {
    override fun setNonNullParameter(ps: PreparedStatement?, i: Int, parameter: Int?, jdbcType: JdbcType?) {
        TODO("Not yet implemented")
    }

    override fun getNullableResult(rs: ResultSet?, columnName: String?): Int {
        TODO("Not yet implemented")
    }

    override fun getNullableResult(rs: ResultSet?, columnIndex: Int): Int {
        TODO("Not yet implemented")
    }

    override fun getNullableResult(cs: CallableStatement?, columnIndex: Int): Int {
        TODO("Not yet implemented")
    }

}