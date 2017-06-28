package com.apps.darkstorm.swrpg.assistant;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.drive.Load;
import com.apps.darkstorm.swrpg.assistant.local.LoadLocal;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Editable;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.sw.Vehicle;

import org.json.JSONException;
import org.json.JSONObject;

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
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.settings);
        ((FloatingActionButton)getActivity().findViewById(R.id.fab)).hide();
        Switch diceLaunch = (Switch)view.findViewById(R.id.dice_launch_switch);
        diceLaunch.setChecked(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.dice_key),false));
        diceLaunch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((SWrpg)getActivity().getApplication()).prefs.edit().putBoolean(getString(R.string.dice_key),isChecked).apply();
            }
        });
        Switch analytics = (Switch)view.findViewById(R.id.analytics_switch);
        analytics.setChecked(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.analytics),true));
        analytics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((SWrpg)getActivity().getApplication()).prefs.edit().putBoolean(getString(R.string.analytics),isChecked).apply();
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
        final Switch cloud = (Switch)view.findViewById(R.id.cloud_switch);
        cloud.setChecked(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false));
        cloud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ApplySharedPref")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((SWrpg)getActivity().getApplication()).prefs.edit().putBoolean(getString(R.string.google_drive_key),isChecked).apply();
                if(isChecked){
                    final boolean[] complete = {false};
                    AlertDialog.Builder bLoad = new AlertDialog.Builder(getActivity());
                    View cont = getActivity().getLayoutInflater().inflate(R.layout.dialog_loading,null);
                    bLoad.setView(cont);
                    ((TextView)cont.findViewById(R.id.loading_message)).setText(R.string.drive_loading);
                    final AlertDialog driveLoading = bLoad.create();
                    final AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            ((MainDrawer)getActivity()).gacMaker();
                        }

                        @Override
                        protected Void doInBackground(Void... params) {
                            if(getActivity()==null)
                                return null;
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
                                driveLoading.cancel();
                                return;
                            }
                            final Character[] characters = LoadLocal.characters(getActivity());
                            final Minion[] minions = LoadLocal.minions(getActivity());
                            final Vehicle[] vehicles = LoadLocal.vehicles(getActivity());
                            if(characters.length==0&&minions.length==0&&vehicles.length==0) {
                                complete[0] = true;
                                driveLoading.dismiss();
                                return;
                            }
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
                                            lc.setOnFinish(new Load.OnLoad() {
                                                @Override
                                                public void onStart() {}
                                                @Override
                                                public boolean onLoad(Editable ed) {
                                                    return false;
                                                }

                                                @Override
                                                public void onFinish(ArrayList<Editable> characters) {
                                                    ArrayList<Integer> IDs = new ArrayList<>();
                                                    for (Editable c : characters) {
                                                        IDs.add(c.ID);
                                                    }
                                                    for(Editable c: characters){
                                                        int ID = 0;
                                                        while (IDs.contains(ID)) {
                                                            ID++;
                                                        }
                                                        c.ID = ID;
                                                        c.save(((SWrpg)getActivity().getApplication()).gac,c.getFileId(getActivity()),true);
                                                        IDs.add(ID);
                                                    }
                                                    finished[0] = true;
                                                }
                                            });
                                            lc.load(getActivity());
                                            final Load.Minions lm = new Load.Minions();
                                            lm.setOnFinish(new Load.OnLoad() {
                                                @Override
                                                public void onStart() {}
                                                @Override
                                                public boolean onLoad(Editable ed) {
                                                    return false;
                                                }

                                                @Override
                                                public void onFinish(ArrayList<Editable> characters) {
                                                    ArrayList<Integer> IDs = new ArrayList<>();
                                                    for (Editable c : characters) {
                                                        IDs.add(c.ID);
                                                    }
                                                    for(Editable c: characters){
                                                        int ID = 0;
                                                        while (IDs.contains(ID)) {
                                                            ID++;
                                                        }
                                                        c.ID = ID;
                                                        c.save(((SWrpg)getActivity().getApplication()).gac,c.getFileId(getActivity()),true);
                                                        IDs.add(ID);
                                                    }
                                                    finished[0] = true;
                                                }
                                            });
                                            lm.load(getActivity());
                                            final Load.Vehicles lv = new Load.Vehicles();
                                            lv.setOnFinish(new Load.OnLoad() {
                                                @Override
                                                public void onStart() {}
                                                @Override
                                                public boolean onLoad(Editable ed) {
                                                    return false;
                                                }

                                                @Override
                                                public void onFinish(ArrayList<Editable> characters) {
                                                    ArrayList<Integer> IDs = new ArrayList<>();
                                                    for (Editable c : characters) {
                                                        IDs.add(c.ID);
                                                    }
                                                    for(Editable c: characters){
                                                        int ID = 0;
                                                        while (IDs.contains(ID)) {
                                                            ID++;
                                                        }
                                                        c.ID = ID;
                                                        c.save(((SWrpg)getActivity().getApplication()).gac,c.getFileId(getActivity()),true);
                                                        IDs.add(ID);
                                                    }
                                                    finished[0] = true;
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
                                            complete[0] = true;
                                            driveLoading.dismiss();
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
                                    driveLoading.dismiss();
                                    complete[0] = true;
                                }
                            });
                            b.show();
                        }
                    };
                    driveLoading.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            if(!complete[0]) {
                                cloud.setChecked(false);
                                asyncTask.cancel(true);
                            }
                        }
                    });
                    driveLoading.show();
                    asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });
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
        view.findViewById(R.id.donate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((SWrpg)getActivity().getApplication()).iaps!=null)
                    ((MainDrawer)getActivity()).iapsMaker();
                ArrayList<String> items = new ArrayList<>();
                items.add("donate1");
                items.add("donate3");
                items.add("donate5");
                items.add("donate10");
                final Bundle itemQuery = new Bundle();
                itemQuery.putStringArrayList("ITEM_ID_LIST", items);
                final String[] prices = new String[4];
                AsyncTask<Void, Void, Void> async = new AsyncTask<Void, Void, Void>() {
                    boolean success = false;

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Bundle details = ((SWrpg) getActivity().getApplication()).iaps.getSkuDetails(3, getActivity().getPackageName(), "inapp", itemQuery);
                            if (details.getInt("RESPONSE_CODE") == 0) {
                                ArrayList<String> resp = details.getStringArrayList("DETAILS_LIST");
                                if (resp == null) return null;
                                for (String s : resp) {
                                    JSONObject obj = new JSONObject(s);
                                    switch (obj.getString("productId")) {
                                        case "donate1":
                                            prices[0] = obj.getString("price");
                                            break;
                                        case "donate3":
                                            prices[1] = obj.getString("price");
                                            break;
                                        case "donate5":
                                            prices[2] = obj.getString("price");
                                            break;
                                        case "donate10":
                                            prices[3] = obj.getString("price");
                                            break;
                                    }
                                }
                                success = true;
                            } else
                                return null;
                        } catch (RemoteException e) {
                            return null;
                        } catch (JSONException e) {
                            return null;
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        if (!success)
                            return;
                        if(getActivity()==null)
                            return;
                        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                        final View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_donate, null);
                        b.setView(v);
                        ((RadioButton) v.findViewById(R.id.one)).setText(prices[0]);
                        ((RadioButton) v.findViewById(R.id.three)).setText(prices[1]);
                        ((RadioButton) v.findViewById(R.id.five)).setText(prices[2]);
                        ((RadioButton) v.findViewById(R.id.ten)).setText(prices[3]);
                        ((RadioButton) v.findViewById(R.id.one)).setChecked(true);
                        b.setPositiveButton(R.string.donate, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RadioGroup rg = (RadioGroup) v.findViewById(R.id.radG);
                                Bundle buyBundle = null;
                                try {
                                    switch (rg.getCheckedRadioButtonId()) {
                                        case R.id.one:
                                            buyBundle = ((SWrpg) getActivity().getApplication()).iaps.getBuyIntent(3, getActivity().getPackageName(), "donate1", "inapp", "");
                                            break;
                                        case R.id.three:
                                            buyBundle = ((SWrpg) getActivity().getApplication()).iaps.getBuyIntent(3, getActivity().getPackageName(), "donate3", "inapp", "");
                                            break;
                                        case R.id.five:
                                            buyBundle = ((SWrpg) getActivity().getApplication()).iaps.getBuyIntent(3, getActivity().getPackageName(), "donate5", "inapp", "");
                                            break;
                                        case R.id.ten:
                                            buyBundle = ((SWrpg) getActivity().getApplication()).iaps.getBuyIntent(3, getActivity().getPackageName(), "donate10", "inapp", "");
                                            break;
                                    }
                                } catch (RemoteException ignored) {}
                                if (buyBundle == null) {
                                    dialog.cancel();
                                    return;
                                }
                                PendingIntent buyInt = buyBundle.getParcelable("BUY_INTENT");
                                try {
                                    getActivity().startIntentSenderForResult(buyInt.getIntentSender(), 100, new Intent(), 0, 0, 0, new Bundle());
                                } catch (IntentSender.SendIntentException ignored) {}
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
                async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        final Switch thanks = (Switch)view.findViewById(R.id.thanks_switch);
        thanks.setChecked(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.thank_you_key),true));
        thanks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((SWrpg)getActivity().getApplication()).prefs.edit().putBoolean(getString(R.string.thank_you_key),isChecked).apply();
            }
        });
        if(((SWrpg)getActivity().getApplication()).bought)
            thanks.setVisibility(View.VISIBLE);
        handle = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                thanks.setVisibility(View.VISIBLE);
            }
        };
    }

    Handler handle;

    public void showThanks(){
        handle.sendEmptyMessage(0);
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
