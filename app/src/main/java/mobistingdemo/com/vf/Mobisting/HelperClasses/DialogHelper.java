package mobistingdemo.com.vf.Mobisting.HelperClasses;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import mobistingdemo.com.vf.Mobisting.R;


/**
 * All Dialog related methods should be part of this class
 *
 * @author Sheraz Ahmad Khilji on 8/17/2015<br>
 *         Developed by Virtual Force <br>
 *         {@see http://www.virtual-force.com/}
 */
public class DialogHelper {
    public static boolean signUpshow;
    private static Dialog progressDialog;
    private static AlertDialog mAlertDialog;

    private DialogHelper() {
    }


    /**
     * Show progress dialog with title. The progress and alert dialog will automatically when this method is executed.
     *
     * @param context
     * @param titleId    set 0 for hiding title
     * @param messageId
     * @param cancelable
     */
    public static void showProgressDialog(Context context, int titleId,
                                                int messageId, boolean cancelable) {
        dismissProgressDialog();
        hideAlertDialog(context);
        progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_fullscreenprogressbar, null);
        TextView tv_loadingMessage = (TextView) view.findViewById(R.id.tv_loadingMessage);
        tv_loadingMessage
                .setText(context.getResources().getString(messageId));
        progressDialog.setContentView(view);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(cancelable);
        progressDialog.show();
    }

    /**
     * Show progress dialog without title. The progress and alert dialog will automatically when this method is executed.
     *
     * @param context
     * @param messageId
     * @param cancelable
     */
    public static void showProgressDialog(Context context,
                                                int messageId, boolean cancelable) {
        dismissProgressDialog();
        hideAlertDialog(context);


        progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_fullscreenprogressbar, null);
        TextView tv_loadingMessage = (TextView) view.findViewById(R.id.tv_loadingMessage);
        tv_loadingMessage
                .setText(context.getResources().getString(messageId));
        progressDialog.setContentView(view);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(cancelable);
        progressDialog.show();
    }


    /**
     * Dismiss Progress dialog.
     */
    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    /**
     * The progress and alert dialog will automatically when this method is executed.
     *
     * @param context
     * @param message
     */
    public static void showAlertDialog(Context context, String message) {
        dismissProgressDialog();
        hideAlertDialog(context);
        AlertDialog.Builder bld = new AlertDialog.Builder(context);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        mAlertDialog = bld.create();
        mAlertDialog.show();
    }

    /**
     * The progress and alert dialog will automatically when this method is executed.
     *
     * @param context
     * @param title
     * @param message
     */
    public static void showAlertDialog(Context context, String title,
                                       String message) {
        dismissProgressDialog();
        hideAlertDialog(context);
        AlertDialog.Builder bld = new AlertDialog.Builder(context);
        bld.setTitle(title);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        mAlertDialog = bld.create();
        mAlertDialog.show();
    }

    /**
     * The progress and alert dialog will automatically when this method is executed.
     *
     * @param context
     * @param customView
     * @param title
     * @param message
     * @param positiveText
     * @param positiveListener
     * @param negativeText
     * @param negativeListener
     */
    public static void showCustomAlertDialog(Context context, View customView,
                                             String title, String message, String positiveText,
                                             OnClickListener positiveListener, String negativeText,
                                             OnClickListener negativeListener) {
        dismissProgressDialog();
        hideAlertDialog(context);
        AlertDialog.Builder bld = new AlertDialog.Builder(context);
        bld.setTitle(title);
        bld.setMessage(message);
        if (customView != null)
            bld.setView(customView);
        bld.setPositiveButton(positiveText, positiveListener);
        bld.setNegativeButton(negativeText, negativeListener);
        mAlertDialog = bld.create();
        mAlertDialog.setCancelable(true);
        mAlertDialog.show();
    }

    public static void hideAlertDialog(Context context) {
        if (context != null)
            if (mAlertDialog != null && mAlertDialog.isShowing())
                mAlertDialog.dismiss();

    }


}
