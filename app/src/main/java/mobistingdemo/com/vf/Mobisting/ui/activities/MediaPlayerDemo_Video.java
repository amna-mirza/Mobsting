/*
 * Copyright (C) 2013 yixia.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mobistingdemo.com.vf.Mobisting.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

//import org.djodjo.widget.MultiSlider;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import mobistingdemo.com.vf.Mobisting.R;

public class MediaPlayerDemo_Video extends Activity implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback {

    private static final String TAG = "MediaPlayerDemo";
    private static final String MEDIA = "media";
    private static final String FILE_PATH = "path";
    private static final int LOCAL_AUDIO = 1;
    private static final int STREAM_AUDIO = 2;
    private static final int RESOURCES_AUDIO = 3;
    private static final int LOCAL_VIDEO = 4;
    private static final int STREAM_VIDEO = 5;
    private int mVideoWidth;
    private int mVideoHeight;
    private MediaPlayer mMediaPlayer;
    private VideoView mPreview;
    private SurfaceHolder holder;
    private String path;
    private Bundle extras;
    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;
    private Context mContext;
    private Button btn_speedControl, btn_normalSpeed, btn_fastSpeed, btn_startTimer, btn_endTimer;
    private SeekBar sb_videoSpeed;
    private TextView tv_videoSpeed;
//    private MultiSlider multiSlider;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.mediaplayer_2);
        mContext = this;
        if (!Vitamio.isInitialized(mContext))
            if (Vitamio.initialize(mContext))
                Log.i("VITAMIO", "initlaized");
            else
                return;
        tv_videoSpeed = (TextView) findViewById(R.id.tv_videoSpeed);
        btn_speedControl = (Button) findViewById(R.id.btn_controlspeed);
        btn_normalSpeed = (Button) findViewById(R.id.btn_normalSpeed);
        btn_fastSpeed = (Button) findViewById(R.id.btn_fastSpeed);
        btn_startTimer = (Button)findViewById(R.id.btn_recordtimestart);
        btn_endTimer = (Button)findViewById(R.id.btn_recordtimeend);
        btn_endTimer.setEnabled(false);
        mPreview = (VideoView) findViewById(R.id.surface);
//        multiSlider = (MultiSlider) findViewById(R.id.range_slider5);
        holder = mPreview.getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.RGBA_8888);

        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(mPreview);
        mPreview.setMediaController(mediaController);
        extras = getIntent().getExtras();
        btn_startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.getDuration();
                btn_endTimer.setEnabled(true);
            }
        });
        btn_endTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_startTimer.setEnabled(false);
                mMediaPlayer.getDuration();
btn_endTimer.setEnabled(false);
            }
        });
        btn_speedControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.setPlaybackSpeed(0.5f);
                tv_videoSpeed.setText("Speed = " + 0.5 + "f");
                sb_videoSpeed.setProgress(0);
            }
        });
        btn_normalSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.setPlaybackSpeed(1.0f);
                tv_videoSpeed.setText("Speed = " + 1.0 + "f");
                sb_videoSpeed.setProgress(100);
            }
        });

        btn_fastSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.setPlaybackSpeed(2.0f);
                tv_videoSpeed.setText("Speed = " + 2.0 + "f");
                sb_videoSpeed.setProgress(200);
            }
        });
//        multiSlider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
//            @Override
//            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
//                if (thumbIndex == 0) {
//                    Log.i("MultiSlider", String.valueOf(value));
//                } else {
//                    Log.i("MultiSlider", String.valueOf(value));
//                }
//            }
//        });

        sb_videoSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                int progress = seekBar.getProgress();
                if (progress <= 25) {
                    mMediaPlayer.setPlaybackSpeed(0.5f);
                    tv_videoSpeed.setText("Speed = " + 0.5 + "f");
                } else if (progress > 25 && progress <= 50) {
                    mMediaPlayer.setPlaybackSpeed(0.6f);
                    tv_videoSpeed.setText("Speed = " + 0.6 + "f");
                } else if (progress > 50 && progress <= 75) {
                    mMediaPlayer.setPlaybackSpeed(0.75f);
                    tv_videoSpeed.setText("Speed = " + 0.75 + "f");
                } else if (progress > 75 && progress < 90) {
                    mMediaPlayer.setPlaybackSpeed(0.85f);
                    tv_videoSpeed.setText("Speed = " + 0.85 + "f");
                } else if (progress >= 90 && progress < 110) {
                    mMediaPlayer.setPlaybackSpeed(1.0f);
                    tv_videoSpeed.setText("Speed = " + 1.0 + "f");
                } else if (progress >= 110 && progress < 125) {
                    mMediaPlayer.setPlaybackSpeed(1.25f);
                    tv_videoSpeed.setText("Speed = " + 1.25 + "f");
                } else if (progress >= 125 && progress < 150) {
                    mMediaPlayer.setPlaybackSpeed(1.5f);
                    tv_videoSpeed.setText("Speed = " + 1.5 + "f");
                } else if (progress >= 150 && progress < 175) {
                    mMediaPlayer.setPlaybackSpeed(1.75f);
                    tv_videoSpeed.setText("Speed = " + 1.75 + "f");
                } else if (progress >= 175 && progress < 190) {
                    mMediaPlayer.setPlaybackSpeed(1.75f);
                    tv_videoSpeed.setText("Speed = " + 1.85 + "f");
                } else if (progress >= 190) {
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

    private void playVideo(Integer Media, String path) {
        doCleanUp();
        try {

            switch (Media) {
                case LOCAL_VIDEO:
                /*
                 * TODO: Set the path variable to a local media file path.
				 */
                    this.path = path;
                    if (path == "") {
                        // Tell the user to provide a media file URL.
                        Toast.makeText(MediaPlayerDemo_Video.this, "Please edit MediaPlayerDemo_Video Activity, " + "and set the path variable to your media file path." + " Your media file must be stored on sdcard.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    break;
                case STREAM_VIDEO:
                /*
                 * TODO: Set path variable to progressive streamable mp4 or
				 * 3gpp format URL. Http protocol should be used.
				 * Mediaplayer can only play "progressive streamable
				 * contents" which basically means: 1. the movie atom has to
				 * precede all the media data atoms. 2. The clip has to be
				 * reasonably interleaved.
				 * 
				 */
                    path = "";
                    if (path == "") {
                        // Tell the user to provide a media file URL.
                        Toast.makeText(MediaPlayerDemo_Video.this, "Please edit MediaPlayerDemo_Video Activity," + " and set the path variable to your media file URL.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    break;

            }

            // Create a new media player and set the listeners
            mMediaPlayer = new MediaPlayer(this);
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    mp.getCurrentPosition();
                }
            });
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            setVolumeControlStream(AudioManager.STREAM_MUSIC);

        } catch (Exception e) {
            Log.e(TAG, "error: " + e.getMessage(), e);
        }
    }

    public void onBufferingUpdate(MediaPlayer arg0, int percent) {
        // Log.d(TAG, "onBufferingUpdate percent:" + percent);

    }

    public void onCompletion(MediaPlayer arg0) {
        Log.d(TAG, "onCompletion called");
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.v(TAG, "onVideoSizeChanged called");
        if (width == 0 || height == 0) {
            Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
            return;
        }
        mIsVideoSizeKnown = true;
        mVideoWidth = width;
        mVideoHeight = height;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    public void onPrepared(MediaPlayer mediaplayer) {
        Log.d(TAG, "onPrepared called");
        mIsVideoReadyToBePlayed = true;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
        Log.d(TAG, "surfaceChanged called");

    }

    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
        Log.d(TAG, "surfaceDestroyed called");
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated called");
        playVideo(extras.getInt(MEDIA), extras.getString(FILE_PATH));

    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
        doCleanUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        doCleanUp();
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
    }

    private void startVideoPlayback() {
        Log.v(TAG, "startVideoPlayback");
        holder.setFixedSize(mVideoWidth, mVideoHeight);
        mMediaPlayer.start();
    }

    public void openVideo(View View) {

    }
}
