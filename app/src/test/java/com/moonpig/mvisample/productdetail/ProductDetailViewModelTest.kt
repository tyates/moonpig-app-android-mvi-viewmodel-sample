package com.moonpig.mvisample.productdetail

import com.moonpig.mvisample.domain.productdetail.ProductDetailAction
import com.moonpig.mvisample.domain.productdetail.ProductDetailResult
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ProductDetailViewModelTest {

    private val productDetailTracker: ProductDetailTracker = mock()

    @Test
    fun `should ignore additional initial intents`() {
        val counter = Counter()
        val viewModel = `a view model with`(
            `a use case that counts calls on`(counter)
        ).also { `observe view state of`(it) }

        viewModel.bindIntents(
            Observable.merge(
                Observable.just(ProductDetailIntent.Initial(PRODUCT_ID)),
                Observable.just(ProductDetailIntent.Initial(PRODUCT_ID))
            )
        )

        assertThat(counter.count).isEqualTo(1)
    }

    @Test
    fun shouldEmitNullObjectProductDetail_whenInitialised() {
        val viewStateObserver = `observe view state of`(`a view model`())

        assertThat(viewStateObserver.values()[0].productDetail).isNull()
    }

    // TODO: Need new style tests here for mapping Intents to Actions and reducing Results into ViewStates

//    @Test
//    fun shouldEmitLoadingState_whenGetProductInFlight() {
//        whenever(productDetailUseCase.resultFrom(ProductDetailAction.LoadProductDetail(PRODUCT_ID)))
//                .thenReturn(Observable.just(ProductDetailResult.GetProductDetail.InFlight))
//
//        viewModel.bindIntents(Observable.just(ProductDetailIntent.Initial(PRODUCT_ID)))
//
//        assertThat(viewStateObserver.values()[0].loadingIndicatorVisibility).isEqualTo(Visibility.GONE)
//        assertThat(viewStateObserver.values()[1].loadingIndicatorVisibility).isEqualTo(Visibility.VISIBLE)
//    }
//
//    @Test
//    fun shouldEmitSuccessState_whenGetProductSuccess() {
//        val productDetail = ProductDetailResult.GetProductDetail.Success(ProductDetail(NAME, DESCRIPTION, PRICE, IMAGE_URL))
//        whenever(productDetailUseCase.resultFrom(ProductDetailAction.LoadProductDetail(PRODUCT_ID))).thenReturn(Observable.just(productDetail))
//
//        viewModel.bindIntents(Observable.just(ProductDetailIntent.Initial(PRODUCT_ID)))
//
//        val viewState = viewStateObserver.values()[1]
//        assertThat(viewState.productDetail?.name).isEqualTo(NAME)
//        assertThat(viewState.productDetail?.description).isEqualTo(DESCRIPTION)
//        assertThat(viewState.productDetail?.price).isEqualTo("£$PRICE")
//        assertThat(viewState.productDetail?.imageUrl).isEqualTo(IMAGE_URL)
//        assertThat(viewState.loadingIndicatorVisibility).isEqualTo(Visibility.GONE)
//    }
//
//    @Test
//    fun shouldEmitErrorState_whenGetProductError() {
//        whenever(productDetailUseCase.resultFrom(ProductDetailAction.LoadProductDetail(PRODUCT_ID)))
//                .thenReturn(Observable.just(ProductDetailResult.GetProductDetail.Error(RuntimeException())))
//
//        viewModel.bindIntents(Observable.just(ProductDetailIntent.Initial(PRODUCT_ID)))
//
//        assertThat(viewStateObserver.values()[1].productDetailErrorVisibility).isEqualTo(Visibility.VISIBLE)
//        assertThat(viewStateObserver.values()[1].loadingIndicatorVisibility).isEqualTo(Visibility.GONE)
//    }
//
//    @Test
//    fun shouldEmitInFlightState_whenAddProductInFlight() {
//        whenever(productDetailUseCase.resultFrom(ProductDetailAction.AddProductToBasket(PRODUCT_ID, QUANTITY)))
//                .thenReturn(Observable.just(ProductDetailResult.AddProduct.InFlight))
//
//        viewModel.bindIntents(Observable.just(ProductDetailIntent.AddToBasket(PRODUCT_ID, QUANTITY)))
//
//        assertThat(viewStateObserver.values()[0].addToBasketInFlight).isFalse()
//        assertThat(viewStateObserver.values()[1].addToBasketInFlight).isTrue()
//    }
//
//    @Test
//    fun shouldEmitSuccessState_whenAddProductSuccess() {
//        whenever(productDetailUseCase.resultFrom(ProductDetailAction.AddProductToBasket(PRODUCT_ID, QUANTITY)))
//                .thenReturn(Observable.just(ProductDetailResult.AddProduct.InFlight, ProductDetailResult.AddProduct.Success))
//
//        viewModel.bindIntents(Observable.just(ProductDetailIntent.AddToBasket(PRODUCT_ID, QUANTITY)))
//
//        assertThat(viewStateObserver.values()[0].addToBasketInFlight).isFalse()
//        assertThat(viewStateObserver.values()[1].addToBasketInFlight).isTrue()
//        assertThat(viewStateObserver.values()[2].addToBasketInFlight).isFalse()
//    }
//
//    @Test
//    fun shouldEmitErrorState_whenAddProductError() {
//        val exception = RuntimeException()
//        val error = ProductDetailResult.AddProduct.Error(exception)
//        whenever(productDetailUseCase.resultFrom(ProductDetailAction.AddProductToBasket(PRODUCT_ID, QUANTITY))).thenReturn(Observable.just(error))
//
//        viewModel.bindIntents(Observable.just(ProductDetailIntent.AddToBasket(PRODUCT_ID, QUANTITY)))
//
//        assertThat(viewStateObserver.values()[0].addToBasketError).isNull()
//        assertThat(viewStateObserver.values()[1].addToBasketError).isEqualTo(exception)
//        assertThat(viewStateObserver.values()[1].addToBasketInFlight).isFalse()
//    }

    private fun `observe view state of`(viewModel: ProductDetailViewModel) =
        viewModel.viewState().test()

    private fun `a view model`() =
        ProductDetailViewModel(
            { Observable.never() },
            productDetailTracker
        )

    private fun `a view model with`(
        useCase: (ProductDetailAction) -> Observable<ProductDetailResult>
    ): ProductDetailViewModel {
        return ProductDetailViewModel(
            useCase,
            productDetailTracker
        )
    }

    private fun `a use case that counts calls on`(counter: Counter): (ProductDetailAction) -> Observable<ProductDetailResult> =
        {
            Observable.never<ProductDetailResult>()
            .also {
                counter.increment()
            }
        }

    companion object {
        const val PRODUCT_ID = "122fkjkjfd"
//        const val QUANTITY = 1
//        const val DESCRIPTION = "description"
//        const val NAME = "name"
//        const val PRICE = 199
//        const val IMAGE_URL = "imageUrl"
    }
}

class Counter {
    var count = 0
    fun increment() = count++
}
