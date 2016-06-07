package mobistingdemo.com.vf.Mobisting.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import mobistingdemo.com.vf.Mobisting.R;

public class MainActivity extends Activity {
    private static final String MEDIA = "media";
    private static final int LOCAL_VIDEO = 4;
    private static final String FILE_PATH = "path";
    private Context mContext;
    private Button btn_pickImage, btn_pickVideo, btn_takePhoto, btn_testFfmpeg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initUI();
    }

    private void initUI() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Mobsting Demo");
        btn_testFfmpeg = (Button) findViewById(R.id.btn_video_editing);
        btn_testFfmpeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testFFmpeg();
            }
        });
        btn_pickImage = (Button) findViewById(R.id.btn_picture);
        btn_pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImagefromGallery();
            }
        });
        btn_pickVideo = (Button) findViewById(R.id.btn_video);
        btn_pickVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickVideofromGallery();
            }
        });
        btn_takePhoto = (Button) findViewById(R.id.btn_takePhoto);
        btn_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
    }

    private void testFFmpeg() {
        Intent i = new Intent(MainActivity.this, TestFFMpegActivity.class);
        startActivity(i);
    }

    private void captureImage() {

    }

    private void pickVideofromGallery() {// a function to start gallery ot pick video and return to calling activtiy
        if (Build.VERSION.SDK_INT <= 19) {
            Intent i = new Intent();
            i.setType("video/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(i, 20); //using startActivityForResult instead of startActivity because we are starting gallery activty to give us a result in form of video, 20 is code to hint which actvity return result
        } else if (Build.VERSION.SDK_INT > 19) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 20); //using startActivityForResult instead of startActivity because we are starting gallery activty to give us a result in form of video, 20 is code to hint which actvity return result
        }
    }

    private void pickImagefromGallery() { // a function to start gallery ot pick images and return to calling activtiy
        if (Build.VERSION.SDK_INT <= 19) {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(i, 10); //using startActivityForResult instead of startActivity because we are starting gallery activty to give us a result in form of image, 10 is code to hint which actvity return result
        } else if (Build.VERSION.SDK_INT > 19) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 10); //using startActivityForResult instead of startActivity because we are starting gallery activty to give us a result in form of image, 10 is code to hint which actvity return result
        }
    }

    // As we intent gallery activity to get image/video as result, so here we get result and we have to handle with associted request code (e.g 10/20) this request code could be any number
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            if (resultCode == Activity.RESULT_OK) { // check either if video/image has been picked from gallery or cancelled
                Uri selectedImageUri = data.getData();
                String selectedImagePath = null;
                if  (requestCode == 20) {
                    selectedImagePath = getRealPathFromURI(selectedImageUri);
//                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                    retriever.setDataSource(this, Uri.parse(selectedImagePath));
//                    String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//                    int timeinMilliSec = Integer.valueOf(duration) / 1000;
//                    if (timeinMilliSec < 30) {
                        Intent intent = new Intent(MainActivity.this,
                                TrimActivity.class);
                        intent.putExtra(FILE_PATH, selectedImagePath);
                        startActivity(intent);
//                    } else {
//                        Toast.makeText(this, "Video is too large, please select video upto 30 seconds", Toast.LENGTH_SHORT).show();
//                    }

                }
            }
        }
    }

    private String getRealPathFromURI(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

}

