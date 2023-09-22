package com.yhy.xposed.xp91.xp;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.yhy.xposed.xp91.xp.hook.HomeActivityHook;
import com.yhy.xposed.xp91.xp.hook.ViewContentActivityHook;

import java.util.Arrays;
import java.util.Objects;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created on 2023-09-22 12:52
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class MainHook implements IXposedHookZygoteInit, IXposedHookLoadPackage, IXposedHookInitPackageResources {
    private static final String[] HOOKED_PKG_NAMES = {
        "vip.pqvln.mykgnx"
    };

    private Context mContext;
    private ClassLoader mClassLoader;
    private String mModulePath;

    public MainHook() {
        HookRegister.instance
            .add(new HomeActivityHook())
            .add(new ViewContentActivityHook());
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam pkgParam) throws Throwable {
        // 1、handleInitPackageResources 先加载资源
        // 2、handleLoadPackage 再加载包
        if (Arrays.stream(HOOKED_PKG_NAMES).noneMatch(it -> Objects.equals(it, pkgParam.packageName))) {
            // 不必要的应用
            return;
        }

        hookApp(() -> {
            Class<?> vApp = XposedHelpers.findClass("com.dft.shot.android.app.VideoApplication", mClassLoader);
            XposedHelpers.findAndHookMethod(vApp, "onCreate", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Toast.makeText(mContext, "Xp91模块加载成功", Toast.LENGTH_SHORT).show();

                    HookRegister.instance.pkgApply(pkgParam, mContext);
                }
            });
        });
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resParam) throws Throwable {
        if (Arrays.stream(HOOKED_PKG_NAMES).noneMatch(it -> Objects.equals(it, resParam.packageName))) {
            // 不必要的应用
            return;
        }

        hookApp(() -> {
            HookRegister.instance.resApply(mModulePath, resParam);
        });
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        mModulePath = startupParam.modulePath;
    }

    private void hookApp(Applier applier) {
        if (null == mContext || null == mClassLoader) {
            // 对于被加固过的应用，需要先获取到其自己的 ClassLoader
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    mContext = (Context) param.args[0];
                    mClassLoader = mContext.getClassLoader();
                    applier.apply();
                }
            });
        } else {
            applier.apply();
        }
    }

    @FunctionalInterface
    interface Applier {

        void apply();
    }
}
