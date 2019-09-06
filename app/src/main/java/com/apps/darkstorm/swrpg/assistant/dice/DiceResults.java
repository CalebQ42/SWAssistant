package com.apps.darkstorm.swrpg.assistant.dice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Editable;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Weapon;

public class DiceResults {

    public int success,failure,advantage,threat,triumph,despair,lightSide,darkSide;

    public void showDialog(final Activity ac){
        DiceResults simp = simplify();
        AlertDialog.Builder build = new AlertDialog.Builder(ac);
        @SuppressLint("InflateParams") View v = ac.getLayoutInflater().inflate(R.layout.dialog_dice_results,null);
        build.setView(v);
        boolean res = false;
        if (simp.success>0){
            res = true;
            v.findViewById(R.id.success_lay).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.success_num)).setText(String.valueOf(simp.success));
        }else if (simp.failure>0){
            res = true;
            v.findViewById(R.id.success_lay).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.success_label)).setText(ac.getString(R.string.failure));
            ((TextView)v.findViewById(R.id.success_num)).setText(String.valueOf(simp.failure));
        }
        if (simp.advantage>0){
            res = true;
            v.findViewById(R.id.advantage_lay).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.advantage_num)).setText(String.valueOf(simp.advantage));
        }else if (simp.threat>0){
            res = true;
            v.findViewById(R.id.advantage_lay).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.advantage_label)).setText(ac.getString(R.string.threat_text));
            ((TextView)v.findViewById(R.id.advantage_num)).setText(String.valueOf(simp.threat));
        }
        if (simp.triumph>0){
            res = true;
            v.findViewById(R.id.triumph_lay).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.triumph_num)).setText(String.valueOf(simp.triumph));
        }
        if (simp.despair>0){
            res = true;
            v.findViewById(R.id.despair_lay).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.despair_num)).setText(String.valueOf(simp.despair));
        }
        if (simp.lightSide>0){
            res = true;
            v.findViewById(R.id.light_lay).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.light_num)).setText(String.valueOf(simp.lightSide));
        }
        if (simp.darkSide>0){
            res = true;
            v.findViewById(R.id.dark_lay).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.dark_num)).setText(String.valueOf(simp.darkSide));
        }
        if (!res){
            v.findViewById(R.id.no_res_text).setVisibility(View.VISIBLE);
        }
        build.setNegativeButton(R.string.modify_results, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showEditDialog(ac,null,null);
                dialog.cancel();
            }
        });
        build.show();
    }

    public void showEditDialog(final Activity ac, final Weapon w, final Editable c){
        AlertDialog.Builder build = new AlertDialog.Builder(ac);
        @SuppressLint("InflateParams") View v = ac.getLayoutInflater().inflate(R.layout.dialog_results_modify,null);
        build.setView(v);
        final TextView successNum = (TextView)v.findViewById(R.id.success_num);
        final TextView failureNum = (TextView)v.findViewById(R.id.failure_num);
        successNum.setText(String.valueOf(success));
        v.findViewById(R.id.success_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                success++;
                successNum.setText(String.valueOf(success));
            }
        });
        v.findViewById(R.id.success_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (success >0){
                    success--;
                    successNum.setText(String.valueOf(success));
                }else if (success == 0){
                    failure++;
                    failureNum.setText(String.valueOf(failure));
                }
            }
        });
        failureNum.setText(String.valueOf(failure));
        v.findViewById(R.id.failure_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                failure++;
                failureNum.setText(String.valueOf(failure));
            }
        });
        v.findViewById(R.id.failure_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (failure >0){
                    failure--;
                    failureNum.setText(String.valueOf(failure));
                }else if (failure == 0){
                    success++;
                    successNum.setText(String.valueOf(success));
                }
            }
        });
        final TextView advantageNum = (TextView)v.findViewById(R.id.advantage_num);
        final TextView threatNum = (TextView)v.findViewById(R.id.threat_num);
        advantageNum.setText(String.valueOf(advantage));
        v.findViewById(R.id.advantage_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advantage++;
                advantageNum.setText(String.valueOf(advantage));
            }
        });
        v.findViewById(R.id.advantage_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (advantage >0){
                    advantage--;
                    advantageNum.setText(String.valueOf(advantage));
                }else if (advantage == 0){
                    threat++;
                    threatNum.setText(String.valueOf(threat));
                }
            }
        });
        threatNum.setText(String.valueOf(threat));
        v.findViewById(R.id.threat_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threat++;
                threatNum.setText(String.valueOf(threat));
            }
        });
        v.findViewById(R.id.threat_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (threat >0){
                    threat--;
                    threatNum.setText(String.valueOf(threat));
                }else if (threat == 0){
                    advantage++;
                    advantageNum.setText(String.valueOf(advantage));
                }
            }
        });
        final TextView triumphNum = (TextView)v.findViewById(R.id.triumph_num);
        triumphNum.setText(String.valueOf(triumph));
        v.findViewById(R.id.triumph_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triumph++;
                triumphNum.setText(String.valueOf(triumph));
            }
        });
        v.findViewById(R.id.triumph_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (triumph >0){
                    triumph--;
                    triumphNum.setText(String.valueOf(triumph));
                }
            }
        });
        final TextView despairNum = (TextView)v.findViewById(R.id.despair_num);
        despairNum.setText(String.valueOf(despair));
        v.findViewById(R.id.despair_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                despair++;
                despairNum.setText(String.valueOf(despair));
            }
        });
        v.findViewById(R.id.despair_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (despair >0){
                    despair--;
                    despairNum.setText(String.valueOf(despair));
                }
            }
        });
        final TextView lightSideNum = (TextView)v.findViewById(R.id.light_side_num);
        lightSideNum.setText(String.valueOf(lightSide));
        v.findViewById(R.id.light_side_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lightSide++;
                lightSideNum.setText(String.valueOf(lightSide));
            }
        });
        v.findViewById(R.id.light_side_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lightSide >0){
                    lightSide--;
                    lightSideNum.setText(String.valueOf(lightSide));
                }
            }
        });
        final TextView darkSideNum = (TextView)v.findViewById(R.id.dark_side_num);
        darkSideNum.setText(String.valueOf(darkSide));
        v.findViewById(R.id.dark_side_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                darkSide++;
                darkSideNum.setText(String.valueOf(darkSide));
            }
        });
        v.findViewById(R.id.dark_side_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (darkSide >0){
                    darkSide--;
                    darkSideNum.setText(String.valueOf(darkSide));
                }
            }
        });
        build.setPositiveButton(R.string.re_show, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (w== null)
                    showDialog(ac);
                else
                    showWeaponDialog(ac,w,c);
                dialog.cancel();
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        build.show();
    }

    public DiceResults simplify(){
        DiceResults hold = this.clone();
        if ((success+triumph)>(failure+despair)){
            hold.success = (success + triumph) - (failure+despair);
            hold.failure = 0;
        }else{
            hold.failure = (failure+despair)-(success+triumph);
            hold.success = 0;
        }
        if (advantage>threat){
            hold.advantage = advantage-threat;
            hold.threat = 0;
        }else{
            hold.threat = threat-advantage;
            hold.advantage = 0;
        }
        return hold;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    public DiceResults clone(){
        DiceResults out = new DiceResults();
        out.success = success;
        out.failure = failure;
        out.advantage = advantage;
        out.threat = threat;
        out.triumph = triumph;
        out.despair = despair;
        out.lightSide = lightSide;
        out.darkSide = darkSide;
        return out;
    }

    public void showWeaponDialog(final Activity ac,final Weapon w, final Editable c){
        AlertDialog.Builder b = new AlertDialog.Builder(ac);
        View v = ac.getLayoutInflater().inflate(R.layout.dialog_weapon_res,null);
        b.setView(v);
        DiceResults simp = simplify();
        if(simp.failure==0 && simp.success==0 && simp.advantage==0&& simp.threat==0&& simp.triumph==0&& simp.despair==0)
            v.findViewById(R.id.no_res).setVisibility(View.VISIBLE);
        if(simp.success>0) {
            v.findViewById(R.id.dmg).setVisibility(View.VISIBLE);
            if(c instanceof Character && w.addBrawn)
                ((TextView)v.findViewById(R.id.dmg_num)).setText(String.valueOf(w.dmg+simp.success+((Character) c).charVals[0]));
            else if(c instanceof Minion && w.addBrawn)
                ((TextView)v.findViewById(R.id.dmg_num)).setText(String.valueOf(w.dmg+simp.success+((Minion) c).charVals[0]));
            else
                ((TextView)v.findViewById(R.id.dmg_num)).setText(String.valueOf(w.dmg+simp.success));
        }else
            v.findViewById(R.id.miss).setVisibility(View.VISIBLE);
        if(simp.advantage>0){
            v.findViewById(R.id.adv_thr).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.adv_thr_label)).setText(ac.getText(R.string.advantage_text));
            ((TextView)v.findViewById(R.id.adv_thr_num)).setText(String.valueOf(simp.advantage));
            if(w.crit>0 || w.chars.size()>0){
                v.findViewById(R.id.specials).setVisibility(View.VISIBLE);
                RecyclerView r = (RecyclerView)v.findViewById(R.id.recycler);
                weaponSpecialAdap adap = new weaponSpecialAdap(ac,w);
                r.setAdapter(adap);
                r.setLayoutManager(new LinearLayoutManager(ac));
            }
        }else if(simp.threat>0){
            v.findViewById(R.id.adv_thr).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.adv_thr_label)).setText(ac.getText(R.string.threat_text));
            ((TextView)v.findViewById(R.id.adv_thr_num)).setText(String.valueOf(simp.threat));
        }
        if(simp.triumph>0){
            v.findViewById(R.id.triumph).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.triumph_num)).setText(String.valueOf(simp.triumph));
        }
        if(simp.despair>0){
            v.findViewById(R.id.despair).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.despair_num)).setText(String.valueOf(simp.despair));
        }
        b.setPositiveButton(R.string.modify_results, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showEditDialog(ac,w,c);
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

    public static class weaponSpecialAdap extends RecyclerView.Adapter<weaponSpecialAdap.ViewHolder>{
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(ac.getLayoutInflater().inflate(R.layout.item_weapon_char,parent,false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            TextView num = (TextView)holder.v.findViewById(R.id.num);
            TextView name = (TextView)holder.v.findViewById(R.id.name);
            if(w.crit>0){
                if(position==0){
                    name.setText(R.string.critical_text);
                    num.setText(String.valueOf(w.crit));
                }else{
                    name.setText(w.chars.get(position-1).name);
                    num.setText(String.valueOf(w.chars.get(position-1).adv));
                }
            }else{
                name.setText(w.chars.get(position).name);
                num.setText(String.valueOf(w.chars.get(position).adv));
            }
        }

        @Override
        public int getItemCount() {
            if(w.crit>0)
                return w.chars.size()+1;
            else
                return w.chars.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            View v;
            public ViewHolder(View itemView) {
                super(itemView);
                v = itemView;
            }
        }

        Weapon w;
        Activity ac;

        public weaponSpecialAdap(Activity ac,Weapon w){
            this.w = w;
            this.ac = ac;
        }
    }
}
