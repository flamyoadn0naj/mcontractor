package com.my_pls;

import java.util.ArrayList;
import java.util.List;

public class Lesson {
    int id;
    public String name;
    public String requirements;
    public List<String> materials;

    public Lesson(int id, String name, String requirements){
        this.id = id;
        this.name = name;
        this.requirements = requirements;
        this.materials = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRequirements() {
        return requirements;
    }

    public List<String> getMaterials() {
        return materials;
    }
}
