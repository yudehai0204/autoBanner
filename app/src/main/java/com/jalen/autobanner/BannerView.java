package com.jalen.autobanner;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jalen on 2016/1/14.
 */
public class BannerView extends FrameLayout implements View.OnClickListener {

    private ItemClickListener itemClickListener;
    /**图片内容*/
    private List<BannerBean> mList;
    /**View*/
    private List<View> mViews;
    /**下标*/
    private List<ImageView> mImgs;
    /**延迟时间*/
    private long delaytime;

    /**是否自动滚动*/
    private boolean isAuto;
    private Handler mHandle ;
    private int currentItem;
    private ViewPager vp;
    private Context mContext;

    public BannerView(Context context) {
        this(context,null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initViews();
    }

    private void initViews() {
        mViews = new ArrayList<>();
        mImgs = new ArrayList<>();
        mHandle = new Handler(mContext.getMainLooper());
        delaytime = 3000;
    }


    public void setData(List<BannerBean> lists){
        this.mList = lists;
        mImgs.clear();
        mViews.clear();
        initDatas();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        switch (visibility){
            case View.VISIBLE:
                Log.d("yu","vis");
                isAuto = true;
                break;
            default:
                Log.d("yu","default :  "+visibility);
                isAuto = false;
                break;
        }
    }

    private void initDatas() {
        int length = mList.size();
        View view = LayoutInflater.from(mContext).inflate(R.layout.banner_view,this,true);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_points);
        vp= (ViewPager) view.findViewById(R.id.vp);
        ll.removeAllViews();
        LinearLayout.LayoutParams ll_parmas = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        ll_parmas.leftMargin=5;
        ll_parmas.rightMargin=5;
        for(int i=0;i<length;i++){
            ImageView img = new ImageView(mContext);
            img.setLayoutParams(ll_parmas);
            if(i==0){
                img.setImageResource(R.mipmap.dot_focus);
            }else{
                img.setImageResource(R.mipmap.dot_blur);
            }
            ll.addView(img);
            mImgs.add(img);

            final ImageView imgforview = new ImageView(mContext);
            imgforview.setOnClickListener(this);
            imgforview.setScaleType(ImageView.ScaleType.FIT_XY);
            if(mList.get(i).getType()==0){//本地图片
                imgforview.setImageResource(mList.get(i).getDrawableforint());

            }else{//网络
                Glide.with(mContext).load(mList.get(i).getDrawableforurl()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imgforview);
//                Glide.with(mContext).load(mList.get(i).getDrawableforurl()).listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        Log.d("yu","Faile:"+e.toString());
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            imgforview.setImageDrawable(resource);
//                        return false;
//                    }
//                }).into(imgforview);
//                Log.d("yu","url: "+mList.get(i).getDrawableforurl());
            }
            mViews.add(imgforview);
        }

        vp.setAdapter(new MyAdapter());
        vp.addOnPageChangeListener(onPageChange);
        isAuto = true;
        mHandle.postDelayed(task,delaytime);

    }


    private Runnable task = new Runnable() {
        @Override
        public void run() {
            if(isAuto){
               currentItem = currentItem%(mViews.size());
//                Log.d("yu","runalbe "+currentItem);
                if(currentItem==0){
                    vp.setCurrentItem(currentItem,false);
                }else{
                    vp.setCurrentItem(currentItem);
                }
                currentItem++;
                mHandle.postDelayed(task,delaytime);
            }else{
                mHandle.postDelayed(task,delaytime);
            }
        }
    };


    private int preposition;
    private ViewPager.OnPageChangeListener onPageChange = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = vp.getCurrentItem();
//            for(int i=0;i<mImgs.size();i++){
//                if(i==position){
//                    mImgs.get(i).setImageResource(R.mipmap.dot_focus);
//                }else{
//                    mImgs.get(i).setImageResource(R.mipmap.dot_blur);
//
//                }
//            }
            if(position==preposition) {
                mImgs.get(position).setImageResource(R.mipmap.dot_focus);
            }else{
                mImgs.get(position).setImageResource(R.mipmap.dot_focus);
                mImgs.get(preposition).setImageResource(R.mipmap.dot_blur);
            }
            preposition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

            switch (state){
                case ViewPager.SCROLL_STATE_IDLE://用户什么都没有操作
//                    isAuto=true;

                    break;
                case ViewPager.SCROLL_STATE_DRAGGING://正在滑动
                    isAuto =false;
                    break;
                case ViewPager.SCROLL_STATE_SETTLING://滑动结束
                    isAuto=true;
                    break;

            }
        }
    };

    public void setItemClickListener(ItemClickListener listener){
        this.itemClickListener = listener;
    }

    public interface  ItemClickListener{
        void click(View view,BannerBean bean,int position);
    }
    @Override
    public void onClick(View v) {
        if(itemClickListener!=null){
            itemClickListener.click(v,mList.get(vp.getCurrentItem()),vp.getCurrentItem());
        }

    }

    private class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViews.get(position));
            return mViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                isAuto=false;
            break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isAuto = true;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
