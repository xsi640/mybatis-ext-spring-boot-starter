package com.github.xsi640.mybatis.core

interface PropertyConverter<Tin, Tout> {

}

class EmptyPropertyConverter : PropertyConverter<Unit, Unit> {}