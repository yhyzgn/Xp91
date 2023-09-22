package com.yhy.xposed.xp91.xp.hook;

import android.content.Context;
import android.content.res.XModuleResources;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhy.xposed.xp91.R;
import com.yhy.xposed.xp91.xp.HookHandler;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
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
    }

    @Override
    public void resApply(String modulePath, XC_InitPackageResources.InitPackageResourcesParam resParam) {
        XModuleResources modRes = XModuleResources.createInstance(modulePath, resParam.res);

        // 修改下载图标
        resParam.res.setReplacement(resParam.packageName, "drawable", "icon_main_share", modRes.fwd(R.mipmap.ic_download));

        // 修改下载文本
        // 貌似无效
        resParam.res.hookLayout(resParam.packageName, "layout", "activity_video_content", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
                ImageView iv = liparam.view.findViewById(liparam.res.getIdentifier("image_share", "id", resParam.packageName));

                // 下载文本的 TextView 没有 ID，需要从父布局中取得（最后一个控件）
                LinearLayout parent = (LinearLayout) iv.getParent();
                TextView tv = (TextView) parent.getChildAt(parent.getChildCount() - 1);
                tv.setText("下载");
            }
        });
    }
}
