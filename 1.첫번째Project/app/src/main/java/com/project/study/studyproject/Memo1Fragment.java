package com.project.study.studyproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;

public class Memo1Fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TabLayout tabLayout;

    private String mParam1;
    private String mParam2;

    private ArrayList<Dictionary> ArrayList = new ArrayList<>();
    private CustomAdapter mAdapter;
    private RecyclerView recyclerView;

    private FloatingActionButton floatingActionButton;

    private OnFragmentInteractionListener mListener;


    public Memo1Fragment() {
    }

    // newInstance 에서 fragment 를 생성, bundle 데이터는 onCreate 에서
    // getArguments(). 이후에 있는 getString(“key”), getInt(“key”)로 받을수 있다.
    public static Memo1Fragment newInstance(String param1, String param2) {
        Memo1Fragment fragment = new Memo1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //데이타를 가져온다.
        prepareData();

        //xml 객체화한다.
        View view = inflater.inflate(R.layout.fragment_memo1, container, false);

        // recyclerView 를 객체화 하고 layoutManager 에 LinearLayoutManager 를 할당한다.
        recyclerView = view.findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // CustomAdapter 를 객체화한다.
        mAdapter = new CustomAdapter(getActivity(), ArrayList);

        // recyclerView 에 customAdapter 를 할당한다.
        recyclerView.setAdapter(mAdapter);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.float_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              ADDData();

            }
        });

        makeCircularFloatingButton();

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        prepareData();
        mAdapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void prepareData() {

        ArrayList.clear();
        DbHandler db = new DbHandler(getActivity());

        final ArrayList<Dictionary> notes = db.getAllDatas();

        db.close();

        //if m contains data
        if (notes.size() > 0) {

            //loop through contents
            for (int i = 0; i < notes.size(); i++) {

                Log.w("NAME", notes.get(i).getContents());
                Log.w("NAME", notes.get(i).getDate());

                //add data to list used in adapter
                ArrayList.add(notes.get(i));

//                //notify data change
//                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void makeCircularFloatingButton() {

        // 메뉴를 만든다.
        ImageView icon = new ImageView(getActivity()); // Create an icon
        icon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.circle06));
        com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton actionButton = new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.Builder(getActivity())
                .setContentView(icon)
                .build();

        // 버튼을 만든다.
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());

        // 버튼1
        ImageView itemIcon = new ImageView(getActivity());
        itemIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.circle01));
        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ADDData();
            }
        });

        // 버튼2
        itemIcon = new ImageView(getActivity());
        itemIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.circle02));
        SubActionButton button2 = itemBuilder.setContentView(itemIcon).build();
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Button2", Toast.LENGTH_SHORT).show();
            }
        });

        // 버튼3
        itemIcon = new ImageView(getActivity());
        itemIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.circle03));
        SubActionButton button3 = itemBuilder.setContentView(itemIcon).build();
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Button3", Toast.LENGTH_SHORT).show();
            }
        });

        // 버튼4
        itemIcon = new ImageView(getActivity());
        itemIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.circle04));
        SubActionButton button4 = itemBuilder.setContentView(itemIcon).build();
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Button4", Toast.LENGTH_SHORT).show();
            }
        });

        // 버튼5
        itemIcon = new ImageView(getActivity());
        itemIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.circle05));
        SubActionButton button5 = itemBuilder.setContentView(itemIcon).build();
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Button5", Toast.LENGTH_SHORT).show();
            }
        });

        // add everything
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                .addSubActionView(button5)

                .attachTo(actionButton)
                .build();
    }

    public void ADDData() {

        Intent intent = new Intent(getActivity(), MemoAdd.class);
        intent.putExtra("check","add");
        startActivity(intent);

    }
}
