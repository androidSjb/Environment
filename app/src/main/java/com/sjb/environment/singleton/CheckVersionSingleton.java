package com.sjb.environment.singleton;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import com.litesuits.http.network.Network;
import com.sjb.environment.R;
import com.sjb.environment.util.Constants;
import com.sjb.environment.util.SharePreferenceUtil;
import com.sjb.environment.util.ThreadManager;
import com.sjb.environment.view.CheckVersionDialog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by bjyn21 on 16/2/26.
 */

public class CheckVersionSingleton {

    private static CheckVersionSingleton instance = null;//单例

    private static String apk_path = "";//apk完整路径
    private static String file_path = "";//apk上层文件目录

    private static ProgressBar mProgress;// 进度条与通知UI刷新的handler和message常量
    private static Dialog downloadDialog;
    private static boolean interceptFlag = false;

    private static ThreadManager downLoadThread;
    private static File ApkFile;
    private static FileOutputStream fos;
    private static int progress = 0;

    private Context mContext;
    private SharedPreferences sharedPreferences;
    private boolean isShowDone;
    private String downUrl = "";

    public void init(Context mContext) {
        this.mContext = mContext;
        this.sharedPreferences = mContext.getSharedPreferences(Constants.SharedPherenceKey.SHAREDPERENCEUSERNAME, mContext.MODE_APPEND);
    }

    public static CheckVersionSingleton getInstance() {
        if(instance == null)
        {
            instance = new CheckVersionSingleton();
        }
        return instance;
    }

    /**
     * 网络获取最新版本号
     **/
    public void getLatestVersionCode(int latest_VersionCode) {
        int local_VersionCode = 0;
        try {
            local_VersionCode = getLocalVersionCode();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        boolean hasnewversion = checkIfNeedUpdate(latest_VersionCode, local_VersionCode);//!latest_VersionCode.equals(local_VersionCode);
        if(hasnewversion){
            //有新版本
            if(hasLatestApkDownloaded(latest_VersionCode)){
                installApk();
            }else{
                showUpdateInfo();
            }
        }else{
            if (isShowDone){

                CheckVersionDialog.Builder builder = new CheckVersionDialog.Builder(mContext);
                builder.setMessage(mContext.getResources().getString(R.string.islatestversion));
                builder.setTitle(mContext.getResources().getString(R.string.prompt));
                builder.setPositiveButton(mContext.getResources().getString(R.string.config), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });


                builder.create().show();

            }

        }

    }


