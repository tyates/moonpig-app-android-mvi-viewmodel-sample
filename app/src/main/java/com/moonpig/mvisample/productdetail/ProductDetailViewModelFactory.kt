package com.moonpig.mvisample.productdetail

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.moonpig.mvisample.domain.productdetail.ProductDetailAction
import com.moonpig.mvisample.domain.productdetail.ProductDetailResult
import io.reactivex.Observable

class ProductDetailViewModelFactory(
    private val productDetailsResultFrom: (ProductDetailAction) -> Observable<ProductDetailResult>,
    private val productDetailsTracker: ProductDetailTracker
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java))
            return ProductDetailViewModel(productDetailsResultFrom, productDetailsTracker) as T

        throw IllegalArgumentException("ViewModel ${modelClass.name} is not supported by this factory")
    }
}