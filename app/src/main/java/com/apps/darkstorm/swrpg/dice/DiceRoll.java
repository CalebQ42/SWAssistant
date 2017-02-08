package com.apps.darkstorm.swrpg.dice;

import java.util.ArrayList;

public class DiceRoll {
    private final String Success = "Success";
    private final String Failure = "Failure";
    private final String Threat = "Threat";
    private final String Advantage = "Advantage";
    private final String Triumph = "Triumph";
    private final String Despair = "Despair";
    private final String LTPoint = "Light Side Point";
    private final String DKPoint = "Dark Side Point";
    private final String[] abiDie = {"", Advantage + Advantage, Success, Advantage, Success + Success, Success + Advantage, Advantage, Success};
    private final String[] profDie = {"", Success + Success, Success + Advantage, Success + Success, Success + Advantage, Success, Success + Advantage, Success, Triumph, Advantage + Advantage, Advantage, Advantage + Advantage};
    private final String[] difDie = {Failure, Threat + Threat, Failure + Failure, "", Failure, Failure + Threat, Threat, Failure};
    private final String[] chalDie = {"", Threat + Threat, Despair, Threat + Threat, Threat, Failure + Threat, Threat, Failure + Threat, Failure, Failure + Failure, Failure, Failure + Failure};
    private final String[] bstDie = {"", "", Advantage, Success + Advantage, Advantage + Advantage, Success};
    private final String[] stbkDie = {"", "", Threat, Threat, Failure, Failure};
    private final String[] frcDie = {DKPoint + DKPoint, DKPoint, LTPoint, DKPoint, LTPoint, DKPoint, LTPoint + LTPoint, DKPoint, LTPoint + LTPoint, DKPoint, LTPoint + LTPoint, DKPoint};
    public DiceResults rollDice(int abi,int prof,int dif,int chal,int bst,int stbk,int frc){
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0;i<abi;i++)
            res.add(abiDie[(int)(Math.random()*abiDie.length)]);
        for (int i = 0;i<prof;i++)
            res.add(profDie[(int)(Math.random()*profDie.length)]);
        for (int i = 0;i<dif;i++)
            res.add(difDie[(int)(Math.random()*difDie.length)]);
        for (int i = 0;i<chal;i++)
            res.add(chalDie[(int)(Math.random()*chalDie.length)]);
        for (int i = 0;i<bst;i++)
            res.add(bstDie[(int)(Math.random()*bstDie.length)]);
        for (int i = 0;i<stbk;i++)
            res.add(stbkDie[(int)(Math.random()*stbkDie.length)]);
        for (int i = 0;i<frc;i++)
            res.add(frcDie[(int)(Math.random()*frcDie.length)]);
        DiceResults out = new DiceResults();
        for (int i = 0;i<res.size();i++){
            switch (res.get(i)){
                case Success:
                    out.suc++;
                    break;
                case Advantage:
                    out.adv++;
                    break;
                case Advantage + Advantage:
                    out.adv += 2;
                    break;
                case Success+Success:
                    out.suc += 2;
                    break;
                case Success + Advantage:
                    out.suc++;
                    out.adv++;
                    break;
                case Failure:
                    out.fail++;
                    break;
                case Threat:
                    out.thr++;
                    break;
                case Failure + Failure:
                    out.fail += 2;
                    break;
                case Threat + Threat:
                    out.thr += 2;
                    break;
                case Failure + Threat:
                    out.fail++;
                    out.thr++;
                    break;
                case Triumph:
                    out.suc++;
                    out.tri++;
                    break;
                case Despair:
                    out.fail++;
                    out.desp++;
                    break;
                case LTPoint:
                    out.lt++;
                    break;
                case DKPoint:
                    out.dk++;
                    break;
                case LTPoint + LTPoint:
                    out.lt += 2;
                    break;
                case DKPoint + DKPoint:
                    out.dk += 2;
                    break;
            }
        }
        return out;
    }
    public DiceResults rollDice(DiceNumHolder dice){
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0;i<dice.ability;i++)
            res.add(abiDie[(int)(Math.random()*abiDie.length)]);
        for (int i = 0;i<dice.proficiency;i++)
            res.add(profDie[(int)(Math.random()*profDie.length)]);
        for (int i = 0;i<dice.difficulty;i++)
            res.add(difDie[(int)(Math.random()*difDie.length)]);
        for (int i = 0;i<dice.challenge;i++)
            res.add(chalDie[(int)(Math.random()*chalDie.length)]);
        for (int i = 0;i<dice.boost;i++)
            res.add(bstDie[(int)(Math.random()*bstDie.length)]);
        for (int i = 0;i<dice.setback;i++)
            res.add(stbkDie[(int)(Math.random()*stbkDie.length)]);
        for (int i = 0;i<dice.force;i++)
            res.add(frcDie[(int)(Math.random()*frcDie.length)]);
        DiceResults out = new DiceResults();
        for (int i = 0;i<res.size();i++){
            switch (res.get(i)){
                case Success:
                    out.suc++;
                    break;
                case Advantage:
                    out.adv++;
                    break;
                case Advantage + Advantage:
                    out.adv += 2;
                    break;
                case Success+Success:
                    out.suc += 2;
                    break;
                case Success + Advantage:
                    out.suc++;
                    out.adv++;
                    break;
                case Failure:
                    out.fail++;
                    break;
                case Threat:
                    out.thr++;
                    break;
                case Failure + Failure:
                    out.fail += 2;
                    break;
                case Threat + Threat:
                    out.thr += 2;
                    break;
                case Failure + Threat:
                    out.fail++;
                    out.thr++;
                    break;
                case Triumph:
                    out.suc++;
                    out.tri++;
                    break;
                case Despair:
                    out.fail++;
                    out.desp++;
                    break;
                case LTPoint:
                    out.lt++;
                    break;
                case DKPoint:
                    out.dk++;
                    break;
                case LTPoint + LTPoint:
                    out.lt += 2;
                    break;
                case DKPoint + DKPoint:
                    out.dk += 2;
                    break;
            }
        }
        return out;
    }
}
