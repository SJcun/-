package cn.sjcup.musicplayer.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.sjcup.musicplayer.R;
import cn.sjcup.musicplayer.util.MusicPlayUtil;
import cn.sjcup.musicplayer.util.PlayState;

import static cn.sjcup.musicplayer.activity.MainActivity.IMG;

/**
 * 歌曲列表
 */
public class MusicListActivity extends Activity {

    private ImageButton mBack;  //返回按钮
    private ListView mMusicList;  //展示歌曲信息
    private TextView mState;  //状态控件

    private JSONArray musicList;  //获取的歌曲列表信息
    private JSONObject musicInfo;  //单条歌曲信息

    private MusicPlayUtil musicPlayUtil = MusicPlayUtil.getInstance();   //获取工具类实体类对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        //初始化界面
        initView();
        //设置相关事件
        initEvent();
    }

    /**
     * 初始化事件，为按钮设置监听事件
     */
    private void initEvent() {
        mMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MusiclistActivity.this,String.valueOf(id)+"   "+String.valueOf(position),Toast.LENGTH_SHORT).show();
                if (mState == null) {
                    //parent.getChildAt(Integer.parseInt(String.valueOf(id)))
                    mState = view.findViewById(R.id.tv_type);
                    mState.setText("播放中");
                    mState.setTextColor(Color.RED);
                    musicPlayUtil.setMusicId(position);
                    musicPlayUtil.setMusicView(PlayState.PLAY_STATE_PLAY);
                    finish();
                } else {
                    mState.setText("");
                    //parent.getChildAt(position)
                    mState = view.findViewById(R.id.tv_type);
                    mState.setText("播放中");
                    mState.setTextColor(Color.RED);
                    musicPlayUtil.setMusicId(position);
                    musicPlayUtil.setMusicView(PlayState.PLAY_STATE_PLAY);
                    finish();
                }

            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicListActivity.this.finish();
            }
        });
    }

    /**
     * 初始化界面，绑定按钮，填充歌曲信息
     */
    private void initView() {
        mBack = findViewById(R.id.ib_title_back);
        mMusicList = findViewById(R.id.lv_music);
        //mLoad = (LinearLayout) findViewById(R.id.loading);

        fillData();  //填充数据
    }

    /**
     * 填充数据
     */
    private void fillData() {

        //mLoad.setVisibility(View.INVISIBLE);
        mMusicList.setAdapter(new MusicAdapter());
    }

    /**
     * listview适配器，方便填充。  这部分在博文上有详细讲解  https://blog.csdn.net/cun_king/article/details/111243881
     */
    private class MusicAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return musicPlayUtil.getMusicNum();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            musicList = musicPlayUtil.getMusicList();
            try{
                musicInfo = musicList.getJSONObject(position);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (convertView == null){
                convertView = LayoutInflater.from(
                        getApplicationContext()).inflate(R.layout.activity_item,parent,false);
                holder = new ViewHolder();
                holder.siv = convertView.findViewById(R.id.siv_img);
                holder.tv_name = convertView.findViewById(R.id.tv_name);
                holder.tv_author = convertView.findViewById(R.id.tv_author);
                holder.tv_type = convertView.findViewById(R.id.tv_type);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.siv.setImageUrl(IMG+musicInfo.optString("img"), R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher);
            holder.tv_name.setText(musicInfo.optString("name"));
            holder.tv_author.setText(musicInfo.optString("author"));
            holder.tv_type.setText("");
            if (musicPlayUtil.getMusicId()==position){
                holder.tv_type.setText("播放中");
                holder.tv_type.setTextColor(Color.RED);
                mState=holder.tv_type;
            }
            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        class ViewHolder{
            TextView tv_name;
            TextView tv_author;
            TextView tv_type;
            SmartImageView siv;
        }
    }
}
