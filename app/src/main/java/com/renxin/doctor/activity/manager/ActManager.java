package com.renxin.doctor.activity.manager;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import com.renxin.doctor.activity.application.DocApplication;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

/**
 * @author: ZhaoYun
 * @date: 2017/10/31
 * @project: customer-android-2th
 * @detail:
 */
public final class ActManager {

    public Application.ActivityLifecycleCallbacks getInnerActivityLifecycleCallbacks() {
        return mActivityLifecycleCallbacks;
    }

    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        /*
         * 弱引用持有，当Activity消失时，stack队列的size会自动减1
         */
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            synchronized (ActManager.this) {
                mActivityStack.push(new WeakReference<>(activity));
                mActivityIdMap.put(activity.hashCode(), mActivityStack.size() - 1);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            synchronized (ActManager.this) {
                final int actHashCodeKey = activity.hashCode();
                if (mActivityIdMap.containsKey(actHashCodeKey)) {
                    mActivityIdMap.remove(actHashCodeKey);
                }
            }
        }
    };

    /**
     * 自定义Activity栈
     */
    private final Stack<WeakReference<Activity>> mActivityStack;//<StackIndex , Activity>
    /**
     * 弱引用保存Application上下文
     */
    private final DocApplication mApplication;
    /**
     * ActivityHashCode => ActivityIndex
     */
    private final Map<Integer, Integer> mActivityIdMap;//<HashCode , StackIndex>

    @Inject
    public ActManager(DocApplication application) {
        mActivityStack = new Stack<>();
        mActivityIdMap = new ConcurrentHashMap<Integer, Integer>();
        mApplication = application;
        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }

    /**
     * 获取当前处于没有被finish状态的Activity集
     *
     * @return
     */
    public Stack<WeakReference<Activity>> getAll() {
        return mActivityStack;
    }

    /**
     * 根据后进先出规律，获取最后压入Stack的Activity
     *
     * @return
     */
    public WeakReference<Activity> currentActivity() {
        if (mActivityStack == null || mActivityStack.isEmpty()) {
            return null;
        }
        return mActivityStack.lastElement();
    }

    /**
     * 将activity(如果存在)杀掉
     *
     * @param activity
     * @return
     */
    public synchronized void finishActivity(Activity activity) {
        final int actHashCodeKey = activity.hashCode();
        if (mActivityIdMap.containsKey(actHashCodeKey)) {
            final int actStacckIndex = mActivityIdMap.get(actHashCodeKey);
            mActivityIdMap.remove(actHashCodeKey);
            if (mActivityStack.size() - 1 >= actStacckIndex && mActivityStack.get(actStacckIndex).get() != null && mActivityStack.get(actStacckIndex).get().hashCode() == actHashCodeKey) {
                final Activity mActivity = mActivityStack.get(actStacckIndex).get();
                if ((Build.VERSION.SDK_INT >= 17 && !mActivity.isDestroyed()) || (Build.VERSION.SDK_INT < 17 && !mActivity.isFinishing())) {
                    mActivity.finish();
                }
            } else {
                activity.finish();
            }
        } else {
            activity.finish();
        }
    }

    /**
     * 结束所有Activity
     */
    public synchronized void finishAllActivity() {
        for (int i = mActivityStack.size() - 1; i >= 0; i--) {
            final Activity activity = getAll().get(i).get();
            if (activity != null) {
                if ((Build.VERSION.SDK_INT >= 17 && !activity.isDestroyed()) || (Build.VERSION.SDK_INT < 17 && !activity.isFinishing())) {
                    activity.finish();
                }
            }
        }
        mActivityStack.clear();
        mActivityIdMap.clear();
    }

    /**
     * 结束所有Activity 除了当前
     */
    public void finishAllActivityWithoutCurrent() {
        //移除 当前actvity
        for (int i = 0; i < mActivityStack.size(); i++) {
            final Activity activity = mActivityStack.get(i).get();
            if (activity != null
                    && currentActivity() != null
                    && !activity.getClass().getName().equals(currentActivity().getClass().getName())) {
                mActivityStack.remove(activity);
                mActivityIdMap.remove(activity.hashCode());
                if ((Build.VERSION.SDK_INT >= 17 && !activity.isDestroyed()) || (Build.VERSION.SDK_INT < 17 && !activity.isFinishing())) {
                    activity.finish();
                }
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
//            ActivityManager activityMgr = (ActivityManager) mApplication.getSystemService(Context.ACTIVITY_SERVICE);
            //activityMgr.killBackgroundProcesses(mApplicationWeakReference.get().getPackageName());
            //android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
