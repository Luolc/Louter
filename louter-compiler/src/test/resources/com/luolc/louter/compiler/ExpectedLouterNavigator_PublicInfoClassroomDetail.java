package com.luolc.louter.compiler;

import android.content.Intent;
import android.support.annotation.AnimRes;
import com.luolc.louter.AbstractNavigator;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;

public final class LouterNavigator_PublicInfoClassroomDetail extends AbstractNavigator<LouterNavigator_PublicInfoClassroomDetail> {
  public LouterNavigator_PublicInfoClassroomDetail(String baseUrl, Object starter) {
    super(baseUrl, "public_info/classroom/detail", starter);
  }

  @Override
  public void start() {
    super.start();
  }

  @Override
  public LouterNavigator_PublicInfoClassroomDetail addFlags(int flags) {
    return super.addFlags(flags);
  }

  @Override
  public LouterNavigator_PublicInfoClassroomDetail forResult(int requestCode) {
    return super.forResult(requestCode);
  }

  @Override
  public LouterNavigator_PublicInfoClassroomDetail withAnim(@AnimRes int enterAnim, @AnimRes int exitAnim) {
    return super.withAnim(enterAnim, exitAnim);
  }

  @Override
  protected boolean isAllRequiredParamsExist() {
    final Intent intent = getIntent();
    boolean result = true;
    result &= intent.hasExtra("holeId");
    result &= intent.hasExtra("from");
    return result;
  }

  public LouterNavigator_PublicInfoClassroomDetail holeId(int holeId) {
    getIntent().putExtra("holeId", holeId);
    return this;
  }

  public LouterNavigator_PublicInfoClassroomDetail from(String from) {
    getIntent().putExtra("from", from);
    return this;
  }

  public LouterNavigator_PublicInfoClassroomDetail hasStarred(boolean hasStarred) {
    getIntent().putExtra("hasStarred", hasStarred);
    return this;
  }
}