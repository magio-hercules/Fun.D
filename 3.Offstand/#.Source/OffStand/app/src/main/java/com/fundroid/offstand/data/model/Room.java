package com.fundroid.offstand.data.model;

public class Room {

    public enum EnumStatus {

        SHUFFLE_NOT_AVAILABLE(0), SHUFFLE_AVAILABLE(1), INGAME(2), GAME_RESULT_AVAILABLE(3), REGAME(4), AUTO_RESULT(5);

        private int enumStatus;

        EnumStatus(int enumStatus) {
            this.enumStatus = enumStatus;
        }

        public int getEnumStatus() {
            return enumStatus;
        }
    }

    private String id;
    private String name;
    private String address;
    private boolean isSelected;

    public Room(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getId() {
        return id;
    }
}
