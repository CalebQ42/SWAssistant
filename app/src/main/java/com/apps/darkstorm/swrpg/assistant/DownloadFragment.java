package com.apps.darkstorm.swrpg.assistant;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.darkstorm.swrpg.assistant.drive.Load;
import com.apps.darkstorm.swrpg.assistant.local.LoadLocal;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Editable;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.sw.Vehicle;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class DownloadFragment extends Fragment {

    public DownloadFragment() {}

    public static DownloadFragment newInstance() {
        return new DownloadFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cat_list, container, false);
    }

    Spinner tps;
    SwipeRefreshLayout srl;
    StorageReference sr;
    RecyclerView r;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sr = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.ads_key),true)) {
            AdView ads = (AdView)view.findViewById(R.id.ad_recycle);
            ads.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().addKeyword("Star Wars").build();
            ads.loadAd(adRequest);
        }
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.download);
        ((FloatingActionButton)getActivity().findViewById(R.id.fab)).hide();
        ((TextView)view.findViewById(R.id.cat_title)).setText(R.string.type);
        ArrayAdapter<String> types = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item);
        types.add("Characters");
        types.add("Minions");
        types.add("Vehicles");
        tps = (Spinner)view.findViewById(R.id.cat_spinner);
        tps.setAdapter(types);
        tps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                downloadItemAdap adap = new downloadItemAdap(position);
                r.setAdapter(adap);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        srl = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getList();
            }
        });
        r = (RecyclerView)view.findViewById(R.id.recycler);
        r.setLayoutManager(new LinearLayoutManager(getActivity()));
        dls.clear();
        dls.add(new ArrayList<String>());
        dls.add(new ArrayList<String>());
        dls.add(new ArrayList<String>());
        getList();
    }

    ArrayList<ArrayList<String>> dls = new ArrayList<>();

    public void getList(){
        AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                srl.setRefreshing(true);
            }

            @Override
            protected Void doInBackground(Void... params) {
                for(ArrayList<String> strs:dls){
                    strs.clear();
                    strs.clear();
                    strs.clear();
                }
                StorageReference lst = sr.child("List");
                final boolean[] complete = {false};
                lst.getBytes(5000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        String out = new String(bytes);
                        String[] chrs = out.substring("{{Characters}}".length(),out.indexOf("{{Minions}}")).split("\n");
                        String[] mins = out.substring(out.indexOf("{{Minions}}")+"{{Minions}}".length(),out.indexOf("{{Vehicles}}")).split("\n");
                        String[] vhcs = out.substring(out.indexOf("{{Vehicles}}")+"{{Vehicles}}".length()).split("\n");
                        for(String c:chrs){
                            if(!c.equals(""))
                                dls.get(0).add(c);
                        }
                        for(String c:mins){
                            if(!c.equals("")) {
                                dls.get(1).add(c);
                            }
                            System.out.println("Mins: "+String.valueOf(dls.get(1).size()));
                        }
                        for(String c:vhcs){
                            if(!c.equals(""))
                                dls.get(2).add(c);
                        }
                        complete[0] = true;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        complete[0] = true;
                    }
                });
                while(!complete[0]){
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
                downloadItemAdap adap = new downloadItemAdap(tps.getSelectedItemPosition());
                r.setAdapter(adap);
                srl.setRefreshing(false);
            }
        };
        async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    class downloadItemAdap extends RecyclerView.Adapter<downloadItemAdap.ViewHolder>{

        class ViewHolder extends RecyclerView.ViewHolder{
            View v;
            ViewHolder(View v){
                super(v);
                this.v = v;
            }
        }

        @Override
        public downloadItemAdap.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle(R.string.download);
            View v = getActivity().getLayoutInflater().inflate(R.layout.card_name,parent,false);
            v.findViewById(R.id.subname).setVisibility(View.GONE);
            final ViewHolder hdl = new ViewHolder(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                    @SuppressLint("InflateParams") View ld = getActivity().getLayoutInflater().inflate(R.layout.dialog_loading,null);
                    b.setView(ld);
                    ((TextView)ld.findViewById(R.id.loading_message)).setText(R.string.downloading);
                    final AlertDialog loading = b.show();
                    switch(type){
                        case 0:
                            if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
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
                                        int ID = 0;
                                        while (IDs.contains(ID)) {
                                            ID++;
                                        }
                                        final Character ch = new Character(ID);
                                        sr.getRoot().child("Characters").child(dls.get(type).get(hdl.getAdapterPosition())+".char")
                                                .getFile(new File(ch.getFileLocation(getActivity()))).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                                                    @Override
                                                    protected Void doInBackground(Void... params) {
                                                        ch.reLoad(ch.getFileLocation(getActivity()));
                                                        ch.cloudSave(((SWrpg)getActivity().getApplication()).gac,ch.getFileId(getActivity()),false);
                                                        return null;
                                                    }

                                                    @Override
                                                    protected void onPostExecute(Void aVoid) {
                                                        loading.cancel();
                                                    }
                                                };
                                                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                loading.cancel();
                                                Toast.makeText(getActivity(),R.string.failure,Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                lc.load(getActivity());
                            }else{
                                Character[] characters = LoadLocal.characters(getActivity());
                                ArrayList<Integer> IDs = new ArrayList<>();
                                for (Character c : characters) {
                                    IDs.add(c.ID);
                                }
                                int ID = 0;
                                while (IDs.contains(ID)) {
                                    ID++;
                                }
                                final Character ch = new Character(ID);
                                sr.getRoot().child("Characters").child(dls.get(type).get(hdl.getAdapterPosition())+".char")
                                        .getFile(new File(ch.getFileLocation(getActivity()))).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        loading.cancel();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loading.cancel();
                                        Toast.makeText(getActivity(),R.string.failure,Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            break;
                        case 1:
                            if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
                                final Load.Minions lc = new Load.Minions();
                                lc.setOnFinish(new Load.OnLoad() {
                                    @Override
                                    public void onStart() {

                                    }

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
                                        int ID = 0;
                                        while (IDs.contains(ID)) {
                                            ID++;
                                        }
                                        final Minion ch = new Minion(ID);
                                        System.out.println("DL: "+dls.get(type).get(hdl.getAdapterPosition()));
                                        sr.getRoot().child("Minions").child(dls.get(type).get(hdl.getAdapterPosition())+".minion")
                                                .getFile(new File(ch.getFileLocation(getActivity()))).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                                                    @Override
                                                    protected Void doInBackground(Void... params) {
                                                        ch.reLoad(ch.getFileLocation(getActivity()));
                                                        System.out.println("DLN: "+ch.name);
                                                        ch.cloudSave(((SWrpg)getActivity().getApplication()).gac,ch.getFileId(getActivity()),false);
                                                        return null;
                                                    }

                                                    @Override
                                                    protected void onPostExecute(Void aVoid) {
                                                        loading.cancel();
                                                    }
                                                };
                                                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                loading.cancel();
                                                Toast.makeText(getActivity(),R.string.failure,Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                lc.load(getActivity());
                            }else{
                                Minion[] minions = LoadLocal.minions(getActivity());
                                ArrayList<Integer> IDs = new ArrayList<>();
                                for (Minion c : minions) {
                                    IDs.add(c.ID);
                                }
                                int ID = 0;
                                while (IDs.contains(ID)) {
                                    ID++;
                                }
                                final Minion ch = new Minion(ID);
                                sr.child("Minions").child(dls.get(type).get(hdl.getAdapterPosition())+".minion")
                                        .getFile(new File(ch.getFileLocation(getActivity()))).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        loading.cancel();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loading.cancel();
                                        Toast.makeText(getActivity(),R.string.failure,Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            break;
                        case 2:
                            if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
                                final Load.Vehicles lc = new Load.Vehicles();
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
                                        int ID = 0;
                                        while (IDs.contains(ID)) {
                                            ID++;
                                        }
                                        final Vehicle ch = new Vehicle(ID);
                                        sr.getRoot().child("Vehicles").child(dls.get(type).get(hdl.getAdapterPosition())+".vhcl")
                                                .getFile(new File(ch.getFileLocation(getActivity()))).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                                                    @Override
                                                    protected Void doInBackground(Void... params) {
                                                        ch.reLoad(ch.getFileLocation(getActivity()));
                                                        System.out.println("DLN: "+ch.name);
                                                        ch.cloudSave(((SWrpg)getActivity().getApplication()).gac,ch.getFileId(getActivity()),false);
                                                        return null;
                                                    }

                                                    @Override
                                                    protected void onPostExecute(Void aVoid) {
                                                        loading.cancel();
                                                    }
                                                };
                                                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                loading.cancel();
                                                Toast.makeText(getActivity(),R.string.failure,Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                lc.load(getActivity());
                            }else{
                                Vehicle[] vehicles = LoadLocal.vehicles(getActivity());
                                ArrayList<Integer> IDs = new ArrayList<>();
                                for (Vehicle c : vehicles) {
                                    IDs.add(c.ID);
                                }
                                int ID = 0;
                                while (IDs.contains(ID)) {
                                    ID++;
                                }
                                final Vehicle ch = new Vehicle(ID);
                                sr.child("Vehicles").child(dls.get(type).get(hdl.getAdapterPosition())+".vhcl")
                                        .getFile(new File(ch.getFileLocation(getActivity()))).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        loading.cancel();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loading.cancel();
                                        Toast.makeText(getActivity(),R.string.failure,Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            break;
                    }
                }
            });
            return hdl;
        }

        @Override
        public void onBindViewHolder(downloadItemAdap.ViewHolder holder, int position) {
            ((TextView)holder.v.findViewById(R.id.name)).setText(dls.get(type).get(position));
        }

        @Override
        public int getItemCount() {
            return dls.get(type).size();
        }

        int type = 0;

        downloadItemAdap(int type){
            this.type = type;
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnFragmentInteractionListener)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    interface OnFragmentInteractionListener {}
}
