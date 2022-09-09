package com.github.xsi640.mybatis.core

class Paged<T>() : ArrayList<T>() {
    var totalItems = 0L
    var totalPages = 0L
    var pageIndex = 0L
    var pageSize = 0L

    constructor(items: List<T>, page: Long, count: Long, total: Long) : this() {
        this.totalItems = total
        this.pageIndex = page
        this.pageSize = count
        this.totalPages = if (total % count == 0L) {
            total / count
        } else {
            total / count + 1
        }
        super.addAll(items)
    }

    constructor(items: List<T>) : this() {
        this.totalItems = items.size.toLong()
        this.pageIndex = 1L
        this.totalPages = 1L
        this.pageSize = 1L
        super.addAll(items)
    }
}