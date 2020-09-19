package com.westwing.campaignviewer

import com.westwing.campaignviewer.schedulers.SchedulersProvider
import io.reactivex.schedulers.Schedulers

class TestSchedulersProvider : SchedulersProvider {
    override fun background() = Schedulers.trampoline()
    override fun io() = Schedulers.trampoline()
    override fun ui() = Schedulers.trampoline()
}