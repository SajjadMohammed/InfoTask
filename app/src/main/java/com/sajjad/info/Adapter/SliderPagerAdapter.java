package com.sajjad.info.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.sajjad.info.R;

import java.lang.ref.WeakReference;
import java.util.List;

public class SliderPagerAdapter extends PagerAdapter {

    private List<PersonInfo> personInfos;

    public SliderPagerAdapter(List<PersonInfo> personInfos) {
        this.personInfos = personInfos;
    }

    @Override
    public int getCount() {
        return personInfos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.slider_item, container, false);

        ImageView sliderImage = view.findViewById(R.id.sliderImage);
        new LoadImageAsync(sliderImage).execute(personInfos.get(position).getPersonPicture());

        Animation animation =
                AnimationUtils.loadAnimation(container.getContext(), android.R.anim.fade_in);
        animation.setDuration(2000);
        animation.start();

        sliderImage.setAnimation(animation);

        TextView sliderTitle = view.findViewById(R.id.sliderTitle);
        sliderTitle.setText(personInfos.get(position).getPersonName());

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }
    public static class LoadImageAsync extends AsyncTask<byte[], String, Bitmap> {

        private WeakReference<ImageView> imageViewWeakReference;
        LoadImageAsync(ImageView personPicture) {
            this.imageViewWeakReference = new WeakReference<>(personPicture);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(byte[]... bytes) {
            return BitmapFactory.decodeByteArray(bytes[0], 0, bytes[0].length);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageViewWeakReference.get().setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
        }
    }
}