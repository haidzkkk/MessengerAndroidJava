package com.example.messenger.AbtractClass;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class LoadMoreScrollListenner extends RecyclerView.OnScrollListener {

    LinearLayoutManager linearLayoutManager;

    public LoadMoreScrollListenner(LinearLayoutManager linearLayoutManager){
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

        int visibleItemCount = linearLayoutManager.getChildCount();     // tong item hien tren man hinh
        int totalItemCount = linearLayoutManager.getItemCount();        // tong item trong linearLayoutManager
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();  // item dau tien hien tren man hinh

        if (isLoading()){ // thằng này ngăn chặn thằng if dưới chạy vô tội vạ :v
            return;
        }

        if ( firstVisibleItemPosition >=0 && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount){  // thằng này để gọi loadmore
            loadMoreItem();
        }
    }

    // mấy hàm này để thg con overload
    public abstract void loadMoreItem();
    public abstract boolean isLoading();
    public abstract boolean isLastPage();
}
