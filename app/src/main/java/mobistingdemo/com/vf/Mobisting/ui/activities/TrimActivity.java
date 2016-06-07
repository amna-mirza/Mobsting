package mobistingdemo.com.vf.Mobisting.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import mobistingdemo.com.vf.Mobisting.HelperClasses.DialogHelper;
import mobistingdemo.com.vf.Mobisting.HelperClasses.TrimVideo;
import mobistingdemo.com.vf.Mobisting.R;

/**
 * Created by amna.mirza on 12/16/2015.
 */
public class TrimActivity extends Activity implements SurfaceHolder.Callback {
    private LinearLayout llVideoFrames;
    private int minTrim, maxTrim, minSlow, maxSlow;
    private VideoView mVideoView;
    private Context mContext;
    private RangeSeekBar<Integer> mRangeSeekBar, trimRangeSeekBar;
    private RelativeLayout rlRangeSelection, rl_trimrangeSelection;
    private List<Bitmap> bitMapList;
    private List<ImageView> imagesList;
    private int videoNanoSec;
    private String videoPath;
    private HorizontalScrollView hsvScroll;
    private SeekBar sb_videoSpeed;
    private TextView tv_videoSpeed;
    private float playbackSpeed;
    private int maxValue;
    private io.vov.vitamio.widget.VideoView mPreview;
    private SurfaceHolder holder;
    private MediaPlayer mMediaPlayer;

    public static String getInternalDirectoryPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trim);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        playbackSpeed = 1.0f;
        minTrim = -1;
        maxTrim = -1;
        mToolbar.setTitle("Video Editing Demo");
        tv_videoSpeed = (TextView) findViewById(R.id.tv_videoSpeed);
        sb_videoSpeed = (SeekBar) findViewById(R.id.sb_videoSpeed); // make seekbar object
        sb_videoSpeed.setProgress(100);
        Intent intent = getIntent();
        videoPath = intent.getStringExtra("path");
        mContext = this;
        if (!Vitamio.isInitialized(mContext))
            if (Vitamio.initialize(mContext))
                Log.i("VITAMIO", "initlaized");
            else
                return;
        mPreview = (io.vov.vitamio.widget.VideoView) findViewById(R.id.surface);
