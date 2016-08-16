package mks.co.mg.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import mks.co.mg.utils.CommonUtilities;

/**
 * Created by Mahesh on 13/8/16.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String[] imageurls = new String[9];

    public ImageAdapter(Context c, String[] imageurls) {
        mContext = c;
        this.imageurls = imageurls;
    }

    @Override
    public int getCount() {
        return imageurls.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        if (position != 8) {
            CommonUtilities.loadLastImage(mContext, imageurls[position], imageView, false);
        } else
            CommonUtilities.loadLastImage(mContext, imageurls[position], imageView, true);
        return imageView;
    }
}
