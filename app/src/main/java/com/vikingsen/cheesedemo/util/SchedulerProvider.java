package com.vikingsen.cheesedemo.util;


import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public interface SchedulerProvider {
    Scheduler computation();
    Scheduler io();
    Scheduler ui();

    class AppSchedulerProvider implements SchedulerProvider {

        @Override
        public Scheduler computation() {
            return Schedulers.computation();
        }

        @Override
        public Scheduler io() {
            return Schedulers.io();
        }

        @Override
        public Scheduler ui() {
            return AndroidSchedulers.mainThread();
        }
    }

    class TrampolineSchedulerProvider implements SchedulerProvider {

        @Override
        public Scheduler computation() {
            return Schedulers.trampoline();
        }

        @Override
        public Scheduler io() {
            return Schedulers.trampoline();
        }

        @Override
        public Scheduler ui() {
            return Schedulers.trampoline();
        }
    }
}
