package com.pascal.database.entities


import com.pascal.database.entities.base.BaseEntity
import com.pascal.database.entities.base.BaseEntityClass
import com.pascal.database.entities.base.BaseIdTable
import com.pascal.model.response.ShopCategory
import org.jetbrains.exposed.v1.core.dao.id.EntityID

object ShopCategoryTable : BaseIdTable("shop_category") {
    val name = text("name")
}

class ShopCategoryDAO(id: EntityID<String>) : BaseEntity(id, ShopCategoryTable) {
    companion object : BaseEntityClass<ShopCategoryDAO>(ShopCategoryTable, ShopCategoryDAO::class.java)

    var name by ShopCategoryTable.name
    fun response() = ShopCategory(id.value, name)
}