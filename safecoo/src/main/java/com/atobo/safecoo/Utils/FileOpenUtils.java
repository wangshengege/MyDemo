package com.atobo.safecoo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.atobo.safecoo.R;

import java.io.File;

import arg.mylibrary.utils.LogTools;
import arg.mylibrary.utils.Tools;
import arg.mylibrary.utils.Utils;

/**
 * 作者: ws
 * 日期: 2016/4/18.
 * 介绍：
 */
public class FileOpenUtils {
    public enum FileType {
        OTHER(0), HTML(1), IMAGE(2), PDF(3), TEXT(4), AUDIO(5), VODIO(6), CHM(7), WORD(8), EXCEL(9), PPT(10),APK(11),ZIP(12);
        public int code;
        public int icon;

        FileType(int code) {
            this.code = code;
            switch (code){
                case 0:
                    icon= R.drawable.format_unkown;
                    break;
                case 1:
                    icon=R.drawable.format_html;
                    break;
                case 3:
                    icon= R.drawable.format_pdf;
                    break;
                case 4:
                    icon=R.drawable.format_text;
                    break;
                case 5:
                    icon= R.drawable.format_music;
                    break;
                case 6:
                    icon=R.drawable.format_media;
                    break;
                case 7:
                    icon= R.drawable.format_chm;
                    break;
                case 8:
                    icon=R.drawable.format_word;
                    break;
                case 9:
                    icon= R.drawable.format_excel;
                    break;
                case 10:
                    icon=R.drawable.format_ppt;
                    break;
                case 11:
                    icon= R.drawable.format_apk;
                    break;
                case 2:
                    icon=R.drawable.format_picture;
                    break;
                case 12:
                    icon=R.drawable.format_zip;
                    break;
            }
        }
    }

    public static void openFile(Context ctx, String path) {
        LogTools.i("FileOpenUtils","open file:"+path);
        if(TextUtils.isEmpty(path)){
            LogTools.i("FileOpenUtils","file path is null");
            return;
        }
        Intent intent = null;
        switch (getFileType(path)) {
            case HTML:
                intent = getHtmlFileIntent(path);
                break;
            case IMAGE:
                intent = getImageFileIntent(path);
                break;
            case PDF:
                intent = getPdfFileIntent(path);
                break;
            case TEXT:
                intent = getTextFileIntent(path, false);
                break;
            case AUDIO:
                intent = getAudioFileIntent(path);
                break;
            case VODIO:
                intent = getVideoFileIntent(path);
                break;
            case CHM:
                intent = getChmFileIntent(path);
                break;
            case WORD:
                intent = getWordFileIntent(path);
                break;
            case EXCEL:
                intent = getExcelFileIntent(path);
                break;
            case PPT:
                intent = getPptFileIntent(path);
                break;
            case APK:
                intent=getApkIntent(path);
                break;
            case ZIP:
                intent=getZipFileIntent(path);
                break;
        }
        if(Tools.isEmpty(intent)){
            Tools.showToast(ctx, "未找到可用软件");
        }else
        ctx.startActivity(intent);
    }

    public static FileType getFileType(String path) {
        String eN = Utils.getExtensionName(path);
        if (eN.equals("html") || eN.equals("HTML") || eN.equals("htm")) {
            return FileType.HTML;
        } else if (eN.equals("png") || eN.equals("PNG")
                || eN.equals("jpg") ||eN.equals("JPG")
                || eN.equals("gpeg")||eN.equals("GPEG")
                || eN.equals("bmp")) {
            return FileType.IMAGE;
        }else if(eN.equals("pdf")||eN.equals("PDF")){
            return FileType.PDF;
        }else if(eN.equals("txt")||eN.equals("TXT")){
            return FileType.TEXT;
        }else if (eN.equals("amr") || eN.equals("AMR")
                ||eN.equals("mid") || eN.equals("mp3") || eN.equals("MP3") || eN.equals("ogg")|| eN.equals("3gpp")){
            return FileType.AUDIO;
        }else if(eN.equals("3gp") || eN.equals("3GP")
                || eN.equals("mp4") || eN.equals("MP4") || eN.equals("avi")){
            return FileType.VODIO;
        }else if(eN.equals("doc") || eN.equals("docx")||eN.equals("rtf")){
            return FileType.WORD;
        }else if (eN.equals("xls")||eN.equals("xlsx")){
            return FileType.EXCEL;
        }else if(eN.equals("ppt")|| eN.equals("pptx")){
            return FileType.PPT;
        }else if(eN.equals("apk")){
            return FileType.APK;
        }else if(eN.equals("zip") || eN.equals("rar")){
            return FileType.ZIP;
        }
        return FileType.OTHER;
    }
    //打开apk文件
    public static Intent getApkIntent(String file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(file)),
                "application/vnd.android.package-archive");
        return intent;
    }
    //android获取一个用于打开HTML文件的intent
    public static Intent getHtmlFileIntent(String param) {

        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.setDataAndType(uri, "text/html");

        return intent;

    }

    //android获取一个用于打开图片文件的intent

    public static Intent getImageFileIntent(String param)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(new File(param));

        intent.setDataAndType(uri, "image/*");

        return intent;

    }

    //android获取一个用于打开PDF文件的intent

    public static Intent getPdfFileIntent(String param)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(new File(param));

        intent.setDataAndType(uri, "application/pdf");

        return intent;

    }

    //android获取一个用于打开文本文件的intent

    public static Intent getTextFileIntent(String param, boolean isUri)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (isUri)

        {

            Uri uri1 = Uri.parse(param);

            intent.setDataAndType(uri1, "text/plain");

        } else

        {

            Uri uri2 = Uri.fromFile(new File(param));

            intent.setDataAndType(uri2, "text/plain");

        }

        return intent;

    }

    //android获取一个用于打开音频文件的intent

    public static Intent getAudioFileIntent(String param)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("oneshot", 0);

        intent.putExtra("configchange", 0);

        Uri uri = Uri.fromFile(new File(param));

        intent.setDataAndType(uri, "audio/*");

        return intent;

    }

    //android获取一个用于打开视频文件的intent

    public static Intent getVideoFileIntent(String param)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("oneshot", 0);

        intent.putExtra("configchange", 0);

        Uri uri = Uri.fromFile(new File(param));

        intent.setDataAndType(uri, "video/*");

        return intent;

    }

    //android获取一个用于打开CHM文件的intent

    public static Intent getChmFileIntent(String param)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(new File(param));

        intent.setDataAndType(uri, "application/x-chm");

        return intent;

    }

    //android获取一个用于打开Word文件的intent

    public static Intent getWordFileIntent(String param)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(new File(param));

        intent.setDataAndType(uri, "application/msword");

        return intent;

    }

    //android获取一个用于打开Excel文件的intent

    public static Intent getExcelFileIntent(String param)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(new File(param));

        intent.setDataAndType(uri, "application/vnd.ms-excel");

        return intent;

    }

    //android获取一个用于打开PPT文件的intent

    public static Intent getPptFileIntent(String param)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(new File(param));

        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");

        return intent;

    }
    //android获取一个用于打开压缩文件的intent

    public static Intent getZipFileIntent(String param)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(new File(param));

        intent.setDataAndType(uri, "application/zip");

        return intent;

    }
}
