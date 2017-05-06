package com.apps.darkstorm.swrpg.assistant;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsFragment extends Fragment {
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((FloatingActionButton)getActivity().findViewById(R.id.fab)).hide();
        Switch diceLaunch = (Switch)view.findViewById(R.id.dice_launch_switch);
        diceLaunch.setChecked(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.dice_key),false));
        diceLaunch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((SWrpg)getActivity().getApplication()).prefs.edit().putBoolean(getString(R.string.dice_key),isChecked).apply();
            }
        });
        Switch diceColor = (Switch)view.findViewById(R.id.dice_color_switch);
        diceColor.setChecked(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.color_dice_key),true));
        diceColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((SWrpg)getActivity().getApplication()).prefs.edit().putBoolean(getString(R.string.color_dice_key),isChecked).apply();
            }
        });
        final TextView saveLoc = (TextView)view.findViewById(R.id.save_location_text);
        saveLoc.setText(((SWrpg)getActivity().getApplication()).prefs.getString(getString(R.string.local_location_key),((SWrpg)getActivity().getApplication()).defaultLoc));
        view.findViewById(R.id.save_location_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
                build.setMessage(R.string.save_warning);
                build.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                        final View cont = getActivity().getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                        b.setView(cont);
                        final TextInputLayout til = (TextInputLayout) cont.findViewById(R.id.edit_layout);
                        til.setHint(getString(R.string.save_location_text));
                        ((EditText)cont.findViewById(R.id.edit_text)).setText(((SWrpg)getActivity().getApplication()).prefs.getString(getString(R.string.local_location_key),
                                ((SWrpg)getActivity().getApplication()).defaultLoc));
                        b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((SWrpg)getActivity().getApplication()).prefs.edit().putString(getString(R.string.local_location_key),((EditText)cont.findViewById(R.id.edit_text))
                                        .getText().toString()).apply();
                                saveLoc.setText(((EditText)cont.findViewById(R.id.edit_text)).getText());
                                dialog.cancel();
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        b.show();
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
        Switch sync = (Switch)view.findViewById(R.id.sync_switch);
        Switch cloud = (Switch)view.findViewById(R.id.cloud_switch);
        cloud.setChecked(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false));
        cloud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ApplySharedPref")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //put stuff here :)
            }
        });
        if(cloud.isChecked()){
            sync.setVisibility(View.VISIBLE);
        }else{
            sync.setVisibility(View.GONE);
        }
        Switch ads = (Switch)view.findViewById(R.id.ads_switch);
        ads.setChecked(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.ads_key),true));
        ads.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((SWrpg)getActivity().getApplication()).prefs.edit().putBoolean(getString(R.string.ads_key),isChecked).apply();
            }
        });
        Switch t = (Switch)view.findViewById(R.id.theme_switch);
        t.setChecked(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.light_side_key),false));
        t.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((SWrpg)getActivity().getApplication()).prefs.edit().putBoolean(getString(R.string.light_side_key),isChecked).apply();
                getActivity().recreate();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnSettingInterfactionInterface)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnSettingInterfactionInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnSettingInterfactionInterface {}
}
