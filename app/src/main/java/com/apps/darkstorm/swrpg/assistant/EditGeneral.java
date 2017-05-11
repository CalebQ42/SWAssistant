package com.apps.darkstorm.swrpg.assistant;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.assistant.sw.Editable;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

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

        final int ad = 2;
        @Override
        public int getItemViewType(int position) {
            if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.ads_key),true)){
                if(position==0)
                    return ad;
                if(position==1)
                    return nameCard;
                return other;
            }else {
                if (position == 0)
                    return nameCard;
                else
                    return other;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==ad) {
                AdView ads = new AdView(getActivity());
                ads.setAdSize(AdSize.BANNER);
                LinearLayout.LayoutParams adLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                adLayout.weight = 0;
                adLayout.gravity = Gravity.CENTER_HORIZONTAL;
                ads.setLayoutParams(adLayout);
                ads.setAdUnitId(getString(R.string.free_banner_ad_id));
                return new adHolder(ads);
            }
            if(viewType==nameCard)
                return new ViewHolder((CardView)getActivity().getLayoutInflater().inflate(R.layout.card_editable_name,parent,false));
            return new ViewHolder((CardView)getActivity().getLayoutInflater().inflate(R.layout.card_hideable,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.ads_key),true)){
                if(position==0){
                    adHolder ah = (adHolder)holder;
                    AdRequest adRequest = new AdRequest.Builder().addKeyword("Star Wars").build();
                    ah.a.loadAd(adRequest);
                }else
                    e.setupCards(getActivity(),this,((ViewHolder)holder).c,position-1,parentHandle);
            }else
                e.setupCards(getActivity(),this,((ViewHolder)holder).c,position,parentHandle);
        }

        @Override
        public int getItemCount() {
            if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.ads_key),true))
                return e.cardNumber()+1;
            else
                return e.cardNumber();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            CardView c;
            ViewHolder(CardView c){
                super(c);
                this.c = c;
            }
        }
        class adHolder extends  RecyclerView.ViewHolder{
            AdView a;
            adHolder(AdView a){
                super(a);
                this.a = a;
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
