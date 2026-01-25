package com.pascal.di

import com.pascal.feature.auth.AuthService
import com.pascal.feature.brand.BrandService
import com.pascal.feature.cart.CartService
import com.pascal.feature.consent.ConsentService
import com.pascal.feature.inventory.InventoryService
import com.pascal.feature.order.OrderService
import com.pascal.feature.payment.PaymentService
import com.pascal.feature.policy.PolicyService
import com.pascal.feature.product.ProductService
import com.pascal.feature.product_category.ProductCategoryService
import com.pascal.feature.product_sub_category.ProductSubCategoryService
import com.pascal.feature.profile.ProfileService
import com.pascal.feature.review_rating.ReviewRatingService
import com.pascal.feature.shipping.ShippingService
import com.pascal.feature.shop.ShopService
import com.pascal.feature.shop_category.ShopCategoryService
import com.pascal.feature.wishlist.WishListService
import org.koin.dsl.module

val serviceModule = module {
    single { BrandService() }
    single { CartService() }
    single { OrderService() }
    single { ProductService() }
    single { ProductCategoryService() }
    single { ProductSubCategoryService() }
    single { ShippingService() }
    single { ShopService() }
    single { ShopCategoryService() }
    single { AuthService() }
    single { ProfileService() }
    single { WishListService() }
    single { PaymentService() }
    single { ReviewRatingService() }
    single { PolicyService() }
    single { ConsentService() }
    single { InventoryService() }
}