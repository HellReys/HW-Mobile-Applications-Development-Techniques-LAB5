package com.example.currencyapp;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {
    public static void showToast(Context ctx, String message) {
        if (ctx == null || message == null) return;
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
}
