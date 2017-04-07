package com.luolc.louter;

import com.luolc.louter.compiler.LouterNavigator_HoleList;
import com.luolc.louter.compiler.LouterNavigator_PublicInfoClassroomDetail;
import java.lang.Object;
import java.lang.String;

public final class CentralNavigator {
  private final Object mStarter;

  private final String mBaseUrl;

  public CentralNavigator(Object starter, String baseUrl) {
    mStarter = starter;
    mBaseUrl = baseUrl;
  }

  public LouterNavigator_HoleList toHoleList() {
    return new LouterNavigator_HoleList(mBaseUrl, mStarter);
  }

  public LouterNavigator_PublicInfoClassroomDetail toPublicInfoClassroomDetail() {
    return new LouterNavigator_PublicInfoClassroomDetail(mBaseUrl, mStarter);
  }
}