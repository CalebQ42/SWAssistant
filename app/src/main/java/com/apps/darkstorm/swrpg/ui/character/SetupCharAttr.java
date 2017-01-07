package com.apps.darkstorm.swrpg.ui.character;

import android.app.Activity;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.BuildConfig;
import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.SWrpg;
import com.apps.darkstorm.swrpg.sw.Character;
import com.apps.darkstorm.swrpg.ui.cards.edit.CharacteristicsCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.CriticalInjuriesCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.DefenseCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.DescriptionCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.DutyCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.EmotionsCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.ForcePowerCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.InventoryCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.NameCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.ObligationCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.SkillsCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.SpecializationsCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.SpeciesCareerCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.TalentsCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.WeaponsCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.WoundStrainCard;
import com.apps.darkstorm.swrpg.ui.cards.edit.XpCard;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class SetupCharAttr {
    public static void setup(LinearLayout linLay,final Activity main, final Character chara){
        if (((SWrpg)main.getApplication()).prefs.getBoolean(main.getString(R.string.ads_key),true)) {
            AdView ads = new AdView(main);
            ads.setAdSize(AdSize.BANNER);
            LinearLayout.LayoutParams adLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            adLayout.weight = 0;
            adLayout.topMargin = (int)(5*main.getResources().getDisplayMetrics().density);
            adLayout.gravity = Gravity.CENTER_HORIZONTAL;
            ads.setLayoutParams(adLayout);
            if (BuildConfig.APPLICATION_ID.equals("com.apps.darkstorm.swrpg"))
                ads.setAdUnitId(main.getString(R.string.free_banner_ad_id));
            else
                ads.setAdUnitId(main.getString(R.string.paid_banner_ad_id));
            AdRequest adRequest = new AdRequest.Builder().addKeyword("Star Wars").build();
            ads.loadAd(adRequest);
            linLay.addView(ads);
        }
        linLay.addView(NameCard.getCard(main,linLay,chara));
        linLay.addView(SpeciesCareerCard.getCard(main,linLay,chara));
        linLay.addView(WoundStrainCard.getCard(main,linLay,chara));
        linLay.addView(CharacteristicsCard.getCard(main,linLay,chara));
        linLay.addView(SkillsCard.getCard(main,linLay,chara));
        linLay.addView(DefenseCard.getCard(main,linLay,chara));
        linLay.addView(WeaponsCard.getCard(main,linLay,chara));
        linLay.addView(CriticalInjuriesCard.getCard(main,linLay,chara));
        linLay.addView(SpecializationsCard.getCard(main,linLay,chara));
        linLay.addView(TalentsCard.getCard(main,linLay,chara));
        linLay.addView(ForcePowerCard.getCard(main,linLay,chara));
        linLay.addView(XpCard.getCard(main,linLay,chara));
        linLay.addView(InventoryCard.getCard(main,linLay,chara));
        linLay.addView(EmotionsCard.getCard(main,linLay,chara));
        linLay.addView(DutyCard.getCard(main,linLay,chara));
        linLay.addView(ObligationCard.getCard(main,linLay,chara));
        linLay.addView(DescriptionCard.getCard(main,linLay,chara));
    }
}