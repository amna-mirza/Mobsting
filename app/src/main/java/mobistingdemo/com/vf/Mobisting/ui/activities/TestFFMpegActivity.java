package mobistingdemo.com.vf.Mobisting.ui.activities;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import mobistingdemo.com.vf.Mobisting.HelperClasses.DialogHelper;

/**
 * Created by amna.mirza on 4/12/2016.
 */
public class TestFFMpegActivity {
    private static String cmd, cmd1, VideoIn, VideoOut, VideoSlow, VideoAfterEffect;

    private static FFmpeg ffmpeg;
    private static Context mContext;
    private static boolean videoreadytoslow;
    private Button btn_cutVideo;


    public static String getInternalDirectoryPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static void saveVideoSlowPart(Context context, String path) {
        mContext = context;
        ffmpeg = FFmpeg.getInstance(context);
//        VideoIn = "/storage/emulated/0/DCIM/testvideos/ninesec.mp4";
//        VideoOut = getInternalDirectoryPath() + "/parttoslow.mp4";
//        cmd ="ffmpeg -i "+path+" -vf \"setpts=2.0*PTS\" "+VideoOut;


//        VideoIn = getInternalDirectoryPath() + "/Download/Ab1s.mp4";
//        VideoOut = getInternalDirectoryPath() + "/Download/Ab331.mp4";
//
//        VideoIn = getInternalDirectoryPath() + "/Download/Ab1.mp4";
//        VideoOut = getInternalDirectoryPath() + "/Download/Ab2.mp4";
//
//        VideoIn = getInternalDirectoryPath() + "/Download/Ab4s.mp4";
//        VideoOut = getInternalDirectoryPath() + "/Download/Ab444.mp4";
//
//        VideoIn = getInternalDirectoryPath() + "/Download/Ab5s.mp4";
//        VideoOut = getInternalDirectoryPath() + "/Download/Ab5555.mp4";
//
//        VideoIn = getInternalDirectoryPath() + "/Download/Ab6s.mp4";
//        VideoOut = getInternalDirectoryPath() + "/Download/Ab766.mp4";
//
        VideoIn = getInternalDirectoryPath() + "/Download/Ab.mp4";
        VideoOut = getInternalDirectoryPath() + "/Download/Ab10226.mp4";
        cmd = "-i "+VideoIn+" -vf setpts=2*PTS -c:v libx264 -c:a aac -strict experimental -vcodec libx264 -preset ultrafast -b:a 128k "+VideoOut;
//        cmd = "-i "+VideoIn+" -vf setpts=2*PTS -strict -2 "+VideoOut;
//
//
        //cmd = "-i "+VideoIn+" -vf setpts=2*PTS "+VideoOut;
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {
                }

                @Override
                public void onFailure() {
                }

                @Override
                public void onSuccess() {
                }

                @Override
                public void onFinish() {
                    CutVideo();
                }
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
        }
    }



    private static void CutVideo(){
        try {
            ffmpeg.execute(cmd ,
                    new ExecuteBinaryResponseHandler() {

                        @Override
                        public void onStart() {
                            //for logcat
                            Log.w(null,"Cut started");
                        }

                        @Override
                public void onProgress(String message) {
                    //for logcat
                    Log.w(null, "slowVideo onProgress");
                }

                @Override
                public void onFailure(String message) {
                    DialogHelper.dismissProgressDialog();
                    Toast.makeText(mContext, "Video slowing failed ", Toast.LENGTH_LONG).show();
                    Log.w(null, message.toString());
                }

                @Override
                public void onSuccess(String message) {
                    DialogHelper.dismissProgressDialog();
                    Toast.makeText(mContext, "Video slowing success ", Toast.LENGTH_LONG).show();
                    Log.w(null, message.toString());
                }

                @Override
                public void onFinish() {
                    DialogHelper.dismissProgressDialog();
                    Toast.makeText(mContext, "Video slowing finished ", Toast.LENGTH_LONG).show();


                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            Toast.makeText(mContext, "Video slowing failed due to exception while cutting", Toast.LENGTH_LONG).show();
            DialogHelper.dismissProgressDialog();

            // Handle if FFmpeg is already running
            e.printStackTrace();
            Log.w(null, e.toString());
        }
    }
}
