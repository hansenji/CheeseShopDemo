package com.vikingsen.cheesedemo.intent;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.vikingsen.cheesedemo.ux.cheesedetail.CheeseDetailActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InternalIntent {

    @Inject
    InternalIntent() {

    }

    @NonNull
    public Intent getCheeseDetailIntent(@NonNull Context context, long cheeseId, @NonNull String cheeseName) {
        Intent intent = new Intent(context, CheeseDetailActivity.class);
        intent.putExtra(CheeseDetailActivity.EXTRA_CHEESE_ID, cheeseId);
        intent.putExtra(CheeseDetailActivity.EXTRA_CHEESE_NAME, cheeseName);
        return intent;
    }
}
