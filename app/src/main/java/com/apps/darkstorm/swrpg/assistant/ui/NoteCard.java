package com.apps.darkstorm.swrpg.assistant.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Note;
import com.apps.darkstorm.swrpg.assistant.R;

public class NoteCard {
    public View NoteCard(final View top, final Context main, final LinearLayout noteLay, final LinearLayout noteEdit,
                         final Character chara, final Note n, final FloatingActionButton fab){
        CardView tmpl = new CardView(main);
        tmpl.setCardElevation(main.getResources().getDimension(R.dimen.cardview_default_elevation));
        CardView.LayoutParams lp = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
        lp.setMargins((int)(4*main.getResources().getDisplayMetrics().density),(int)(4*main.getResources().getDisplayMetrics().density),
                (int)(4*main.getResources().getDisplayMetrics().density),(int)(4*main.getResources().getDisplayMetrics().density));
        lp.setMarginEnd((int)(4*main.getResources().getDisplayMetrics().density));
        lp.setMarginStart((int)(4*main.getResources().getDisplayMetrics().density));
        tmpl.setLayoutParams(lp);
        LinearLayout topLay = new LinearLayout(main);
        topLay.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lytlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lytlp.setMargins(5, 5, 5, 5);
        topLay.setLayoutParams(lytlp);
        LinearLayout.LayoutParams namelp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        namelp.weight = 1;
        final TextView name = new TextView(main);
        name.setTextSize(24);
        name.setLayoutParams(namelp);
        name.setText(n.title);
        topLay.addView(name);
        TypedValue outVal = new TypedValue();
        main.getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless, outVal, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            tmpl.setForeground(main.getDrawable(outVal.resourceId));
        tmpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteEdit.setVisibility(View.VISIBLE);
                noteEdit.setAlpha(0);
                noteLay.setVisibility(View.GONE);
                noteEdit.animate().alpha(1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
                ((EditText)noteEdit.findViewById(R.id.notes_title)).setText(n.title);
                ((EditText)noteEdit.findViewById(R.id.notes_text)).setText(n.note);
                fab.setImageResource(R.drawable.save);
                fab.show();
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        n.title = ((EditText)noteEdit.findViewById(R.id.notes_title)).getText().toString();
                        n.note = ((EditText)noteEdit.findViewById(R.id.notes_text)).getText().toString();
                        name.setText(n.title);
                        noteLay.setVisibility(View.VISIBLE);
                        noteLay.setAlpha(0);
                        noteEdit.setVisibility(View.GONE);
                        noteLay.animate().alpha(1.0f).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                            }
                        });
                        fab.setImageResource(R.drawable.add);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                newNote(top,main,noteLay, noteEdit, chara, fab);
                            }
                        });
                        top.requestFocus();
                    }
                });
            }
        });
        tmpl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                build.setMessage(main.getResources().getString(R.string.noted_delete));
                build.setNegativeButton(main.getResources().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int tmp = chara.nts.remove(n);
                        noteLay.removeViewAt(tmp);
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        noteEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                    fab.show();
            }
        });
        tmpl.addView(topLay);
        return tmpl;
    }
    public void newNote(final View top, final Context main, final LinearLayout noteLay, final LinearLayout noteEdit,
                        final Character chara, final FloatingActionButton fab){
        ((EditText)noteEdit.findViewById(R.id.notes_title)).setText("");
        ((EditText)noteEdit.findViewById(R.id.notes_text)).setText("");
        fab.setImageResource(R.drawable.save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note tmp = new Note();
                tmp.note = ((EditText)noteEdit.findViewById(R.id.notes_text)).getText().toString();
                tmp.title = ((EditText)noteEdit.findViewById(R.id.notes_title)).getText().toString();
                InputMethodManager imm = (InputMethodManager)main.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(noteEdit.getWindowToken(), 0);
                chara.nts.add(tmp);
                fab.setImageResource(R.drawable.add);
                noteLay.addView(NoteCard(top,main,noteLay,noteEdit,chara,chara.nts.get(chara.nts.size()-1),fab));
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newNote(top,main,noteLay, noteEdit, chara, fab);
                    }
                });
                noteLay.setVisibility(View.VISIBLE);
                noteLay.setAlpha(0);
                noteEdit.setVisibility(View.GONE);
                noteLay.animate().alpha(1.0f).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
                top.requestFocus();
            }
        });
        noteEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                    fab.show();
            }
        });
        noteEdit.setVisibility(View.VISIBLE);
        noteEdit.setAlpha(0);
        noteLay.setVisibility(View.GONE);
        noteEdit.animate().alpha(1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        fab.show();
    }
}
