package com.westwing.campaignviewer.schedulers

import io.reactivex.Scheduler

interface SchedulersProvider {
    fun background(): Scheduler
    fun io(): Scheduler
    fun ui(): Scheduler
}