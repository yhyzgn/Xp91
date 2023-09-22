package com.yhy.xposed.xp91.xp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created on 2023-09-22 14:01
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class HookRegister {
    public final static HookRegister instance = new HookRegister();

    private final List<HookHandler> mHandlerList = new ArrayList<>();

    private HookRegister() {
    }

    public HookRegister add(HookHandler handler) {
        mHandlerList.add(handler);
        return this;
    }

    public void pkgApply(XC_LoadPackage.LoadPackageParam pkgParam, Context appContext) {
        mHandlerList.forEach(it -> it.pkgApply(pkgParam, appContext));
    }

    public void resApply(String modulePath, XC_InitPackageResources.InitPackageResourcesParam resParam) {
        mHandlerList.forEach(it -> it.resApply(modulePath, resParam));
    }
}
