package com.vikingsen.cheesedemo.ux.cheesedetail;


import com.vikingsen.cheesedemo.model.database.cheese.Cheese;
import com.vikingsen.cheesedemo.model.database.comment.Comment;

import java.util.List;

interface CheeseDetailContract {
    interface View {
//        void showLoading(boolean loading);
        void showCheese(Cheese cheese);
        void showCheeseError();
        void showMissingCheese();

        void showComments(List<Comment> comments);
        void showCommentError();
    }
}
