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

import java.util.Objects;

import javax.inject.Inject;


public class SearchFragment extends BaseFragment<FragmentSearchBinding, SearchViewModel> implements SearchNavigator, View.OnClickListener {

    public static final String TAG = SearchFragment.class.getSimpleName();

    private SearchListChannelAdapter adapter_channels;
    private SearchListUserAdapter adapter_users;
    private SearchListUserFilterAdapter adapter_filter;
    int TabState = 1;


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
        adapter_users.setModel_Users(getViewModel().users);
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

//        getViewDataBinding().setViewModel(getViewModel());

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
                getViewDataBinding().recyclerViewUsers.setVisibility(View.INVISIBLE);
                getViewDataBinding().recyclerViewChannels.setVisibility(View.VISIBLE);

                imm.hideSoftInputFromWindow(getViewDataBinding().etSearch.getWindowToken(),0);
                TabState = 1;
                getViewDataBinding().etSearchInput.setText("");
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
                getViewDataBinding().recyclerViewChannels.setVisibility(View.INVISIBLE);
                getViewDataBinding().recyclerViewUsers.setVisibility(View.VISIBLE);

                imm.hideSoftInputFromWindow(getViewDataBinding().etSearch.getWindowToken(),0);
                TabState = 2;
                getViewDataBinding().etSearchInput.setText("");
            }
        });

        getViewDataBinding().etSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TabState==1) {
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

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.dialog_search_user_filter);

        getViewDataBinding().btUserFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("SearchFragment:userFilter 클릭");

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
                        Toast.makeText(getActivity(),"구현 중",Toast.LENGTH_SHORT).show();
                    }
                });

                bottomSheetDialog.show();
            }
        });

        String[] spinner_str_user_region = {"-","서울특별시", "부산광역시","인천광역시","대구광역시", "광주광역시", "대전광역시", " 울산광역시", "세종시", "경기도", "강원도", "충청남도", "충청북도", "경상북도","경상남도", "전라북도","전라남도", "제주도"};
        String[] spinner_str_user_job =  {"-","기획자","개발자", "디자이너", "마케터"};
        String[] spinner_str_user_gender = {"-","남자","여자"};
        String[] spinner_str_user_age = {"-","10대", "20대","30대","40대", "50대 이상"};

        Spinner spinner_region = bottomSheetDialog.findViewById(R.id.spinner_region);
        spinner_region.setGravity(Gravity.RIGHT);
        Spinner spinner_job = bottomSheetDialog.findViewById(R.id.spinner_job);
        spinner_job.setGravity(Gravity.RIGHT);
        Spinner spinner_gender = bottomSheetDialog.findViewById(R.id.spinner_gender);
        spinner_gender.setGravity(Gravity.RIGHT);
        Spinner spinner_age = bottomSheetDialog.findViewById(R.id.spinner_age);
        spinner_age.setGravity(Gravity.RIGHT);


        final ArrayAdapter<String> adapter_spinner_user_region = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),R.layout.item_search_spinner,spinner_str_user_region) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#7E57C2"));
                return v;
            }

        };


        final ArrayAdapter<String> adapter_spinner_user_job = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),R.layout.item_search_spinner,spinner_str_user_job) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#7E57C2"));
                return v;

            }

        };

        final ArrayAdapter<String> adapter_spinner_user_gender = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),R.layout.item_search_spinner,spinner_str_user_gender) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#7E57C2"));
                return v;
            }

        };
        final ArrayAdapter<String> adapter_spinner_user_age = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),R.layout.item_search_spinner,spinner_str_user_age) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#7E57C2"));
                return v;
            }

        };

        spinner_region.setAdapter(adapter_spinner_user_region);
        spinner_job.setAdapter(adapter_spinner_user_job);
        spinner_gender.setAdapter(adapter_spinner_user_gender);
        spinner_age.setAdapter(adapter_spinner_user_age);

        spinner_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Logger.i("SearchFragment:"+spinner_region.getSelectedItem().toString()+"is selected");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_job.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Logger.i("SearchFragment:"+spinner_job.getSelectedItem().toString()+"is selected");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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



        getViewDataBinding().recyclerViewChannels.setHasFixedSize(true);
        getViewDataBinding().recyclerViewChannels.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter_channels = new SearchListChannelAdapter();
        getViewDataBinding().recyclerViewChannels.setAdapter(adapter_channels);

        getViewDataBinding().recyclerViewUsers.setHasFixedSize(true);
        getViewDataBinding().recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter_users = new SearchListUserAdapter();
        getViewDataBinding().recyclerViewUsers.setAdapter(adapter_users);

        getViewModel().getChannelsInfo();
        getViewModel().getUsersInfo();
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
