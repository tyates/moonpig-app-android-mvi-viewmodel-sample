package com.moonpig.mvisample.domain.productdetail

import io.reactivex.Observable

object ProductDetailUseCase {

    fun constructProductDetailResultFrom(repository: ProductDetailRepository): (action: ProductDetailAction) -> Observable<ProductDetailResult> =
        { action ->
            when (action) {
                is ProductDetailAction.LoadProductDetail -> getProductDetail(
                    repository,
                    action.productId
                )
                is ProductDetailAction.AddProductToBasket -> addProductToBasket(
                    repository,
                    action.productId,
                    action.quantity
                )
            }
        }

    private fun getProductDetail(
        repository: ProductDetailRepository,
        productId: String
    ): Observable<ProductDetailResult> =
        repository.getProductDetails(productId)
            .map {
                when (it) {
                    is RepositoryState.GetProductDetail.InFlight -> ProductDetailResult.GetProductDetail.InFlight
                    is RepositoryState.GetProductDetail.Success -> ProductDetailResult.GetProductDetail.Success(
                        it.productDetail
                    )
                    is RepositoryState.GetProductDetail.Error -> ProductDetailResult.GetProductDetail.Error(
                        it.throwable
                    )
                }
            }

    private fun addProductToBasket(
        repository: ProductDetailRepository,
        productId: String,
        quantity: Int
    ): Observable<ProductDetailResult> =
        repository.addProductToBasket(AddProductRequest(productId, quantity))
            .map {
                when (it) {
                    is RepositoryState.AddProduct.InFlight -> ProductDetailResult.AddProduct.InFlight
                    is RepositoryState.AddProduct.Success -> ProductDetailResult.AddProduct.Success
                    is RepositoryState.AddProduct.Error -> ProductDetailResult.AddProduct.Error(it.throwable)
                }
            }
}