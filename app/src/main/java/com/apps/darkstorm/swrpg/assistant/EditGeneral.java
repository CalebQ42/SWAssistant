package com.apps.darkstorm.swrpg.assistant;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.apps.darkstorm.swrpg.assistant.sw.Editable;

public class EditGeneral extends Fragment {
    public EditGeneral() {}

    Editable e;

    public static EditGeneral newInstance(Editable e) {
        EditGeneral fragment = new EditGeneral();
        fragment.e = e;
        return fragment;
    }

    Handler parentHandle = null;
    public static EditGeneral newInstance(Editable e,Handler parentHandle) {
        EditGeneral fragment = new EditGeneral();
        fragment.e = e;
        fragment.parentHandle = parentHandle;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_general, container, false);
    }

    StaggeredGridLayoutManager sgl;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        if(parentHandle==null&&((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.ads_key),true)){
//            AdView adv = (AdView)view.findViewById(R.id.ad_recycle);
//            AdRequest adRequest = new AdRequest.Builder().addKeyword("Star Wars").addKeyword("Tabletop Roleplay").addKeyword("RPG").build();
//            adv.loadAd(adRequest);
//            adv.setVisibility(View.VISIBLE);
//        }
        RecyclerView r = (RecyclerView)view.findViewById(R.id.recycler);
        EditableAdap adap = new EditableAdap();
        adap.setHasStableIds(false);
        r.setHasFixedSize(false);
        r.setAdapter(adap);
        sgl = new StaggeredGridLayoutManager(1,RecyclerView.VERTICAL);
        r.setLayoutManager(sgl);
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
    public class EditableAdap extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        final int nameCard = 0;

        final int other = 1;
        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return nameCard;
            else
                return other;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==nameCard)
                return new ViewHolder((CardView)getActivity().getLayoutInflater().inflate(R.layout.card_editable_name,parent,false));
            return new ViewHolder((CardView)getActivity().getLayoutInflater().inflate(R.layout.card_hideable,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            e.setupCards(getActivity(),this,((ViewHolder)holder).c,position,parentHandle);
        }

        @Override
        public int getItemCount() {
            if (e != null)
                return e.cardNumber();
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            CardView c;
            ViewHolder(CardView c){
                super(c);
                this.c = c;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnEditInteractionListener)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnEditInteractionListener");
        }
    }

    public interface OnEditInteractionListener {}
}
