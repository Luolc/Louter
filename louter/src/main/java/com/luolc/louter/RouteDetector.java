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
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

/**
 * @author LuoLiangchen
 * @since 2017/4/5
 */
final class RouteDetector {

  private RouteDetector() {}

  static void detect(final Context context, final String baseUrl) {
    final PackageManager pm = context.getPackageManager();
    try {
      final ActivityInfo[] activities
          = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES).activities;
      for (final ActivityInfo activityInfo : activities) {
        try {
          final Class<?> clazz = Class.forName(activityInfo.name);
          if (!Activity.class.isAssignableFrom(clazz)) {
            throw new IllegalStateException(
                String.format("@Route must annotate Activity class, but not %s", clazz.getName()));
          }
          if (clazz.isAnnotationPresent(Route.class)) {
            final String url = baseUrl + clazz.getAnnotation(Route.class).value();
            AbstractNavigator.putUrlActivityToCache(url, clazz);
          }
        } catch (ClassNotFoundException ignore) {
          // ignore
        }
      }
    } catch (PackageManager.NameNotFoundException ignore) {
      // ignore
    }
  }
}
