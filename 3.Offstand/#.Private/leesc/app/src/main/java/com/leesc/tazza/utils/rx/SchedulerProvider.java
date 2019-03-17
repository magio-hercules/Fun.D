package com.leesc.tazza.utils.rx;

import io.reactivex.Scheduler;

public interface SchedulerProvider {

    Scheduler ui();

    Scheduler io();

    Scheduler newwThread();

    Scheduler computation();

}
