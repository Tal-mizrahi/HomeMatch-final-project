
package com.example.homematch.Utilities;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.example.homematch.Adapters.HouseAdapter;
import com.example.homematch.Interfaces.ScheduleCallBack;
import com.example.homematch.Models.House;
import com.example.homematch.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Calendar;

public class ScheduleOpenHouseDialogManager {

    private Dialog dialog;
    private TextInputEditText dateInput, timeInput;
    private TextInputLayout dateLayout, timeLayout;
    private ScheduleCallBack scheduleCallBack;
    private  MaterialButton confirmButton, cancelButton;

    public ScheduleOpenHouseDialogManager(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.open_house_dialog);
        findViews();
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    public void showDialog(House house, int position, Context context) {

        resetUI();
        dateInput.setOnClickListener(v -> showDatePicker(context));

        // Set up time selection
        timeInput.setOnClickListener(v -> showTimePicker(context));

        // Set up confirm button action
        confirmButton.setOnClickListener(v -> {
            if(validateInput()){
                house.setOpenHouseDate(dateInput.getText().toString());
                house.setOpenHouseTime(timeInput.getText().toString());
                if(scheduleCallBack != null){
                    scheduleCallBack.onScheduleOpenHouse(house, position);
                }
                dialog.dismiss();
            }
        });

        // Set up cancel button action
        cancelButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    public void resetUI(){
        dateLayout.setError(null);
        timeLayout.setError(null);
    }

    public boolean validateInput() {
        boolean isValid = true;

        // Validate date
        if (TextUtils.isEmpty(dateInput.getText())) {
            dateLayout.setError("Date is required");
            isValid = false;
        } else {
            dateLayout.setError(null);
        }

        // Validate time
        if (TextUtils.isEmpty(timeInput.getText())) {
            timeLayout.setError("Time is required");
            isValid = false;
        } else {
            timeLayout.setError(null);
        }
        return isValid;
    }

//    private void showDatePicker(Context context) {
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(context,
//            (view, selectedYear, selectedMonth, selectedDay) -> {
//                String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
//                dateInput.setText(selectedDate);
//            }, year, month, day);
//        datePickerDialog.show();
//    }

    private void showDatePicker(Context context) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(context,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    dateInput.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }



//    private void showTimePicker(Context context) {
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//
//        android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(context,
//            (view, selectedHour, selectedMinute) -> {
//                String selectedTime = selectedHour + ":" + String.format("%02d", selectedMinute);
//                timeInput.setText(selectedTime);
//            }, hour, minute, true);
//        timePickerDialog.show();
//    }


    private void showTimePicker(Context context) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(context,
                (view, selectedHour, selectedMinute) -> {
                    String selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                    timeInput.setText(selectedTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    public void setScheduleCallBack(ScheduleCallBack scheduleCallBack) {
        this.scheduleCallBack = scheduleCallBack;
    }


    private void findViews() {
        dateInput = dialog.findViewById(R.id.text_input_date);
        timeInput = dialog.findViewById(R.id.text_input_time);
        confirmButton = dialog.findViewById(R.id.btn_confirm);
        cancelButton = dialog.findViewById(R.id.btn_cancel);
        dateLayout = dialog.findViewById(R.id.text_input_layout_date);
        timeLayout = dialog.findViewById(R.id.text_input_layout_time);
    }
}
