package com.fund.iam.ui.main.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fund.iam.BR;
import com.fund.iam.R;
import com.fund.iam.data.DataManager;
import com.fund.iam.databinding.FragmentSearchBinding;
import com.fund.iam.di.ViewModelProviderFactory;
import com.fund.iam.ui.base.BaseFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;


public class SearchFragment extends BaseFragment<FragmentSearchBinding, SearchViewModel> implements SearchNavigator, View.OnClickListener {

    public static final String TAG = SearchFragment.class.getSimpleName();

    private SearchListChannelAdapter adapter_channels;
    private SearchListUserAdapter adapter_users;
    private SearchListUserFilterAdapter adapter_filter;
    private Spinner spinner_location;
    private Spinner spinner_job;
    private Spinner spinner_gender;
    private Spinner spinner_age;

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
    public void updateChannels() {
        adapter_channels.setModel_Channels(getViewModel().channels);
        adapter_channels.setKeyWord(getViewDataBinding().etSearchInput.getText().toString());
    }

    @Override
    public void updateUsers() {
        adapter_users.setModel_Users(getViewModel().users,getViewModel().jobs);
        adapter_users.setKeyWord(getViewDataBinding().etSearchInput.getText().toString());
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

        getViewDataBinding().btChannelList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewDataBinding().btUserList.setCardBackgroundColor(Color.parseColor("#7E57C2"));
                getViewDataBinding().btChannelList.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                getViewDataBinding().etSearch.setHint("채널을 검색하세요");
                getViewDataBinding().userOptionSet.setVisibility(View.INVISIBLE);
                getViewDataBinding().channelOptionSet.setVisibility(View.VISIBLE);
                getViewDataBinding().recyclerViewUsers.setVisibility(View.INVISIBLE);
                getViewDataBinding().recyclerViewChannels.setVisibility(View.VISIBLE);

                imm.hideSoftInputFromWindow(getViewDataBinding().etSearch.getWindowToken(),0);
                getViewModel().TabState = 1;
                getViewDataBinding().etSearchInput.setText("");
            }
        });

        getViewDataBinding().btUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewDataBinding().btChannelList.setCardBackgroundColor(Color.parseColor("#7E57C2"));
                getViewDataBinding().btUserList.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                getViewDataBinding().etSearch.setHint("유저를 검색하세요");
                getViewDataBinding().userOptionSet.setVisibility(View.VISIBLE);
                getViewDataBinding().channelOptionSet.setVisibility(View.INVISIBLE);
                getViewDataBinding().recyclerViewChannels.setVisibility(View.INVISIBLE);
                getViewDataBinding().recyclerViewUsers.setVisibility(View.VISIBLE);

                imm.hideSoftInputFromWindow(getViewDataBinding().etSearch.getWindowToken(),0);
                getViewModel().TabState = 2;
                getViewDataBinding().etSearchInput.setText("");
            }
        });

        getViewDataBinding().etSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getViewModel().TabState==1) {
                    adapter_channels.setKeyWord(getViewDataBinding().etSearchInput.getText().toString());
                } else {
                    adapter_users.setKeyWord(getViewDataBinding().etSearchInput.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        String[] spinner_str_channel = {"최신순", "참여인원많은순"};

        final ArrayAdapter<String> adapter_spinner = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),R.layout.support_simple_spinner_dropdown_item,spinner_str_channel) {
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
        getViewDataBinding().spinner.setGravity(Gravity.CENTER);
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

        getViewDataBinding().recyclerViewFilter.setHasFixedSize(true);
        getViewDataBinding().recyclerViewFilter.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        adapter_filter = new SearchListUserFilterAdapter();
        getViewDataBinding().recyclerViewFilter.setAdapter(adapter_filter);



        getViewDataBinding().recyclerViewChannels.setHasFixedSize(true);
        getViewDataBinding().recyclerViewChannels.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter_channels = new SearchListChannelAdapter();
        getViewDataBinding().recyclerViewChannels.setAdapter(adapter_channels);

        getViewDataBinding().recyclerViewUsers.setHasFixedSize(true);
        getViewDataBinding().recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter_users = new SearchListUserAdapter();
        getViewDataBinding().recyclerViewUsers.setAdapter(adapter_users);

        // UserFilterBottomSheet 세팅
        initUserFilterBottomSheet();

        getViewModel().getChannelsInfo();


    }

    // UserFilterBottomSheet 세팅
    private void initUserFilterBottomSheet() {

        // User필터 BottomSheet 초기세팅
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.dialog_search_user_filter);

        // User필터 Spinner 초기세팅
        spinner_location = bottomSheetDialog.findViewById(R.id.spinner_location);
        spinner_location.setGravity(Gravity.RIGHT);
        spinner_job = bottomSheetDialog.findViewById(R.id.spinner_job);
        spinner_job.setGravity(Gravity.RIGHT);
        spinner_gender = bottomSheetDialog.findViewById(R.id.spinner_gender);
        spinner_gender.setGravity(Gravity.RIGHT);
        spinner_age = bottomSheetDialog.findViewById(R.id.spinner_age);
        spinner_age.setGravity(Gravity.RIGHT);

        final ArrayAdapter<String> adapter_spinner_user_gender = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),R.layout.item_search_spinner,getViewModel().spinner_str_user_gender) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#7E57C2"));
                return v;
            }

        };

        final ArrayAdapter<String> adapter_spinner_user_age = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),R.layout.item_search_spinner,getViewModel().spinner_str_user_age) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#7E57C2"));
                return v;
            }

        };

        spinner_gender.setAdapter(adapter_spinner_user_gender);
        spinner_age.setAdapter(adapter_spinner_user_age);

        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Logger.i("SearchFragment:"+spinner_gender.getSelectedItem().toString()+"is selected");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Logger.i("SearchFragment:"+spinner_age.getSelectedItem().toString()+"is selected");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getViewModel().getJobsInfo();
        getViewModel().getLocationsInfo();

        // User필터 BottomSheet 클릭리스너 코드
        getViewDataBinding().btUserFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("SearchFragment:userFilter 클릭");

                spinner_location.setSelection(Integer.parseInt(getViewModel().user_filters.get(0).toString()));
                spinner_job.setSelection(Integer.parseInt(getViewModel().user_filters.get(1).toString()));
                spinner_gender.setSelection(Integer.parseInt(getViewModel().user_filters.get(2).toString()));
                spinner_age.setSelection(Integer.parseInt(getViewModel().user_filters.get(3).toString()));

                bottomSheetDialog.show();
            }
        });

        TextView bt_close = bottomSheetDialog.findViewById(R.id.bt_close);
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        Button bt_apply = bottomSheetDialog.findViewById(R.id.bt_apply);
        bt_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 구현 중..
                getViewModel().user_filters.removeAll(getViewModel().user_filters);
                getViewModel().user_filters.add(spinner_location.getSelectedItemPosition()+"");
                getViewModel().user_filters.add(spinner_job.getSelectedItemPosition()+"");
                getViewModel().user_filters.add(spinner_gender.getSelectedItemPosition()+"");
                getViewModel().user_filters.add(spinner_age.getSelectedItemPosition()+"");
                adapter_filter.setUserFilter(getViewModel().locations,getViewModel().jobs,getViewModel().spinner_str_user_gender,getViewModel().spinner_str_user_age, getViewModel().user_filters);
                bottomSheetDialog.dismiss();
            }
        });

        Button bt_reset = bottomSheetDialog.findViewById(R.id.bt_reset);
        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_location.setSelection(0);
                spinner_job.setSelection(0);
                spinner_gender.setSelection(0);
                spinner_age.setSelection(0);
            }
        });

    }

    @Override
    public void updateJobs() {
        ArrayList arrayList_jobs = new ArrayList<>();;
        for(int i =0; i<getViewModel().jobs.size(); i++) {
            arrayList_jobs.add(getViewModel().jobs.get(i).getName());
        }
        arrayList_jobs.add(0,"  선택없음  ");

        final ArrayAdapter<String> adapter_spinner_user_job = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),R.layout.item_search_spinner, arrayList_jobs) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#7E57C2"));
                return v;

            }

        };

        spinner_job.setAdapter(adapter_spinner_user_job);


        spinner_job.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Logger.i("SearchFragment:"+spinner_job.getSelectedItem().toString()+"is selected");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getViewModel().getUsersInfo();

    }

    @Override
    public void updateLocations() {

        ArrayList arrayList_locations = new ArrayList<>();;
        for(int i =0; i<getViewModel().locations.size(); i++) {
            arrayList_locations.add(getViewModel().locations.get(i).getName());
        }
        arrayList_locations.add(0,"  선택없음  ");

        // 서버에서 Location리스트를 받아와 User필터에 세팅
        final ArrayAdapter<String> adapter_spinner_user_location = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),R.layout.item_search_spinner,arrayList_locations) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#7E57C2"));
                return v;
            }

        };

        spinner_location.setAdapter(adapter_spinner_user_location);

        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Logger.i("SearchFragment:"+spinner_location.getSelectedItem().toString()+"is selected");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
