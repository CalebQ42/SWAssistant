package com.apps.darkstorm.swrpg.assistant.dice;

import java.util.ArrayList;

public class DiceHolder {

    private static final String success = "success";
    private static final String failure = "failure";
    private static final String advantage = "advantage";
    private static final String threat = "threat";
    private static final String triumph = "triumph";
    private static final String despair = "despair";
    private static final String lightSide = "LT";
    private static final String darkSide = "DK";

    private static final String[] abilitySides = {"", advantage + advantage, success, advantage,
            success + success, success + advantage,advantage, success};
    private static final String[] proficiencySides = {"", success + success, success + advantage,
            success + success, success + advantage, success, success + advantage, success, triumph, advantage + advantage, advantage,
            advantage + advantage};
    private static final String[] boostSides = {"", "", advantage, success + advantage, advantage + advantage, success};
    private static final String[] difficultySides = {failure, threat + threat, failure + failure, "", failure, failure + threat, threat, failure};
    private static final String[] challengeSides = {"", threat + threat, despair, threat + threat, threat, failure + threat,
            threat, failure + threat, failure, failure + failure, failure, failure + failure};
    private static final String[] setbackSides = {"", "", threat, threat, failure, failure};
    private static final String[] forceSides = {darkSide + darkSide, darkSide, lightSide, darkSide, lightSide, darkSide,
            lightSide + lightSide, darkSide, lightSide + lightSide, darkSide, lightSide + lightSide, darkSide};

    public int ability, proficiency, boost, difficulty ,challenge, setback, force;

    public DiceResults roll(){
        ArrayList<String> reses = new ArrayList<>();
        for (int i = 0;i<ability;i++){
            reses.add(new Die(abilitySides).Roll());
        }
        for (int i = 0;i<proficiency;i++){
            reses.add(new Die(proficiencySides).Roll());
        }
        for (int i = 0;i<boost;i++){
            reses.add(new Die(boostSides).Roll());
        }
        for (int i = 0;i<difficulty;i++){
            reses.add(new Die(difficultySides).Roll());
        }
        for (int i = 0;i<challenge;i++){
            reses.add(new Die(challengeSides).Roll());
        }
        for (int i = 0;i<setback;i++){
            reses.add(new Die(setbackSides).Roll());
        }
        for (int i = 0;i<force;i++){
            reses.add(new Die(forceSides).Roll());
        }
        DiceResults dr = new DiceResults();
        for (String res:reses){
            while (res.contains(success)){
                res = res.replaceFirst(success,"");
                dr.success++;
            }
            while (res.contains(failure)){
                res = res.replaceFirst(failure,"");
                dr.failure++;
            }
            while (res.contains(advantage)){
                res = res.replaceFirst(advantage,"");
                dr.advantage++;
            }
            while (res.contains(threat)){
                res = res.replaceFirst(threat,"");
                dr.threat++;
            }
            while (res.contains(triumph)){
                res = res.replaceFirst(triumph,"");
                dr.triumph++;
                dr.success++;
            }
            while (res.contains(despair)){
                res = res.replaceFirst(despair,"");
                dr.despair++;
                dr.failure++;
            }
            while (res.contains(lightSide)){
                res = res.replaceFirst(lightSide,"");
                dr.lightSide++;
            }
            while (res.contains(darkSide)){
                res = res.replaceFirst(darkSide,"");
                dr.darkSide++;
            }
        }
        return dr;
    }

    public void reset(){
        ability = 0;
        proficiency = 0;
        boost = 0;
        difficulty = 0;
        challenge = 0;
        setback = 0;
        force = 0;
    }
}
