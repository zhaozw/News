package com.guozaiss.news.common.utils.imageLoad;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.guozaiss.news.R;

/**
 * Glide图片加载类
 * Created by guozaiss on 16/5/10.
 */
public class GlideUtils {

    /**
     * 加载图片Method
     *
     * @param object    可以为Context、Activity、Fragment（&V4）、FragmentActivity
     * @param imageView ImageView
     * @param URL       加载图片的URL
     */
    public static void disPlay(Object object, ImageView imageView, String URL) {
        disPlay(object, imageView, URL, null);
    }

    /**
     * 加载图片Method
     *
     * @param object    可以为Context、Activity、Fragment（&V4）、FragmentActivity
     * @param imageView ImageView
     * @param URL       加载图片的URL
     */
    public static void disPlay(Object object, ImageView imageView, String URL, RequestListener requestListener) {
        init(object, URL, requestListener).into(imageView);
    }

    /**
     * @param object
     * @param URL
     * @return
     */
    private static DrawableRequestBuilder<String> init(Object object, String URL, RequestListener requestListener) {
        return load(object, URL)
                .listener(requestListener);
    }


    /**
     * loadGlide
     *
     * @param object
     * @param URL
     * @return
     */
    private static DrawableRequestBuilder<String> load(Object object, String URL) {
        RequestManager requestManager = null;

        if (object instanceof Context) {

            requestManager = Glide.with((Context) object);

        } else if (object instanceof Activity) {

            requestManager = Glide.with((Activity) object);

        } else if (object instanceof FragmentActivity) {

            requestManager = Glide.with((FragmentActivity) object);

        } else if (object instanceof android.app.Fragment) {

            requestManager = Glide.with((android.app.Fragment) object);

        } else if (object instanceof android.support.v4.app.Fragment) {

            requestManager = Glide.with((android.support.v4.app.Fragment) object);

        } else {

            throw new NullPointerException("Glide do not with object");
        }
        return requestManager
                .load(URL)
                .error(R.drawable.load_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

}
