package com.fund.iam.ui.main.letterbox;


import android.content.Context;

import com.annimon.stream.Stream;
import com.fund.iam.data.DataManager;
import com.fund.iam.data.bus.LetterBoxBus;
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.Letter;
import com.fund.iam.data.model.LetterBox;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;

public class LetterBoxViewModel extends BaseViewModel<LetterBoxNavigator> {


    public LetterBoxViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);
        subscribeEvent();
    }


    private void subscribeEvent() {
        getCompositeDisposable().add(getDataManager().postLetterBoxes(getDataManager().getMyInfo().getId())
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(letterBoxes -> {
                    if (letterBoxes.isSuccessful()) {
                        List<LetterBox> updatedLetterBoxes = Stream.of(letterBoxes.body()).map(letterBox -> {
                            if (letterBox.getJob() == null) {
                                letterBox.setJobTitle("직업 없음");
                                letterBox.setJobColor("#ff00ff");
                            } else {
                                letterBox.setJobTitle(Stream.of(getDataManager().getJobs()).filter(job -> job.getId() == Integer.parseInt(letterBox.getJob())).findFirst().get().getName());
                                letterBox.setJobColor(Stream.of(getDataManager().getJobs()).filter(job -> job.getId() == Integer.parseInt(letterBox.getJob())).findFirst().get().getColor());
                            }
                            return letterBox;
                        }).toList();

                        getNavigator().onRepositoriesChanged(updatedLetterBoxes);
                    }
                }));
    }


}
