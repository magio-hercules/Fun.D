package com.fundroid.offstand.utils.rx;

import io.reactivex.Scheduler;

public interface SchedulerProvider {

    Scheduler ui();

    Scheduler io();

    Scheduler newwThread();

    Scheduler computation();

}
