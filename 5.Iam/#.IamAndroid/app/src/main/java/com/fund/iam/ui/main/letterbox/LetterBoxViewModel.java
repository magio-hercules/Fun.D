package com.fund.iam.ui.main.letterbox;

import com.annimon.stream.Stream;
import com.fund.iam.data.DataManager;
import com.fund.iam.data.bus.LetterBus;
import com.fund.iam.data.model.LetterBox;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class LetterBoxViewModel extends BaseViewModel<LetterBoxNavigator> {
    private List<LetterBox> updatedLetterBoxes = new ArrayList<>();

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
                        updatedLetterBoxes = Stream.of(letterBoxes.body()).map(letterBox -> {
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
                }, onError -> getNavigator().handleError(onError)));

        getCompositeDisposable().add(LetterBus.getInstance().getBadge()
                .observeOn(getSchedulerProvider().ui())
                .subscribe(userId -> {
                    if (updatedLetterBoxes != null) {
                        Stream.of(updatedLetterBoxes).filter(letterBox -> letterBox.getFriendId() == userId).findFirst().ifPresent(letterBox -> letterBox.setBadge(true));
                        getNavigator().onRepositoriesChanged(updatedLetterBoxes);
                    }
                }, onError -> Logger.e("test " + onError)));

    }


}
