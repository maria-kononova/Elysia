package com.example.elysia.themes;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.elysia.R;

public class PurpleTheme implements ChangeAppTheme {
    @Override
    public int id() {
        return 0;
    }

    @Override
    public int firstActivityBackgroundColor(@NonNull Context context) {
        return ContextCompat.getColor(context, R.color.purple);
    }

    @Override
    public int firstActivityTextColor(@NonNull Context context) {
        return ContextCompat.getColor(context,  R.color.white);
    }

    @Override
    public int firstActivityIconColor(@NonNull Context context) {
        return ContextCompat.getColor(context, R.color.purple);
    }
}
