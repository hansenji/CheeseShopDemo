package com.vikingsen.cheesedemo.inject;

import com.vikingsen.cheesedemo.model.webservice.WebServiceModule;
import com.vikingsen.cheesedemo.ux.cheesedetail.CheeseDetailComponent;
import com.vikingsen.cheesedemo.ux.cheesedetail.CheeseDetailModule;
import com.vikingsen.cheesedemo.ux.cheeselist.CheeseListComponent;
import com.vikingsen.cheesedemo.ux.cheeselist.CheeseListModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, WebServiceModule.class})
public interface AppComponent {
    CheeseListComponent include(CheeseListModule module);
    CheeseDetailComponent include(CheeseDetailModule cheeseDetailModule);
}
