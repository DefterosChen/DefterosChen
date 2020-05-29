
package com.cxa.base.widget.waterfall.collector.huaban.ui;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.horizon.doodle.Doodle;
import com.kky.healthcaregardens.R;
import com.kky.healthcaregardens.common.widget.waterfall.base.ui.BaseAdapter;
import com.kky.healthcaregardens.common.widget.waterfall.base.widget.FlowImageView;
import com.kky.healthcaregardens.common.widget.waterfall.collector.common.ExtraKey;
import com.kky.healthcaregardens.common.widget.waterfall.collector.common.PhotoDetailActivity;
import com.kky.healthcaregardens.common.widget.waterfall.collector.huaban.source.Pin;

import java.util.List;

public class PinAdapter extends BaseAdapter<Pin, PinAdapter.PinHolder> {

    public PinAdapter(Context context, List<Pin> data, boolean isLoadMoreOpen) {
        super(context, data, isLoadMoreOpen);
    }

    @Override
    protected PinHolder getItemHolder(ViewGroup parent) {
        return new PinHolder(inflate(R.layout.item_flow, parent));
    }

    @Override
    protected void bindHolder(Pin item, int position, PinHolder holder) {
        holder.flowIv.setSourceSize(item.width, item.height);
        holder.flowIv.requestLayout();

        int desWidth;
        if (holder.flowIv.getWidth() > 0) {
            desWidth = holder.flowIv.getWidth();
        } else {
            Resources resources = mContext.getResources();
            int margin = resources.getDimensionPixelSize(R.dimen.flow_item_margin);
            int width = resources.getDisplayMetrics().widthPixels;
            desWidth = (width - 6 * margin) / 3;
        }

        float rate = (float) item.height / (float) item.width;
        int desHeight = desWidth > 0 ? Math.round(desWidth * rate) : 0;

        Doodle.load(item.url)
                .host(getHost())
                .override(desWidth, desHeight)
                .into(holder.flowIv);
    }

    class PinHolder extends RecyclerView.ViewHolder {
        private FlowImageView flowIv;

        PinHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toPinDetail(getAdapterPosition());
                }
            });
            flowIv = itemView.findViewById(R.id.flow_iv);
        }

        private void toPinDetail(int position) {
            if (position >= 0 && position < mData.size()) {
                Intent intent = new Intent(mContext, PhotoDetailActivity.class);
                intent.putExtra(ExtraKey.DETAIL_URL, mData.get(position).url);
                mContext.startActivity(intent);
            }
        }
    }

}
