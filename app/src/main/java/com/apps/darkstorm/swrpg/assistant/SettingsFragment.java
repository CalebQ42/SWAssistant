package com.apps.darkstorm.swrpg.assistant;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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

import com.apps.darkstorm.swrpg.assistant.drive.Load;
import com.apps.darkstorm.swrpg.assistant.local.LoadLocal;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.sw.Vehicle;

import java.util.ArrayList;

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
        final Switch sync = (Switch)view.findViewById(R.id.sync_switch);
        sync.setChecked(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.sync_key),true));
        sync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((SWrpg)getActivity().getApplication()).prefs.edit().putBoolean(getString(R.string.sync_key),isChecked).apply();
            }
        });
        final Switch cloud = (Switch)view.findViewById(R.id.cloud_switch);
        cloud.setChecked(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false));
        cloud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ApplySharedPref")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((SWrpg)getActivity().getApplication()).prefs.edit().putBoolean(getString(R.string.google_drive_key),isChecked).apply();
                if(isChecked){
                    AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            ((MainDrawer)getActivity()).gacMaker();
                        }

                        @Override
                        protected Void doInBackground(Void... params) {
                            while(!((SWrpg)getActivity().getApplication()).driveFail&&((SWrpg)getActivity().getApplication()).charsFold==null){
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            if(((SWrpg)getActivity().getApplication()).driveFail){
                                cloud.setChecked(false);
                                return;
                            }
                            final Character[] characters = LoadLocal.characters(getActivity());
                            final Minion[] minions = LoadLocal.minions(getActivity());
                            final Vehicle[] vehicles = LoadLocal.vehicles(getActivity());
                            if(characters.length==0&&minions.length==0&&vehicles.length==0)
                                return;
                            AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                            b.setMessage(R.string.upload_question);
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                                    View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_loading,null);
                                    b.setView(v);
                                    TextView msg = (TextView)v.findViewById(R.id.loading_message);
                                    msg.setText(R.string.uploading);
                                    b.setCancelable(false);
                                    final AlertDialog ad = b.show();
                                    AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            final boolean[] finished =new boolean[]{false,false,false};
                                            final Load.Characters lc = new Load.Characters();
                                            lc.setOnFinish(new Load.onFinish() {
                                                @Override
                                                public void finish() {
                                                    ArrayList<Integer> IDs = new ArrayList<>();
                                                    for (Character c : lc.characters) {
                                                        IDs.add(c.ID);
                                                    }
                                                    for(Character c:characters){
                                                        int ID = 0;
                                                        while (IDs.contains(ID)) {
                                                            ID++;
                                                        }
                                                        c.ID = ID;
                                                        c.cloudSave(((SWrpg)getActivity().getApplication()).gac,c.getFileId(getActivity()),false);
                                                        IDs.add(ID);
                                                    }
                                                    finished[0] = true;
                                                }
                                            });
                                            lc.load(getActivity());
                                            final Load.Minions lm = new Load.Minions();
                                            lm.setOnFinish(new Load.onFinish() {
                                                @Override
                                                public void finish() {
                                                    ArrayList<Integer> IDs = new ArrayList<>();
                                                    for (Minion c : lm.minions) {
                                                        IDs.add(c.ID);
                                                    }
                                                    for(Minion c:minions){
                                                        int ID = 0;
                                                        while (IDs.contains(ID)) {
                                                            ID++;
                                                        }
                                                        c.ID = ID;
                                                        c.cloudSave(((SWrpg)getActivity().getApplication()).gac,c.getFileId(getActivity()),false);
                                                        IDs.add(ID);
                                                    }
                                                    finished[1] = true;
                                                }
                                            });
                                            lm.load(getActivity());
                                            final Load.Vehicles lv = new Load.Vehicles();
                                            lv.setOnFinish(new Load.onFinish() {
                                                @Override
                                                public void finish() {
                                                    ArrayList<Integer> IDs = new ArrayList<>();
                                                    for (Vehicle c : lv.vehicles) {
                                                        IDs.add(c.ID);
                                                    }
                                                    for(Vehicle c:vehicles){
                                                        int ID = 0;
                                                        while (IDs.contains(ID)) {
                                                            ID++;
                                                        }
                                                        c.ID = ID;
                                                        c.cloudSave(((SWrpg)getActivity().getApplication()).gac,c.getFileId(getActivity()),false);
                                                        IDs.add(ID);
                                                    }
                                                    finished[2] = true;
                                                }
                                            });
                                            lv.load(getActivity());
                                            while(!finished[0]||!finished[1]||!finished[2]){
                                                try {
                                                    Thread.sleep(500);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            return null;
                                        }
                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            ad.cancel();
                                        }
                                    };
                                    async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                    };
                    asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                if(cloud.isChecked()){
                    sync.setVisibility(View.VISIBLE);
                }else{
                    sync.setVisibility(View.GONE);
                }
            }
        });
        sync.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setMessage(R.string.sync_desc);
                b.show();
                return true;
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
