package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private int value = 0;

    protected BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int x = intent.getIntExtra(FibService.RESULT, 0);
            binding.text.setText(" " + x);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerReceiver(receiver, new IntentFilter(FibService.CHANNEL), RECEIVER_EXPORTED);

        binding.text.setText(" " + value);

        binding.minus.setOnClickListener(v -> {
            if (value > 0) {
                value--;
                binding.text.setText(" " + value);
            }
        });
        binding.plus.setOnClickListener(v -> {
            value++;
            binding.text.setText(" " + value);
        });
        binding.imageView.setOnClickListener(v -> {
            Toast.makeText(this, "Му", Toast.LENGTH_SHORT).show();
        });
        binding.Service.setOnClickListener(v -> {
            Intent intent = new Intent(this, FibService.class);
            intent.putExtra(FibService.PARAM, value);
            startService(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, FibService.class);
        stopService(intent);
    }
}