package com.apps.darkstorm.swrpg.assistant;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.drive.Load;
import com.apps.darkstorm.swrpg.assistant.local.LoadLocal;
import com.apps.darkstorm.swrpg.assistant.sw.Vehicle;

import java.util.ArrayList;

public class VehicleList extends Fragment {

    Vehicle[] vehic;

    public VehicleList() {}

    public static VehicleList newInstance() {
        return new VehicleList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vehic = LoadLocal.vehicles(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cat_list,container,false);
    }

    Vehicle[] vehicles;
    ArrayList<CharSequence> cats;
    ArrayList<ArrayList<Vehicle>> vehicleCats;

    StaggeredGridLayoutManager sgl;

    Spinner sp;
    SwipeRefreshLayout srl;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.characters);
        srl = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        sp = (Spinner)view.findViewById(R.id.cat_spinner);
        cats = new ArrayList<>();
        vehicleCats = new ArrayList<>();
        final NameCardAdap adap = new NameCardAdap();
        RecyclerView r = (RecyclerView)view.findViewById(R.id.recycler);
        r.setAdapter(adap);
        sgl = new StaggeredGridLayoutManager(1,RecyclerView.VERTICAL);
        r.setLayoutManager(sgl);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adap.vehicles = vehicleCats.get(position);
                adap.notifyDataSetChanged();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadVehicles();
            }
        });
        loadVehicles();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_XLARGE)){
            sgl.setSpanCount(3);
        }else if (((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_LARGE)||
                ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_XLARGE)){
            sgl.setSpanCount(2);
        }else{
            sgl.setSpanCount(1);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_XLARGE)){
            sgl.setSpanCount(3);
        }else if (((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_LARGE)||
                ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_XLARGE)){
            sgl.setSpanCount(2);
        }else{
            sgl.setSpanCount(1);
        }
    }

    public void loadVehicles(){
        if (((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
            final Load.Vehicles ch = new Load.Vehicles();
            ch.setOnFinish(new Load.onFinish() {
                @Override
                public void finish() {
                    ch.saveLocal(getActivity());
                    cats.clear();
                    vehicleCats.clear();
                    cats.add("All");
                    vehicleCats.add(new ArrayList<Vehicle>());
                    for (Vehicle c:ch.vehicles){
                        if(cats.contains(c.category)){
                            vehicleCats.get(cats.indexOf(c.category)).add(c);
                        }else if(!c.category.equals("")){
                            cats.add(c.category);
                            vehicleCats.add(new ArrayList<Vehicle>());
                            vehicleCats.get(vehicleCats.size()-1).add(c);
                        }
                        vehicleCats.get(0).add(c);
                    }
                    ArrayAdapter<CharSequence> apAdap = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,cats);
                    sp.setAdapter(apAdap);
                    srl.setRefreshing(false);
                    sp.setSelection(0);
                }
            });
            srl.setRefreshing(true);
            ch.load(getActivity());
        }else{
            srl.setRefreshing(true);
            vehicles = LoadLocal.vehicles(getActivity());
            cats.clear();
            vehicleCats.clear();
            cats.add("All");
            vehicleCats.add(new ArrayList<Vehicle>());
            for (Vehicle c:vehicles){
                if(cats.contains(c.category)){
                    vehicleCats.get(cats.indexOf(c.category)).add(c);
                }else if(!c.category.equals("")){
                    cats.add(c.category);
                    vehicleCats.add(new ArrayList<Vehicle>());
                    vehicleCats.get(vehicleCats.size()-1).add(c);
                }
                vehicleCats.get(0).add(c);
            }
            ArrayAdapter<CharSequence> apAdap = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,cats);
            sp.setAdapter(apAdap);
            srl.setRefreshing(false);
            sp.setSelection(0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
            if(((SWrpg)getActivity().getApplication()).gac==null ||!((SWrpg)getActivity().getApplication()).gac.isConnected())
                ((MainDrawer)getActivity()).gacMaker();
        }
    }

    class NameCardAdap extends RecyclerView.Adapter<NameCardAdap.NameCard> {

        ArrayList<Vehicle> vehicles = new ArrayList<>();

        @Override
        public NameCard onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView c = (CardView)getActivity().getLayoutInflater().inflate(R.layout.card_name,parent,false);
            final NameCard n = new NameCard(c);
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    TODO: getFragmentManager().beginTransaction().replace(R.id.content_main,VehicleEdit.newInstance(n.vehicle))
//                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).commit();
                }
            });
            return n;
        }

        @Override
        public void onBindViewHolder(NameCard holder, int position) {
            ((TextView)holder.c.findViewById(R.id.name)).setText(vehicles.get(position).name);
            if (vehicles.get(position).model.equals(""))
                holder.c.findViewById(R.id.subname).setVisibility(View.GONE);
            else
                ((TextView) holder.c.findViewById(R.id.subname)).setText(vehicles.get(position).model);
            holder.vehicle = vehicles.get(position);
        }

        @Override
        public int getItemCount() {
            return vehicles.size();
        }

        class NameCard extends RecyclerView.ViewHolder {
            CardView c;
            Vehicle vehicle;
            NameCard(CardView c) {
                super(c);
                this.c = c;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnVehicleListInteractionListener)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnVehicleListInteractionListener");
        }
    }

    public interface OnVehicleListInteractionListener {
    }
}
