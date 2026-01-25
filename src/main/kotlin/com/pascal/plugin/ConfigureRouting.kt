package com.pascal.plugin

import com.pascal.feature.auth.AuthService
import com.pascal.feature.auth.authRoutes
import com.pascal.feature.brand.BrandService
import com.pascal.feature.brand.brandRoutes
import com.pascal.feature.cart.CartService
import com.pascal.feature.cart.cartRoutes
import com.pascal.feature.consent.ConsentService
import com.pascal.feature.consent.consentRoutes
import com.pascal.feature.inventory.InventoryService
import com.pascal.feature.inventory.inventoryRoutes
import com.pascal.feature.order.OrderService
import com.pascal.feature.order.orderRoutes
import com.pascal.feature.payment.PaymentService
import com.pascal.feature.payment.paymentRoutes
import com.pascal.feature.policy.PolicyService
import com.pascal.feature.policy.policyRoutes
import com.pascal.feature.product.ProductService
import com.pascal.feature.product.productRoutes
import com.pascal.feature.product_category.ProductCategoryService
import com.pascal.feature.product_category.productCategoryRoutes
import com.pascal.feature.product_sub_category.ProductSubCategoryService
import com.pascal.feature.product_sub_category.productSubCategoryRoutes
import com.pascal.feature.profile.ProfileService
import com.pascal.feature.profile.profileRoutes
import com.pascal.feature.review_rating.ReviewRatingService
import com.pascal.feature.review_rating.reviewRatingRoutes
import com.pascal.feature.shipping.ShippingService
import com.pascal.feature.shipping.shippingRoutes
import com.pascal.feature.shop.ShopService
import com.pascal.feature.shop.shopRoutes
import com.pascal.feature.shop_category.ShopCategoryService
import com.pascal.feature.shop_category.shopCategoryRoutes
import com.pascal.feature.wishlist.WishListService
import com.pascal.feature.wishlist.wishListRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRoute() {
    val authController: AuthService by inject()
    val userProfileController: ProfileService by inject()
    val shopCategoryController: ShopCategoryService by inject()
    val shopController: ShopService by inject()
    val brandController: BrandService by inject()
    val productCategoryController: ProductCategoryService by inject()
    val productSubCategoryController: ProductSubCategoryService by inject()
    val productController: ProductService by inject()
    val reviewRatingController: ReviewRatingService by inject()
    val cartController: CartService by inject()
    val wishListController: WishListService by inject()
    val shippingController: ShippingService by inject()
    val orderController: OrderService by inject()
    val paymentController: PaymentService by inject()
    val policyController: PolicyService by inject()
    val consentController: ConsentService by inject()
    val inventoryController: InventoryService by inject()
    routing {
        authRoutes(authController)
        profileRoutes(userProfileController)
        shopCategoryRoutes(shopCategoryController)
        shopRoutes(shopController)
        brandRoutes(brandController)
        productCategoryRoutes(productCategoryController)
        productSubCategoryRoutes(productSubCategoryController)
        productRoutes(productController)
        reviewRatingRoutes(reviewRatingController)
        cartRoutes(cartController)
        wishListRoutes(wishListController)
        shippingRoutes(shippingController)
        orderRoutes(orderController)
        paymentRoutes(paymentController)
        policyRoutes(policyController)
        consentRoutes(consentController)
        inventoryRoutes(inventoryController)
    }
}
