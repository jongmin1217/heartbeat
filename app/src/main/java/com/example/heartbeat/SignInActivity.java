package com.example.heartbeat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_age)
    EditText editAge;
    @BindView(R.id.checkbox_male)
    CheckBox ckMale;
    @BindView(R.id.checkbox_female)
    CheckBox ckFemale;
    @BindView(R.id.btn_start)
    Button btnStart;

    private SharedPreferences userData;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        userData = getSharedPreferences("userData", 0);
        editor = userData.edit();

        btnStart.setOnClickListener(this);
        ckMale.setOnClickListener(this);
        ckFemale.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.checkbox_male:
                if(ckFemale.isChecked()){
                    ckFemale.setChecked(false);
                }
                break;
            case R.id.checkbox_female:
                if(ckMale.isChecked()){
                    ckMale.setChecked(false);
                }
                break;
            case R.id.btn_start:
                editor.putString("name",editName.getText().toString());
                editor.putString("age",editAge.getText().toString());
                if(ckMale.isChecked()){
                    editor.putString("gender","남자");
                }else{
                    editor.putString("gender","여자");
                }
                editor.commit();
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
