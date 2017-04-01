package com.vikingsen.cheesedemo.inject;

import com.vikingsen.cheesedemo.model.webservice.WebServiceModule;

import dagger.Component;

@Component(modules = {AppModule.class, WebServiceModule.class})
public interface AppComponent {
}
