package com.vikingsen.cheesedemo.ux.cheeselist;


import com.vikingsen.cheesedemo.model.database.cheese.Cheese;

import java.util.List;

interface CheeseListContract {
    interface View {
        void showLoading(boolean loading);
        void showCheeses(List<Cheese> cheeses);
        void showError();
    }
}