//        multiSlider = (MultiSlider) findViewById(R.id.range_slider5);
        holder = mPreview.getHolder();
        holder.addCallback((SurfaceHolder.Callback) mContext);
        holder.setFormat(PixelFormat.RGBA_8888);


        initUi();
        loadFrames(videoPath);
        // Setup the new range seek bar
        mRangeSeekBar = new RangeSeekBar<Integer>(this);
        mRangeSeekBar.setRangeValues(0, 100);
        mRangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // handle changed range values
                int max = imagesList.size();
                int maxChange = maxValue / 3;
                int minChange = minValue / 3;
                int x, y;
                minSlow = minChange * 3;
                maxSlow = maxChange * 3;

                if ((minChange - 1) > 0) {
                    x = imagesList.get(minChange - 1).getLeft();
                    y = imagesList.get(minChange - 1).getTop();

                    hsvScroll.scrollTo(x, y);
                } else
                    hsvScroll.scrollTo(1, 1);
                for (int i = 0; i < minChange; i++) {
                    ImageView im = imagesList.get(i);
                    im.setAlpha(0.3f);
                }
                for (int i = minChange; i <= maxChange; i++) {
                    ImageView im = imagesList.get(i);
                    im.setAlpha(1.0f);
                }
                for (int i = (maxChange + 1); i < max; i++) {
                    ImageView im = imagesList.get(i);
                    im.setAlpha(0.3f);
                }
            }

        });
        rlRangeSelection.addView(mRangeSeekBar);

        trimRangeSeekBar = new RangeSeekBar<Integer>(this);
        trimRangeSeekBar.setRangeValues(0, 100);
        trimRangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // handle changed range values
                int max = imagesList.size();
                int maxChange = maxValue / 3;
                int minChange = minValue / 3;
                int x, y;
                minTrim = minChange * 3;
                maxTrim = maxChange * 3;
                if ((minChange - 1) > 0) {
                    x = imagesList.get(minChange - 1).getLeft();
                    y = imagesList.get(minChange - 1).getTop();

                    hsvScroll.scrollTo(x, y);
                } else
                    hsvScroll.scrollTo(1, 1);
                for (int i = 0; i < minChange; i++) {
                    ImageView im = imagesList.get(i);
                    im.setAlpha(0.3f);
                }
                for (int i = minChange; i <= maxChange; i++) {
                    ImageView im = imagesList.get(i);
                    im.setAlpha(1.0f);
                }
                for (int i = (maxChange + 1); i < max; i++) {
                    ImageView im = imagesList.get(i);
                    im.setAlpha(0.3f);
                }
            }

        });
        rl_trimrangeSelection.addView(trimRangeSeekBar);
        // Set the range
        sb_videoSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                int progress = seekBar.getProgress();
                if (progress <= 25) {
                    playbackSpeed = 0.5f;
                    mMediaPlayer.setPlaybackSpeed(0.5f);
                    tv_videoSpeed.setText("Speed = " + 0.5 + "f");
                } else if (progress > 25 && progress <= 50) {
                    mMediaPlayer.setPlaybackSpeed(0.6f);
                    playbackSpeed = 0.6f;
                    tv_videoSpeed.setText("Speed = " + 0.6 + "f");
                } else if (progress > 50 && progress <= 75) {
                    playbackSpeed = 0.75f;
                    mMediaPlayer.setPlaybackSpeed(0.75f);
                    tv_videoSpeed.setText("Speed = " + 0.75 + "f");
                } else if (progress > 75 && progress < 90) {
                    playbackSpeed = 0.85f;
                    mMediaPlayer.setPlaybackSpeed(0.85f);
                    tv_videoSpeed.setText("Speed = " + 0.85 + "f");
                } else if (progress >= 90 && progress < 110) {
                    playbackSpeed = 1.0f;
                    mMediaPlayer.setPlaybackSpeed(1.0f);
                    tv_videoSpeed.setText("Speed = " + 1.0 + "f");
                } else if (progress >= 110 && progress < 125) {
                    playbackSpeed = 1.25f;
                    mMediaPlayer.setPlaybackSpeed(1.25f);
                    tv_videoSpeed.setText("Speed = " + 1.25 + "f");
                } else if (progress >= 125 && progress < 150) {
                    playbackSpeed = 1.5f;
                    mMediaPlayer.setPlaybackSpeed(1.5f);
                    tv_videoSpeed.setText("Speed = " + 1.5 + "f");
                } else if (progress >= 150 && progress < 175) {
                    playbackSpeed = 1.75f;
                    mMediaPlayer.setPlaybackSpeed(1.75f);
                    tv_videoSpeed.setText("Speed = " + 1.75 + "f");
                } else if (progress >= 175 && progress < 190) {
                    playbackSpeed = 1.85f;
                    mMediaPlayer.setPlaybackSpeed(1.85f);
                    tv_videoSpeed.setText("Speed = " + 1.85 + "f");
                } else if (progress >= 190) {
                    playbackSpeed = 2.0f;
                    mMediaPlayer.setPlaybackSpeed(2.0f);
                    tv_videoSpeed.setText("Speed = " + 2.0 + "f");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub


            }
        });

    }

    private void initPreviewVideo(String videopath) throws IOException {

        io.vov.vitamio.widget.MediaController mediaController = new io.vov.vitamio.widget.MediaController(mContext);
        mPreview.setMediaController(mediaController);
        // Create a new media player and set the listeners
        mMediaPlayer = new MediaPlayer(this);
        mMediaPlayer.setDataSource(videopath);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.setDisplay(holder);
        mMediaPlayer.prepareAsync();

        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                mp.getCurrentPosition();
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                mMediaPlayer.start();
                mMediaPlayer.setVolume(0, 0);
            }
        });

    }

    private void initUi() {
        final Handler mHandler = new Handler();
        llVideoFrames = (LinearLayout) findViewById(R.id.ll_videoFrames);
        rlRangeSelection = (RelativeLayout) findViewById(R.id.ll_rangeSelection);
        rl_trimrangeSelection = (RelativeLayout) findViewById(R.id.ll_trimrangeSelection);
        hsvScroll = (HorizontalScrollView) findViewById(R.id.hsv_scroll);
        mVideoView = (VideoView) findViewById(R.id.video_view);
        Button btn_saveTrim = (Button) findViewById(R.id.btn_saveTrim);
        int date = new Date().getSeconds();
        final String newPath = getInternalDirectoryPath() + "/Download/trimmedVideo" + date + ".mp4";
        btn_saveTrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (minTrim != -1 || maxTrim != -1) {
                    if (minTrim > 0 || maxTrim < maxValue - 2) {
                        DialogHelper.showProgressDialog(mContext, R.string.preapring_trim, false);

                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    TrimVideo.trimVideo(videoPath, newPath, minTrim, maxTrim);
                                    videoPath = newPath;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        DialogHelper.dismissProgressDialog();
                                        Intent showVideoIntent = new Intent(TrimActivity.this, VideoPreviewActivity.class);
                                        showVideoIntent.putExtra("min", minSlow);
                                        showVideoIntent.putExtra("max", maxSlow);
                                        showVideoIntent.putExtra("videoPath", videoPath);
                                        showVideoIntent.putExtra("playBackSpeed", playbackSpeed);
                                        finish();
                                        startActivity(showVideoIntent);
                                    }
                                });
                            }
                        });
                        t.start();
                    } else {
                        Intent showVideoIntent = new Intent(TrimActivity.this, VideoPreviewActivity.class);
                        showVideoIntent.putExtra("min", minSlow);
                        showVideoIntent.putExtra("max", maxSlow);
                        showVideoIntent.putExtra("videoPath", videoPath);
                        showVideoIntent.putExtra("playBackSpeed", playbackSpeed);
                        finish();
                        startActivity(showVideoIntent);
                    }
                } else {
                    Intent showVideoIntent = new Intent(TrimActivity.this, VideoPreviewActivity.class);
                    showVideoIntent.putExtra("min", minSlow);
                    showVideoIntent.putExtra("max", maxSlow);
                    showVideoIntent.putExtra("videoPath", videoPath);
                    showVideoIntent.putExtra("playBackSpeed", playbackSpeed);
                    finish();
                    startActivity(showVideoIntent);
                }
            }
        });
    }

    private void showVideo(String value) {
// add RangeSeekBar to pre-defined layout
        maxValue = bitMapList.size() - 1;
        mRangeSeekBar.setRangeValues(0, maxValue);
        trimRangeSeekBar.setRangeValues(0, maxValue);
        DialogHelper.dismissProgressDialog();
        MediaController mController = new MediaController(this);
        mVideoView.setMediaController(mController);
        if (Build.VERSION.SDK_INT < 19)
            mVideoView.setRotation(90);
        else
            mVideoView.setRotation(0);
        mVideoView.setVideoPath(value);
        mVideoView.requestFocus();
        mVideoView.start();

    }

    private void loadFrames(final String videoPath) {
        DialogHelper.showProgressDialog(mContext, R.string.loading_frames_for_trimm, false);
        bitMapList = new ArrayList<Bitmap>();
        imagesList = new ArrayList<ImageView>();
        final Handler hn = new Handler();
        final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, Uri.parse(videoPath));
        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int timeinMilliSec = Integer.valueOf(duration);
