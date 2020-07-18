package com.albertviaplana.chamaassignment.infrastructure.places

import com.albertviaplana.chamaasignment.entities.BusinessStatus
import com.albertviaplana.chamaasignment.entities.Coordinates
import com.albertviaplana.chamaasignment.entities.PriceLevel
import com.albertviaplana.chamaassignment.infrastructure.BuildConfig
import com.albertviaplana.chamaassignment.infrastructure.di.provideGsonConverter
import com.albertviaplana.chamaassignment.infrastructure.di.provideOkHttpClient
import com.albertviaplana.chamaassignment.infrastructure.di.provideRetrofit
import com.albertviaplana.chamaassignment.infrastructure.places.PlacesRepository.Companion.MAX_RANGE
import com.albertviaplana.chamaassignment.infrastructure.places.api.PlacesAuthInterceptor
import com.albertviaplana.chamaassignment.infrastructure.places.api.ResponseStatus
import com.albertviaplana.chamaassignment.infrastructure.places.di.providePlacesApi
import com.albertviaplana.chamaassignment.infrastructure.places.entities.*
import com.github.kittinunf.result.Result
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test

class PlacesRepositoryTest {
    private val mockServer: MockWebServer = MockWebServer()
    private val repository: PlacesRepository

    init {
        val authInterceptor = PlacesAuthInterceptor(BuildConfig.PLACES_API_KEY)
        val okHttpClient = provideOkHttpClient(authInterceptor)
        val gsonConverterFactory = provideGsonConverter()
        val retrofit = provideRetrofit(
            okHttpClient,
            gsonConverterFactory,
            mockServer.url("/")
        )
        val api = providePlacesApi(retrofit)
        repository = PlacesRepository(api)
    }

    @Test
    fun `Successful response returns Success`() {
        runBlocking {
            // Given
            val placeData = getMockPlaceData()
            val status = ResponseStatus.OK
            val responseJson = generateWrappedResponse(status, listOf(placeData))
            mockSuccessResponse(responseJson)
            val coordinates = Coordinates(12.toDouble(), 12.toDouble())

            // When
            val response = repository.getNearbyPlaces(coordinates, 12)

            // Then
            assert(response is Result.Success)
            val places = response.get()
            assert(places.size == 1)
            assert(placeData.equalTo(places.first()))
        }
    }

    @Test
    fun `Wrong range returns Failure`() {
        runBlocking {
            // Given
            val placeData = getMockPlaceData()
            val status = ResponseStatus.INVALID_REQUEST
            val responseJson = generateWrappedResponse(status, listOf(placeData))
            mockSuccessResponse(responseJson)
            val coordinates = Coordinates(12.toDouble(), 12.toDouble())

            // When
            val response = repository.getNearbyPlaces(coordinates, MAX_RANGE + 1)

            // Then
            assert(response is Result.Failure)
        }
    }

    private fun getMockPlaceData(): PlaceData {
        val photoData = PhotoData(
            width = 10,
            height = 20,
            reference = "Photo Reference"
        )

        return PlaceData(
            id = "Place id",
            name = "Name",
            vicinity = "Vicinity",
            icon = "Icon url",
            rating = 3.5f,
            geometry = GeometryData(LocationData(1.toDouble(), 2.toDouble())),
            openingHours = OpeningHoursData(true),
            status = BusinessStatus.OPERATIONAL,
            priceLevel = PriceLevel.MODERATE.level,
            photos = listOf(photoData),
            types = listOf("Type 1", "Type 2")
        )
    }

    private fun mockSuccessResponse(responseBody: String) {
        val mockedResponse = MockResponse().apply {
            setResponseCode(200)
            setBody(responseBody)
        }

        mockServer.enqueue(mockedResponse)
    }

    private fun<T> Collection<T>.equalTo(toCompare: Collection<T>) =
        size == toCompare.size &&
            this.zip(toCompare).all { (x, y) -> x == y }

    private fun generateWrappedResponse(status: ResponseStatus, results: List<PlaceData>) = """
        {
            "html_attributions" : [],
            "results" : ${results.joinToString(prefix = "[", postfix = "]") {it.toJson() }},
            "status" : "${status.name}"
        }""".trimIndent()


    private fun PlaceData.equalTo(toCompare: PlaceData) =
        toCompare.let {
            name == it.name &&
            icon == it.icon &&
            vicinity == it.vicinity &&
            openingHours.isOpen == it.openingHours.isOpen &&
            id == it.id &&
            rating == it.rating &&
            priceLevel == it.priceLevel &&
            status?.name == it.status?.name
            geometry.location.latitude == it.geometry.location.latitude &&
            geometry.location.longitude == it.geometry.location.longitude &&
            types.equalTo(it.types) &&
            arePhotosEqual(photos, it.photos)
        }

    private fun arePhotosEqual(a: List<PhotoData>?, b: List<PhotoData>?) =
        if (a != null && b != null) {
            a.size == b.size &&
                    a.zip(b).all { (x, y) -> x.equalTo(y) }
        } else {
            a == b
        }


    private fun PlaceData.toJson(): String {
        var json = """
            {
                "geometry" : {
                    "location" : {
                      "lat" : ${geometry.location.latitude},
                      "lng" : ${geometry.location.longitude}
                    }
                },
                "icon" : "$icon",
                "id" : "",
                "name" : "$name",
                "rating" : "$rating",
                "opening_hours" : {
                    "open_now" : ${openingHours.isOpen}
                },
                "place_id" : "$id",
                "reference" : "",
                "types" : ${types.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }},
                "vicinity" : "$vicinity",""${'"'},
                "price_level": $priceLevel,
                "price_level": $priceLevel
            """

        if (photos != null) {
            json += """ "photos" : """
            json += photos.joinToString(prefix = "[", postfix = "]") { it.toJson() }
            json += ","
        }

        if (status != null) {
            json += """"business_status": "${status?.name}" , """
        }

        json += """}"""".trimIndent()

        return json
    }

    private fun PhotoData.toJson() =
        """{
              "height" : $height,
              "html_attributions" : [],
              "photo_reference" : "$reference",
              "width" : $width
        }""".trimIndent()

    private fun PhotoData.equalTo(toCompare: PhotoData)   =
        toCompare.let {
            width == it.width &&
            height == it.height &&
            reference == it.reference
        }
}