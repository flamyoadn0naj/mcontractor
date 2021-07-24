package com.my_pls.application.components;

import com.my_pls.application.App;

import java.util.Map;

public class Pair<M, C> {
    public static Map<String, Object> map;
    public static App.CurrUser user;

    public Pair(Map<String, Object> map, App.CurrUser user) {
        this.map = map;
        this.user = user;
    }

    public Pair() {
        this.map = null;
        this.user = new App.CurrUser("","","","");
    }

    public static Map<String,Object> fst() {
        return map;
    }

    public static App.CurrUser snd() {
        return user;
    }
}
