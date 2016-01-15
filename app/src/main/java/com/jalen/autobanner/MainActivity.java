package com.jalen.autobanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String urls[] = new String[]{
            "http://cemzn.img47.wal8.com/img47/536055_20160114185713/145276909478.jpg",
            "http://cemzn.img47.wal8.com/img47/536055_20160114185713/145276909415.jpg",
            "http://cemzn.img47.wal8.com/img47/536055_20160114185713/145276905919.png"

    };
    private BannerView bannerView;
    private int ids[] = new int[]{R.mipmap.one,R.mipmap.two,R.mipmap.three,R.mipmap.four,R.mipmap.five};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bannerView = (BannerView) findViewById(R.id.banner);
        List<BannerBean> mList = new ArrayList<BannerBean>();
        for(int i = 0 ;i<ids.length;i++){
            BannerBean bean = new BannerBean();
            bean.setType(0);
            bean.setDrawableforint(ids[i]);
            mList.add(bean);
        }
        for(int i =0;i<urls.length;i++){
            BannerBean bean = new BannerBean();
            bean.setType(1);
            bean.setDrawableforurl(urls[i]);
            mList.add(bean);
        }
        bannerView.setData(mList);
        bannerView.setItemClickListener(new BannerView.ItemClickListener() {
            @Override
            public void click(View view, BannerBean bean,int position) {
                if(bean.getType()==0){
                    Toast.makeText(MainActivity.this,bean.getDrawableforint()+"  "+position,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,bean.getDrawableforurl()+"   "+position,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
