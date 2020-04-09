package com.fund.iam.di.provider;

import io.reactivex.Scheduler;

public interface SchedulerProvider {

    Scheduler ui();

    Scheduler io();

    Scheduler newThread();

    Scheduler computation();

}
