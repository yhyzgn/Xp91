package com.yhy.xposed.xp91.xp.hook;

import android.content.Context;

import com.yhy.xposed.xp91.xp.HookHandler;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * HomeActivity
 * <p>
 * Created on 2023-09-22 13:24
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class HomeActivityHook implements HookHandler {

    @Override
    public void pkgApply(XC_LoadPackage.LoadPackageParam pkgParam, Context appContext) {
        // 去除首页广告弹窗
        Class<?> clazz = XposedHelpers.findClass("com.dft.shot.android.ui.HomeActivity", pkgParam.classLoader);
        XposedHelpers.findAndHookMethod(clazz, "J0", String.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                return null;
            }
        });
    }

    @Override
    public void resApply(String modulePath, XC_InitPackageResources.InitPackageResourcesParam resParam) {
    }
}
