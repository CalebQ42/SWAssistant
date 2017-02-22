package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.apps.darkstorm.swrpg.ui.cards.StringCard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class DownloadFragment extends Fragment {
    private OnDownloadInteractionListener mListener;

    public DownloadFragment() {}

    public static DownloadFragment newInstance() {
        return new DownloadFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_download, container, false);
    }

    PagerAdapter adap = null;

    View chara,mini,vehic;

    Handler charHandle,miniHandle,vehicHandle,main;

    @Override
    public void onViewCreated(View top, @Nullable final Bundle savedInstanceState) {
        Toast.makeText(getActivity(),R.string.tap_download,Toast.LENGTH_LONG).show();
        top.findViewById(R.id.refresh_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAll();
            }
        });
        TabLayout tab = (TabLayout) top.findViewById(R.id.tab_lay);
        ViewPager pager = (ViewPager)top.findViewById(R.id.pager);
        ((FloatingActionButton)getActivity().findViewById(R.id.uni_fab)).hide();
        vehic = getLayoutInflater(savedInstanceState).inflate(R.layout.general_list,pager,false);
        mini = getLayoutInflater(savedInstanceState).inflate(R.layout.general_list,pager,false);
        chara = getLayoutInflater(savedInstanceState).inflate(R.layout.general_list,pager,false);
        adap = new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                switch(position){
                    case 0:
                        container.removeView(chara);
                        container.addView(chara);
                        return chara;
                    case 1:
                        container.removeView(mini);
                        container.addView(mini);
                        return mini;
                    case 2:
                        container.removeView(vehic);
                        container.addView(vehic);
                        return vehic;
                    default:
                        return null;
                }
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                switch(position){
                    case 0:
                        chara.destroyDrawingCache();
                        return;
                    case 1:
                        mini.destroyDrawingCache();
                        return;
                    case 2:
                        vehic.destroyDrawingCache();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch(position){
                    case 0:
                        return "Characters";
                    case 1:
                        return "Minions";
                    case 2:
                        return "Vehicles";
                    default:
                        return "";
                }
            }
        };
        pager.setAdapter(adap);
        tab.setupWithViewPager(pager);
        main = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1 == -1){
                    Toast.makeText(getActivity(),"Failure!",Toast.LENGTH_SHORT).show();
                }else if(msg.arg1==1){
                    Toast.makeText(getActivity(),"Success!",Toast.LENGTH_SHORT).show();
                }else if(msg.arg1==20){
                    Toast.makeText(getActivity(),"Download Started",Toast.LENGTH_SHORT).show();
                }
            }
        };
        charHandle = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(final Message msg) {
                final String name = (String)msg.obj;
                AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        Message out = main.obtainMessage();
                        out.arg1 = 20;
                        main.sendMessage(out);
                    }
                    @Override
                    protected Void doInBackground(Void... params) {
                        if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
                            DriveLoadCharacters dlc = new DriveLoadCharacters(getActivity());
                            dlc.saveLocal(getActivity());
                        }
                        LoadCharacters lc = new LoadCharacters(getActivity());
                        ArrayList<Integer> taken = new ArrayList<>();
                        for(Character chara:lc.characters)
                            taken.add(chara.ID);
                        int id = 0;
                        for (int i = 0;i<taken.size();i++){
                            if(taken.get(i)==id){
                                id++;
                                i = -1;
                            }
                        }
                        final Character tmp = new Character(id);
                        StorageReference stor = FirebaseStorage.getInstance().getReference().getRoot().child("Characters");
                        stor.child(name + ".char").getFile(new File(tmp.getFileLocation(getActivity()))).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)) {
                                    tmp.reLoad(tmp.getFileLocation(getActivity()));
                                    tmp.cloudSave(((SWrpg) getActivity().getApplication()).gac, tmp.getFileId(getActivity()), false);
                                }
                                Message msg = main.obtainMessage();
                                msg.arg1 = 1;
                                main.sendMessage(msg);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Message msg = main.obtainMessage();
                                msg.arg1 = -1;
                                main.sendMessage(msg);
                            }
                        });
                        return null;
                    }
                };
                async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        };
        miniHandle = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(final Message msg) {
                final String name = (String)msg.obj;
                AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        Message out = main.obtainMessage();
                        out.arg1 = 20;
                        main.sendMessage(out);
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
                            DriveLoadMinions dlc = new DriveLoadMinions(getActivity());
                            dlc.saveLocal(getActivity());
                        }
                        LoadMinions lc = new LoadMinions(getActivity());
                        ArrayList<Integer> taken = new ArrayList<>();
                        for(Minion chara:lc.minions)
                            taken.add(chara.ID);
                        int id = 0;
                        for (int i = 0;i<taken.size();i++){
                            if(taken.get(i)==id){
                                id++;
                                i = -1;
                            }
                        }
                        final Minion tmp = new Minion(id);
                        StorageReference stor = FirebaseStorage.getInstance().getReference().getRoot().child("Minions");
                        stor.child(name + ".minion").getFile(new File(tmp.getFileLocation(getActivity()))).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)) {
                                    tmp.reLoad(tmp.getFileLocation(getActivity()));
                                    tmp.cloudSave(((SWrpg) getActivity().getApplication()).gac, tmp.getFileId(getActivity()), false);
                                }
                                Message msg = main.obtainMessage();
                                msg.arg1 = 1;
                                main.sendMessage(msg);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Message msg = main.obtainMessage();
                                msg.arg1 = -1;
                                main.sendMessage(msg);
                            }
                        });
                        return null;
                    }
                };
                async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        };
        vehicHandle = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(final Message msg) {
                final String name = (String)msg.obj;
                AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        Message out = main.obtainMessage();
                        out.arg1 = 20;
                        main.sendMessage(out);
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
                            DriveLoadVehicles dlc = new DriveLoadVehicles(getActivity());
                            dlc.saveLocal(getActivity());
                        }
                        LoadVehicles lc = new LoadVehicles(getActivity());
                        ArrayList<Integer> taken = new ArrayList<>();
                        for(Vehicle chara:lc.vehicles)
                            taken.add(chara.ID);
                        int id = 0;
                        for (int i = 0;i<taken.size();i++){
                            if(taken.get(i)==id){
                                id++;
                                i = -1;
                            }
                        }
                        final Vehicle tmp = new Vehicle(id);
                        StorageReference stor = FirebaseStorage.getInstance().getReference().getRoot().child("Vehicles");
                        stor.child(name + ".vhcl").getFile(new File(tmp.getFileLocation(getActivity()))).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)) {
                                    tmp.reLoad(tmp.getFileLocation(getActivity()));
                                    tmp.cloudSave(((SWrpg) getActivity().getApplication()).gac, tmp.getFileId(getActivity()), false);
                                }
                                Message msg = main.obtainMessage();
                                msg.arg1 = 1;
                                main.sendMessage(msg);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Message msg = main.obtainMessage();
                                msg.arg1 = -1;
                                main.sendMessage(msg);
                            }
                        });
                        return null;
                    }
                };
                async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        };
        updateAll();
    }

    public void updateAll(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference stor = storage.getReferenceFromUrl("gs://swrpg-48c41.appspot.com");
        stor.child("List").getBytes(102400).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                String tot = new String(bytes);
                String[] chars = tot.substring(tot.indexOf("{{Characters}}")+"{{Characters}}".length(),tot.indexOf("{{Minions}}")).split("\n");
                String[] minis = tot.substring(tot.indexOf("{{Minions}}")+"{{Minions}}".length(),tot.indexOf("{{Vehicles}}")).split("\n");
                String[] vehis = tot.substring(tot.indexOf("{{Vehicles}}")+"{{Vehicles}}".length()).split("\n");
                if(adap != null && getActivity()!=null){
                    LinearLayout charaLay = (LinearLayout)chara.findViewById(R.id.main_lay);
                    charaLay.removeAllViews();
                    for(String ch:chars) {
                        if(!ch.trim().equals(""))
                            charaLay.addView(StringCard.getCard(getActivity(), charaLay, ch, charHandle));
                    }
                    LinearLayout minLay = (LinearLayout)mini.findViewById(R.id.main_lay);
                    minLay.removeAllViews();
                    for(String ch:minis) {
                        if(!ch.trim().equals(""))
                            minLay.addView(StringCard.getCard(getActivity(), charaLay, ch, miniHandle));
                    }
                    LinearLayout vhLay = (LinearLayout)vehic.findViewById(R.id.main_lay);
                    vhLay.removeAllViews();
                    for(String ch:vehis) {
                        if(!ch.trim().equals(""))
                            vhLay.addView(StringCard.getCard(getActivity(), charaLay, ch, vehicHandle));
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Message msg = main.obtainMessage();
                msg.arg1 = -1;
                main.sendMessage(msg);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDownloadInteractionListener) {
            mListener = (OnDownloadInteractionListener) context;
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

    public interface OnDownloadInteractionListener {}
}