    /**
     * 本地已安装应用的版本号 versionName
     **/
    public int getLocalVersionCode() throws PackageManager.NameNotFoundException {
        int locallVersion = 0;
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            locallVersion = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            locallVersion = 0;
            e.printStackTrace();
        }
        return locallVersion;
    }



    public  void showUpdateInfo() {

        if(Network.isConnected(mContext))
        {
            CheckVersionDialog.Builder builder = new CheckVersionDialog.Builder(mContext);
            builder.setMessage(mContext.getResources().getString(R.string.ifupdate));
            builder.setTitle(mContext.getResources().getString(R.string.prompt));
            builder.setPositiveButton(mContext.getResources().getString(R.string.config), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showDownloadDialog();
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton(mContext.getResources().getString(R.string.cancel), new  DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            builder.create().show();

        }else{
            AppSingleton.getInstance().showDialog(mContext.getResources().getString(R.string.networkErrorInfo),mContext,false);
        }

    }

    private  void showDownloadDialog() {
        CheckVersionDialog.Builder builder = new CheckVersionDialog.Builder(mContext);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.undate_progess_new, null);

        Button cencelBtn = (Button) v.findViewById(R.id.cencelBtn);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        cencelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                interceptFlag = true;
                downloadDialog.cancel();
            }
        });
        builder.setContentView(v);
        downloadDialog = builder.create();
        downloadDialog.show();
        downloadApk();
    }

    /**下载apk*/
    private  void downloadApk() {
        if(Network.isConnected(mContext))
        {//有网
            downLoadThread = new ThreadManager(mdownApkRunnable);
            downLoadThread.start();
        }else{//无网
            AppSingleton.getInstance().showDialog(mContext.getResources().getString(R.string.networkErrorInfo),mContext,false);
        }


    }

    /** 保存apk存储的详细地址 **/
    private void rememberApkPath(String apk_path,String file_path){
        SharePreferenceUtil.setPrefString(mContext, Constants.SharedPherenceKey.SHAREDPERENCEUSERNAME, mContext.getResources().getString(R.string.APKPATH), apk_path);
        SharePreferenceUtil.setPrefString(mContext, Constants.SharedPherenceKey.SHAREDPERENCEUSERNAME, mContext.getResources().getString(R.string.FILEPATH),file_path);
    }

    /** 判断文件是否存在 **/
    public boolean fileIsExists(String strFile) {
        try {
            File f=new File(strFile);
            if(!f.exists())
            {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean hasLatestApkDownloaded(int latest_VersionCode){
        if(hasDefaultApkPath()){
            //下载过安装包，则有默认安装地址
            apk_path = this.sharedPreferences.getString(mContext.getResources().getString(R.string.APKPATH),"");
            if(this.fileIsExists(apk_path)){
                PackageManager pm = mContext.getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(apk_path, PackageManager.GET_ACTIVITIES);
                if(info != null) {
                    ApplicationInfo appInfo = info.applicationInfo;
                    String appName = pm.getApplicationLabel(appInfo).toString();
                    String packageName = appInfo.packageName;  //得到安装包名称
                    int version = info.versionCode;       //得到版本信息
                    if (version == latest_VersionCode) {
                        return true;
                    } else {
                        return false;
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }


    public void check(Context context, boolean isShowDone,int latest_VersionCode,String downUrl){
        this.init(context);
        Log.i("LJ", "检测版本");
        this.downUrl = downUrl;
        this.isShowDone = isShowDone;
        this.getLatestVersionCode(latest_VersionCode);

    }

    /**
     * 判断是否需要进行版本更新
     * @param latest_VersionCode 获得的最新版本号
     * @param local_VersionCode  本地获得的版本号
     * @return 是否需要版本更新
     */
    private boolean checkIfNeedUpdate(int latest_VersionCode, int local_VersionCode){

        if(latest_VersionCode>local_VersionCode){
            return true;
        }
        return false;
    }


    /**
     * 判断字符串中的数字的大小
     * @param latest_VersionItem 最新版本中的数
     * @param local_VersionItem  当前版本的数
     * @return 返回LATESTVERSIONISNEW时，最新版本大，需要更新，返回LOCALVERSIONISNEW时，当前版本大，需要更新，相等时，VERSIONEQUAL
     */

    private int comPareDigitalString(String latest_VersionItem, String local_VersionItem){
        int latestNum = -1;
        int localNum = -1;

        try {
            latestNum = Integer.parseInt(latest_VersionItem);
            localNum = Integer.parseInt(local_VersionItem);
        } catch (NumberFormatException e) {
            return  Constants.VersonUpdateValue.LOCALVERSIONISNEW;
        }
        if (latestNum > localNum){
            return  Constants.VersonUpdateValue.LATESTVERSIONISNEW;
        }else if (latestNum < localNum){
            return Constants.VersonUpdateValue.LOCALVERSIONISNEW;
        }else{
            return Constants.VersonUpdateValue.VERSIONEQUAL;
        }
    }

    /**判断手机是否有SD卡**/
    private  boolean isSDCardExistAndReadable() {
        // 获取扩展卡设备状态
        String sdcardState = Environment.getExternalStorageState();
        // 检测SDCard是否存在并且可读写
        if (sdcardState.equals(Environment.MEDIA_MOUNTED))
            return true;
        return false;
    }

    /** 安装apk **/
    @SuppressLint("SdCardPath")
    private void installApk() {
        File apkfile = new File(apk_path);
        if (!apkfile.exists()) {
            return;
        }
        if(apk_path.contains(Constants.DirPath.SDPATH)){
            //从SDcard获取apk安装
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                    "application/vnd.android.package-archive");
            mContext.startActivity(i);
        }else{
            //从/data/data/获取apk安装
            Uri uri = Uri.fromFile(apkfile);
            Intent startGameIntent = new Intent(Intent.ACTION_VIEW, uri);
            startGameIntent.setData(uri);
            startGameIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startGameIntent
                    .setClassName("com.android.packageinstaller",
                            "com.android.packageinstaller.PackageInstallerActivity");
            mContext.startActivity(startGameIntent);
        }

    }

    /** 是否保存过APK存储地址 **/
    private Boolean hasDefaultApkPath(){
        return (this.sharedPreferences.getString(mContext.getResources().getString(R.string.APKPATH),"") != null
                && this.sharedPreferences.getString(mContext.getResources().getString(R.string.APKPATH),"") != "");
    }

    private  Runnable mdownApkRunnable = new Runnable() {
        @SuppressLint("SdCardPath")
        @Override
        public void run() {
            try {
                URL url = new URL(downUrl);

                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                if(hasDefaultApkPath()){
                    //非第一次下载安装包，有默认的安装包存储地址
                    apk_path = sharedPreferences.getString(mContext.getResources().getString(R.string.APKPATH),"");
                    file_path = sharedPreferences.getString(mContext.getResources().getString(R.string.FILEPATH),"");
                    File file = new File(file_path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    ApkFile = new File(apk_path);
                    fos = new FileOutputStream(ApkFile);
                }else{
                    //第一次下载安装包，没有默认地址
                    if (isSDCardExistAndReadable()) {
                        file_path = Constants.DirPath.SDPATH;
                    }else {
                        System.out.println("无SD卡，下载到data");
                        file_path = Constants.DirPath.DATAPATH;
                    }
                    File file = new File(file_path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    apk_path = file_path + Constants.EQUIPMENT + ".apk";
                    ApkFile = new File(apk_path);
                    fos = new FileOutputStream(ApkFile);
                    //记住下载路径
                    rememberApkPath(apk_path,file_path);
                }


                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mProgress.setProgress(progress);
                    if (numread <= 0) {
                        downloadDialog.dismiss();
                        // 下载完成通知安装
                        installApk();
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载.

                fos.close();
                is.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
