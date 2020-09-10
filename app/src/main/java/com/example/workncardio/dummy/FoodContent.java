package com.example.workncardio.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FoodContent {



    /**
     * An array of sample (dummy) items.
     */
    public static final List<FoodItem> ITEMS = new ArrayList<FoodItem>();



    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<Integer, FoodItem> ITEM_MAP = new HashMap<Integer, FoodItem>();

    private static final int COUNT = 25;



    private static void addItem(FoodItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }





    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class FoodItem {
        public int id;
        public String item;
        public Float Calorie;

        public FoodItem(int id, String item, Float calorie) {
            this.id = id;
            this.item = item;
            Calorie = calorie;
        }

        @Override
        public String toString() {
            return "FoodItem{" +
                    "id=" + id +
                    ", item='" + item + '\'' +
                    ", Calorie=" + Calorie +
                    '}';
        }
    }
}