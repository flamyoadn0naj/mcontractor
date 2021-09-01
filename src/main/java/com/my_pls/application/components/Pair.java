package com.my_pls.application.components;

import com.my_pls.application.App;

import java.util.HashMap;
import java.util.Map;

public class Pair {
    public static Map<String, Object> map;
    public static User user;

    public Pair() {
        this.map = new HashMap<>();
        this.user = new User();
    }

    public Pair(Map<String, Object> map, User user) {
        this.map = map;
        this.user = user;
    }

    public static Map<String,Object> fst() {
        return map;
    }

    public static User snd() {
        return user;
    }
}
