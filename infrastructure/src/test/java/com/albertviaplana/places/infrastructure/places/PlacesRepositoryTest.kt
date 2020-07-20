package com.albertviaplana.places.infrastructure.places

import com.albertviaplana.places.entities.BusinessStatus
import com.albertviaplana.places.entities.Coordinates
import com.albertviaplana.places.entities.PlaceType
import com.albertviaplana.places.entities.PriceLevel
import com.albertviaplana.places.infrastructure.BuildConfig
import com.albertviaplana.places.infrastructure.di.provideGsonConverter
import com.albertviaplana.places.infrastructure.di.provideOkHttpClient
import com.albertviaplana.places.infrastructure.di.provideRetrofit
import com.albertviaplana.places.infrastructure.places.PlacesRepository.Companion.MAX_RADIUS
import com.albertviaplana.places.infrastructure.places.api.PlacesAuthInterceptor
import com.albertviaplana.places.infrastructure.places.api.ResponseStatus
import com.albertviaplana.places.infrastructure.places.di.providePlacesApi
import com.albertviaplana.places.infrastructure.places.entities.*
import com.github.kittinunf.result.Result
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test

class PlacesRepositoryTest: BasePlacesApiTest() {
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
            val response = repository.getNearbyPlaces(coordinates, 12, PlaceType.BAR.name)

            // Then
            assert(response is Result.Success)
            val places = response.get()
            assert(places.size == 1)
            assert(placeData.equalTo(places.first()))
        }
    }

    @Test
    fun `Wrong radius returns Failure`() {
        runBlocking {
            // Given
            val placeData = getMockPlaceData()
            val status = ResponseStatus.INVALID_REQUEST
            val responseJson = generateWrappedResponse(status, listOf(placeData))
            mockSuccessResponse(responseJson)
            val coordinates = Coordinates(12.toDouble(), 12.toDouble())

            // When
            val response = repository.getNearbyPlaces(coordinates, MAX_RADIUS + 1, PlaceType.BAR.name)

            // Then
            assert(response is Result.Failure)
        }
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
            openingHours?.openNow == it.openingHours?.openNow &&
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
                "place_id" : "$id",
                "reference" : "Reference",
                "types" : ${types.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }},
                "vicinity" : "$vicinity",
                "price_level": $priceLevel,
            """

        openingHours?.let {
            json += """ 
                "opening_hours" : { 
                    "open_now" : 
                """
            json += it.openNow
            json += "},"
        }

        if (photos != null) {
            json += """ "photos" : """
            json += photos.joinToString(prefix = "[", postfix = "]") { it.toJson() }
            json += ","
        }

        if (status != null) {
            json += """"business_status": "${status?.name}" """
        }

        json += "}".trimIndent()

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