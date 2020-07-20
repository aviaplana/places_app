package com.albertviaplana.chamaassignment.infrastructure.places

import com.albertviaplana.chamaasignment.DomainException
import com.albertviaplana.chamaasignment.entities.Coordinates
import com.albertviaplana.chamaasignment.entities.PlaceType
import com.albertviaplana.chamaassignment.infrastructure.places.entities.toDomain
import com.github.kittinunf.result.Result
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.IOException


class PlacesRepositoryAdapterTest: BasePlacesApiTest() {

    @Test
    fun `Success result should be mapped to domain entity`() {
        // Given
        val mockedPlaceData = getMockPlaceData()
        val place = mockedPlaceData.toDomain()
        val mockResult = Result.success(listOf(mockedPlaceData))
        val mockedRepository = mock<PlacesRepository> {
            onBlocking { getNearbyPlaces(any(), any(), any()) } doReturn mockResult
        }
        val mockCoordinates = Coordinates(1.toDouble(), 2.toDouble())
        val mockRadius = 1000
        val mockType = place.types.first()

        val adapter = PlacesRepositoryAdapter(mockedRepository)

        // When
        val result = runBlocking {
            adapter.getNearbyPlaces(mockCoordinates, mockRadius, mockType)
        }

        // Then
        assert(result.get().first() == place)

    }

    @Test
    fun `Exception should be mapped to domain exception`() {
        // Given
        val mockedRepository = mock<PlacesRepository> {
            onBlocking { getNearbyPlaces(any(), any(), any()) } doReturn Result.error(IOException())
        }

        val mockCoordinates = Coordinates(1.toDouble(), 2.toDouble())
        val mockRadius = 1000
        val mockType = PlaceType.BAR

        val adapter = PlacesRepositoryAdapter(mockedRepository)

        // When
        val result = runBlocking {
            adapter.getNearbyPlaces(mockCoordinates, mockRadius, mockType)
        }

        // Then
        assert(result is Result.Failure)
        assert(result.component2() is DomainException)
    }

}
