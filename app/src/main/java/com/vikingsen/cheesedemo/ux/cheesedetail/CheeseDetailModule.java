package com.vikingsen.cheesedemo.ux.cheesedetail;


import dagger.Module;
import dagger.Provides;

@Module
public class CheeseDetailModule {
    private final CheeseDetailContract.View view;

    CheeseDetailModule(CheeseDetailContract.View view) {
        this.view = view;
    }

    @Provides
    CheeseDetailContract.View provideView() {
        return view;
    }
}
