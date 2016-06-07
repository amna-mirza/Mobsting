package mobistingdemo.com.vf.Mobisting.ui.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import mobistingdemo.com.vf.Mobisting.HelperClasses.DialogHelper;
import mobistingdemo.com.vf.Mobisting.R;


/**
 * Created by amna.mirza on 9/3/2015.
 * <p/>
 * Diplays video preview after capturing from custom(vidCode) camera
 */
public class VideoPreviewActivity extends Activity {
    private static final String MEDIA = "media";
    private static final int LOCAL_VIDEO = 4;
    private static final String FILE_PATH = "path";
    boolean startslowflag = true;
    boolean endslowflag = true;
    private VideoView mVideoView;
    //    private Button btn_shareVideo, btn_slow;
    private SurfaceHolder holder;
    private String videopath;
    private double playbackSpeed;
    private int startslow, startslowin, endSlow, endSlowin;
    private boolean running;
    private TextView tv_pro;
    private Context mContext;
    private Button btn_save;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videopreview);
        initUi();
        Intent i = getIntent();
        mContext = this;
        videopath = i.getStringExtra("videoPath");
        playbackSpeed = i.getFloatExtra("playBackSpeed", 1.0f);
        startslowin = i.getIntExtra("min", 0);
        endSlowin = i.getIntExtra("max", 10);
        startslow = startslowin * 1000;
        endSlow = endSlowin * 1000;

        if (!Vitamio.isInitialized(this))
            if (Vitamio.initialize(this))
                Log.i("VITAMIO", "initlaized");
            else
                return;
        playVideo(videopath);
    }

    public void playVideo(String videopath) {
        LinearLayout VideoView = (LinearLayout) findViewById(R.id.videoview);
        VideoView.setVisibility(View.VISIBLE);
        mVideoView = (VideoView) findViewById(R.id.surface_view1);
        mVideoView.setVideoPath(videopath);
        final MediaController mediaController = new MediaController(this);
        mVideoView.setMediaController(mediaController);
        mVideoView.requestFocus();
        mVideoView.start();

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mediaPlayer) {
                running = true;

                // optional need Vitamio 4.0
                //mediaPlayer.setPlaybackSpeed(1.0f)
                mediaPlayer.getCurrentPosition();
                //  callSlow(mediaPlayer);
//                final int duration = (int) mVideoView.getDuration();
                new Thread(new Runnable() {
                    public void run() {
                        do {
                            tv_pro.post(new Runnable() {
                                public void run() {

//                                    int time = (int) ((duration - mVideoView.getCurrentPosition()) / 1000);
//                                    if (time <= 0)
//                                        running = false;
                                    if (mVideoView.getCurrentPosition() >= startslow && startslowflag == true) {
                                        mediaPlayer.setPlaybackSpeed((float) playbackSpeed);
                                        startslowflag = false;
                                    } else if (mVideoView.getCurrentPosition() >= endSlow && endslowflag == true) {
                                        mediaPlayer.setPlaybackSpeed(1.0f);
                                        endslowflag = false;
                                    }
                                }
                            });
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
//                            if (!running) break;
                        }
                        while (mContext != null);
                    }
                }).start();

            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                running = false;
            }
        });

    }


    private void showVideo(String value) {
        MediaController mController = new MediaController(this);
        mVideoView.setMediaController(mController);
        if (Build.VERSION.SDK_INT < 19)
            mVideoView.setRotation(90);
        mVideoView.setVideoPath(value);
        mVideoView.requestFocus();
        mVideoView.start();

    }


    private void initUi() {
//        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setTitle("Video Editing Demo");
        tv_pro = (TextView) findViewById(R.id.tv_progress);
        mVideoView = (VideoView) findViewById(R.id.video_view);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSlowpart();
            }
        });
//        btn_shareVideo = (Button) findViewById(R.id.btn_share);
//        btn_slow = (Button) findViewById(R.id.btn_slow);
//        btn_shareVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openMobileShareSheetforVideo();
//            }
//        });
//        btn_slow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                playVideoinSlowMotion();
//            }
//        });
    }

    private void saveSlowpart() {
        DialogHelper.showProgressDialog(mContext, R.string.processin_image, false);
        String start = "00:00:" + startslowin;
        String end = "00:00:" + endSlowin;
        TestFFMpegActivity.saveVideoSlowPart(mContext, videopath);
    }

//    private void playVideoinSlowMotion() {
//        Intent intent = new Intent(VideoPreviewActivity.this, MediaPlayerDemo_Video.class);
//        intent.putExtra(MEDIA, LOCAL_VIDEO);
//        intent.putExtra(FILE_PATH, videopath);
//        startActivity(intent);
//    }
//
//    private void openMobileShareSheetforVideo() {
//        File video = new File(videopath);
//        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//        intent.setType("video/mp4");
//        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + video));
//        startActivity(Intent.createChooser(intent, "Share Video"));
//    }


}
