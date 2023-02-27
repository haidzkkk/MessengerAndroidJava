package com.example.messenger.ClassTool;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.Adapter.MessHistoriAdapter;
import com.example.messenger.Interface.ItemTouchHeplerListenner;

public class RecycleviewItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    ItemTouchHeplerListenner itemTouchHeplerListenner;

    /**
     * Creates a Callback for the given drag and swipe allowance. These values serve as
     * defaults
     * and if you want to customize behavior per ViewHolder, you can override
     * {@link #getSwipeDirs(RecyclerView, ViewHolder)}
     * and / or {@link #getDragDirs(RecyclerView, ViewHolder)}.
     *
     * @param dragDirs  Binary OR of direction flags in which the Views can be dragged. Must be
     *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
     *                  #END},
     *                  {@link #UP} and {@link #DOWN}.
     * @param swipeDirs Binary OR of direction flags in which the Views can be swiped. Must be
     *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
     *                  #END},
     *                  {@link #UP} and {@link #DOWN}.
     */
    public RecycleviewItemTouchHelper(int dragDirs, int swipeDirs, ItemTouchHeplerListenner itemTouchHeplerListenner) {
        super(dragDirs, swipeDirs);
        this.itemTouchHeplerListenner = itemTouchHeplerListenner;
    }

    // vuốt item
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (itemTouchHeplerListenner != null && viewHolder.getAdapterPosition() > 0) {   // lớn hơn 0 để bỏ cái item list userFont đi
            itemTouchHeplerListenner.onSwiped(viewHolder);      // vuốt item thì gọi interface này để chuyền data của item đc vuốt
        }
    }


    // mấy phương thức này là hiệu ứng khi vuốt
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null && viewHolder.getAdapterPosition() > 0) {
            View viewFoureGround = ((MessHistoriAdapter.BodyViewHoler) viewHolder).rlMain;
            getDefaultUIUtil().onSelected(viewFoureGround);
        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder.getAdapterPosition() > 0) {
            View viewFoureGround = ((MessHistoriAdapter.BodyViewHoler) viewHolder).rlMain;
            getDefaultUIUtil().onDrawOver(c, recyclerView, viewFoureGround, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder.getAdapterPosition() > 0) {
            View viewFoureGround = ((MessHistoriAdapter.BodyViewHoler) viewHolder).rlMain;

            getDefaultUIUtil().onDraw(c, recyclerView, viewFoureGround, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getAdapterPosition() > 0) {
            View viewFoureGround = ((MessHistoriAdapter.BodyViewHoler) viewHolder).rlMain;
            getDefaultUIUtil().clearView(viewFoureGround);
        }
    }
}
