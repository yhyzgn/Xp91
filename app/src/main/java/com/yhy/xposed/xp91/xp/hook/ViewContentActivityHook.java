package com.yhy.xposed.xp91.xp.hook;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.yhy.xposed.xp91.xp.HookHandler;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * ViewContentActivity
 * <p>
 * Created on 2023-09-22 14:38
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class ViewContentActivityHook implements HookHandler {
    @Override
    public void pkgApply(XC_LoadPackage.LoadPackageParam pkgParam, Context appContext) {
        // 完整视频播放页面
        Class<?> clazz = XposedHelpers.findClass("com.dft.shot.android.ui.VideoContentActivity", pkgParam.classLoader);

        XposedHelpers.findAndHookMethod(clazz, "initView", new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Object s = XposedHelpers.getObjectField(param.thisObject, "s");

                // 爱心
                Object v0 = XposedHelpers.getObjectField(s, "V0");
                // 评论
                Object w0 = XposedHelpers.getObjectField(s, "W0");
                // 分享
                Object y0 = XposedHelpers.getObjectField(s, "Y0");

                // 分享事件重写
                XposedHelpers.callMethod(y0, "setOnClickListener", (View.OnClickListener) v -> {
                    Object b0 = XposedHelpers.getObjectField(param.thisObject, "B0");
                    String title = (String) XposedHelpers.getObjectField(b0, "title");
                    String playUrl = (String) XposedHelpers.getObjectField(b0, "playUrl");
                    if (!TextUtils.isEmpty(playUrl)) {
                        playUrl = playUrl.replace("10play", "long").replace("18play", "long");
                    }
                    XposedBridge.log("title = " + title + ", playUrl = " + playUrl);

                    Intent sender = new Intent(Intent.ACTION_SEND);
                    sender.putExtra("name", title);
                    sender.putExtra("url", playUrl);
                    sender.setType("text/plain");

                    Intent chooser = Intent.createChooser(sender, "好东西要分享");
                    XposedHelpers.callMethod(param.thisObject, "startActivity", chooser);
                });
            }
        });
    }

    @Override
    public void resApply(String modulePath, XC_InitPackageResources.InitPackageResourcesParam resParam) {
    }
}
