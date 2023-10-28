// Copyright (c) 2020 Facebook, Inc. and its affiliates.
// All rights reserved.
//
// This source code is licensed under the BSD-style license found in the
// LICENSE file in the root directory of this source tree.

package com.example.myapplication.ui.notifications.trashdetective;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.myapplication.ui.notifications.trashdetective.PrePostProcessor.mClasses;


public class ResultView extends View {

    private final static int TEXT_X = 40;
    private final static int TEXT_Y = 35;
    private final static int TEXT_WIDTH = 260;
    private final static int TEXT_HEIGHT = 50;

    private Paint mPaintRectangle;
    private Paint mPaintText;
    private ArrayList<Result> mResults;


    public ResultView(Context context) {
        super(context);
    }

    public ResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintRectangle = new Paint();
        mPaintRectangle.setColor(Color.YELLOW);
        mPaintText = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mResults == null) return;
        for (Result result : mResults) {
            mPaintRectangle.setStrokeWidth(5);
            mPaintRectangle.setStyle(Paint.Style.STROKE);
            canvas.drawRect(result.rect, mPaintRectangle);

            Path mPath = new Path();
            RectF mRectF = new RectF(result.rect.left, result.rect.top, result.rect.left + TEXT_WIDTH, result.rect.top + TEXT_HEIGHT);
            mPath.addRect(mRectF, Path.Direction.CW);
            mPaintText.setColor(Color.MAGENTA);
            canvas.drawPath(mPath, mPaintText);

            mPaintText.setColor(Color.WHITE);
            mPaintText.setStrokeWidth(0);
            mPaintText.setStyle(Paint.Style.FILL);
            mPaintText.setTextSize(32);
            canvas.drawText(String.format("%s %.2f", mClasses[result.classIndex], result.score), result.rect.left + TEXT_X, result.rect.top + TEXT_Y, mPaintText);
        }
        //Log.v("class",mClasses[result.classIndex]);
/*
        ArrayList<String> overlapResult = new ArrayList<>();
        ArrayList<String> noOverlapResult = new ArrayList<>();
        for (int i = 0; i < mResults.size(); i++) {
            Result result1 = mResults.get(i);
            Rect rect1 = new Rect(result1.rect);
            RectF rectF1 = new RectF(rect1);
            boolean isOverlap = false;
            List<Integer> overlappingClassIndexes = new ArrayList<>();

            for (int j = i + 1; j < mResults.size(); j++) {
                Result result2 = mResults.get(j);
                Rect rect2 = new Rect(result2.rect);
                RectF rectF2 = new RectF(rect2);

                if (RectF.intersects(rectF1, rectF2)) {
                    // 겹치는 박스를 감지한 경우
                    isOverlap = true;
                    overlappingClassIndexes.add(result2.classIndex);
                }
            }
            Intent intent = new Intent(getContext(), DetectedClassResult.class);
            if (isOverlap) {
                // 겹치는 상자들의 classIndex를 배열로 정리하여 로그로 출력
                StringBuilder overlappingClasses = new StringBuilder();
                overlappingClasses.append(mClasses[result1.classIndex]);
                for (Integer classIndex : overlappingClassIndexes) {
                    overlappingClasses.append(", ").append(mClasses[classIndex]);
                }
                Log.v("Overlap", "Overlap detected: " + overlappingClasses.toString());
                //intent.putExtra("Overlap", overlappingClasses.toString());
                overlapResult.add(overlappingClasses.toString());


            } else {
                // 겹치지 않는 박스의 classIndex를 로그로 출력
                Log.v("Overlap", "No overlap detected: " + mClasses[result1.classIndex]);
                //intent.putExtra("NoOverlap", mClasses[result1.classIndex]);
                noOverlapResult.add(mClasses[result1.classIndex]);


            }
            intent.putStringArrayListExtra("Overlap", overlapResult);
            intent.putStringArrayListExtra("NoOverlap", noOverlapResult);
            getContext().startActivity(intent);

        }
/*
        for (int i = 0; i < mResults.size(); i++) {
            Result result1 = mResults.get(i);
            Rect rect1 = new Rect(result1.rect);
            RectF rectF1 = new RectF(rect1);

            for (int j = i + 1; j < mResults.size(); j++) {
                Result result2 = mResults.get(j);
                Rect rect2 = new Rect(result2.rect);
                RectF rectF2 = new RectF(rect2);

                if (RectF.intersects(rectF1, rectF2)) {
                    // 겹치는 박스를 감지한 경우
                    String class1 = mClasses[result1.classIndex];
                    String class2 = mClasses[result2.classIndex];
                    Log.v("Overlap", "Overlap detected: " + class1 + " and " + class2);
                }
            }
        }*/
    }

    public ArrayList<ArrayList<String>> getOverlapResult() {
        ArrayList<String> overlapResult = new ArrayList<>();
        ArrayList<String> noOverlapResult = new ArrayList<>();
        List<Integer> overlappingClassIndexes = new ArrayList<>(); // overlappingClassIndexes 선언 및 초기화

        for (int i = 0; i < mResults.size(); i++) {
            Result result1 = mResults.get(i);
            Rect rect1 = new Rect(result1.rect);
            RectF rectF1 = new RectF(rect1);
            boolean isOverlap = false;

            overlappingClassIndexes.clear(); // overlappingClassIndexes 초기화

            for (int j = i + 1; j < mResults.size(); j++) {
                Result result2 = mResults.get(j);
                Rect rect2 = new Rect(result2.rect);
                RectF rectF2 = new RectF(rect2);

                if (RectF.intersects(rectF1, rectF2)) {
                    // 겹치는 박스를 감지한 경우
                    isOverlap = true;
                    overlappingClassIndexes.add(result2.classIndex);
                }
            }

            if (isOverlap) {
                // 겹치하는 경우 처리
                StringBuilder overlappingClasses = new StringBuilder();
                overlappingClasses.append(mClasses[result1.classIndex]);
                for (Integer classIndex : overlappingClassIndexes) {
                    overlappingClasses.append(", ").append(mClasses[classIndex]);
                }
                overlapResult.add(overlappingClasses.toString());
            } else {
                // 겹치지 않는 경우 처리
                noOverlapResult.add(mClasses[result1.classIndex]);
            }
        }

        // 겹치지 않는 경우에 겹친 결과들을 noOverlapResult에서 제거
        Iterator<String> iterator = noOverlapResult.iterator();
        while (iterator.hasNext()) {
            String classValue = iterator.next();
            for (int i = 0; i < mResults.size(); i++) {
                Result result = mResults.get(i);
                if (classValue.equals(mClasses[result.classIndex])) {
                    for (Integer overlappingIndex : overlappingClassIndexes) {
                        if (overlappingIndex == result.classIndex) {
                            iterator.remove(); // 안전하게 요소를 제거
                            break;
                        }
                    }
                }
            }
        }

        // overlapResult와 noOverlapResult를 담고 있는 ArrayList를 반환
        ArrayList<ArrayList<String>> results = new ArrayList<>();
        results.add(overlapResult);
        results.add(noOverlapResult);
        return results;
    }





    public void setResults(ArrayList<Result> results) {
        mResults = results;
    }
}