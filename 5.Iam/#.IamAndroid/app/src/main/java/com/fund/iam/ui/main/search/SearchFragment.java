package com.fund.iam.ui.main.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fund.iam.BR;
import com.fund.iam.R;
import com.fund.iam.data.DataManager;
import com.fund.iam.databinding.FragmentSearchBinding;
import com.fund.iam.databinding.FragmentSettingBinding;
import com.fund.iam.di.ViewModelProviderFactory;
import com.fund.iam.ui.base.BaseFragment;
import com.orhanobut.logger.Logger;

import java.util.Objects;

import javax.inject.Inject;


public class SearchFragment extends BaseFragment<FragmentSearchBinding, SearchViewModel> implements SearchNavigator, View.OnClickListener {

    public static final String TAG = SearchFragment.class.getSimpleName();

    private SearchListChanelAdapter adapter;
    private SearchListUserFilterAdapter adapter_filter;


    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    DataManager dataManager;

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public SearchViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), viewModelProviderFactory).get(SearchViewModel.class);
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("SearchFragment:onCreate");
        getViewModel().setNavigator(this);
        setHasOptionsMenu(true);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

    }

    private void initViews(View view) {

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;

        getViewDataBinding().btChanelList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewDataBinding().btUserList.setCardBackgroundColor(Color.parseColor("#7E57C2"));
                getViewDataBinding().btChanelList.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                getViewDataBinding().etSearch.setHint("채널을 검색하세요");
                getViewDataBinding().userOptionSet.setVisibility(View.INVISIBLE);
                getViewDataBinding().channelOptionSet.setVisibility(View.VISIBLE);
                imm.hideSoftInputFromWindow(getViewDataBinding().etSearch.getWindowToken(),0);
            }
        });

        getViewDataBinding().btUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewDataBinding().btChanelList.setCardBackgroundColor(Color.parseColor("#7E57C2"));
                getViewDataBinding().btUserList.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                getViewDataBinding().etSearch.setHint("유저를 검색하세요");
                getViewDataBinding().userOptionSet.setVisibility(View.VISIBLE);
                getViewDataBinding().channelOptionSet.setVisibility(View.INVISIBLE);
                imm.hideSoftInputFromWindow(getViewDataBinding().etSearch.getWindowToken(),0);
            }
        });

        String[] spinner_str = {"최신순", "참여인원많은순"};
        final ArrayAdapter<String> adapter_spinner = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),R.layout.support_simple_spinner_dropdown_item,spinner_str) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#7E57C2"));
                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView)v).setTextColor(Color.parseColor("#7E57C2"));
                return v;
            }

        };

        getViewDataBinding().spinner.setAdapter(adapter_spinner);
        getViewDataBinding().spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Logger.i("SearchFragment:"+getViewDataBinding().spinner.getSelectedItem().toString()+"is selected");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //TODO 서브페이지를 Activity로 할지 Fragment로 할지 정하기
        getViewDataBinding().btMoveToCreateChanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), CreateChannelActivity.class);
//                startActivity(intent);
            }
        });

        getViewDataBinding().recyclerViewFilter.setHasFixedSize(true);
        getViewDataBinding().recyclerViewFilter.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        adapter_filter = new SearchListUserFilterAdapter();
        getViewDataBinding().recyclerViewFilter.setAdapter(adapter_filter);

        getViewDataBinding().btUserFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("SearchFragment:userFilter 클릭");
            }
        });

        getViewDataBinding().recyclerView.setHasFixedSize(true);
        getViewDataBinding().recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SearchListChanelAdapter();
        getViewDataBinding().recyclerView.setAdapter(adapter);

    }


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
        Logger.e("SearchFragment:handleError " + throwable.getMessage());
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
    }
}
