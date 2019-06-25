package com.eram.googleplaycrawler.googleplay.repo;

import com.eram.googleplaycrawler.googleplay.DownloadData;
import com.eram.googleplaycrawler.googleplay.GooglePlay.DetailsResponse;
import com.eram.googleplaycrawler.googleplay.GooglePlayAPI;

import net.dongliu.apk.parser.ApkParser;
import net.dongliu.apk.parser.bean.ApkMeta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

/**
 * Models an Android app in the "appstore".
 *
 * @author patrick
 */
public class AndroidApp {

    private long appId;

    private String packageName;

    private String version;

    private int versionCode;

    private int mainVersion;

    private int patchVersion;

    private String name;

    private String description;

    private List<AppGroup> groups;

    private int minSdk;

    private String minScreen;

    private List<String> usesPermissions;

    /**
     * Check if this app is a member of the given group
     *
     * @param group the group.
     * @return false if not member of the group or no groups have been set.
     */
    public boolean memberOf(AppGroup group) {
        if (groups == null) {
            return false;
        }
        for (AppGroup g : groups) {
            if (g.getGroupId() == group.getGroupId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Wrapper for usesPermissions
     *
     * @return permissions as a list
     */
    public List<String> getUsesPermissions() {
        return usesPermissions;
    }

    /**
     * Wrapper for usesPermissions
     *
     * @param perms list of permissions.
     */
    public void setUsesPermissions(List<String> perms) {
        usesPermissions = perms;
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long id) {
        this.appId = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinSdk() {
        return minSdk;
    }

    public void setMinSdk(int minSdk) {
        this.minSdk = minSdk;
    }

    public String getMinScreen() {
        return minScreen;
    }

    public void setMinScreen(String minScreen) {
        this.minScreen = minScreen;
    }

    public List<AppGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<AppGroup> groups) {
        this.groups = groups;
    }

    public int getMainVersion() {
        return mainVersion;
    }

    public void setMainVersion(int mainVersion) {
        this.mainVersion = mainVersion;
    }

    public int getPatchVersion() {
        return patchVersion;
    }

    public void setPatchVersion(int patchVersion) {
        this.patchVersion = patchVersion;
    }

    public static AndroidApp analyze(File apk) {
        ApkParser apkParser = null;
        FileInputStream fis = null;
        ApkMeta meta = null;
        try {
            apkParser = new ApkParser(apk);
            apkParser.setPreferredLocale(Locale.getDefault());
            meta = apkParser.getApkMeta();
            apkParser.close();
            fis = new FileInputStream(apk);
            fis.close();
        } catch (Exception e) {
            // Lets try to stay compatible with Java6 for now.
            try {
                apkParser.close();
                fis.close();
            } catch (Exception e1) {
            }
            return null;
        }

        AndroidApp ret = new AndroidApp();
        ret.setPackageName(meta.getPackageName());
        if (meta.getLabel() != null) {
            ret.setName(meta.getLabel());
        } else {
            ret.setName(meta.getPackageName());
        }
        ret.setVersionCode(meta.getVersionCode().intValue());
        ret.setVersion(meta.getVersionName());
        ret.setUsesPermissions(meta.getUsesPermissions());
        try {
            ret.setMinSdk(Integer.parseInt(meta.getMinSdkVersion()));
        } catch (NumberFormatException e) {
            // Not that important that we would make a fuss about it.
        }
        return ret;
    }

    public static File downloadApp(GooglePlayAPI api, String doc, int ot) {

        int vcode = -1;
        int len;
        byte[] buffer = new byte[1024 * 8];
        try {
            DetailsResponse dr = api.details(doc);
            vcode = dr.getDocV2().getDetails().getAppDetails().getVersionCode();
        } catch (IOException e) {
            e.printStackTrace();
        }


        DownloadData data = null;

        try {
            data = api.purchaseAndDeliver(doc, vcode, ot);
        } catch (Exception e) {
            e.printStackTrace();
        }

        File apkFile = new AppInstallerNode(Layout.DEFAULT, doc, vcode).resolve();
        apkFile.getParentFile().mkdirs();
        File mainFile = new AppExpansionMainNode(Layout.DEFAULT, doc,
                data.getMainFileVersion()).resolve();
        File patchFile = new AppExpansionPatchNode(Layout.DEFAULT, doc,
                data.getPatchFileVersion()).resolve();

        try {
            InputStream in = data.openApp();
            OutputStream out = new FileOutputStream(apkFile);
            System.out.println(apkFile);
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
                System.out.print('#');
            }
            System.out.println();
            out.close();
            AndroidApp download = analyze(apkFile);

            if (data.hasMainExpansion()) {
                download.setMainVersion(data.getMainFileVersion());
                in = data.openMainExpansion();
                out = new FileOutputStream(mainFile);
                System.out.println(mainFile);
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                    System.out.print('#');
                }
                System.out.println();
                out.close();
            }
            if (data.hasPatchExpansion()) {
                download.setPatchVersion(data.getPatchFileVersion());
                in = data.openPatchExpansion();
                out = new FileOutputStream(patchFile);
                System.out.println(patchFile);
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                    System.out.print('#');
                }
                System.out.println();
                out.close();
            }


            return apkFile;


        } catch (Exception e) {
            apkFile.delete();
            mainFile.delete();
            patchFile.delete();
            return null;
        }
    }


}
