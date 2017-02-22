package com.apps.darkstorm.swrpg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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

import com.apps.darkstorm.swrpg.load.DriveLoadCharacters;
import com.apps.darkstorm.swrpg.load.DriveLoadMinions;
import com.apps.darkstorm.swrpg.load.DriveLoadVehicles;
import com.apps.darkstorm.swrpg.load.LoadCharacters;
import com.apps.darkstorm.swrpg.load.LoadMinions;
import com.apps.darkstorm.swrpg.load.LoadVehicles;
import com.apps.darkstorm.swrpg.sw.Character;
import com.apps.darkstorm.swrpg.sw.Minion;
import com.apps.darkstorm.swrpg.sw.Vehicle;

import java.util.ArrayList;

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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    public void onViewCreated(final View top,Bundle saved){
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
                ((SWrpg)getActivity().getApplication()).prefs.edit().putBoolean(getString(R.string.google_drive_key),isChecked).commit();
                if(isChecked){
                    sync.setVisibility(View.VISIBLE);
                    AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
                    build.setMessage(R.string.upload_question);
                    build.setPositiveButton(R.string.upload_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
                            View dia = getActivity().getLayoutInflater().inflate(R.layout.dialog_loading,null);
                            build.setView(dia);
                            ((TextView)dia.findViewById(R.id.loading_text)).setText(R.string.uploading);
                            build.setCancelable(false);
                            final AlertDialog alertDialog = build.create();
                            alertDialog.show();
                            if(((SWrpg)getActivity().getApplication()).gac==null ||!((SWrpg)getActivity().getApplication()).gac.isConnected())
                                ((MainActivity)getActivity()).gacMaker();
                            AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                                boolean fail = false;
                                @Override
                                protected Void doInBackground(Void... params) {
                                    while(((SWrpg)getActivity().getApplication()).vehicFold==null
                                            && !((SWrpg) getActivity().getApplication()).driveFail){
                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if(((SWrpg) getActivity().getApplication()).driveFail){
                                        fail = true;
                                        return null;
                                    }
                                    DriveLoadCharacters dlc = new DriveLoadCharacters(getActivity());
                                    LoadCharacters lc = new LoadCharacters(getActivity());
                                    ArrayList<Integer> taken = new ArrayList<>();
                                    for(Character chara:dlc.characters)
                                        taken.add(chara.ID);
                                    for (int i = 0;lc.characters.size()!=0;i++){
                                        if(!taken.contains(i)){
                                            lc.characters.get(0).ID = i;
                                            lc.characters.get(0).cloudSave(((SWrpg)getActivity().getApplication()).gac,
                                                    lc.characters.get(0).getFileId(getActivity()),false);
                                            lc.characters.remove(0);
                                        }
                                    }
                                    DriveLoadMinions dlm = new DriveLoadMinions(getActivity());
                                    LoadMinions lm = new LoadMinions(getActivity());
                                    taken = new ArrayList<>();
                                    for(Minion chara:dlm.minions)
                                        taken.add(chara.ID);
                                    for (int i = 0;lm.minions.size()!=0;i++){
                                        if(!taken.contains(i)){
                                            lm.minions.get(0).ID = i;
                                            lm.minions.get(0).cloudSave(((SWrpg)getActivity().getApplication()).gac,
                                                    lm.minions.get(0).getFileId(getActivity()),false);
                                            lm.minions.remove(0);
                                        }
                                    }
                                    DriveLoadVehicles dlv = new DriveLoadVehicles(getActivity());
                                    LoadVehicles lv = new LoadVehicles(getActivity());
                                    taken = new ArrayList<>();
                                    for(Vehicle chara:dlv.vehicles)
                                        taken.add(chara.ID);
                                    for (int i = 0;lv.vehicles.size()!=0;i++){
                                        if(!taken.contains(i)){
                                            lv.vehicles.get(0).ID = i;
                                            lv.vehicles.get(0).cloudSave(((SWrpg)getActivity().getApplication()).gac,
                                                    lv.vehicles.get(0).getFileId(getActivity()),false);
                                            lv.vehicles.remove(0);
                                        }
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    alertDialog.dismiss();
                                    if(fail){
                                        Toast.makeText(getActivity(),R.string.failure,Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(getActivity(),R.string.success,Toast.LENGTH_LONG).show();
                                    }
                                }
                            };
                            async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();

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
