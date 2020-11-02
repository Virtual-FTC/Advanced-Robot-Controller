package com.example.webrosrobotcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class ConfigurationEditor extends AppCompatActivity {

    Button cancelButton;
    Button saveButton;

    EditText motor1;
    EditText motor2;
    EditText motor3;
    EditText motor4;
    EditText motor5;
    EditText motor6;
    EditText motor7;
    EditText motor8;

    String currentConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_editor);

        currentConfig = getIntent().getStringExtra("CONFIGURATION_NAME");
        TextView activeConfigName = findViewById(R.id.activeConfigurationNameLabelEdit);
        activeConfigName.setText(getIntent().getStringExtra("ACTIVE_CONFIGURATION_NAME"));
        motor1 = findViewById(R.id.motor1);
        motor2 = findViewById(R.id.motor2);
        motor3 = findViewById(R.id.motor3);
        motor4 = findViewById(R.id.motor4);
        motor5 = findViewById(R.id.motor5);
        motor6 = findViewById(R.id.motor6);
        motor7 = findViewById(R.id.motor7);
        motor8 = findViewById(R.id.motor8);

        try {
            StringBuilder sb = new StringBuilder();
            FileInputStream fis = new FileInputStream(new File(this.getFilesDir() + "/" + currentConfig + ".txt"));
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader buff = new BufferedReader(isr);

            String line;
            while ((line = buff.readLine()) != null) {
                sb.append(line + "\n");
            }

            fis.close();
            JSONObject allConfigs = new JSONObject(sb.toString());

            motor1.setText(allConfigs.getJSONArray("devices").get(0).toString());
            motor2.setText(allConfigs.getJSONArray("devices").get(1).toString());
            motor3.setText(allConfigs.getJSONArray("devices").get(2).toString());
            motor4.setText(allConfigs.getJSONArray("devices").get(3).toString());
            motor5.setText(allConfigs.getJSONArray("devices").get(4).toString());
            motor6.setText(allConfigs.getJSONArray("devices").get(5).toString());
            motor7.setText(allConfigs.getJSONArray("devices").get(6).toString());
            motor8.setText(allConfigs.getJSONArray("devices").get(7).toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        cancelButton = findViewById(R.id.cancelEditingConfigurationButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveButton = findViewById(R.id.saveConfigurationButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saveConfiguration()) {
                    if(currentConfig.equals(getIntent().getStringExtra("ACTIVE_CONFIGURATION_NAME"))) {
                        try {
                            StringBuilder sb = new StringBuilder();
                            FileInputStream fis = new FileInputStream(new File(getFilesDir() + "/" + getIntent().getStringExtra("ACTIVE_CONFIGURATION_NAME") + ".txt"));
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader buff = new BufferedReader(isr);

                            String line;
                            while ((line = buff.readLine()) != null) {
                                sb.append(line + "\n");
                            }

                            fis.close();
                            JSONObject allConfigs = new JSONObject(sb.toString());

                            String motor1Name = allConfigs.getJSONArray("devices").get(0).toString();
                            String motor2Name = allConfigs.getJSONArray("devices").get(1).toString();
                            String motor3Name = allConfigs.getJSONArray("devices").get(2).toString();
                            String motor4Name = allConfigs.getJSONArray("devices").get(3).toString();
                            String motor5Name = allConfigs.getJSONArray("devices").get(4).toString();
                            String motor6Name = allConfigs.getJSONArray("devices").get(5).toString();
                            String motor7Name = allConfigs.getJSONArray("devices").get(6).toString();
                            String motor8Name = allConfigs.getJSONArray("devices").get(7).toString();
                            String newActivityConfigFile = "motors: [\n" +
                                    "  {frc: \"frontLeft\", sim: \"" + motor1Name + "\"},\n" +
                                    "  {frc: \"frontRight\", sim: \"" + motor2Name + "\"},\n" +
                                    "  {frc: \"backLeft\", sim: \"" + motor3Name + "\"},\n" +
                                    "  {frc: \"backRight\", sim: \"" + motor4Name + "\"},\n" +
                                    "  {frc: \"intake\", sim: \"" + motor5Name + "\"},\n" +
                                    "  {frc: \"hopper\", sim: \"" + motor6Name + "\"},\n" +
                                    "  {frc: \"leftShooter\", sim: \"" + motor7Name + "\"},\n" +
                                    "  {frc: \"rightShooter\", sim: \"" + motor8Name + "\"},\n" +
                                    "]";

                            writeFileOnInternalStorage(ConfigurationEditor.this, "activeConfiguration.yaml", newActivityConfigFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    finish();
                }
            }
        });
    }

    private boolean saveConfiguration() {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            String motor1Text = motor1.getText().toString();
            String motor2Text = motor2.getText().toString();
            String motor3Text = motor3.getText().toString();
            String motor4Text = motor4.getText().toString();
            String motor5Text = motor5.getText().toString();
            String motor6Text = motor6.getText().toString();
            String motor7Text = motor7.getText().toString();
            String motor8Text = motor8.getText().toString();

            if(!motor1Text.equals("") || !motor2Text.equals("") || !motor3Text.equals("") || !motor4Text.equals("") || !motor5Text.equals("") || !motor6Text.equals("") || !motor7Text.equals("") || !motor8Text.equals("")) {
                jsonArray.put(motor1Text);
                jsonArray.put(motor2Text);
                jsonArray.put(motor3Text);
                jsonArray.put(motor4Text);
                jsonArray.put(motor5Text);
                jsonArray.put(motor6Text);
                jsonArray.put(motor7Text);
                jsonArray.put(motor8Text);

                jsonObject.put("devices", jsonArray);
                writeFileOnInternalStorage(ConfigurationEditor.this, currentConfig + ".txt", jsonObject.toString());
                return true;
            } else {
                Toast.makeText(this, "One or more fields are blank", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception ignore) {
            return false;
        }
    }

    private void writeFileOnInternalStorage(Context context, String fileName, String fileContent) {
        try {
            File file = new File(context.getFilesDir(), fileName);
            FileWriter writer = new FileWriter(file);
            writer.append(fileContent);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}