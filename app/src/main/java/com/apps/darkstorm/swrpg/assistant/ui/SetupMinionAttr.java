package com.apps.darkstorm.swrpg.assistant.ui;

import android.app.Activity;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.assistant.SWrpg;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.ui.cards.edit.CharacteristicsCard;
import com.apps.darkstorm.swrpg.assistant.ui.cards.edit.DefenseCard;
import com.apps.darkstorm.swrpg.assistant.ui.cards.edit.DescriptionCard;
import com.apps.darkstorm.swrpg.assistant.ui.cards.edit.InventoryCard;
import com.apps.darkstorm.swrpg.assistant.ui.cards.edit.MinionNumberCard;
import com.apps.darkstorm.swrpg.assistant.ui.cards.edit.NameCard;
import com.apps.darkstorm.swrpg.assistant.ui.cards.edit.SkillsCard;
import com.apps.darkstorm.swrpg.assistant.ui.cards.edit.TalentsCard;
import com.apps.darkstorm.swrpg.assistant.ui.cards.edit.WeaponsCard;
import com.apps.darkstorm.swrpg.assistant.BuildConfig;
import com.apps.darkstorm.swrpg.assistant.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class SetupMinionAttr {
    public static void setup(final LinearLayout linLay, final Activity main, final Minion minion){
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
        linLay.addView(NameCard.getCard(main,linLay,minion));
        linLay.addView(MinionNumberCard.getCard(main,linLay,minion));
        linLay.addView(CharacteristicsCard.getCard(main,linLay,minion));
        linLay.addView(SkillsCard.getCard(main,linLay,minion));
        linLay.addView(DefenseCard.getCard(main,linLay,minion));
        linLay.addView(WeaponsCard.getCard(main,linLay,minion));
        linLay.addView(TalentsCard.getCard(main,linLay,minion));
        linLay.addView(InventoryCard.getCard(main,linLay,minion));
        linLay.addView(DescriptionCard.getCard(main,linLay,minion));
    }
}
