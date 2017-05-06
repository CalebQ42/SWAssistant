package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.DiceRollFragment;
import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.dice.DiceHolder;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Editable;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;

import java.util.ArrayList;
import java.util.Arrays;

public class Skills{
    Skill[] sk;
    public Skills(){
        sk = new Skill[0];
    }
    public void add(Skill s){
        sk = Arrays.copyOf(sk,sk.length+1);
        sk[sk.length-1] = s;
    }
    public int remove(Skill s){
        int i = -1;
        for (int j =0;j<sk.length;j++){
            if (sk[j].equals(s)){
                i = j;
                break;
            }
        }
        if (i != -1) {
            Skill[] newSk = new Skill[sk.length - 1];
            for (int j = 0; j < i; j++)
                newSk[j] = sk[j];
            for (int j = i + 1; j < sk.length; j++)
                newSk[j - 1] = sk[j];
            sk = newSk;
        }
        return i;
    }
    public Skill get(int i){
        return sk[i];
    }
    public int size(){
        return sk.length;
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        for (Skill k:sk){
            tmp.add(k.serialObject());
        }
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        ArrayList<Skill> out = new ArrayList<>();
        for (Object o:tmp){
            Skill s = new Skill();
            s.loadFromObject(o);
            out.add(s);
        }
        sk = out.toArray(sk);
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Skills))
            return false;
        Skills in = (Skills)obj;
        if(in.sk.length != sk.length)
            return false;
        for (int i = 0;i<sk.length;i++){
            if (!in.sk[i].equals(sk[i]))
                return false;
        }
        return true;
    }
    public Skills clone(){
        Skills out = new Skills();
        out.sk = new Skill[sk.length];
        for (int i = 0;i<sk.length;i++)
            out.sk[i] = sk[i].clone();
        return out;
    }

    public static class SkillsAdap extends RecyclerView.Adapter<SkillsAdap.ViewHolder>{
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(ac.getLayoutInflater().inflate(R.layout.item_skill,parent,false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if(c instanceof Character) {
                final Skills skls = ((Character)c).skills;
                if (skls.get(position).career)
                    ((TextView) holder.v.findViewById(R.id.skill_name)).setText("*" + skls.get(position).name);
                else
                    ((TextView) holder.v.findViewById(R.id.skill_name)).setText(" " + skls.get(position).name);
                ((TextView) holder.v.findViewById(R.id.skill_value)).setText(String.valueOf(skls.get(position).val));
                holder.v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Skill.editSkill(ac, c, holder.getAdapterPosition(), false, new Skill.onSave() {
                            public void save() {
                                SkillsAdap.this.notifyItemChanged(holder.getAdapterPosition());
                            }

                            public void delete() {
                                int ind = skls.remove(skls.get(holder.getAdapterPosition()));
                                SkillsAdap.this.notifyItemRemoved(ind);
                            }

                            public void cancel() {
                            }
                        });
                        return true;
                    }
                });
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder b = new AlertDialog.Builder(ac);
                        final View view = ac.getLayoutInflater().inflate(R.layout.fragment_dice_roll, null);
                        b.setView(view);
                        view.findViewById(R.id.instant_recycler).setVisibility(View.GONE);
                        view.findViewById(R.id.instant_dice_text).setVisibility(View.GONE);
                        final DiceHolder dh = new DiceHolder();
                        if (((Character)c).charVals[skls.get(holder.getAdapterPosition()).baseChar] > skls.get(holder.getAdapterPosition()).val) {
                            dh.ability = ((Character)c).charVals[skls.get(holder.getAdapterPosition()).baseChar] - skls.get(holder.getAdapterPosition()).val;
                            dh.proficiency = skls.get(holder.getAdapterPosition()).val;
                        } else {
                            dh.ability = skls.get(holder.getAdapterPosition()).val - ((Character)c).charVals[skls.get(holder.getAdapterPosition()).baseChar];
                            dh.proficiency = ((Character)c).charVals[skls.get(holder.getAdapterPosition()).baseChar];
                        }
                        final DiceRollFragment.DiceList dl = new DiceRollFragment.DiceList(ac, dh);
                        RecyclerView r = (RecyclerView) view.findViewById(R.id.dice_recycler);
                        r.setAdapter(dl);
                        r.setLayoutManager(new LinearLayoutManager(ac));
                        b.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dh.roll().showDialog(ac);
                                dialog.cancel();
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        b.show();
                    }
                });
            }else if(c instanceof Minion){
                final Skills skls = ((Minion)c).skills;
                if (skls.get(position).career)
                    ((TextView) holder.v.findViewById(R.id.skill_name)).setText("*" + skls.get(position).name);
                else
                    ((TextView) holder.v.findViewById(R.id.skill_name)).setText(" " + skls.get(position).name);
                ((TextView) holder.v.findViewById(R.id.skill_value)).setText(String.valueOf(skls.get(position).val));
                holder.v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Skill.editSkill(ac, c, holder.getAdapterPosition(), false, new Skill.onSave() {
                            public void save() {
                                SkillsAdap.this.notifyItemChanged(holder.getAdapterPosition());
                            }

                            public void delete() {
                                int ind = skls.remove(skls.get(holder.getAdapterPosition()));
                                SkillsAdap.this.notifyItemRemoved(ind);
                            }

                            public void cancel() {
                            }
                        });
                        return true;
                    }
                });
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder b = new AlertDialog.Builder(ac);
                        final View view = ac.getLayoutInflater().inflate(R.layout.fragment_dice_roll, null);
                        b.setView(view);
                        view.findViewById(R.id.instant_recycler).setVisibility(View.GONE);
                        view.findViewById(R.id.instant_dice_text).setVisibility(View.GONE);
                        final DiceHolder dh = new DiceHolder();
                        if (((Minion)c).charVals[skls.get(holder.getAdapterPosition()).baseChar] > skls.get(holder.getAdapterPosition()).val) {
                            dh.ability = ((Minion)c).charVals[skls.get(holder.getAdapterPosition()).baseChar] - skls.get(holder.getAdapterPosition()).val;
                            dh.proficiency = skls.get(holder.getAdapterPosition()).val;
                        } else {
                            dh.ability = skls.get(holder.getAdapterPosition()).val - ((Minion)c).charVals[skls.get(holder.getAdapterPosition()).baseChar];
                            dh.proficiency = ((Minion)c).charVals[skls.get(holder.getAdapterPosition()).baseChar];
                        }
                        final DiceRollFragment.DiceList dl = new DiceRollFragment.DiceList(ac, dh);
                        RecyclerView r = (RecyclerView) view.findViewById(R.id.dice_recycler);
                        r.setAdapter(dl);
                        r.setLayoutManager(new LinearLayoutManager(ac));
                        b.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dh.roll().showDialog(ac);
                                dialog.cancel();
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        b.show();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if(c instanceof Character)
                return ((Character)c).skills.size();
            else if(c instanceof Minion)
                return ((Minion)c).skills.size();
            else
                return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            View v;
            ViewHolder(View v){
                super(v);
                this.v = v;
            }
        }
        Editable c;
        Activity ac;
        public SkillsAdap(Editable c, Activity ac){
            this.c = c;
            this.ac = ac;
        }
    }
}
