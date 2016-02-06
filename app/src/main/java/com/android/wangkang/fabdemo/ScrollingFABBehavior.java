/*
 * Copyright (c) 2015 Ngewi Fet <ngewif@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.wangkang.fabdemo;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * Behavior for floating action button when list is scrolled
 * Courtesy: https://mzgreen.github.io/2015/06/23/How-to-hideshow-Toolbar-when-list-is-scrolling(part3)/
 */
public class ScrollingFABBehavior extends CoordinatorLayout.Behavior<FloatingActionsMenu> {
    private int toolbarHeight;
    private double statusBarHeight;
    public ScrollingFABBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.toolbarHeight = getToolbarHeight(context);
        this.statusBarHeight=getStatusBarHeight(context);

    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionsMenu fab, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionsMenu fab, View dependency) {
        if (dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = fab.getHeight() + fabBottomMargin;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {

                float ty=dependency.getY()-(float)statusBarHeight;
                float ratio = ty / (float) toolbarHeight;

                fab.setTranslationY(-distanceToScroll * ratio);
            }
        }
        return true;
    }

    private int getToolbarHeight(Context context){
        TypedValue tv = new TypedValue();
        int actionBarHeight = android.support.v7.appcompat.R.attr.actionBarSize;
        if (context.getTheme().resolveAttribute(R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    context.getResources().getDisplayMetrics());
        }

        return actionBarHeight;
    }

    //第一种获得状态栏高度的方法
   /* private double getStatusBarHeight(Context context){
        double statusBarHeight = Math.ceil(25 * context.getResources().getDisplayMetrics().density);
        return statusBarHeight;
    }*/

    //第二种获得状态栏高度的方法
   private int getStatusBarHeight(Context context) {
       int result = 0;
       int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
       if (resourceId > 0) {
           result = context.getResources().getDimensionPixelSize(resourceId);
       }
       return result;
   }
}
