package com.example.myapplication.ui.notifications;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentNotificationsBinding;
import com.example.myapplication.ui.notifications.trashdetective.DetectedClassResult;
import com.example.myapplication.ui.notifications.trashdetective.PrePostProcessor;
import com.example.myapplication.ui.notifications.trashdetective.Result;
import com.example.myapplication.ui.notifications.trashdetective.ResultView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.pytorch.IValue;
import org.pytorch.LiteModuleLoader;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    private ImageView mImageView;
    private ResultView mResultView;
    private Button mButtonDetect;
    private ProgressBar mProgressBar;
    private Bitmap mBitmap = null;
    private Module mModule = null;
    private float mImgScaleX, mImgScaleY, mIvScaleX, mIvScaleY, mStartX, mStartY;

    ArrayList<String> sendclasses = new ArrayList<String>();
    ArrayList<Result> mResults;
    private ArrayList<Result> results;

    private View root;
    private Button mbuttonTest;
    private Button buttonSelect;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        root = inflater.inflate(R.layout.fragment_notifications, container, false);

        mImageView = root.findViewById(R.id.imageView);
        mResultView = root.findViewById(R.id.resultView);
        mbuttonTest = root.findViewById(R.id.NextPage);
        buttonSelect = root.findViewById(R.id.selectButton);
        mButtonDetect = root.findViewById(R.id.detectButton);
        mProgressBar = root.findViewById(R.id.progressBar);

        mImageView.setImageBitmap(mBitmap);
        mResultView.setVisibility(View.INVISIBLE);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mbuttonTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<ArrayList<String>> resultsList = mResultView.getOverlapResult();
                ArrayList<String> overlapResult = resultsList.get(0);
                ArrayList<String> noOverlapResult = new ArrayList<>(); // 중복되지 않는 결과를 저장하는 리스트

                for (ArrayList<String> resultList : resultsList) {
                    for (String result : resultList) {
                        if (!overlapResult.contains(result)) {
                            noOverlapResult.add(result);
                        }
                    }
                }

                // 모달 다이얼로그 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Detection Result");
                builder.setMessage("Overlap: " + overlapResult.toString() + "\n\nNo Overlap: " + noOverlapResult.toString());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // OK 버튼을 클릭했을 때 동작 설정
                        dialog.dismiss(); // 다이얼로그 닫기

                        // DetectedClassResult 액티비티로 이동하는 코드 추가
                        Intent intent = new Intent(getContext(), DetectedClassResult.class);
                        intent.putStringArrayListExtra("Overlap", overlapResult);
                        intent.putStringArrayListExtra("NoOverlap", noOverlapResult);
                        getContext().startActivity(intent);
                    }
                });

                // 모달 다이얼로그 표시
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        buttonSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mResultView.setVisibility(View.INVISIBLE);

                final CharSequence[] options = {"Choose from Photos", "Take Picture", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("New Test Image");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Picture")) {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);
                        } else if (options[item].equals("Choose from Photos")) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, 1);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        mButtonDetect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mBitmap != null) {
                    mButtonDetect.setEnabled(false);
                    mButtonDetect.setText(getString(R.string.run_model));
                    mProgressBar.setVisibility(View.VISIBLE);
                    mbuttonTest.setVisibility(View.VISIBLE);

                    mImgScaleX = (float) mBitmap.getWidth() / PrePostProcessor.mInputWidth;
                    mImgScaleY = (float) mBitmap.getHeight() / PrePostProcessor.mInputHeight;

                    mIvScaleX = (mBitmap.getWidth() > mBitmap.getHeight()) ? (float) mImageView.getWidth() / mBitmap.getWidth() : (float) mImageView.getHeight() / mBitmap.getHeight();
                    mIvScaleY = (mBitmap.getHeight() > mBitmap.getWidth()) ? (float) mImageView.getHeight() / mBitmap.getHeight() : (float) mImageView.getWidth() / mBitmap.getWidth();

                    mStartX = (mImageView.getWidth() - mIvScaleX * mBitmap.getWidth()) / 2;
                    mStartY = (mImageView.getHeight() - mIvScaleY * mBitmap.getHeight()) / 2;

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendclasses.clear();
                            Bitmap resizedBitmap = Bitmap.createScaledBitmap(mBitmap, PrePostProcessor.mInputWidth, PrePostProcessor.mInputHeight, true);
                            final Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(resizedBitmap, PrePostProcessor.NO_MEAN_RGB, PrePostProcessor.NO_STD_RGB);
                            IValue[] outputTuple = mModule.forward(IValue.from(inputTensor)).toTuple();
                            final Tensor outputTensor = outputTuple[0].toTensor();
                            final float[] outputs = outputTensor.getDataAsFloatArray();
                            results = PrePostProcessor.outputsToNMSPredictions(outputs, mImgScaleX, mImgScaleY, mIvScaleX, mIvScaleY, mStartX, mStartY);

                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mButtonDetect.setEnabled(true);
                                    mButtonDetect.setText(getString(R.string.detect));
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                    mResultView.setResults(results);

                                    for (Result result : mResults = results) {
                                        sendclasses.add(String.format("%s", PrePostProcessor.mClasses[result.classIndex]));
                                    }

                                    mResultView.invalidate();
                                    mResultView.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                    thread.start();
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        }

        try {
            mModule = LiteModuleLoader.load(assetFilePath(requireContext(), "best.torchscript.ptl"));
            BufferedReader br = new BufferedReader(new InputStreamReader(requireActivity().getAssets().open("classes.txt")));
            String line;
            List<String> classes = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                classes.add(line);
            }
            PrePostProcessor.mClasses = new String[classes.size()];
            classes.toArray(PrePostProcessor.mClasses);

        } catch (IOException e) {
            Log.e("Object Detection", "Error reading assets", e);
            getActivity().finish();
        }

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        mBitmap = (Bitmap) data.getExtras().get("data");
                        Matrix matrix = new Matrix();
                        //matrix.postRotate(90.0f); //사진 90도 회전시켜버리니까 객체를 인식 못해버림. 그래서 걍 뺌
                        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
                        mImageView.setImageBitmap(mBitmap);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = requireActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                mBitmap = BitmapFactory.decodeFile(picturePath);
                                Matrix matrix = new Matrix();
                                matrix.postRotate(90.0f);
                                mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
                                mImageView.setImageBitmap(mBitmap);
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}