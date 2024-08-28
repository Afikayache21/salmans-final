package com.example.afinal.utils;

import android.content.Context;
import android.widget.Toast;

public class AndoridUtil {
   public static void showToast(Context context,String message){
       Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
   }
}
