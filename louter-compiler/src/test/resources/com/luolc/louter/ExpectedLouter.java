package com.luolc.louter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import java.lang.String;

public final class Louter {
  private final String mBaseUrl;

  Louter(String baseUrl) {
    mBaseUrl = baseUrl;
  }

  public void init(@NonNull Context context) {
    RouteDetector.detect(context, mBaseUrl);
  }

  public CentralNavigator with(@NonNull Context context) {
    return new CentralNavigator(context, mBaseUrl);
  }

  public CentralNavigator with(@NonNull Activity activity) {
    return new CentralNavigator(activity, mBaseUrl);
  }

  public CentralNavigator with(@NonNull Fragment fragment) {
    return new CentralNavigator(fragment, mBaseUrl);
  }

  public CentralNavigator with(@NonNull android.app.Fragment fragment) {
    return new CentralNavigator(fragment, mBaseUrl);
  }

  public static final class Builder {
    private String mBaseUrl;

    public Builder baseUrl(@NonNull String baseUrl) {
      mBaseUrl = baseUrl;
      return this;
    }

    public Louter build() {
      return new Louter(mBaseUrl);
    }
  }
}