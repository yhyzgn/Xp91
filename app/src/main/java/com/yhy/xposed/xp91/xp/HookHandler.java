package com.yhy.xposed.xp91.xp;

import android.content.Context;

import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created on 2023-09-22 14:00
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public interface HookHandler {

    void pkgApply(XC_LoadPackage.LoadPackageParam pkgParam, Context appContext);

    void resApply(String modulePath, XC_InitPackageResources.InitPackageResourcesParam resParam);
}
