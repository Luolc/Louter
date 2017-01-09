/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Luolc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.luolc.louter.navigator;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.AnimRes;
import android.support.v4.app.Fragment;

import com.luolc.louter.TargetActivityInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LuoLiangchen
 * @since 2017/1/7
 */
abstract class AbstractNavigator<N extends AbstractNavigator> {

  private static final int REQUEST_CODE_NONE = Integer.MAX_VALUE;

  @SuppressWarnings("ThrowableInstanceNeverThrown")
  private static final IllegalStateException INVALID_STARTER_EXCEPTION
      = new IllegalStateException("starter should be an instance of Activity or Fragment");

  private static final Object TARGETS_LOCK = new Object();

  private static volatile Map<String, TargetActivityInfo> sCachedTargets;

  private final Intent mIntent = new Intent(Intent.ACTION_VIEW);

  private final String mBaseUrl;

  private final Object mStarter;

  private final String mPath;

  private int mRequestCode = REQUEST_CODE_NONE;

  private int mFlags;

  @AnimRes
  private int mEnterAnim;

  @AnimRes
  private int mExitAnim;

  AbstractNavigator(final String baseUrl, final String path, final Object starter) {
    mBaseUrl = baseUrl;
    mPath = path;
    mStarter = starter;
  }

  private static Map<String, TargetActivityInfo> getCachedTargets() {
    Map<String, TargetActivityInfo> targets = sCachedTargets;
    if (targets == null) {
      synchronized (TARGETS_LOCK) {
        targets = sCachedTargets;
        if (targets == null) {
          targets = new HashMap<>();
          sCachedTargets = targets;
        }
      }
    }
    return targets;
  }

  public void start() {
    if (!isAllRequiredParamsExist()) {
      throw new IllegalStateException("required params is absence");
    }
    mIntent.setFlags(mFlags);

    setupTargetActivityClass(getContext());

    if (mRequestCode == REQUEST_CODE_NONE) {
      startActivity();
    } else {
      startActivityForResult();
    }

    if (mEnterAnim != 0) {
      getStarterActivity().overridePendingTransition(mEnterAnim, mExitAnim);
    }
  }

  private void setupTargetActivityClass(final Context context) {
    final String url = mBaseUrl + mPath;
    final TargetActivityInfo target = getCachedTargets().get(url);
    if (target != null) {
      mIntent.setClassName(target.packageName(), target.className());
      return;
    }

    mIntent.setData(Uri.parse(url));
    mIntent.setPackage(context.getPackageName());
    final PackageManager pm = context.getPackageManager();
    final ResolveInfo info = pm.resolveActivity(mIntent, PackageManager.MATCH_DEFAULT_ONLY);
    if (info == null) {
      throw new ActivityNotFoundException("no activity found with " + mIntent);
    } else {
      final TargetActivityInfo newTarget
          = new TargetActivityInfo(info.activityInfo.packageName, info.activityInfo.name);
      mIntent.setClassName(newTarget.packageName(), newTarget.className());
      getCachedTargets().put(url, newTarget);
    }
  }

  private Context getContext() {
    Context context;
    if (mStarter instanceof Activity) {
      context = (Activity) mStarter;
    } else if (mStarter instanceof Fragment) {
      context = ((Fragment) mStarter).getContext();
    } else if (mStarter instanceof android.app.Fragment) {
      context = ((android.app.Fragment) mStarter).getActivity();
    } else {
      throw INVALID_STARTER_EXCEPTION;
    }
    return context.getApplicationContext();
  }

  private Activity getStarterActivity() {
    Activity starterActivity;
    if (mStarter instanceof Activity) {
      starterActivity = (Activity) mStarter;
    } else if (mStarter instanceof Fragment) {
      starterActivity = ((Fragment) mStarter).getActivity();
    } else if (mStarter instanceof android.app.Fragment) {
      starterActivity = ((android.app.Fragment) mStarter).getActivity();
    } else {
      throw INVALID_STARTER_EXCEPTION;
    }
    return starterActivity;
  }

  private void startActivity() {
    if (mStarter instanceof Activity) {
      ((Activity) mStarter).startActivity(mIntent);
    } else if (mStarter instanceof Fragment) {
      ((Fragment) mStarter).startActivity(mIntent);
    } else if (mStarter instanceof android.app.Fragment) {
      ((android.app.Fragment) mStarter).startActivity(mIntent);
    } else {
      throw INVALID_STARTER_EXCEPTION;
    }
  }

  private void startActivityForResult() {
    if (mStarter instanceof Activity) {
      ((Activity) mStarter).startActivityForResult(mIntent, mRequestCode);
    } else if (mStarter instanceof Fragment) {
      ((Fragment) mStarter).startActivityForResult(mIntent, mRequestCode);
    } else if (mStarter instanceof android.app.Fragment) {
      ((android.app.Fragment) mStarter).startActivityForResult(mIntent, mRequestCode);
    } else {
      throw INVALID_STARTER_EXCEPTION;
    }
  }

  @SuppressWarnings("unchecked")
  public N addFlags(final int flags) {
    mFlags |= flags;
    return (N) this;
  }

  @SuppressWarnings("unchecked")
  public N forResult(final int requestCode) {
    mRequestCode = requestCode;
    return (N) this;
  }

  @SuppressWarnings("unchecked")
  public N withAnim(@AnimRes final int enterAnim, @AnimRes final int exitAnim) {
    mEnterAnim = enterAnim;
    mExitAnim = exitAnim;
    return (N) this;
  }

  abstract boolean isAllRequiredParamsExist();

  Intent getIntent() {
    return mIntent;
  }
}
