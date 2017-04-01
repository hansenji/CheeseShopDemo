package com.vikingsen.cheesedemo.ux.cheeselist;


import com.vikingsen.cheesedemo.inject.ActivityScope;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = CheeseListModule.class)
public interface CheeseListComponent {
    void inject(CheeseListActivity target);
}
