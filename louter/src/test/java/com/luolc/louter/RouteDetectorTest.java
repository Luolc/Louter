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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author LuoLiangchen
 * @since 2017/4/5
 */
public class RouteDetectorTest {

  private final Map<String, TargetActivityInfo> mTargets = AbstractNavigator.getCachedTargets();

  private PackageManager mPackageManager = mock(PackageManager.class);

  private Context mContext = mock(Context.class);
  @Before
  public void setUp() {
    when(mContext.getPackageManager()).thenReturn(mPackageManager);
    when(mContext.getPackageName()).thenReturn("com.example");
  }

  @Test
  public void testDefault() throws Exception {
    mTargets.clear();
    PackageInfo packageInfo = createPackageInfo(Foo1.class, Foo3.class);
    when(mPackageManager.getPackageInfo("com.example", PackageManager.GET_ACTIVITIES))
        .thenReturn(packageInfo);
    RouteDetector.detect(mContext, "http://test.com/");

    assertEquals(1, mTargets.size());
    assertTrue(mTargets.containsKey("http://test.com/foo1"));
    TargetActivityInfo target = mTargets.get("http://test.com/foo1");
    assertEquals(Foo1.class.getPackage().getName(), target.packageName());
    assertEquals(Foo1.class.getName(), target.className());
  }

  @Test
  public void testClassNotFoundException() throws Exception {
    mTargets.clear();
    PackageInfo packageInfo = createPackageInfo("not exist", Foo1.class, Foo2.class);
    when(mPackageManager.getPackageInfo("com.example", PackageManager.GET_ACTIVITIES))
        .thenReturn(packageInfo);
    RouteDetector.detect(mContext, "http://test.com/");

    assertEquals(2, mTargets.size());
  }

  @Test
  public void testPackageNameNotFoundException() throws Exception {
    mTargets.clear();
    when(mPackageManager.getPackageInfo("com.example", PackageManager.GET_ACTIVITIES))
        .thenThrow(PackageManager.NameNotFoundException.class);
    RouteDetector.detect(mContext, "http://test.com/");

    assertEquals(0, mTargets.size());
  }

  @Test
  public void testAnnotateInvalidClass() throws Exception {
    mTargets.clear();
    PackageInfo packageInfo = createPackageInfo(Foo1.class, Foo4.class);
    when(mPackageManager.getPackageInfo("com.example", PackageManager.GET_ACTIVITIES))
        .thenReturn(packageInfo);
    try {
      RouteDetector.detect(mContext, "http://test.com/");
      throw new AssertionError("expect exception");
    } catch (IllegalStateException e) {
      assertEquals("@Route must annotate Activity class, but not " + Foo4.class.getName(),
          e.getMessage());
    }
  }

  private PackageInfo createPackageInfo(Object... args) {
    final ActivityInfo[] activities = new ActivityInfo[args.length];
    for (int i = 0; i < args.length; i++) {
      final Object arg = args[i];
      final ActivityInfo info = new ActivityInfo();
      if (arg instanceof Class) {
        info.name = ((Class) arg).getName();
      } else if (arg instanceof String) {
        info.name = (String) arg;
      }
      activities[i] = info;
    }
    final PackageInfo packageInfo = new PackageInfo();
    packageInfo.activities = activities;
    return packageInfo;
  }

  @Route("foo1")
  private static class Foo1 extends Activity {}

  @Route("foo2")
  private static class Foo2 extends Activity {}

  private static class Foo3 extends Activity {}

  @Route("foo4")
  private static class Foo4 {}
}
