package com.litesoft.processingjs.utils;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerSpaceDecorator extends RecyclerView.ItemDecoration {
   private int space;

   public RecyclerSpaceDecorator(int space) {
       this.space = space;
   }

   @Override
   public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
       outRect.left = space;
       outRect.right = space;
       outRect.bottom = space;

       // Добавьте условие, если нужно добавить отступ к верхнему элементу
       if (parent.getChildAdapterPosition(view) == 0) {
           outRect.top = space;
       } else {
           outRect.top = 0;
       }
   }
}