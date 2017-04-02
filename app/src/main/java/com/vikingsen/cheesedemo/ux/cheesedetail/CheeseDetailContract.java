package com.vikingsen.cheesedemo.ux.cheesedetail;


import com.vikingsen.cheesedemo.model.database.cheese.Cheese;

interface CheeseDetailContract {
    interface View {
//        void showLoading(boolean loading);
        void showCheese(Cheese cheese);
        void showError();
        void showMissingCheese();
    }
}
