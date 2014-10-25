package com.city.guide.api.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.city.guide.R;
import com.city.guide.api.types.ApiResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toufikzitouni on 14-10-24.
 */
public class CityGuideAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private List<ApiResults> mApiResults;
    private Context mContext;
    private String mItemType;
    private int mIconId;

    public CityGuideAdapter(Context context, int iconId, String itemType) {
        mContext = context;
        mItemType = itemType;
        mIconId = iconId;
        mInflater = LayoutInflater.from(context);
        mApiResults = new ArrayList<ApiResults>();
    }

    @Override
    public int getCount() {
        return mApiResults.size();
    }

    @Override
    public Object getItem(int position) {
        return mApiResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class CityGuideViewHolder {
        public ImageView mIcon;
        public TextView mName;
        public ImageView[] mStars;
        public TextView mDistance;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityGuideViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.place_row, parent, false);
            holder = new CityGuideViewHolder();

            holder.mStars = new ImageView[5];
            holder.mIcon = (ImageView) convertView.findViewById(R.id.icon);
            holder.mName = (TextView) convertView.findViewById(R.id.name);
            holder.mDistance = (TextView) convertView.findViewById(R.id.distance);
            holder.mStars[0] = (ImageView) convertView.findViewById(R.id.star_1);
            holder.mStars[1] = (ImageView) convertView.findViewById(R.id.star_2);
            holder.mStars[2] = (ImageView) convertView.findViewById(R.id.star_3);
            holder.mStars[3] = (ImageView) convertView.findViewById(R.id.star_4);
            holder.mStars[4] = (ImageView) convertView.findViewById(R.id.star_5);

            convertView.setTag(holder);
        } else {
            holder = (CityGuideViewHolder) convertView.getTag();
        }

        ApiResults result = mApiResults.get(position);
        holder.mName.setText(result.getName());
        int rating = (int) result.getRating() - 1;
        for (int i = 0; i < holder.mStars.length; i++) {
            int res = R.drawable.star_grey;
            if (i <= rating) {
                res = R.drawable.star_pink;
            }
            holder.mStars[i].setImageDrawable(mContext.getResources()
                    .getDrawable(res));
        }
        holder.mIcon.setImageDrawable(mContext.getResources().getDrawable(mIconId));
        holder.mDistance.setText(result.getDist());

        return convertView;
    }

    public void setData(List<ApiResults> results) {
        List<ApiResults> filtered = new ArrayList<ApiResults>();
        for (ApiResults result : results) {
            for (String type : result.getTypes()) {
                if (type.equalsIgnoreCase(mItemType)) {
                    filtered.add(result);
                }
            }
        }
        mApiResults = filtered;
        notifyDataSetChanged();
    }
}
