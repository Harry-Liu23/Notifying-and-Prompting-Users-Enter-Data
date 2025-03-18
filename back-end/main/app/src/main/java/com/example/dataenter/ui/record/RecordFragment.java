package com.example.dataenter.ui.record;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.dataenter.databinding.FragmentRecordBinding;
import com.example.dataenter.database.DatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RecordFragment extends Fragment {

    private FragmentRecordBinding binding;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        databaseHelper = new DatabaseHelper(requireContext());

        // Export button logic
        binding.exportButton.setOnClickListener(v -> exportData());
        populateTable();

        return root;
    }

    private void exportData() {
        StringBuilder data = new StringBuilder();
        try (Cursor cursor = databaseHelper.getAllRecords()) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    try {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                        String mood = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MOOD));
                        String water = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WATER));
                        String calorie = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CALORIE));
                        String entryTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATA_ENTRY_TIME));
                        String notificationTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTIFICATION_TIME));
                        String triggeredBy = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRIGGERED_BY));
                        data.append("ID: ").append(id)
                                .append(", Mood: ").append(mood)
                                .append(", Water: ").append(water)
                                .append(", Calorie: ").append(calorie)
                                .append(", Entry Time: ").append(entryTime)
                                .append(", Notification Time: ").append(notificationTime)
                                .append(", Triggered By: ").append(triggeredBy)
                                .append("\n");
                    } catch (IllegalArgumentException e) {
                        Log.e("DatabaseError", "Column missing in cursor: " + e.getMessage());
                    }
                } while (cursor.moveToNext());
            } else {
                Log.e("DatabaseError", "Cursor is empty or null.");
            }
        }

        if (data.length() == 0) {
            Toast.makeText(requireContext(), "No data to export", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(requireContext().getExternalFilesDir(null), "exported_data.txt");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data.toString().getBytes());
            Toast.makeText(requireContext(), "Data exported successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(requireContext(), "Failed to export data", Toast.LENGTH_SHORT).show();
            Log.e("RecordFragment", "Error writing file", e);
            return;
        }
        if (!file.exists() || file.length() == 0) {
            Log.e("ExportError", "File was not created or is empty.");
            Toast.makeText(requireContext(), "Export failed: File is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        shareFile(file);
    }



    private void shareFile(File file) {
        Uri fileUri = FileProvider.getUriForFile(requireContext(), "com.example.dataenter.fileprovider", file);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        requireContext().grantUriPermission(requireContext().getPackageName(), fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (shareIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivity(Intent.createChooser(shareIntent, "Share data via"));
        } else {
            Toast.makeText(requireContext(), "No app available to share file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void populateTable() {
        // Clear previous rows, if any
        binding.tableLayout.removeViews(1, Math.max(0, binding.tableLayout.getChildCount() - 1));

        // Get all records from the database
        try (Cursor cursor = databaseHelper.getAllRecords()) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    TableRow row = new TableRow(requireContext());
                    row.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    // Retrieve data from the cursor
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                    String mood = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MOOD));
                    String water = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WATER));
                    String calorie = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CALORIE));
                    String entryTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATA_ENTRY_TIME));
                    String notificationTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTIFICATION_TIME));

                    // Create and populate TextViews for each column
                    row.addView(createTextView(String.valueOf(id)));
                    row.addView(createTextView(mood));
                    row.addView(createTextView(water));
                    row.addView(createTextView(calorie));
                    row.addView(createTextView(entryTime));
                    row.addView(createTextView(notificationTime));

                    // Add the row to the table layout
                    binding.tableLayout.addView(row);
                } while (cursor.moveToNext());
            } else {
                Toast.makeText(requireContext(), "No records found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Helper method to create TextViews
    private TextView createTextView(String text) {
        TextView textView = new TextView(requireContext());
        textView.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        textView.setPadding(8, 8, 8, 8);
        textView.setText(text);
        return textView;
    }
}
