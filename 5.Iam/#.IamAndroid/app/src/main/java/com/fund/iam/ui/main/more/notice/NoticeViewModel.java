package com.fund.iam.ui.main.more.notice;


import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.LetterBox;
import com.fund.iam.data.model.Notice;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Maybe;

public class NoticeViewModel extends BaseViewModel<NoticeNavigator> {


    public NoticeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);

        subscribeEvent();
    }

    public void getDummyNotices() {

        Notice notice1 = new Notice("2019.03.29", "업데이트 변경사항", "안녕하세요, 운영진입니다.\n" +
                "이번 업데이트 변경사항 안내드립니다.\n" +
                "- 속도 개선\n" +
                "- UI 개변\n" +
                "- 사용자 편의성 개편\n" +
                "\n" +
                "감사합니다.");
        Notice notice2 = new Notice("2019.02.11", "공지사항", "안녕하세요, 운영진입니다.\n" +
                "이번 업데이트 변경사항 안내드립니다.\n" +
                "- 속도 개선\n" +
                "- UI 개변\n" +
                "- 사용자 편의성 개편\n" +
                "\n" +
                "감사합니다.");
        Notice notice3 = new Notice("2019.02.01", "안녕하세요. 운영진입니다.", "안녕하세요, 운영진입니다.\n" +
                "이번 업데이트 변경사항 안내드립니다.\n" +
                "- 속도 개선\n" +
                "- UI 개변\n" +
                "- 사용자 편의성 개편\n" +
                "\n" +
                "감사합니다.");
        Notice notice4 = new Notice("2019.01.01", "앱 런칭!", "안녕하세요, 운영진입니다.\n" +
                "이번 업데이트 변경사항 안내드립니다.\n" +
                "- 속도 개선\n" +
                "- UI 개변\n" +
                "- 사용자 편의성 개편\n" +
                "\n" +
                "감사합니다.");
        ArrayList<Notice> notices = new ArrayList<>();
        notices.add(notice1);
        notices.add(notice2);
        notices.add(notice3);
        notices.add(notice4);

        getCompositeDisposable().add(Maybe.just(notices).subscribe(test -> {
            getNavigator().onRepositoriesChanged(test);
        }));
    }


    private void subscribeEvent() {
        getCompositeDisposable().add(getDataManager().postNotices()
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(notices -> {
                    if (notices.isSuccessful()) {
                        getNavigator().onRepositoriesChanged(notices.body());
                    }
                }, onError -> getNavigator().handleError(onError)));
    }

}
