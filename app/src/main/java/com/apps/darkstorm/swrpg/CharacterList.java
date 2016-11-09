package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.StarWars.DriveLoadChars;
import com.apps.darkstorm.swrpg.StarWars.LoadChars;
import com.apps.darkstorm.swrpg.UI.CharacterCard;
import com.google.android.gms.common.api.GoogleApiClient;

import com.apps.darkstorm.swrpg.StarWars.Character;

import java.util.ArrayList;

public class CharacterList extends Fragment {
    private OnListInteractionListener mListener;

    GoogleApiClient gac = null;
    ArrayList<Character> chars;

    public CharacterList() {}

    public static CharacterList newInstance(GoogleApiClient gac) {
        CharacterList frag = new CharacterList();
        frag.gac = gac;
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Handler mainHandle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View top = inflater.inflate(R.layout.fragment_character_list, container, false);
        chars = new ArrayList<>();
        final FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.universeFab);
        fab.show();
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int max = -1;
                for (Character chara:chars){
                    if (chara.ID>max){
                        max = chara.ID;
                    }
                }
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                        android.R.anim.fade_in,android.R.anim.fade_out).replace(R.id.content_navigation, CharacterEditMain.newInstance(max+1,gac))
                        .addToBackStack("Creating a new Character").commit();
                fab.hide();
            }
        });
        mainHandle = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message in){
                if (in.obj instanceof com.apps.darkstorm.swrpg.StarWars.Character) {
                    Character tmp = (Character)in.obj;
                    for (int i = 0; i < chars.size(); i++) {
                        if (chars.get(i).equalsChar(tmp)) {
                            ((LinearLayout) top.findViewById(R.id.mainLay)).removeViewAt(i);
                            break;
                        }
                    }
                }else if(in.obj instanceof ArrayList){
                    ArrayList<Character> charsNew = (ArrayList<Character>)in.obj;
                    if (!charsNew.equals(chars)){
                        chars = charsNew;
                        ((LinearLayout)top.findViewById(R.id.mainLay)).removeAllViews();
                        for (Character chara:chars){
                            ((LinearLayout)top.findViewById(R.id.mainLay)).addView(new CharacterCard()
                                    .getCard(CharacterList.this,chara,mainHandle,gac));
                        }
                    }
                }
            }
        };
        final SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE);
        if (pref.getBoolean(getString(R.string.cloud_key),false) && gac == null){
            AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    if(pref.getBoolean(getString(R.string.cloud_key),false)){
                        DriveLoadChars dlc = new DriveLoadChars(CharacterList.this.getContext(),gac);
                        dlc.saveToFile(CharacterList.this.getContext(),false);
                    }
                    LoadChars lc = new LoadChars(CharacterList.this.getContext());
                    Message tmp = mainHandle.obtainMessage();
                    tmp.obj = lc.chars;
                    mainHandle.sendMessage(tmp);
                    return null;
                }
            };
            async.execute();
        }else{
            AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    if(pref.getBoolean(getString(R.string.cloud_key),false)){
                        DriveLoadChars dlc = new DriveLoadChars(CharacterList.this.getContext(),gac);
                        dlc.saveToFile(CharacterList.this.getContext(),false);
                    }
                    LoadChars lc = new LoadChars(CharacterList.this.getContext());
                    Message tmp = mainHandle.obtainMessage();
                    tmp.obj = lc.chars;
                    mainHandle.sendMessage(tmp);
                    return null;
                }
            };
            async.execute();
        }
        return top;
    }

    public void onStart(){
        super.onStart();
        final SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE);
        if (pref.getBoolean(getString(R.string.cloud_key),false) && gac == null){
            AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    if(pref.getBoolean(getString(R.string.cloud_key),false)){
                        DriveLoadChars dlc = new DriveLoadChars(CharacterList.this.getContext(),gac);
                        dlc.saveToFile(CharacterList.this.getContext(),false);
                    }
                    LoadChars lc = new LoadChars(CharacterList.this.getContext());
                    Message tmp = mainHandle.obtainMessage();
                    tmp.obj = lc.chars;
                    mainHandle.sendMessage(tmp);
                    return null;
                }
            };
            async.execute();
        }else{
            AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    if(pref.getBoolean(getString(R.string.cloud_key),false)){
                        DriveLoadChars dlc = new DriveLoadChars(CharacterList.this.getContext(),gac);
                        dlc.saveToFile(CharacterList.this.getContext(),false);
                    }
                    LoadChars lc = new LoadChars(CharacterList.this.getContext());
                    Message tmp = mainHandle.obtainMessage();
                    tmp.obj = lc.chars;
                    mainHandle.sendMessage(tmp);
                    return null;
                }
            };
            async.execute();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListInteractionListener) {
            mListener = (OnListInteractionListener) context;
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

    public interface OnListInteractionListener {
        void onListInteraction();
    }
}