//        int timeinMilliSec = Integer.valueOf(duration);
        videoNanoSec = timeinMilliSec * 1000;
        // unit: microsecond
        try {
            // instantiate MediaMetadataRetriever for the video
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    int time = 0;
                    // Bitmap[] b = new Bitmap[5000];
                    while (time < videoNanoSec) {
                        Bitmap frame = retriever.getFrameAtTime(time, MediaMetadataRetriever.OPTION_CLOSEST);
                        bitMapList.add(frame);
                        time += 1000000;
                    }
                    hn.post(new Runnable() {
                        @Override
                        public void run() {
                            llVideoFrames.removeAllViews();
                            imagesList.clear();
                            int index = 0;
                            Bitmap frame_bitMap;
                            for (int i = 0; i < bitMapList.size(); i += 3) {
                                final ImageView image = new ImageView(mContext);
                                RelativeLayout.LayoutParams vg = new RelativeLayout.LayoutParams(150, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                frame_bitMap = bitMapList.get(i);
                                image.setLayoutParams(vg);
                                image.setScaleType(ImageView.ScaleType.FIT_XY);
                                image.setPadding(2, 0, 2, 0);
                                image.setImageBitmap(frame_bitMap);
                                image.setTag(i);

                                llVideoFrames.addView(image, index);
                                imagesList.add(image);
                                index++;
                            }
                            showVideo(videoPath);

                        }
                    });
                }
            });
            t.start();

        } catch (Exception e) {
            Log.e("GUN", Log.getStackTraceString(e));
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            initPreviewVideo(videoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
//        mMediaPlayer.stop();
    }


    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();

    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

}
