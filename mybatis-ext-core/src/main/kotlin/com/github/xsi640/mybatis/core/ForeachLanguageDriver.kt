package com.github.xsi640.mybatis.core

import org.apache.ibatis.mapping.SqlSource
import org.apache.ibatis.scripting.LanguageDriver
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver
import org.apache.ibatis.session.Configuration
import java.util.regex.Pattern

class ForeachLanguageDriver : XMLLanguageDriver(), LanguageDriver {

    override fun createSqlSource(configuration: Configuration, script: String, parameterType: Class<*>): SqlSource {
        var newScript = script
        val matcher = InListPattern.matcher(script)
        if (matcher.find()) {
            newScript =
                matcher.replaceAll("(<foreach collection=\"$1\" item=\"__item\" separator=\",\" >#{__item}</foreach>)")
        }
        newScript = "<script>$newScript</script>"
        return super.createSqlSource(configuration, newScript, parameterType)
    }

    companion object {
        private val InListPattern = Pattern.compile("\\(#\\{(\\w+)\\}\\)")
    }
}