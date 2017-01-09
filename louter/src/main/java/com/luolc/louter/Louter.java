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

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.luolc.louter.navigator.CentralNavigator;

/**
 * @author LuoLiangchen
 * @since 2017/1/7
 */
public final class Louter {

  @NonNull
  private final String mBaseUrl;

  @SuppressWarnings("WeakerAccess")
  Louter(@NonNull final String baseUrl) {
    mBaseUrl = baseUrl;
  }

  public CentralNavigator with(@NonNull final Context context) {
    return new CentralNavigator(context, mBaseUrl);
  }

  public CentralNavigator with(@NonNull final Activity activity) {
    return new CentralNavigator(activity, mBaseUrl);
  }

  public CentralNavigator with(@NonNull final Fragment fragment) {
    return new CentralNavigator(fragment, mBaseUrl);
  }

  public CentralNavigator with(@NonNull final android.app.Fragment fragment) {
    return new CentralNavigator(fragment, mBaseUrl);
  }

  public static final class Builder {

    private String mBaseUrl;

    public Builder baseUrl(@NonNull final String baseUrl) {
      mBaseUrl = baseUrl;
      return this;
    }

    public Louter build() {
      return new Louter(mBaseUrl);
    }
  }
}
