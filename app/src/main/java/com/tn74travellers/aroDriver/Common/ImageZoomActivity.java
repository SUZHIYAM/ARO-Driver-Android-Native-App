package com.tn74travellers.aroDriver.Common;

/* Created by enterkey on 25/8/17 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tn74travellers.aroDriver.R;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageZoomActivity extends AppCompatActivity {

    Context context = ImageZoomActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_zoom_screen);

        ImageView iv_image_zoom         = findViewById(R.id.iv_image_zoom);
        FloatingActionButton fab_close  = findViewById(R.id.fab_close);

        Intent intent = getIntent();

        Bitmap bitmap = null;

        String path_type = getIntent().getStringExtra(CommonVariables.INTENT_IMAGE_ZOOM_PATH_TYPE);

        if(path_type != null)
        {
            switch (path_type)
            {
                case CommonVariables.CASE_IMAGE_ZOOM_RESOURCE_ID:
                {
                    bitmap = BitmapFactory.decodeResource(getResources(), intent.getIntExtra(CommonVariables.INTENT_IMAGE_ZOOM_IMG_PATH, 0));
                    Log.e(getLocalClassName(),">> bitmap : "+bitmap);

                    iv_image_zoom.setImageBitmap(bitmap);
                    break;
                }
                case CommonVariables.CASE_IMAGE_ZOOM_URL_PATH:
                {
                    bitmap = BitmapFactory.decodeFile(intent.getStringExtra(CommonVariables.INTENT_IMAGE_ZOOM_IMG_PATH));
                    Log.e(getLocalClassName(),">> bitmap : "+bitmap);

                    iv_image_zoom.setImageBitmap(bitmap);
                    break;
                }
                case CommonVariables.CASE_IMAGE_ZOOM_IMG_PATH:
                {
                    //bitmap = BitmapFactory.decodeFile(intent.getStringExtra(CommonVariables.INTENT_IMAGE_ZOOM_IMG_PATH));

                    Glide.with(context).load(intent.getStringExtra(CommonVariables.CASE_IMAGE_URL)).into(iv_image_zoom);
                    break;
                }
            }
        }
        else
        {
            Log.e("path_type : ", "null");
        }

        new PhotoViewAttacher(iv_image_zoom).update();

        fab_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
