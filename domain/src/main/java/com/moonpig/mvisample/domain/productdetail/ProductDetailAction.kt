package com.moonpig.mvisample.domain.productdetail

sealed class ProductDetailAction  {
    data class LoadProductDetail(val productId:String) : ProductDetailAction()
    data class AddProductToBasket(val productId: String, val quantity: Int) : ProductDetailAction()
}

