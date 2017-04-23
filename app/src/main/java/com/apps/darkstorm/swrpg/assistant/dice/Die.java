package com.apps.darkstorm.swrpg.assistant.dice;

import java.util.Random;

public class Die {
    private String[] sides;
    public Die(String[] sides){
        this.sides = sides;
    }
    public String Roll(){
        int i = new Random().nextInt(sides.length);
        return sides[i];
    }
}
