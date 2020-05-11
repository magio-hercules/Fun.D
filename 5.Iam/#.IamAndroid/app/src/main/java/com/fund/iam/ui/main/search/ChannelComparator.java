package com.fund.iam.ui.main.search;

import com.fund.iam.data.model.Channel;

import java.util.Comparator;

public class ChannelComparator implements Comparator<Channel> {
    @Override
    public int compare(Channel o1, Channel o2) {
        int firstValue = o1.nowUser;
        int secondValue = o2.nowUser;

        if (firstValue > secondValue) {
            return -1;
        } else if (o1.nowUser < o2.nowUser) {
            return 1;
        } else {
            return 0;
        }
    }
}
