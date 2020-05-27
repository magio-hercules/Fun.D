package com.fund.iam.ui.main.more.setting.contact_us;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fund.iam.BR;
import com.fund.iam.R;
import com.fund.iam.data.model.LetterBox;
import com.fund.iam.data.model.User;
import com.fund.iam.databinding.FragmentContactUsBinding;
import com.fund.iam.di.ViewModelProviderFactory;
import com.fund.iam.ui.base.BaseFragment;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import javax.inject.Inject;

import static androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM;


public class ContactUsFragment extends BaseFragment<FragmentContactUsBinding, ContactUsViewModel> implements ContactUsNavigator, View.OnClickListener {

    public static final String TAG = ContactUsFragment.class.getSimpleName();

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    ContactAdapter contactAdapter;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_contact_us;
    }

    @Override
    public ContactUsViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), viewModelProviderFactory).get(ContactUsViewModel.class);
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("onCreate");
        getViewModel().setNavigator(this);
        setHasOptionsMenu(true);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupActionBar();
        initViews();
    }

    private void setupActionBar() {
        getBaseActivity().setSupportActionBar(getViewDataBinding().toolbar);
        getBaseActivity().getSupportActionBar().setDisplayOptions(DISPLAY_SHOW_CUSTOM);
        getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews() {

        getViewDataBinding().contacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        getViewDataBinding().contacts.setAdapter(contactAdapter);
        ArrayList<User> contacts = new ArrayList<>();
        contacts.add(new User("김종민","https://lh3.googleusercontent.com/a-/AOh14GjtOXolIFGJn9t7Oc1OWPN3LzW96rAA3JcNd5sgy7M=s96-c","kjmhercules@gmail.com","PO ∙ 개발"));
        contacts.add(new User("마지연","","wlwlbebe@naver.com","PM ∙ 디자인"));
        contacts.add(new User("문형두","https://fund7bucket.s3.ap-northeast-2.amazonaws.com/iam_bucket/newfirenergy%40gmail.com_GOOGLE_profile.jpg","newfirenergy@gmail.com","기획"));
        contacts.add(new User("김유진","","collor02@gmail.com","기획"));
        contacts.add(new User("황교욱","https://fund7bucket.s3.ap-northeast-2.amazonaws.com/iam_bucket/ghkdroqkf%40gmail.com_GOOGLE_profile.jpg","hkw0101@hotmail.com","디자인"));
        contacts.add(new User("신상호","","sangho8216@gmail.com","디자인"));
        contacts.add(new User("이승철","https://lh3.googleusercontent.com/-ltg6UIIi5Xk/AAAAAAAAAAI/AAAAAAAAAAA/AMZuucm68psHhJvAu4f6cEsQQ3jtQ5n-yw/s96-c/photo.jpg","slee8789@naver.com","개발"));
        contacts.add(new User("김민석","","feelsogoodtime@gmail.com","개발"));
        contacts.add(new User("김진흥","https://fund7bucket.s3.ap-northeast-2.amazonaws.com/iam_bucket/jinhungkim%40gmail.com_GOOGLE_profile.jpg","jinhungkim@gmail.com","개발"));
        contacts.add(new User("김기현","https://k.kakaocdn.net/dn/cHhXXD/btqD0hD7j3f/1J2FVn9Qn5ZkLRpMaiRbn0/img_110x110.jpg","kimki1125@kakao.com","개발"));
        contactAdapter.addItems(contacts);
    }
//    예) PO : 김종민
//    PM : 마지연
//    기획 : 문형두, 김유진, 마지연
//    디자인 : 황교욱, 신상호
//    개발 : 김종민, 이승철, 김민석, 김진흥, 김기현


    @Override
    public void onClick(View view) {
        goBack();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleError(Throwable throwable) {
        Logger.e("handleError " + throwable.getMessage());
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
    }
}
