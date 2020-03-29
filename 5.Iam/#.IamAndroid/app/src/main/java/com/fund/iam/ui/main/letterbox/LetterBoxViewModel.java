package com.fund.iam.ui.main.letterbox;


import android.content.Context;

import com.fund.iam.data.DataManager;
import com.fund.iam.data.bus.LetterBoxBus;
import com.fund.iam.data.model.LetterBox;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Maybe;

public class LetterBoxViewModel extends BaseViewModel<LetterBoxNavigator> {


    public LetterBoxViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);

        subscribeEvent();
    }

    private void subscribeEvent() {

    }

    public void dummyLetterBoxes() {
        LetterBox dummyLetterBox1 = new LetterBox();
        dummyLetterBox1.setName("김똥개");
        dummyLetterBox1.setEmail("slee8789@naver.com");
        dummyLetterBox1.setJob("개발자");
        dummyLetterBox1.setImageUrl("http://k.kakaocdn.net/dn/Xup1y/btqy7KEvemW/Sa6SqsgQT81i2ysl4F79J0/img_640x640.jpg");
        LetterBox dummyLetterBox2 = new LetterBox();
        dummyLetterBox2.setName("아무개");
        dummyLetterBox2.setEmail("slee8789@gmail.com");
        dummyLetterBox2.setJob("디자이너");
        dummyLetterBox2.setImageUrl("https://phinf.pstatic.net/contact/20191017_14/1571291197868zOLyb_PNG/avatar_profile.png");
        LetterBox dummyLetterBox3 = new LetterBox();
        dummyLetterBox3.setName("홍길동");
        dummyLetterBox3.setEmail("slee8789@hanmail.net");
        dummyLetterBox3.setJob("기획자");
        dummyLetterBox3.setImageUrl("http://k.kakaocdn.net/dn/sH462/btqp7aley1W/4WbdJ53Bo0rSOXBaUbRsY0/img_640x640.jpg");
        ArrayList<LetterBox> dummyLetterBoxes = new ArrayList<>();
        dummyLetterBoxes.add(dummyLetterBox1);
        dummyLetterBoxes.add(dummyLetterBox2);
        dummyLetterBoxes.add(dummyLetterBox3);

        getCompositeDisposable().add(Maybe.just(dummyLetterBoxes).subscribe(letterBoxes -> {
            getNavigator().onRepositoriesChanged(letterBoxes);
        }));
    }

}
