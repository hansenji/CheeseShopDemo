package com.vikingsen.cheesedemo.ux.cheesedetail;

import com.vikingsen.cheesedemo.inject.ActivityScope;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = CheeseDetailModule.class)
public interface CheeseDetailComponent {
    void inject(CheeseDetailActivity target);
}
