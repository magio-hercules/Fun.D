package com.fund.iam.ui.main.letterbox;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.databinding.ObservableField;
import androidx.navigation.Navigation;

import com.fund.iam.data.bus.LetterBoxBus;
import com.fund.iam.data.model.LetterBox;
import com.fund.iam.data.model.User;
import com.fund.iam.ui.letter.LetterActivity;
import com.fund.iam.ui.main.bookmark.BookmarkFragmentDirections;
import com.orhanobut.logger.Logger;

public class ItemLetterBoxViewModel {

    public ObservableField<String> job = new ObservableField<>();
    public ObservableField<Integer> jobColor = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> email = new ObservableField<>();
    public ObservableField<String> imageUrl = new ObservableField<>();
    private LetterBox letterBox;
    private Context context;
    private View view;

    private LetterBoxListener mListener;

    public interface LetterBoxListener {
        void onRead();
    }

    public ItemLetterBoxViewModel(View view, Context context, LetterBox letterBox, LetterBoxListener listener) {
        Logger.d("ItemLetterBoxViewModel " + letterBox);
        this.context = context;
        this.view = view;
        this.letterBox = letterBox;
        mListener = listener;
        imageUrl.set(letterBox.getImageUrl());
        job.set(letterBox.getJobTitle());
        jobColor.set(Color.parseColor(letterBox.getJobColor()));
        name.set(letterBox.getName());
        email.set(letterBox.getEmail());

    }

    public void onItemClick() {
//        BookmarkFragmentDirections.ActionNavigationBookmarkToNavigationHome action = BookmarkFragmentDirections.actionNavigationBookmarkToNavigationHome();
//        action.setArgProfileEmail(letterBox.getEmail());
//        action.setArgProfileType(letterBox.get);
//        Navigation.findNavController(view).navigate(action);
    }

    public void onLetterClick() {
        mListener.onRead();
        LetterBoxBus.getInstance().sendLetterBox(new LetterBox(new User(letterBox.getFriendId(), letterBox.getName(), letterBox.getImageUrl(), letterBox.getToken())));
        LetterActivity.start(context);
    }

}
