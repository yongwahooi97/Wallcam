package cn.easyar.samples.helloarvideo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppointmentActivity extends AppCompatActivity {
    Button buttonAppointment;
    EditText editTextDate, editTextName, editTextAddress, editTextContact;
    TextView textViewProduct;
    Spinner spinnerTime;
    CheckBox checkBoxBrick, checkBoxBlue, checkBoxWooden, checkBoxWord, checkBoxArt;
    private static  final String TAG = "AppointmentActivity";
    private DatePickerDialog.OnDateSetListener onDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextDate = (EditText) findViewById(R.id.editTextAppointmentDate);
        editTextName = (EditText) findViewById(R.id.editTextResidentName);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextContact = (EditText) findViewById(R.id.editTextContact);
        textViewProduct = (TextView) findViewById(R.id.textViewProduct) ;
        spinnerTime = (Spinner) findViewById(R.id.spinnerTime);
        checkBoxBrick = (CheckBox) findViewById(R.id.checkBoxBrick);
        checkBoxBlue = (CheckBox) findViewById(R.id.checkBoxBlue);
        checkBoxWooden = (CheckBox) findViewById(R.id.checkBoxWooden);
        checkBoxWord = (CheckBox) findViewById(R.id.checkBoxWord);
        checkBoxArt = (CheckBox) findViewById(R.id.checkBoxArt);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.time_array, R.layout.spinner_item);
        spinnerTime.setAdapter(adapter);

        buttonAppointment = (Button) findViewById(R.id.buttonAppointment);
        buttonAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid = validation();
                if(valid){
                    saveRecord();
                }
            }
        });

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, 7);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog diaglog = new DatePickerDialog(
                        AppointmentActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
                diaglog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                diaglog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyyy: " + year + "/" + month + "/" + day);

                String date = day + "/" + month + "/" + year;
                editTextDate.setText(date);
            }
        };


    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), CameraActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }

    public boolean validation(){
        // Reset errors.
        editTextDate.setError(null);
        editTextName.setError(null);
        editTextAddress.setError(null);
        editTextContact.setError(null);
        textViewProduct.setError(null);


        // Store values at the time of the login attempt.
        String date = editTextDate.getText().toString();
        String name = editTextName.getText().toString();
        String address = editTextAddress.getText().toString();
        String contact = editTextContact.getText().toString();
        //String spinner = spinnerTime.getSelectedItem().toString();

        boolean cancel = false;
        View focusView = null;

        //Check checkbox
        if(!checkBoxArt.isChecked()&&!checkBoxBrick.isChecked() && !checkBoxBlue.isChecked() && !checkBoxWooden.isChecked() && !checkBoxWord.isChecked() && !checkBoxArt.isChecked()){
            textViewProduct.setError(getString(R.string.error_product));
            focusView = textViewProduct;
            cancel = true;
        }
        //Check Appointment Date
        if (TextUtils.isEmpty(date)) {

            editTextDate.setError(getString(R.string.error_field_required));
            focusView = editTextDate;
            cancel = true;
        }else if(!isDateValid(date)){
            editTextDate.setError(getString(R.string.error_date));
            focusView = editTextDate;
            cancel = true;
        }

        //Check contact
        if (TextUtils.isEmpty(contact)) {
            editTextContact.setError(getString(R.string.error_field_required));
            focusView = editTextContact;
            cancel = true;
        } else if (!isContactValid(contact) && !isContactValid1(contact)) {
            editTextContact.setError(getString(R.string.error_invalid_contact_format));
            focusView = editTextContact;
            cancel = true;
        }

        //Check for valid address
        if (TextUtils.isEmpty(address)) {
            editTextAddress.setError(getString(R.string.error_field_required));
            focusView = editTextAddress;
            cancel = true;
        }

        //Check for valid name
        if (TextUtils.isEmpty(name)) {
            editTextName.setError(getString(R.string.error_field_required));
            focusView = editTextName;
            cancel = true;
        } else if (!isNameValid(name)) {
            editTextName.setError(getString(R.string.error_invalid_name));
            focusView = editTextName;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean isNameValid(String name) {
        return name.length() > 5;
    }


    private boolean isContactValid(String contact) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(\\+?6?01)[0-46-9]-*[0-9]{7,8}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(contact);

        return matcher.matches();
    }

    private boolean isContactValid1(String contact) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(\\+?6?01)[0-46-9]*[0-9]{7,8}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(contact);

        return matcher.matches();
    }

    private boolean isDateValid(String date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date appointmentDate = null;
        try {
            appointmentDate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DAY_OF_MONTH, 6);
        Date validDate = cal1.getTime();

        if(appointmentDate.after(validDate)){
            return true;
        }else{
            return false;
        }
    }

    public void saveRecord() {
        Appointment appointment = new Appointment();

        appointment.setResidentName(editTextName.getText().toString());
        appointment.setAddress(editTextAddress.getText().toString());
        appointment.setContact(editTextContact.getText().toString());
        appointment.setAppointmentDate(editTextDate.getText().toString());
        appointment.setAppointmentTime(spinnerTime.getSelectedItem().toString());

        String product = "";
        if(checkBoxArt.isChecked()){ product+= "Art Work ";}
        if(checkBoxWord.isChecked()){ product+= "Word Pattern Wallpaper ";}
        if(checkBoxWooden.isChecked()){ product+= "Wooden Pattern Wallpaper "; }
        if(checkBoxBlue.isChecked()){ product+= "Blue Pattern Wallpaper ";}
        if(checkBoxBrick.isChecked()){ product+= "Brick Pattern Wallpaper ";}

        appointment.setProduct(product);

        try {
            makeServiceCall(this, "https://wallcam.000webhostapp.com/insert_appointment.php", appointment);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void makeServiceCall(Context context, String url, final Appointment appointment) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);

        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String message = jsonObject.getString("message");
                                if (success==1) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                                    startActivity(intent);
                                }else if(success==-1){
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("residentName", appointment.getResidentName());
                    params.put("address", appointment.getAddress());
                    params.put("contact", appointment.getContact());
                    params.put("appointmentDate", appointment.getAppointmentDate());
                    params.put("appointmentTime", appointment.getAppointmentTime());
                    params.put("product", appointment.getProduct());
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
