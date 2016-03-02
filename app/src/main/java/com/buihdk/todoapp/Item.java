package com.buihdk.todoapp;

public class Item {
    public String item_name;
    public String due_date;

    public Item(String name, String date) {
        this.item_name = name;
        this.due_date = date;
    }

//    public static ArrayList<Item> getItems() {
//        ArrayList<Item> items = new ArrayList<Item>();
//        items.add(new Item("See a dentist", "04/11/2016"));
//        items.add(new Item("Fix my bicycle", "03/31/2016"));
//        items.add(new Item("Go to DMV", "05/15/2016"));
//        return items;
//    }
}
