package com.cleveroad.testrecycler.models;

public class AthleticModel {

    private final String name;
    private final Country country;
    private final long score;

    public AthleticModel(String name, Country country, long score) {
        this.name = name;
        this.country = country;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public Country getCountry() {
        return country;
    }

    public long getScore() {
        return score;
    }
}
