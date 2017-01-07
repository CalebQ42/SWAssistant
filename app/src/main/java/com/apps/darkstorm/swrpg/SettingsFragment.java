package com.apps.darkstorm.swrpg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.zip.Inflater;

public class SettingsFragment extends Fragment {
    private OnSettingsInteractionListener mListener;

    public SettingsFragment() {}

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_settings, container, false);
        final SWrpg app = (SWrpg)getActivity().getApplication();
        FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.uni_fab);
        fab.hide();

        Switch launchDice = (Switch)top.findViewById(R.id.dice_launch_switch);
        launchDice.setChecked(app.prefs.getBoolean(getString(R.string.dice_key),false));
        launchDice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                app.prefs.edit().putBoolean(getString(R.string.dice_key),isChecked).apply();
            }
        });

        Switch diceColor = (Switch)top.findViewById(R.id.dice_color_switch);
        diceColor.setChecked(app.prefs.getBoolean(getString(R.string.color_dice_key),true));
        diceColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                app.prefs.edit().putBoolean(getString(R.string.color_dice_key),isChecked).apply();
            }
        });

        final TextView loc = (TextView)top.findViewById(R.id.save_location_text);
        loc.setText(app.prefs.getString(getString(R.string.local_location_key),app.defaultLoc));
        top.findViewById(R.id.save_location_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(getContext());
                build.setMessage(R.string.save_warning)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        AlertDialog.Builder build = new AlertDialog.Builder(getContext());
                        build.setTitle(R.string.save_location_text);
                        LayoutInflater inflate = getActivity().getLayoutInflater();
                        View simple = inflate.inflate(R.layout.dialog_simple_text,null);
                        final EditText edit = (EditText)simple.findViewById(R.id.editText);
                        edit.setText(app.prefs.getString(getString(R.string.local_location_key),app.defaultLoc));
                        build.setView(simple).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                app.prefs.edit().putString(getString(R.string.local_location_key),edit.getText().toString())
                                        .apply();
                                loc.setText(edit.getText());
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        build.show();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSettingsInteractionListener) {
            mListener = (OnSettingsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSettingsInteractionListener {}
}
