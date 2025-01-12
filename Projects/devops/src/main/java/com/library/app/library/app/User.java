package com.library.app.library.app;

import java.util.ArrayList;

public class User {
    private String username;
    private Inventory inventory;

    public User(String username) {
        this.username = username;
        this.inventory = new Inventory();
    }
    public Inventory getInventory() {
        return inventory;
    }
}
