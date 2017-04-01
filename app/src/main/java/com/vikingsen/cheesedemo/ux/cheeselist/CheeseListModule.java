package com.vikingsen.cheesedemo.ux.cheeselist;

import dagger.Module;
import dagger.Provides;

@Module
public class CheeseListModule {
    private final CheeseListContract.View view;

    CheeseListModule(CheeseListContract.View view) {
        this.view = view;
    }

    @Provides
    CheeseListContract.View provideView() {
        return view;
    }
}
