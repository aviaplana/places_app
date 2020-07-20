package com.albertviaplana.chamaassignment.presentation.nearbyPlaces.viewModel

import com.albertviaplana.chamaasignment.PlacesService
import com.github.kittinunf.result.Result
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
class NearbyPlacesViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `On Create action send CheckPermissions event`() {
        // Given
        val placesService = mock<PlacesService>()
        val viewModel = NearbyPlacesViewModel(placesService)
        val eventSubscription = viewModel.event

        // When
        viewModel reduce Created

        // Then
        testDispatcher.runBlockingTest {
            val event = eventSubscription.receive()
            assert(event is CheckPermissions)
        }
    }

    @Test
    fun `On RadiusChanged action send UpdateRadiusLabel event`() {
        // Given
        val radius = 1000
        val placesService = mock<PlacesService>()
        val viewModel = NearbyPlacesViewModel(placesService)
        val eventSubscription = viewModel.event

        // When
        viewModel reduce RadiusChanged(radius)

        // Then
        testDispatcher.runBlockingTest {
            val event = eventSubscription.receive()
            assert(event is UpdateRadiusLabel)
        }
    }

    @Test
    fun `On FiltersDismissed action should fetch data and send ShowFiltersButton event`() {
        testDispatcher.runBlockingTest {
            // Given
            val radius = 1000
            val placesService = mock<PlacesService> {
                onBlocking { getNearbyPlaces(any(), any()) } doReturn Result.success(listOf())
            }
            val viewModel = NearbyPlacesViewModel(placesService, testScope)
            val eventSubscription = viewModel.event
            val states = mutableListOf<NearbyPlacesVM>()

            val job =
                viewModel.state
                    .drop(1) // Drop initial state
                    .onEach { states.add(it)}.launchIn(testScope)


            // When
            viewModel reduce FiltersDismissed(
                progress = 100,
                onlyOpenNow = false,
                type = "BAR"
            )

            // Then
            val event = eventSubscription.receive()
            assert(states[0].isLoading)

            assert(!states[1].isLoading)
            assert(states[1].places.isEmpty())

            assert(event is ShowFiltersButton)
            job.cancel()
        }
    }
    @Test
    fun `On PermissionsDenied action send ShowError event`() {
        testDispatcher.runBlockingTest {
            // Given
            val placesService = mock<PlacesService>()
            val viewModel = NearbyPlacesViewModel(placesService, testScope)
            val eventSubscription = viewModel.event

            // When
            viewModel reduce PermissionsDenied

            // Then
            testDispatcher.runBlockingTest {
                val event = eventSubscription.receive()
                assert(event is ShowError)
            }
        }
    }
}