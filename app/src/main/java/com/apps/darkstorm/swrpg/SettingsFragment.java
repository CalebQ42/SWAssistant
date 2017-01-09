package com.apps.darkstorm.swrpg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsFragment extends Fragment {
    private OnSettingsInteractionListener mListener;

    public SettingsFragment() {}

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_settings, container, false);
        FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.uni_fab);
        fab.hide();

        Switch launchDice = (Switch)top.findViewById(R.id.dice_launch_switch);
        launchDice.setChecked(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.dice_key),false));
        launchDice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((SWrpg)getActivity().getApplication()).prefs.edit().putBoolean(getString(R.string.dice_key),isChecked).apply();
            }
        });

        Switch diceColor = (Switch)top.findViewById(R.id.dice_color_switch);
        diceColor.setChecked(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.color_dice_key),true));
        diceColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((SWrpg)getActivity().getApplication()).prefs.edit().putBoolean(getString(R.string.color_dice_key),isChecked).apply();
            }
        });

        final TextView loc = (TextView)top.findViewById(R.id.save_location_text);
        loc.setText(((SWrpg)getActivity().getApplication()).prefs.getString(getString(R.string.local_location_key),((SWrpg)getActivity().getApplication()).defaultLoc));
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
                        final EditText edit = (EditText)simple.findViewById(R.id.edit_val);
                        edit.setText(((SWrpg)getActivity().getApplication()).prefs.getString(getString(R.string.local_location_key),((SWrpg)getActivity().getApplication()).defaultLoc));
                        build.setView(simple).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                ((SWrpg)getActivity().getApplication()).prefs.edit().putString(getString(R.string.local_location_key),edit.getText().toString())
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
        Switch cloud = (Switch)top.findViewById(R.id.cloud_switch);
        final Switch sync = (Switch)top.findViewById(R.id.sync_switch);
        cloud.setChecked(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false));
        cloud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((SWrpg)getActivity().getApplication()).prefs.edit().putBoolean(getString(R.string.google_drive_key),isChecked).apply();
                if(isChecked){
                    sync.setVisibility(View.VISIBLE);
                    ((MainActivity)getActivity()).gacMaker();
                }else{
                    sync.setVisibility(View.GONE);
                }
            }
        });
        if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
            sync.setVisibility(View.VISIBLE);
        }
        sync.setChecked(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.sync_key),true));
        sync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((SWrpg)getActivity().getApplication()).prefs.edit().putBoolean(getString(R.string.sync_key),isChecked).apply();
            }
        });
        sync.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(getContext());
                build.setMessage(R.string.sync_desc);
                build.show();
                return true;
            }
        });
        Switch ads = (Switch)top.findViewById(R.id.ads_switch);
        ads.setChecked(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.ads_key),true));
        ads.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((SWrpg)getActivity().getApplication()).prefs.edit().putBoolean(getString(R.string.ads_key),isChecked).apply();
            }
        });
        ads.setEnabled(false);
        Switch light = (Switch)top.findViewById(R.id.theme_switch);
        light.setChecked(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.light_side_key),false));
        light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((SWrpg)getActivity().getApplication()).prefs.edit().putBoolean(getString(R.string.light_side_key),isChecked).apply();
                if (isChecked){
                    Toast.makeText(getActivity(),R.string.light_side_toast,Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(),R.string.dark_side_toast, Toast.LENGTH_LONG).show();
                }
                TaskStackBuilder.create(getActivity())
                        .addNextIntent(getActivity().getIntent())
                        .startActivities();
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
