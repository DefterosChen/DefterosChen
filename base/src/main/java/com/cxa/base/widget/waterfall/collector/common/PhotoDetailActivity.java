

package com.cxa.base.widget.waterfall.collector.common;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.github.chrisbanes.photoview.PhotoView;
import com.horizon.doodle.DiskCacheStrategy;
import com.horizon.doodle.Doodle;
import com.horizon.doodle.MemoryCacheStrategy;
import com.horizon.task.base.Priority;
import com.kky.healthcaregardens.R;
import com.kky.healthcaregardens.common.view.layout.MyBaseActivity;


public class PhotoDetailActivity extends MyBaseActivity {
    private String mUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_detail);
        String url = getIntent().getStringExtra(ExtraKey.DETAIL_URL);
        mUrl = url;
        PhotoView photoView = findViewById(R.id.photo_view);
        Doodle.load(url)
                .priority(Priority.IMMEDIATE)
                .host(this)
                .override(4096, 4096)
                .scaleType(ImageView.ScaleType.CENTER_INSIDE)
                .memoryCacheStrategy(MemoryCacheStrategy.WEAK)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(photoView);
    }

}
