package com.pascal.database.entities


import com.pascal.database.entities.base.BaseEntity
import com.pascal.database.entities.base.BaseEntityClass
import com.pascal.database.entities.base.BaseIdTable
import com.pascal.model.response.ProductSubCategory
import org.jetbrains.exposed.v1.core.dao.id.EntityID

object ProductSubCategoryTable : BaseIdTable("sub_category") {
    val categoryId = reference("category_id", ProductCategoryTable.id)
    val name = text("name")
    val image = text("image").nullable()
}

class ProductSubCategoryDAO(id: EntityID<String>) : BaseEntity(id, ProductSubCategoryTable) {
    companion object : BaseEntityClass<ProductSubCategoryDAO>(ProductSubCategoryTable, ProductSubCategoryDAO::class.java)

    var categoryId by ProductSubCategoryTable.categoryId
    var name by ProductSubCategoryTable.name
    var image by ProductSubCategoryTable.image
    fun response() = ProductSubCategory(id.value, categoryId.value, name, image)
}