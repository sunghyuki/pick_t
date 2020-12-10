package com.example.pickt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCarActivity extends AppCompatActivity {

    private final int GET_GALLERY_IMAGE = 200;

    ImageView imageView;

    public static SQLiteHelper sqLiteHelper;

    private static final String TAG_TEXT = "text";

    TextView textview_result;

    List<Map<String, Object>> dialogItemList;
    String[] accommodation_list = {"퀸 베드","2층 침대","전자레인지","냉장고","온수보일러","욕조"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        sqLiteHelper = new SQLiteHelper(this, "PicktDB.sqlite", null, 1);

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS CARS(Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, price VARCHAR, image BLOB)");

        imageView = (ImageView) findViewById(R.id.addImageView);
        EditText editName = (EditText) findViewById(R.id.editName);
        EditText editLicense = (EditText) findViewById(R.id.editLicense);
        Button SaveBtn = (Button) findViewById(R.id.saveBtn);
        Button ListBtn = (Button)findViewById(R.id.listBtn);

        EditText editNum = (EditText)findViewById(R.id.accommodatedNum);
        EditText CarDescription = (EditText)findViewById(R.id.carDescription);
        TextView selected = (TextView)findViewById(R.id.selected_text);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sqLiteHelper.insertData(
                            editName.getText().toString().trim(),
                            editLicense.getText().toString().trim(),
                            imageViewToByte(imageView)
                    );
                    Toast.makeText(getApplicationContext(), "Added successfully!", Toast.LENGTH_SHORT).show();
                    editName.setText("");
                    editLicense.setText("");
                    imageView.setImageResource(R.mipmap.ic_launcher);

                    editNum.setText("");
                    CarDescription.setText("");
                    selected.setText("");
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        ListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCarActivity.this, CarList.class);
                startActivity(intent);
            }
        });

        textview_result = (TextView)findViewById(R.id.selected_text);

        Button accommodationBtn = (Button)findViewById(R.id.accommodationBtn);
        accommodationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAletDialog();
            }
        });

        dialogItemList = new ArrayList<>();
        for (int i = 0; i < accommodation_list.length; i++)
        {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put(TAG_TEXT, accommodation_list[i]);

            dialogItemList.add(itemMap);
        }
    }

    private void showAletDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddCarActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog, null);
        builder.setView(view);

        final ListView accommodation_listView = (ListView)view.findViewById(R.id.listview_alertdialog_list);
        final AlertDialog dialog = builder.create();

        SimpleAdapter simpleAdapter = new SimpleAdapter(AddCarActivity.this, dialogItemList, R.layout.alert_dialog_row, new String[]{TAG_TEXT}, new int[]{R.id.alertDialogTextView});

        accommodation_listView.setAdapter(simpleAdapter);
        accommodation_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textview_result.setText(accommodation_list[position]);
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_GALLERY_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    imageView.setImageBitmap(img);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
