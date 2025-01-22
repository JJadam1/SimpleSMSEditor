package com.example.simplesmseditor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Spinner templateSpinner;
    private EditText templateContent;
    private EditText variablesInput;
    private String[] templateNames;
    private String[] templateContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        templateSpinner = findViewById(R.id.templateSpinner);
        templateContent = findViewById(R.id.templateContent);
        variablesInput = findViewById(R.id.variablesInput);
        Button saveButton = findViewById(R.id.saveTemplateButton);
        Button sendButton = findViewById(R.id.sendButton);

        // 加载模板
        templateNames = getResources().getStringArray(R.array.template_names);
        templateContents = getResources().getStringArray(R.array.template_contents);

        // 设置下拉框
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, templateNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        templateSpinner.setAdapter(adapter);

        // 选择模板时更新内容
        templateSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                templateContent.setText(templateContents[position]);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        // 保存模板
        saveButton.setOnClickListener(v -> {
            int position = templateSpinner.getSelectedItemPosition();
            templateContents[position] = templateContent.getText().toString();
            Toast.makeText(this, "模板已保存", Toast.LENGTH_SHORT).show();
        });

        // 发送短信
        sendButton.setOnClickListener(v -> {
            String content = templateContent.getText().toString();
            String variables = variablesInput.getText().toString();

            // 替换变量
            if (!variables.isEmpty()) {
                String[] pairs = variables.split(",");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    if (keyValue.length == 2) {
                        content = content.replace("{" + keyValue[0].trim() + "}", keyValue[1].trim());
                    }
                }
            }

            // 打开短信应用
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, content);
            startActivity(Intent.createChooser(intent, "选择应用发送短信"));
        });
    }
}
