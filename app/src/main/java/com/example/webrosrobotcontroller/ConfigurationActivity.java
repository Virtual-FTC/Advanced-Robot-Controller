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

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationActivity extends AppCompatActivity {

    String activeConfigurationName;
    TextView activeConfigurationNameLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        activeConfigurationName = getIntent().getStringExtra("ACTIVE_CONFIGURATION_NAME");
        activeConfigurationNameLabel = findViewById(R.id.activeConfigurationNameLabel);
        activeConfigurationNameLabel.setText(activeConfigurationName);

        ArrayList<String> existingConfigurationNames = new ArrayList<>();

        existingConfigurationNames.add("a");
        existingConfigurationNames.add("b");
        existingConfigurationNames.add("c");

        ListView listView = findViewById(R.id.configurationListView);
        listView.setAdapter(new ConfigurationListAdapter(this, R.layout.configuration_listview_item, existingConfigurationNames));
    }

    public void writeFileOnInternalStorage(Context context, String fileName, String fileContent){
        try {
            File file = new File(context.getFilesDir(), fileName);
            FileWriter writer = new FileWriter(file);
            writer.append(fileContent);
            writer.flush();
            writer.close();
        } catch (Exception e){
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
            if(convertView == null) {
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
                        if(allConfigurationNames.get(position).equals(activeConfigurationName)) {
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