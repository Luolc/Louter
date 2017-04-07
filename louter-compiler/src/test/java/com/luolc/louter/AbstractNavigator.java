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

package com.luolc.louter;

import com.google.common.annotations.VisibleForTesting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.AnimRes;

import java.util.HashMap;
import java.util.Map;

/** A fake {@code com.luolc.louter.AbstractNavigator}. */
public abstract class AbstractNavigator<N extends AbstractNavigator> {

  private static final int REQUEST_CODE_NONE = Integer.MAX_VALUE;

  @SuppressWarnings("ThrowableInstanceNeverThrown")
  private static final IllegalStateException INVALID_STARTER_EXCEPTION
      = new IllegalStateException("starter should be an instance of Activity or Fragment");

  private static final Object TARGETS_LOCK = new Object();

  private static volatile Map<String, TargetActivityInfo> sCachedTargets;

  private final Intent mIntent = new Intent();

  private final String mBaseUrl;

  private final Object mStarter;

  private final String mPath;

  private int mRequestCode = REQUEST_CODE_NONE;

  private int mFlags;

  @AnimRes
  private int mEnterAnim;

  @AnimRes
  private int mExitAnim;

  protected AbstractNavigator(final String baseUrl, final String path, final Object starter) {
    mBaseUrl = baseUrl;
    mPath = path;
    mStarter = starter;
  }

  static void putUrlActivityToCache(final String url, final Class<?> clazz) {
    final TargetActivityInfo target
        = new TargetActivityInfo(clazz.getPackage().getName(), clazz.getName());
    getCachedTargets().put(url, target);
  }

  @VisibleForTesting
  static Map<String, TargetActivityInfo> getCachedTargets() {
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
  }

  private void setupTargetActivityClass(final Context context) {
  }

  private Context getContext() {
    return new Context();
  }

  private Activity getStarterActivity() {
    return new Activity();
  }

  private void startActivity() {
  }

  private void startActivityForResult() {
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

  protected abstract boolean isAllRequiredParamsExist();

  protected Intent getIntent() {
    return mIntent;
  }
}
