package com.aymsou.rstaurantsapp.adapters;

import android.content.Context;
import android.net.Uri;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aymsou.rstaurantsapp.R;
import com.aymsou.rstaurantsapp.model.PlaceInfo;
import com.aymsou.rstaurantsapp.utils.Const;
import com.facebook.drawee.view.SimpleDraweeView;


public class PlacePhotosAdapter extends PagerAdapter {

    //private PlaceInfo.PlacePhoto[] placePhotos;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    /*public PlacePhotosAdapter(Context context, PlaceInfo.PlacePhoto[] place_photos) {
        placePhotos = place_photos;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }*/

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.place_image, container, false);

        SimpleDraweeView placeImg = (SimpleDraweeView) itemView.findViewById(R.id.placeImg);
        //PlaceInfo.PlacePhoto photo = placePhotos[position];
        //placeImg.setImageURI(Uri.parse(String.format(Const.PLACE_PHOTO_URI, String.valueOf(photo.getWidth()), photo.getPhotoReference())));
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public int getCount() {
        return 0;
        //return placePhotos.length;
    }

}
