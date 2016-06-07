package mobistingdemo.com.vf.Mobisting.HelperClasses;

import android.app.Activity;
import android.content.Intent;

import io.vov.vitamio.Vitamio;

/**
 * Created by amna.mirza on 4/6/2016.
 */
public class VitamioLibChecker {

    public static final String FROM_ME = "fromVitamioInitActivity";

    public static final boolean checkVitamioLibs(Activity ctx) {
        if (!Vitamio.isInitialized(ctx) && !ctx.getIntent().getBooleanExtra(FROM_ME, false)) {
            Intent i = new Intent();
            i.setClassName(Vitamio.getVitamioPackage(), "io.vov.vitamio.activity.InitActivity");
            i.putExtras(ctx.getIntent());
            i.setData(ctx.getIntent().getData());
            i.putExtra("package", ctx.getPackageName());
            i.putExtra("className", ctx.getClass().getName());
            ctx.startActivity(i);
            ctx.finish();
            return false;
        }
        return true;
    }
}

