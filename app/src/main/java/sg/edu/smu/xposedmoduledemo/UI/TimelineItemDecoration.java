package sg.edu.smu.xposedmoduledemo.UI;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TimelineItemDecoration extends RecyclerView.ItemDecoration {
    private Paint paint;
    private int itemView_leftinterval;
    private int circle_radius;
    private Bitmap mIcon;

    public TimelineItemDecoration(){
        paint = new Paint();
        paint.setColor(Color.GRAY);
        itemView_leftinterval = 60;
        circle_radius = 10;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(itemView_leftinterval,0,0,0);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //get the amount of child view in the recyclerview
        int childCount = parent.getChildCount();
        for(int i = 0; i < childCount; i++){
            View child = parent.getChildAt(i);
            //draw the circle
            float centerX = child.getLeft() - itemView_leftinterval / 2.0f;
            float centerY = child.getTop() + child.getHeight() / 2.0f;
            c.drawCircle(centerX, centerY, circle_radius, paint);

            /*
            * draw the first half line
            * */
            // top point of upline
            float upLine_up_X = centerX;
            float upLine_up_Y = child.getTop();

            // bottom point of upline
            float upLine_bottom_X = centerX;
            float upLine_bottom_Y = centerY - circle_radius;
            // draw the upline
            c.drawLine(upLine_up_X, upLine_up_Y, upLine_bottom_X, upLine_bottom_Y, paint);
            /*
             * draw the bottomline
             * */
            // top point of bottom line
            float bottomLine_up_x = centerX;
            float bottom_up_y = centerY + circle_radius;

            // bottom point of bottom line
            float bottomLine_bottom_x = centerX;
            float bottomLine_bottom_y = child.getBottom();

            // draw the bottom line
            c.drawLine(bottomLine_up_x, bottom_up_y, bottomLine_bottom_x, bottomLine_bottom_y, paint);
        }



    }
}
