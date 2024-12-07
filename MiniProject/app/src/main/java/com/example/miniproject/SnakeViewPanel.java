package com.example.miniproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class GridPosition {
    private int x;
    private int y;

    public GridPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

class GridSquare {
    private int mType;

    public GridSquare(int type) {
        mType = type;
    }

    interface GameType {
        int GRID = 0;
        int FOOD = 1;
        int SNAKE = 2;

        int LEFT = 1;
        int TOP = 2;
        int RIGHT = 3;
        int BOTTOM = 4;
    }

    public int getColor() {
        switch (mType) {
            case GameType.GRID:
                return Color.WHITE;
            case GameType.FOOD:
                return Color.BLUE;
            case GameType.SNAKE:
                return Color.parseColor("#FF4081");
        }
        return Color.WHITE;
    }

    public void setType(int type) {
        mType = type;
    }
}

public class SnakeViewPanel extends View implements GridSquare.GameType {

    private List<List<GridSquare>> mGridSquare = new ArrayList<>();
    private List<GridPosition> mSnakePositions = new ArrayList<>();

    private GridPosition mSnakeHeader;
    private GridPosition mFoodPosition;
    private int mSnakeLength = 3;
    private long mSpeed = 8;
    private int mSnakeDirection = GridSquare.GameType.RIGHT;
    static boolean mIsEndGame = false;
    private int mGridSize = 20;
    private Paint mGridPaint = new Paint();
    private Paint mStrokePaint = new Paint();
    private int mRectSize = dp2px(getContext(), 15);
    private int mStartX, mStartY;

    public SnakeViewPanel(Context context) {
        this(context, null);
    }

    public SnakeViewPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }



    public SnakeViewPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        List<GridSquare> squares;
        for (int i = 0; i < mGridSize; i++) {
            squares = new ArrayList<>();
            for (int j = 0; j < mGridSize; j++) {
                squares.add(new GridSquare(GridSquare.GameType.GRID));
            }
            mGridSquare.add(squares);
        }
        mSnakeHeader = new GridPosition(10, 10);
        mSnakePositions.add(new GridPosition(mSnakeHeader.getX(), mSnakeHeader.getY()));
        mFoodPosition = new GridPosition(0, 0);
        mIsEndGame = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mStartX = w / 2 - mGridSize * mRectSize / 2;
        mStartY = dp2px(getContext(), 40);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = mStartY * 2 + mGridSize * mRectSize;
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        mGridPaint.reset();
        mGridPaint.setAntiAlias(true);
        mGridPaint.setStyle(Paint.Style.FILL);
        mGridPaint.setAntiAlias(true);

        mStrokePaint.reset();
        mStrokePaint.setColor(Color.BLACK);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setAntiAlias(true);

        for (int i = 0; i < mGridSize; i++) {
            for (int j = 0; j < mGridSize; j++) {
                int left = mStartX + i * mRectSize;
                int top = mStartY + j * mRectSize;
                int right = left + mRectSize;
                int bottom = top + mRectSize;
                canvas.drawRect(left, top, right, bottom, mStrokePaint);
                mGridPaint.setColor(mGridSquare.get(i).get(j).getColor());
                canvas.drawRect(left, top, right, bottom, mGridPaint);
            }
        }
    }

    private void refreshFood(GridPosition foodPosition) {
        mGridSquare.get(foodPosition.getX()).get(foodPosition.getY()).setType(GridSquare.GameType.FOOD);
    }

    public void setSnakeDirection(int snakeDirection) {
        if (mSnakeDirection == GridSquare.GameType.RIGHT && snakeDirection == GridSquare.GameType.LEFT) return;
        if (mSnakeDirection == GridSquare.GameType.LEFT && snakeDirection == GridSquare.GameType.RIGHT) return;
        if (mSnakeDirection == GridSquare.GameType.TOP && snakeDirection == GridSquare.GameType.BOTTOM) return;
        if (mSnakeDirection == GridSquare.GameType.BOTTOM && snakeDirection == GridSquare.GameType.TOP) return;
        mSnakeDirection = snakeDirection;
    }


    private class GameMainThread extends Thread {

        @Override
        public void run() {
            while (!mIsEndGame) {
                moveSnake(mSnakeDirection);
                checkCollision();
                refreshGridSquare();
                handleSnakeTail();
                postInvalidate(); // Redraw screen
                handleSpeed();
            }
        }

        private void refreshGridSquare() {
            // First, reset all grid squares to 'GRID' type
            for (int i = 0; i < mGridSize; i++) {
                for (int j = 0; j < mGridSize; j++) {
                    mGridSquare.get(i).get(j).setType(GridSquare.GameType.GRID);
                }
            }

            // Refresh the snake positions
            for (GridPosition position : mSnakePositions) {
                mGridSquare.get(position.getX()).get(position.getY()).setType(GridSquare.GameType.SNAKE);
            }

            // Refresh the food position
            mGridSquare.get(mFoodPosition.getX()).get(mFoodPosition.getY()).setType(GridSquare.GameType.FOOD);
        }



        private void handleSpeed() {
            try {
                sleep(1000 / mSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkCollision() {
        GridPosition headerPosition = mSnakePositions.get(mSnakePositions.size() - 1);
        for (int i = 0; i < mSnakePositions.size() - 2; i++) {
            GridPosition position = mSnakePositions.get(i);
            if (headerPosition.getX() == position.getX() && headerPosition.getY() == position.getY()) {
                mIsEndGame = true;
                showMessageDialog();
                return;
            }
        }

        if (headerPosition.getX() == mFoodPosition.getX() && headerPosition.getY() == mFoodPosition.getY()) {
            mSnakeLength++;
            generateFood();
        }
    }

    private void showMessageDialog() {
        post(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(getContext()).setMessage("Game Over!")
                        .setCancelable(false)
                        .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                reStartGame();
                            }
                        })
                        .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    public void reStartGame() {
        if (!mIsEndGame) return;
        for (List<GridSquare> squares : mGridSquare) {
            for (GridSquare square : squares) {
                square.setType(GridSquare.GameType.GRID);
            }
        }

        mSnakeHeader.setX(10);
        mSnakeHeader.setY(10);
        mSnakePositions.clear();
        mSnakePositions.add(new GridPosition(mSnakeHeader.getX(), mSnakeHeader.getY()));
        mSnakeLength = 3;
        mSnakeDirection = GridSquare.GameType.RIGHT;
        mSpeed = 8;
        mFoodPosition.setX(0);
        mFoodPosition.setY(0);
        generateFood();
        mIsEndGame = false;
        new GameMainThread().start();
    }

    private void generateFood() {
        Random random = new Random();
        int x = random.nextInt(mGridSize);
        int y = random.nextInt(mGridSize);

        if (mGridSquare.get(x).get(y).getColor() == Color.parseColor("#FF4081")) {
            generateFood();
        } else {
            mFoodPosition.setX(x);
            mFoodPosition.setY(y);
            refreshFood(mFoodPosition);
        }
    }

    private void moveSnake(int direction) {
        int x = mSnakePositions.get(mSnakePositions.size() - 1).getX();
        int y = mSnakePositions.get(mSnakePositions.size() - 1).getY();
        switch (direction) {
            case GridSquare.GameType.LEFT:
                x--;
                break;
            case GridSquare.GameType.TOP:
                y--;
                break;
            case GridSquare.GameType.RIGHT:
                x++;
                break;
            case GridSquare.GameType.BOTTOM:
                y++;
                break;
        }

        if (x < 0) x = mGridSize - 1;
        if (x >= mGridSize) x = 0;
        if (y < 0) y = mGridSize - 1;
        if (y >= mGridSize) y = 0;

        GridPosition newHead = new GridPosition(x, y);
        mSnakePositions.add(newHead);
        if (mSnakePositions.size() > mSnakeLength) {
            mSnakePositions.remove(0);
        }
    }

    private void handleSnakeTail() {
        for (int i = 0; i < mSnakePositions.size() - 1; i++) {
            GridPosition position = mSnakePositions.get(i);
            mGridSquare.get(position.getX()).get(position.getY()).setType(GridSquare.GameType.SNAKE);
        }
    }

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
