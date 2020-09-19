package com.westwing.campaignviewer.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.westwing.campaignviewer.presentation.viewstate.CampaignViewState
import com.westwing.campaignviewer.repository.campaign.CampaignRepository
import com.westwing.campaignviewer.repository.campaign.CampaignRepositoryModel
import com.westwing.campaignviewer.utils.getOrAwaitValue
import com.westwing.domain.CampaignModel
import io.reactivex.Flowable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when` as whenever

class GetCampaignsUseCaseTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mockCampaignRepository: CampaignRepository =
        Mockito.mock(CampaignRepository::class.java)

    private lateinit var getCampaignsUseCase: GetCampaignsUseCase

    @Before
    fun setUp() {
        getCampaignsUseCase = GetCampaignsUseCase(mockCampaignRepository)
    }

    @Test
    fun `Should fetch not present state from repository and map it to loading state`() {
        // given
        whenever(mockCampaignRepository.campaignsStream()).thenReturn(
            Flowable.just(CampaignRepositoryModel.NotPresent)
        )

        // when
        getCampaignsUseCase.execute()

        // then
        assertEquals(
            getCampaignsUseCase.campaignViewStateLiveData.getOrAwaitValue(),
            CampaignViewState.Loading
        )
    }

    @Test
    fun `Should fetch campaigns and map it to content state`() {
        // given
        whenever(mockCampaignRepository.campaignsStream()).thenReturn(Flowable.just(fakeData))

        // when
        getCampaignsUseCase.execute()

        // then
        assertEquals(
            getCampaignsUseCase.campaignViewStateLiveData.getOrAwaitValue(),
            CampaignViewState.Content(fakeData.campaignModelList)
        )
    }

    @Test
    fun `Should fail on fetching campaigns and map it to error state`() {
        // given
        whenever(mockCampaignRepository.campaignsStream()).thenReturn(
            Flowable.just(
                CampaignRepositoryModel.Error
            )
        )

        // when
        getCampaignsUseCase.execute()

        // then
        assertEquals(
            getCampaignsUseCase.campaignViewStateLiveData.getOrAwaitValue(),
            CampaignViewState.Error
        )
    }

    private val fakeData: CampaignRepositoryModel.Data = CampaignRepositoryModel.Data(
        listOf(
            CampaignModel("xD", "my long campaign title", "someinvalidurl"),
            CampaignModel("xDD", "my second long campaign title", "someinvalidurl"),
            CampaignModel("xDDD", "my third long campaign title", "someinvalidurl")
        )
    )

}