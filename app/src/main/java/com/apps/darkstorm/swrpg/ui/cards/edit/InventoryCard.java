package com.apps.darkstorm.swrpg.ui.cards.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.sw.Character;
import com.apps.darkstorm.swrpg.sw.Minion;
import com.apps.darkstorm.swrpg.sw.stuff.Item;
import com.apps.darkstorm.swrpg.ui.ItemLayout;

public class InventoryCard {
    public static View getCard(final Activity main, ViewGroup root, final Character chara){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_inventory,root);
        final TextView creds = (TextView)top.findViewById(R.id.credits_val);
        creds.setText(String.valueOf(chara.credits));
        top.findViewById(R.id.credits_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.credits_text);
                final EditText val = (EditText)dia.findViewById(R.id.editText);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.credits));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!val.getText().toString().equals("")){
                            chara.credits = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.credits = 0;
                        }
                        creds.setText(String.valueOf(chara.credits));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        final TextView encum = (TextView)top.findViewById(R.id.encum_val);
        encum.setText(String.valueOf(chara.encumCapacity));
        top.findViewById(R.id.encum_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.encumbrance_capacity_text);
                final EditText val = (EditText)dia.findViewById(R.id.editText);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.encumCapacity));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!val.getText().toString().equals("")){
                            chara.encumCapacity = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.encumCapacity = 0;
                        }
                        encum.setText(String.valueOf(chara.encumCapacity));
                        if (chara.isOverEncum())
                            top.findViewById(R.id.encum_warning).setVisibility(View.VISIBLE);
                        else
                            top.findViewById(R.id.encum_warning).setVisibility(View.GONE);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        if (chara.isOverEncum())
            top.findViewById(R.id.encum_warning).setVisibility(View.VISIBLE);
        else
            top.findViewById(R.id.encum_warning).setVisibility(View.GONE);
        final LinearLayout invLay = (LinearLayout)top.findViewById(R.id.inventory_layout);
        for (int i = 0;i<chara.inv.size();i++)
            invLay.addView(new ItemLayout().ItemLayout(top,main,invLay,chara,chara.inv.get(i)));
        top.findViewById(R.id.inventory_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Item tmp = new Item();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_item_edit,null);
                build.setView(dia);
                final EditText item = (EditText)dia.findViewById(R.id.item_name);
                item.setText(tmp.name);
                final EditText desc = (EditText)dia.findViewById(R.id.item_desc);
                desc.setText(tmp.desc);
                final EditText num = (EditText)dia.findViewById(R.id.item_num);
                num.setText(String.valueOf(tmp.count));
                final EditText encum = (EditText)dia.findViewById(R.id.encum_num);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tmp.name = item.getText().toString();
                        tmp.desc = desc.getText().toString();
                        if (!num.getText().toString().equals(""))
                            tmp.count = Integer.parseInt(num.getText().toString());
                        else
                            tmp.count = 0;
                        if (!encum.getText().toString().equals(""))
                            tmp.encum = Integer.parseInt(encum.getText().toString());
                        else
                            tmp.encum = 0;
                        chara.inv.add(tmp);
                        invLay.addView(new ItemLayout().ItemLayout(top,main,invLay,chara,chara.inv.get(chara.inv.size()-1)));
                        if (chara.isOverEncum())
                            top.findViewById(R.id.encum_warning).setVisibility(View.VISIBLE);
                        else
                            top.findViewById(R.id.encum_warning).setVisibility(View.GONE);
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
        });
        return top;
    }
    public static View getCard(final Activity main, ViewGroup root, final Minion minion){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_inventory,root);
        top.findViewById(R.id.credits_layout).setVisibility(View.GONE);
        top.findViewById(R.id.encum_layout).setVisibility(View.GONE);
        final LinearLayout invLay = (LinearLayout)top.findViewById(R.id.inventory_layout);
        for (int i = 0;i<minion.inv.size();i++)
            invLay.addView(new ItemLayout().ItemLayout(main,invLay,minion,minion.inv.get(i)));
        top.findViewById(R.id.inventory_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Item tmp = new Item();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_item_edit,null);
                build.setView(dia);
                final EditText item = (EditText)dia.findViewById(R.id.item_name);
                item.setText(tmp.name);
                final EditText desc = (EditText)dia.findViewById(R.id.item_desc);
                desc.setText(tmp.desc);
                final EditText num = (EditText)dia.findViewById(R.id.item_num);
                num.setText(String.valueOf(tmp.count));
                final EditText encum = (EditText)dia.findViewById(R.id.encum_num);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tmp.name = item.getText().toString();
                        tmp.desc = desc.getText().toString();
                        if (!num.getText().toString().equals(""))
                            tmp.count = Integer.parseInt(num.getText().toString());
                        else
                            tmp.count = 0;
                        if (!encum.getText().toString().equals(""))
                            tmp.encum = Integer.parseInt(encum.getText().toString());
                        else
                            tmp.encum = 0;
                        minion.inv.add(tmp);
                        invLay.addView(new ItemLayout().ItemLayout(main,invLay,minion,minion.inv.get(minion.inv.size()-1)));
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
        });
        return top;
    }
}
