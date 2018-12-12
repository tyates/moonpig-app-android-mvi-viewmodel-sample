package com.moonpig.mvisample.productdetail

class ProductDetailRenderer {

    fun render(view: ProductDetailView, viewState: ProductDetailScreenViewState) {
        renderLoadingIndicator(view, viewState)
        renderProductDetail(view, viewState)
        renderErrorIndicator(view, viewState)
    }

    private fun renderLoadingIndicator(view: ProductDetailView, viewState: ProductDetailScreenViewState) {
        view.isLoading(viewState.getProductDetailInFlight)
    }

    private fun renderProductDetail(view: ProductDetailView, viewState: ProductDetailScreenViewState) {
        viewState.productDetail?.let {
            view.displayPrice("£${it.price}")
        }
    }

    private fun renderErrorIndicator(view: ProductDetailView, viewState: ProductDetailScreenViewState) {
        view.showLoadingError(viewState.getProductDetailError != null)
    }
}
