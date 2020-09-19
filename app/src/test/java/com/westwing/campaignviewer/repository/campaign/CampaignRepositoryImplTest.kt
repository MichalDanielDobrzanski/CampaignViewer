package com.westwing.campaignviewer.repository.campaign

import com.westwing.campaignviewer.TestSchedulersProvider
import com.westwing.campaignviewer.schedulers.SchedulersProvider
import com.westwing.campaignviewer.webservice.WestwingCampaignWebservice
import com.westwing.campaignviewer.webservice.models.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when` as whenever

class CampaignRepositoryImplTest {

    private val testSchedulers: SchedulersProvider = TestSchedulersProvider()

    private val mockWestwingCampaignWebservice = mock(WestwingCampaignWebservice::class.java)

    private lateinit var campaignRepository: CampaignRepository

    @Before
    fun setUp() {
        campaignRepository = CampaignRepositoryImpl(mockWestwingCampaignWebservice, testSchedulers)
    }

    @Test
    fun `Should begin with loading state`() {
        // when
        val testStream = campaignRepository.campaignsStream().test()

        // then
        testStream.assertValueCount(1)
        testStream.assertValueAt(0) { it is CampaignRepositoryModel.NotPresent }
        testStream.assertNoErrors()
        testStream.assertNotComplete()
    }

    @Test
    fun `Should fetch campaigns and emit a data event in stream`() {
        // given
        whenever(mockWestwingCampaignWebservice.fetchCampaigns())
            .thenReturn(Single.just(fakeData))
        val testStream = campaignRepository.campaignsStream().test()

        // when
        campaignRepository.fetchCampaigns()

        // then
        testStream.assertValueCount(2)
        testStream.assertValueAt(0) { it is CampaignRepositoryModel.NotPresent }
        testStream.assertValueAt(1) { it is CampaignRepositoryModel.Data && validateData(it) }
        testStream.assertNoErrors()
        testStream.assertNotComplete()
    }

    @Test
    fun `Should fail on fetching campaigns and emit error`() {
        // given
        val error = Throwable("Network fail")
        whenever(mockWestwingCampaignWebservice.fetchCampaigns())
            .thenReturn(Single.error(error))
        val testStream = campaignRepository.campaignsStream().test()

        // when
        campaignRepository.fetchCampaigns()

        // then
        testStream.assertValueCount(2)
        testStream.assertValueAt(1) { it is CampaignRepositoryModel.Error }
        testStream.assertNoErrors()
        testStream.assertNotComplete()
    }

    @Test
    fun `Should fetch another campaign and emit two data events in stream`() {
        // given
        whenever(mockWestwingCampaignWebservice.fetchCampaigns())
            .thenReturn(Single.just(fakeData))
        val testStream = campaignRepository.campaignsStream().test()

        // when
        campaignRepository.fetchCampaigns()
        campaignRepository.fetchCampaigns()

        // then
        testStream.assertValueCount(3)
        testStream.assertValueAt(0) { it is CampaignRepositoryModel.NotPresent }
        testStream.assertValueAt(1) { it is CampaignRepositoryModel.Data && validateData(it) }
        testStream.assertValueAt(2) { it is CampaignRepositoryModel.Data && validateData(it) }
        testStream.assertNoErrors()
        testStream.assertNotComplete()
    }

    private fun validateData(data: CampaignRepositoryModel.Data): Boolean {
        val list = data.campaignModelList
        var correct = true
        for (i in list.indices) {
            val fakeDataItem = fakeData.metadata.data[i]
            correct =
                correct && (list[i].title == fakeDataItem.name)
                        && (list[i].description == fakeDataItem.description)
                        && (list[i].imageUrl == fakeDataItem.image.url)
        }
        return correct
    }

    private val fakeData: CampaignDataModel = CampaignDataModel(
        true,
        Messages(emptyList()),
        Metadata(
            2,
            listOf(
                Data("xD", "my long campaign title", "someinvalidurl", Image("res")),
                Data("xDD", "my second long campaign title", "someinvalidurl", Image("res")),
                Data("xDDD", "my third long campaign title", "someinvalidurl", Image("res"))

            )
        )
    )
}