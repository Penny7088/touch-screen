package com.yysp.ecandroid.view;

import com.yysp.ecandroid.application.ECApplication;
import com.jkframework.fragment.JKBaseFragment;
import com.squareup.leakcanary.RefWatcher;

public class ECBaseFragment extends JKBaseFragment {

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = ECApplication.getRefWatcher(GetActivity());
        refWatcher.watch(this);
    }

}
