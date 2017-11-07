package com.vikingsen.cheesedemo.util


import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface SchedulerProvider {
    fun computation(): Scheduler
    fun io(): Scheduler
    fun ui(): Scheduler

    class AppSchedulerProvider : SchedulerProvider {

        override fun computation(): Scheduler {
            return Schedulers.computation()
        }

        override fun io(): Scheduler {
            return Schedulers.io()
        }

        override fun ui(): Scheduler {
            return AndroidSchedulers.mainThread()
        }
    }

    class TrampolineSchedulerProvider : SchedulerProvider {

        override fun computation(): Scheduler {
            return Schedulers.trampoline()
        }

        override fun io(): Scheduler {
            return Schedulers.trampoline()
        }

        override fun ui(): Scheduler {
            return Schedulers.trampoline()
        }
    }
}
