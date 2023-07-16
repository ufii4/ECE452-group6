package com.ece452.watfit.ui.post;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import com.ece452.watfit.EditPostActivity;
import com.ece452.watfit.R;

import java.io.ByteArrayOutputStream;

public class PostActivityHelper {
    public static void startEditPostActivity(Intent intent, View view, Context context) {
        byte[] screenShot = getScreenShot(view);
        intent.putExtra(EditPostActivity.SCREEN_SHOT, screenShot);
        context.startActivity(intent);
    }

    private static byte[] getScreenShot(View view) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(view.getResources().getColor(R.color.light_beige_1));
        view.draw(canvas);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
