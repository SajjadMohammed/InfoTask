package com.sajjad.info;

import android.content.Context;
import android.content.SharedPreferences;

class SaveModel {
    private SharedPreferences preferences;
    private Context context;


    SaveModel(Context context) {
        preferences = context.getSharedPreferences(context.getString(R.string.user_info), Context.MODE_PRIVATE);
        this.context = context;
    }

   void saveUserName(String userName) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.user_name_key), userName);
        editor.apply();
    }

    void saveUserPassword(String userPassword) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.user_password_key), userPassword);
        editor.apply();
    }

    String getUserName() {
        return preferences.getString(context.getString(R.string.user_name_key), "");
    }
    String getUserPassword() {
        return preferences.getString(context.getString(R.string.user_password_key), "");
    }

    public void saveLogInStatus(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(context.getString(R.string.log_in_status), true);
        editor.apply();
    }

    public boolean getLogInStatus(){
        return preferences.getBoolean(context.getString(R.string.log_in_status),false);
    }
}