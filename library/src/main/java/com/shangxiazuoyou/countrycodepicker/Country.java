package com.shangxiazuoyou.countrycodepicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

public class Country implements PinyinEntity {

    private static final String TAG = Country.class.getSimpleName();

    public int code;
    public String name;
    public String locale;
    public String pinyin;
    public int flag;
    private static ArrayList<Country> countries = null;

    public Country(int code, String name, String locale, int flag) {
        this.code = code;
        this.name = name;
        this.flag = flag;
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "Country{" +
                "code='" + code + '\'' +
                "flag='" + flag + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public static ArrayList<Country> getAll(@NonNull Context ctx, @Nullable ExceptionCallback callback) {
        if (countries != null) return countries;
        countries = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ctx.getResources().getAssets().open("code.json")));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
            JSONArray ja = new JSONArray(sb.toString());
            String key = getKey(ctx);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                int flag = 0;
                String locale = jo.getString("locale");
                if (!TextUtils.isEmpty(locale)) {
                    flag = ctx.getResources().getIdentifier("flag_" + locale.toLowerCase(), "drawable", ctx.getPackageName());
                }
                countries.add(new Country(jo.getInt("code"), jo.getString(key), locale, flag));
            }

            Log.i(TAG, countries.toString());
        } catch (IOException e) {
            if (callback != null) callback.onIOException(e);
            e.printStackTrace();
        } catch (JSONException e) {
            if (callback != null) callback.onJSONException(e);
            e.printStackTrace();
        }
        return countries;
    }

    public static Country getCountryByCode(@NonNull Context ctx, int code) {
        Country.getAll(ctx, null);
        for (Country country : countries) {
            if (code == country.code) {
                return country;
            }
        }
        return null;
    }

    public static Country fromJson(String json) {
        if (TextUtils.isEmpty(json)) return null;
        try {
            JSONObject jo = new JSONObject(json);
            return new Country(jo.optInt("code"), jo.optString("name"), jo.optString("locale"), jo.optInt("flag"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toJson() {
        return "{\"name\":\"" + name + "\", \"code\":" + code + ", \"flag\":" + flag + ",\"locale\":\"" + locale + "\"}";
    }

    public static void destroy() {
        countries = null;
    }

    private static String getKey(Context ctx) {
        if (inChina(ctx)) {
            return "zh";
        } else {
            return "en";
        }
    }

    private static boolean inChina(Context ctx) {
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @NonNull
    @Override
    public String getPinyin() {
        if (pinyin == null) {
            pinyin = PinyinUtil.getPingYin(name);
        }
        return pinyin;
    }
}
