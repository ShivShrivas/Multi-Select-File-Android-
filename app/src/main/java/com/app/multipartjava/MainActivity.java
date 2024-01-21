package com.app.multipartjava;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
        Button pickFile,uploadFiles;
    private static final int PICK_FILES_REQUEST_CODE = 123;

    private RecyclerView listView;
    private ArrayList<String> selectedFilesList;
    public ArrayList<File> fileArrayList;
    private ImageViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pickFile=findViewById(R.id.button);
        uploadFiles=findViewById(R.id.button2);
        listView = findViewById(R.id.listView);
        selectedFilesList = new ArrayList<>();
        fileArrayList = new ArrayList<>();
        listView.setLayoutManager(new GridLayoutManager(this,4));
        adapter = new ImageViewAdapter(MainActivity.this,fileArrayList);
        listView.setAdapter(adapter);

        pickFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 100);
            }
        });
       uploadFiles.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Log.d("TAG", "onClick: "+selectedFilesList.size());
               uploadFilestoServer();
           }
       });

    }

    private void uploadFilestoServer() {

        RestClient restClient=new RestClient();
        ApiService apiService=restClient.getApiService();

        MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[fileArrayList.size()];
        for (int i = 0; i < fileArrayList.size(); i++) {
            Log.d("TAG", "uploadFilestoServer: "+fileArrayList.get(i).getAbsolutePath());


            RequestBody surveyBody = RequestBody.create(MediaType.parse("*/*"),
                    fileArrayList.get(i));
            surveyImagesParts[i] = MultipartBody.Part.createFormData("file",fileArrayList.get(i).getName(),surveyBody);

        }
        RequestBody gallery_id = RequestBody.create(MediaType.parse("text/plain"),"1");
        RequestBody channelPartnerReg_Id = RequestBody.create(MediaType.parse("text/plain"),"16");
        RequestBody caption = RequestBody.create(MediaType.parse("text/plain"),"");
        Call<String> call=apiService.uploadImage(surveyImagesParts,gallery_id,channelPartnerReg_Id,caption);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("TAG", "onResponse: "+response.body());
                Toast.makeText(MainActivity.this, response.body(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
            Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data != null) {
                Log.d("TAG", "onActivityResult: "+data.getData());
               if (data.getClipData() != null) {
                    // Multiple files selected
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                          File file=new File(getRealPathFromURI(data.getClipData().getItemAt(i).getUri()));
                        fileArrayList.add(file);

                    }

                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}