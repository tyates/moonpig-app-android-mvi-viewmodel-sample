package com.moonpig.mvisample.domain.productdetail

import com.moonpig.mvisample.domain.entities.ProductDetail

sealed class ProductDetailResult {
    sealed class GetProductDetail : ProductDetailResult() {
        object InFlight : GetProductDetail()
        data class Success(val productDetail: ProductDetail) : GetProductDetail()

        data class Error(val throwable: Throwable) : GetProductDetail()
    }

    sealed class AddProduct : ProductDetailResult() {
        object InFlight : AddProduct()
        object Success : AddProduct()
        data class Error(val throwable: Throwable) : AddProduct()
    }
}
