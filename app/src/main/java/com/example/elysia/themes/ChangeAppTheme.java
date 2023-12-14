package com.example.elysia.themes;

import android.content.Context;

import com.dolatkia.animatedThemeManager.AppTheme;

import org.jetbrains.annotations.NotNull;

public interface ChangeAppTheme extends AppTheme {
    int firstActivityBackgroundColor(@NotNull Context context);
    int firstActivityTextColor(@NotNull Context context);
    int firstActivityIconColor(@NotNull Context context);
}
