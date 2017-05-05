package com.apps.darkstorm.swrpg.assistant.sw;

import android.app.Activity;
import android.support.v7.widget.CardView;

import com.apps.darkstorm.swrpg.assistant.EditGeneral;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.CriticalInjuries;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Notes;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Weapons;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveId;

public abstract class Editable {
    public int ID;
    public String name = "";
    public Notes nts = new Notes();
    public Weapons weapons = new Weapons();
    public String category = "";
    public CriticalInjuries critInjuries = new CriticalInjuries();
    public String desc = "";
    public abstract int cardNumber();
    public abstract void setupCards(Activity ac, EditGeneral.EditableAdap ea, CardView c, int pos);
    public abstract void save(String filename);
    public abstract void cloudSave(GoogleApiClient gac, DriveId fil, boolean async);
    public abstract void reLoad(GoogleApiClient gac,DriveId fil);
    public abstract void reLoad(String filename);
    public abstract void startEditing(final Activity main, final DriveId fold);
    public abstract void startEditing(final Activity main);
    public abstract void stopEditing();
}
