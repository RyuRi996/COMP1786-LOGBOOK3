package com.example.contactdatabase.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactdatabase.R;
import com.example.contactdatabase.database.AppDatabase;
import com.example.contactdatabase.models.Detail;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.droidsonroids.gif.GifImageView;

public class AddActivity extends AppCompatActivity {
    private int index = 0;
    private AppDatabase appDatabase;
    MaterialButton saveBtn, next, previous;
    FloatingActionButton home;
    private EditText inputName,inputEmail, inputPhone;
    private TextView inputDob;
    private GifImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user_detail.db")
                .allowMainThreadQueries().build();

        getAvatar();

        inputDob = findViewById(R.id.inputDate);
        inputDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        home = findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddActivity.this, MainActivity.class));
            }
        });

        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }
    private void getData(){
        ArrayList<Integer> imageAvatarList = getImageDrawable();

        inputName = findViewById(R.id.inputName);
        inputDob = findViewById(R.id.inputDate);
        inputEmail = findViewById(R.id.inputEmail);
        inputPhone = findViewById(R.id.inputPhone);

        String name = inputName.getText().toString();
        String dob = inputDob.getText().toString();
        String email = inputEmail.getText().toString();
        String phone = inputPhone.getText().toString();

        if (validateEmail(email) && !name.isEmpty() && !dob.isEmpty() && validatePhone(phone)){

            Detail detail = new Detail();
            detail.avatar = imageAvatarList.get(index);
            detail.name = name;
            detail.dob = dob;
            detail.email = email;
            detail.phone = phone;

            appDatabase.detailDAO().insertDetail(detail);

            Toast.makeText(this, "Contact has been saved",
                    Toast.LENGTH_LONG
            ).show();

            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (name.isEmpty() || dob.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill out requirement field",
                    Toast.LENGTH_LONG
            ).show();
        } else if (!validateEmail(email)){
            Toast.makeText(this, "Wrong Email format please check and fill again!",
                    Toast.LENGTH_LONG
            ).show();
        } else if (!validatePhone(phone)){
            Toast.makeText(this, "Wrong Phone number format please check and fill again!",
                    Toast.LENGTH_LONG
            ).show();
        }
    }
    public static boolean validateEmail(String email){
        Pattern pattern = Pattern.compile("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b");
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }
    public static boolean validatePhone(String phone){
        return phone.length() == 10;
    }
    private void getAvatar(){
        ArrayList<Integer> imageAvatarList = getImageDrawable();

        avatar = findViewById(R.id.avatar);
        next = findViewById(R.id.nextImage);
        previous = findViewById(R.id.previousImage);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == imageAvatarList.size()-1){
                    index = 0;
                } else {
                    index++;
                }
                avatar.setImageResource(imageAvatarList.get(index));
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index==0 || index<0){
                    index = imageAvatarList.size()-1;
                } else {
                    index--;
                }
                avatar.setImageResource(imageAvatarList.get(index));
            }
        });
    }
    @NonNull
    private ArrayList<Integer> getImageDrawable() {
        Field[] drawableFiles = R.drawable.class.getFields();
        ArrayList<Integer> imageList = new ArrayList<>();

        TypedValue value = new TypedValue();
        for(int i =0; i<drawableFiles.length;i++){
            try {
                getResources().getValue(drawableFiles[i].getInt(null), value, true);
                String fileName = value.string.toString();
                if (fileName.endsWith("jpg")||
                        fileName.endsWith("png")||
                        fileName.endsWith("gif")){
                    imageList.add(drawableFiles[i].getInt(null));
                }
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        return imageList;
    }
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
        {
            LocalDate d = LocalDate.now();
            int year = d.getYear();
            int month = d.getMonthValue();
            int day = d.getDayOfMonth();
            return new DatePickerDialog(getActivity(), this, year, --month, day);}
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day){
            LocalDate DoB = LocalDate.of(year, ++month, day);
            ((AddActivity)getActivity()).updateDOB(DoB);;
        }
    }
    public void updateDOB(LocalDate Dob){
        inputDob = findViewById(R.id.inputDate);
        String formatDate = Dob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        inputDob.setText(formatDate);
    }
}