package com.github.xsi640.mybatis.test

import com.github.xsi640.mybatis.core.*import org.apache.ibatis.type.JdbcType

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
    var id:Long,
    @Column(name = "name", nullable = false)
    var name:String,
    @Converter(javaType = Int::class, jdbcType = JdbcType.VARCHAR, typeHandler = SexConverter::class)
    var sex:Int
)

class SexConverter:PropertyConverter<Int,String>(){
    
}