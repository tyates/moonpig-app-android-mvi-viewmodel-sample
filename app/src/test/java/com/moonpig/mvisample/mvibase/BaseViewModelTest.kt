package com.moonpig.mvisample.mvibase

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import io.reactivex.Observable.just
import io.reactivex.ObservableTransformer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class BaseViewModelTest {

    private val testTracker: BaseTracker<TestViewState, TestIntent> = mock()

    @Test
    fun shouldBindInitialIntentAndReturnInitialViewState() {
        val testViewModel = givenATestViewModel()

        val testObserver = testViewModel.viewState().test()
        testViewModel.bindIntents(just(TestIntent.First))

        assertThat(testObserver.hasSubscription()).isTrue()
        assertThat(testObserver.valueCount()).isEqualTo(2)
        assertThat(testObserver.values()[0]).isEqualTo(TestViewState.Idle)
        assertThat(testObserver.values()[1]).isEqualTo(TestViewState.First)
    }

    @Test
    fun shouldNotEmitFirstIntentMoreThanOnce() {
        val testViewModel = givenATestViewModel()

        val testObserver = testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.merge(just(TestIntent.First),
                                                   just(TestIntent.Second),
                                                   just(TestIntent.First)))

        assertThat(testObserver.hasSubscription()).isTrue()
        assertThat(testObserver.valueCount()).isEqualTo(3)
        assertThat(testObserver.values()[0]).isEqualTo(TestViewState.Idle)
        assertThat(testObserver.values()[1]).isEqualTo(TestViewState.First)
        assertThat(testObserver.values()[2]).isEqualTo(TestViewState.Second)
    }

    @Test
    fun shouldNotEmitMultipleViewStates_whenNoChanges() {
        val testViewModel = givenATestViewModel()

        val testObserver = testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.merge(just(TestIntent.First), just(TestIntent.First)))

        assertThat(testObserver.hasSubscription()).isTrue()
        assertThat(testObserver.valueCount()).isEqualTo(2)
        assertThat(testObserver.values()[0]).isEqualTo(TestViewState.Idle)
        assertThat(testObserver.values()[1]).isEqualTo(TestViewState.First)
    }

    @Test
    fun shouldEmitMultipleViewState_whenChanges() {
        val testViewModel = givenATestViewModel()

        val testObserver = testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.merge(just(TestIntent.First), just(TestIntent.Second)))

        assertThat(testObserver.hasSubscription()).isTrue()
        assertThat(testObserver.valueCount()).isEqualTo(3)
        assertThat(testObserver.values()[0]).isEqualTo(TestViewState.Idle)
        assertThat(testObserver.values()[1]).isEqualTo(TestViewState.First)
        assertThat(testObserver.values()[2]).isEqualTo(TestViewState.Second)
    }

    @Test
    fun shouldTrackViewState_whenReduced() {
        val testViewModel = givenATestViewModel()

        testViewModel.viewState().test()
        testViewModel.bindIntents(just(TestIntent.First))

        verify(testTracker).trackViewState(TestViewState.First)
    }

    @Test
    fun shouldTrackIntent_whenEmitted() {
        val testViewModel = givenATestViewModel()

        testViewModel.viewState().test()
        testViewModel.bindIntents(just(TestIntent.First))

        verify(testTracker).trackIntent(TestIntent.First)
    }

    private fun givenATestViewModel(): TestViewModel =
        TestViewModel(
            { action ->
                when (action) {
                    TestAction.First -> just(TestResult.First)
                    TestAction.Second -> just(TestResult.Second)
                }
            },
            testTracker
        )
}

class TestViewModel(testResultFrom: (TestAction) -> Observable<TestResult>,
                    testTracker: BaseTracker<TestViewState, TestIntent>) :
        BaseViewModel<TestIntent, TestAction, TestResult, TestViewState>(testResultFrom, testTracker) {

    override fun intentFilter(): ObservableTransformer<TestIntent, TestIntent> =
            ObservableTransformer { observable ->
                observable.publish {
                    Observable.merge(it.ofType(TestIntent.First::class.java).take(1),
                                     it.filter { intent -> intent !is TestIntent.First })
                }
            }

    override fun actionFrom(intent: TestIntent): TestAction =
            when (intent) {
                TestIntent.First -> TestAction.First
                TestIntent.Second -> TestAction.Second
            }

    override fun initialViewState(): TestViewState = TestViewState.Idle

    override fun reduce(previousViewState: TestViewState, result: TestResult): TestViewState =
            when (result) {
                TestResult.First -> TestViewState.First
                TestResult.Second -> TestViewState.Second
            }
}

sealed class TestIntent : BaseIntent {
    object First : TestIntent()
    object Second : TestIntent()
}

sealed class TestAction {
    object First : TestAction()
    object Second : TestAction()
}

sealed class TestResult {
    object First : TestResult()
    object Second : TestResult()
}

sealed class TestViewState : BaseViewState {
    object Idle : TestViewState()
    object First : TestViewState()
    object Second : TestViewState()
}