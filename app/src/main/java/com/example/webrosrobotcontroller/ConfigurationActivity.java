package com.example.webrosrobotcontroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationActivity extends AppCompatActivity implements NewConfigurationDialog.NewConfigurationDialogListener {

    String activeConfigurationName;
    TextView activeConfigurationNameLabel;
    ArrayList<String> existingConfigurationNames;
    ListView listView;
    ConfigurationListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        activeConfigurationName = getIntent().getStringExtra("ACTIVE_CONFIGURATION_NAME");
        activeConfigurationNameLabel = findViewById(R.id.activeConfigurationNameLabel);
        activeConfigurationNameLabel.setText(activeConfigurationName);

        Button newConfiguration = findViewById(R.id.newConfigurationButton);
        newConfiguration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewConfigurationDialog configurationDialog = new NewConfigurationDialog();
                configurationDialog.show(getSupportFragmentManager(), "newConfigDialog");
            }
        });
        existingConfigurationNames = new ArrayList<>();

        try {
            StringBuilder sb = new StringBuilder();
            FileInputStream fis = new FileInputStream(new File(this.getFilesDir() + "/" + "configurations.txt"));
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader buff = new BufferedReader(isr);

            String line;
            while ((line = buff.readLine()) != null) {
                sb.append(line + "\n");
            }

            fis.close();
            JSONObject allConfigs = new JSONObject(sb.toString());

            for(int i = 0; i < allConfigs.getJSONArray("configurations").length(); i++) {
                existingConfigurationNames.add(allConfigs.getJSONArray("configurations").get(i).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button doneButton = findViewById(R.id.doneButtonConfigurationMenu);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView = findViewById(R.id.configurationListView);
        adapter = new ConfigurationListAdapter(this, R.layout.configuration_listview_item, existingConfigurationNames);
        listView.setAdapter(adapter);
    }

    @Override
    public void createNewConfiguration(String name) {
        existingConfigurationNames.add(name);
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < existingConfigurationNames.size(); i++) {
                jsonArray.put(existingConfigurationNames.get(i));
            }
            jsonObject.put("configurations", jsonArray);
            writeFileOnInternalStorage(this, "configurations.txt", jsonObject.toString());
        } catch (Exception ignore) {

        }
        adapter.notifyDataSetChanged();
    }

    public void writeFileOnInternalStorage(Context context, String fileName, String fileContent) {
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

    private class ConfigurationListAdapter extends ArrayAdapter<String> {
        private int layout;
        private List<String> allConfigurationNames;

        public ConfigurationListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            layout = resource;
            allConfigurationNames = objects;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ConfigurationListViewHolder mainViewHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ConfigurationListViewHolder viewHolder = new ConfigurationListViewHolder();
                viewHolder.configurationName = convertView.findViewById(R.id.configurationListViewNameText);
                viewHolder.editConfiguration = convertView.findViewById(R.id.configurationListViewEditButton);
                viewHolder.activateConfiguration = convertView.findViewById(R.id.configurationListViewActivateButton);
                viewHolder.deleteConfiguration = convertView.findViewById(R.id.configurationListViewDeleteButton);

                viewHolder.editConfiguration.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

                viewHolder.activateConfiguration.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activeConfigurationName = allConfigurationNames.get(position);
                        activeConfigurationNameLabel.setText(activeConfigurationName);
                        writeFileOnInternalStorage(getContext(), "activeConfig.txt", activeConfigurationName);
                    }
                });

                viewHolder.deleteConfiguration.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (allConfigurationNames.get(position).equals(activeConfigurationName)) {
                            activeConfigurationName = "";
                            activeConfigurationNameLabel.setText(activeConfigurationName);
                        }
                        allConfigurationNames.remove(position);
                        notifyDataSetChanged();

                    }
                });
                viewHolder.configurationName.setText(getItem(position));
                convertView.setTag(viewHolder);
            } else {
                mainViewHolder = (ConfigurationListViewHolder) convertView.getTag();
                mainViewHolder.configurationName.setText(getItem(position));
            }

            return convertView;
        }

        @Nullable
        @Override
        public String getItem(int position) {
            return allConfigurationNames.get(position);
        }
    }

    public class ConfigurationListViewHolder {
        TextView configurationName;
        Button editConfiguration;
        Button activateConfiguration;
        Button deleteConfiguration;
    }
}