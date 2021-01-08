package com.example.spaceshot.Gioco.Object;

public enum BonusType {

    Double("Double"),
    Invincible("Invincible"),
    Slow("Slow");

    String name;

    BonusType(String name)
    {
        this.name = name;
    }

    public String getValue()
    {
        return name;
    }
}
