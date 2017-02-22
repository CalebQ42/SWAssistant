package com.apps.darkstorm.swrpg.ui.vehicle;

import android.app.Activity;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.BuildConfig;
import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.SWrpg;
import com.apps.darkstorm.swrpg.sw.Vehicle;
import com.apps.darkstorm.swrpg.ui.cards.edit.CriticalInjuriesCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.DefenseCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.DescriptionCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.NameCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.VehicleInfoCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.WeaponsCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.WoundStrainCard;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class SetupVehicEdit {
    public static void setup(final LinearLayout linLay, Activity main, final Vehicle vh){
        if (((SWrpg)main.getApplication()).prefs.getBoolean(main.getString(R.string.ads_key),true)) {
            AdView ads = new AdView(main);
            ads.setAdSize(AdSize.BANNER);
            LinearLayout.LayoutParams adLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            adLayout.weight = 0;
            adLayout.topMargin = (int)(5*main.getResources().getDisplayMetrics().density);
            adLayout.gravity = Gravity.CENTER_HORIZONTAL;
            ads.setLayoutParams(adLayout);
            if(BuildConfig.DEBUG){
                ads.setAdUnitId(main.getString(R.string.banner_test));
            }else {
                if (BuildConfig.APPLICATION_ID.equals("com.apps.darkstorm.swrpg"))
                    ads.setAdUnitId(main.getString(R.string.free_banner_ad_id));
                else
                    ads.setAdUnitId(main.getString(R.string.paid_banner_ad_id));
            }
            AdRequest adRequest = new AdRequest.Builder().addKeyword("Star Wars").build();
            ads.loadAd(adRequest);
            linLay.addView(ads);
        }
        linLay.addView(NameCard.getCard(main,linLay,vh));
        linLay.addView(VehicleInfoCard.getCard(main,linLay,vh));
        linLay.addView(DefenseCard.getCard(main,linLay,vh));
        linLay.addView(WoundStrainCard.getCard(main,linLay,vh));
        linLay.addView(WeaponsCard.getCard(main,linLay,vh));
        linLay.addView(CriticalInjuriesCard.getCard(main,linLay,vh));
        linLay.addView(DescriptionCard.getCard(main,linLay,vh));
    }
}
