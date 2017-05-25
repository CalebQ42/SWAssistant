package com.apps.darkstorm.swrpg.assistant.sw;

import android.util.JsonReader;
import android.util.JsonWriter;

import java.io.IOException;

public interface JsonSavable {
    void saveJson(JsonWriter jw)throws IOException;
    void loadJson(JsonReader jw) throws IOException;
}
