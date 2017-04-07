package com.luolc.louter.compiler;

import android.content.Intent;
import android.support.annotation.AnimRes;
import com.luolc.louter.AbstractNavigator;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;

public final class LouterNavigator_HoleList extends AbstractNavigator<LouterNavigator_HoleList> {
  public LouterNavigator_HoleList(String baseUrl, Object starter) {
    super(baseUrl, "hole/list", starter);
  }

  @Override
  public void start() {
    super.start();
  }

  @Override
  public LouterNavigator_HoleList addFlags(int flags) {
    return super.addFlags(flags);
  }

  @Override
  public LouterNavigator_HoleList forResult(int requestCode) {
    return super.forResult(requestCode);
  }

  @Override
  public LouterNavigator_HoleList withAnim(@AnimRes int enterAnim, @AnimRes int exitAnim) {
    return super.withAnim(enterAnim, exitAnim);
  }

  @Override
  protected boolean isAllRequiredParamsExist() {
    final Intent intent = getIntent();
    boolean result = true;
    return result;
  }
}