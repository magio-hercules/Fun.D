package com.fund.iam.model;

public class PortfolioInfo {

    private final int id;
    private final int user_id;
    private final int type;
    private final String text;
    private final String image_url;
    private final String create_date;
    private final String modify_date;

    public PortfolioInfo(int id, int user_id, int type, String text, String image_url, String create_date, String modify_date) {
        this.id = id;
        this.user_id = user_id;
        this.type = type;
        this.text = text;
        this.image_url = image_url;
        this.create_date = create_date;
        this.modify_date = modify_date;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return user_id;
    }

    public int getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getCreate_date() {
        return create_date;
    }

    public String getModify_date() {
        return modify_date;
    }
}
