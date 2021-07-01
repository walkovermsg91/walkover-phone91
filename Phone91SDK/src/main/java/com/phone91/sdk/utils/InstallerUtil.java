package com.phone91.sdk.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInstaller;
import android.os.Build;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class InstallerUtil {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void install(Context context, String packageName, String apkPath) throws IOException {
        PackageInstaller packageInstaller = context.getPackageManager().getPackageInstaller();

        // Prepare params for installing one APK file with MODE_FULL_INSTALL
        // We could use MODE_INHERIT_EXISTING to install multiple split APKs
        PackageInstaller.SessionParams params = new PackageInstaller
                .SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL);

        params.setAppPackageName(packageName);

        // Get a PackageInstaller.Session for performing the actual update
        int sessionId;
        PackageInstaller.Session session = null;
        sessionId = packageInstaller.createSession(params);
        session = packageInstaller.openSession(sessionId);

        // Copy APK file bytes into OutputStream provided by install Session
        OutputStream out = session.openWrite(packageName, 0, -1);
        FileInputStream fis = new FileInputStream(new File(apkPath));

        long sizeBytes = 0;
        out = session.openWrite("myappone", 0, sizeBytes);

        int total = 0;
        byte[] buffer = new byte[65536];
        int c;
        while ((c = fis.read(buffer)) != -1) {
            total =  total + c;
            out.write(buffer, 0, c);
        }
        fis.close();
        out.close();

        session.commit(createIntentSender(context,sessionId));
        session.close();
    }

    private static IntentSender createIntentSender(Context context, int sessionId) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                sessionId,
                new Intent("android.intent.action.MAIN"),
                0);
        return pendingIntent.getIntentSender();
    }
}
