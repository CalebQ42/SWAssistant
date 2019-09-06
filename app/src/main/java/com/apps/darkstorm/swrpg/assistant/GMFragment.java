package com.apps.darkstorm.swrpg.assistant;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.legacy.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Editable;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.sw.Vehicle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class GMFragment extends Fragment {
    public GMFragment() {}

    public static GMFragment newInstance() {
        return new GMFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gm, container, false);
    }

    Handler handle;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.gm_mode);
        ((FloatingActionButton)getActivity().findViewById(R.id.fab)).hide();
        ViewPager listPager = (ViewPager)view.findViewById(R.id.list_pager);
        final TabLayout tab = (TabLayout)view.findViewById(R.id.tab_lay);
        tab.setupWithViewPager(listPager);
        final FragmentStatePagerAdapter adap = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch(position){
                    case 0:
                        return CharacterList.newInstance(handle);
                    case 1:
                        return MinionList.newInstance(handle);
                    case 2:
                        return VehicleList.newInstance(handle);
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch(position){
                    case 0:
                        return getString(R.string.characters);
                    case 1:
                        return getString(R.string.minions);
                    case 2:
                        return getString(R.string.vehicles);
                    default:
                        return "";
                }
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };
        listPager.setAdapter(adap);
        FloatingActionButton add = (FloatingActionButton)view.findViewById(R.id.add_fab);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(tab.getSelectedTabPosition()){
                    case 0:
                        ArrayList<Integer> IDs = new ArrayList<>();
                        for (Character c : characters) {
                            IDs.add(c.ID);
                        }
                        int ID = 0;
                        while (IDs.contains(ID)) {
                            ID++;
                        }
                        Character ch = new Character(ID);
                        getChildFragmentManager().beginTransaction().replace(R.id.edit_content, EditFragment.newInstance(ch,handle))
                                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).addToBackStack("Character Edit").commit();
                        adap.notifyDataSetChanged();
                        break;
                    case 1:
                        IDs = new ArrayList<>();
                        for (Minion c : minions) {
                            IDs.add(c.ID);
                        }
                        ID = 0;
                        while (IDs.contains(ID)) {
                            ID++;
                        }
                        Minion min = new Minion(ID);
                        getChildFragmentManager().beginTransaction().replace(R.id.edit_content, EditFragment.newInstance(min,handle))
                                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).addToBackStack("Minion Edit").commit();
                        adap.notifyDataSetChanged();
                        break;
                    case 2:
                        IDs = new ArrayList<>();
                        for (Vehicle c : vehicles) {
                            IDs.add(c.ID);
                        }
                        ID = 0;
                        while (IDs.contains(ID)) {
                            ID++;
                        }
                        Vehicle veh = new Vehicle(ID);
                        getChildFragmentManager().beginTransaction().replace(R.id.edit_content, EditFragment.newInstance(veh,handle))
                                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).addToBackStack("Vehicle Edit").commit();
                        adap.notifyDataSetChanged();
                        break;
                }
            }
        });
        handle = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.obj instanceof Editable){
                    Editable ed = (Editable)msg.obj;
                    getChildFragmentManager().beginTransaction().replace(R.id.edit_content, EditFragment.newInstance(ed,handle))
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).addToBackStack("Editable Edit").commit();
                }else if(msg.obj instanceof ArrayList){
                    ArrayList tmp = (ArrayList)msg.obj;
                    if(!tmp.isEmpty()){
                        if(tmp.get(0) instanceof Character){
                            characters.clear();
                            for(Object c:tmp)
                                characters.add((Character)c);
                        }else if(tmp.get(0)instanceof Minion){
                            minions.clear();
                            for(Object c:tmp)
                                minions.add((Minion)c);
                        }else if(tmp.get(0)instanceof Vehicle){
                            vehicles.clear();
                            for(Object c:tmp)
                                vehicles.add((Vehicle)c);
                        }
                    }
                }else if(msg.arg2!= 0){
                    adap.notifyDataSetChanged();
                }
            }
        };
    }

    ArrayList<Character> characters = new ArrayList<>();
    ArrayList<Minion> minions = new ArrayList<>();
    ArrayList<Vehicle> vehicles = new ArrayList<>();

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
