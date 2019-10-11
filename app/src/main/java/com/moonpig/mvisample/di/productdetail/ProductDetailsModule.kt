package com.moonpig.mvisample.di.productdetail

import com.moonpig.mvisample.domain.productdetail.ProductDetailAction
import com.moonpig.mvisample.domain.productdetail.ProductDetailRepository
import com.moonpig.mvisample.domain.productdetail.ProductDetailResult
import com.moonpig.mvisample.domain.productdetail.ProductDetailUseCase
import com.moonpig.mvisample.productdetail.ProductDetailTracker
import com.moonpig.mvisample.productdetail.ProductDetailViewModelFactory
import dagger.Module
import dagger.Provides
import io.reactivex.Observable

@Module
class ProductDetailsModule {

    @Provides
    fun provideProductDetailsResultFrom(productDetailsRepository: ProductDetailRepository) =
        ProductDetailUseCase.constructProductDetailResultFrom(productDetailsRepository)

    @Provides
    fun provideProductDetailTracker(): ProductDetailTracker =
            ProductDetailTracker()

    @Provides
    fun provideProductDetailsViewModelFactory(
        productDetailsResultFrom: @JvmSuppressWildcards(true) (action: ProductDetailAction) -> Observable<ProductDetailResult>,
        productDetailTracker: ProductDetailTracker
    ): ProductDetailViewModelFactory {
        return ProductDetailViewModelFactory(productDetailsResultFrom, productDetailTracker)
    }
}