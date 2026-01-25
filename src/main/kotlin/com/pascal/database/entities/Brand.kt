package com.pascal.database.entities

import com.pascal.database.entities.base.BaseEntity
import com.pascal.database.entities.base.BaseEntityClass
import com.pascal.database.entities.base.BaseIdTable
import com.pascal.model.response.Brand
import org.jetbrains.exposed.v1.core.dao.id.EntityID

object BrandTable : BaseIdTable("brand") {
    val name = text("name")
    val logo = text("logo").nullable()
}

class BrandDAO(id: EntityID<String>) : BaseEntity(id, BrandTable) {
    companion object : BaseEntityClass<BrandDAO>(BrandTable, BrandDAO::class.java)

    var name by BrandTable.name
    var logo by BrandTable.logo
    fun response() = Brand(id.value, name, logo)
}