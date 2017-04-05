package com.vikingsen.cheesedemo.model.data.comment;


public class CommentChange {
    private final long cheeseId;
    private final boolean bulkChange;

    static CommentChange bulkOperation() {
        return new CommentChange(-1, true);
    }

    static CommentChange forCheese(long cheeseId) {
        return new CommentChange(cheeseId, false);
    }

    private CommentChange(long cheeseId, boolean bulkChange) {
        this.cheeseId = cheeseId;
        this.bulkChange = bulkChange;
    }

    public long getCheeseId() {
        return cheeseId;
    }

    public boolean isBulkChange() {
        return bulkChange;
    }
}
