package com.android.nldlam.sample.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.nldlam.sample.weatherapp.utils.ApiUtils
import com.android.nldlam.sample.weatherapp.utils.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.net.HttpURLConnection

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchWeatherViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mockWebServer = MockWebServer()
        mockWebServer.start()
        //weatherRepository = WeatherRepository.getInstance()
    }

    @Test
    fun giveSuccess_whenFetch() {
        coroutineTestRule.runBlockingTest {
            val numOfDays = 1
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody("{\"city\":{\"id\":1580578,\"name\":\"Ho Chi Minh City\",\"coord\":{\"lon\":106.6667,\"lat\":10.8333},\"country\":\"VN\",\"population\":0,\"timezone\":25200},\"cod\":\"200\",\"message\":0.0833186,\"cnt\":1,\"list\":[{\"dt\":1627704000,\"sunrise\":1627684894,\"sunset\":1627730263,\"temp\":{\"day\":304.84,\"min\":297.6,\"max\":306.12,\"night\":299.48,\"eve\":305.54,\"morn\":297.6},\"feels_like\":{\"day\":310.09,\"night\":299.48,\"eve\":312.5,\"morn\":298.5},\"pressure\":1008,\"humidity\":62,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"speed\":6.16,\"deg\":261,\"gust\":11.35,\"clouds\":86,\"pop\":0.56,\"rain\":0.55}]}")
            mockWebServer.enqueue(response)

            val apiService = ApiUtils.getApiServiceInstanceForTesting()
            val actualResponse = apiService?.queryWeatherByCity("saigon", numOfDays)?.execute()
            Assert.assertEquals(response.toString().contains("200"), actualResponse?.isSuccessful)
        }
    }

    @Test
    fun giveFail_whenFetch() {
        coroutineTestRule.runBlockingTest {
            val numOfDays = 1
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
            mockWebServer.enqueue(response)

            val apiService = ApiUtils.getApiServiceInstanceForTesting()
            val actualResponse = apiService?.queryWeatherByCity("saigonxx", numOfDays)?.execute()
            Assert.assertEquals(response.toString().contains("404"), actualResponse?.code().toString().contains("404"))
        }
    }
}