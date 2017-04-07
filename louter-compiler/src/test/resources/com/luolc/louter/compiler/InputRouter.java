package com.luolc.louter.compiler;

import android.support.annotation.Nullable;

import com.luolc.louter.Navigator;

public interface InputRouter {
  @Navigator("public_info/classroom/detail")
  void publicInfoClassroomDetail(int holeId, String from, @Nullable boolean hasStarred);

  @Navigator("hole/list")
  void holeList();
}
