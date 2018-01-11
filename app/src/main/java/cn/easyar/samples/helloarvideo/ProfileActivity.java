package cn.easyar.samples.helloarvideo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {
    private EditText editTextName, editTextBirthday, editTextMobile;
    private TextView textViewPassword;
    private static  final String TAG = "ProfileActivity";
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private Button buttonUpdate, buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
        String name, dob, mobile;


        name = sharedpreferences.getString("nameKey", "");
        dob = sharedpreferences.getString("DOBKey", "");
        mobile = sharedpreferences.getString("mobileKey", "");

        editTextName = (EditText) findViewById(R.id.editTextPName);
        editTextBirthday = (EditText) findViewById(R.id.editTextPBirthday);
        editTextMobile = (EditText) findViewById(R.id.editTextPContact);
        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid = validation();
                if(valid){
                    updateRecord();
                }
            }
        });
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Thanks you for using your apps!\nHope to See You Again!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        textViewPassword = (TextView) findViewById(R.id.textViewPassword);
        String first = "Wish to change password? ";
        String next = "<font color='#EE0000'>Change now</font>";
        textViewPassword.setText(Html.fromHtml(first + next));
        textViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        editTextName.setText(name);
        editTextBirthday.setText(dob);
        editTextMobile.setText(mobile);

        editTextBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog diaglog = new DatePickerDialog(
                        ProfileActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
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
                editTextBirthday.setText(date);
            }
        };


        BottomNavigationView bottomNavigationItemView = (BottomNavigationView) findViewById(R.id.NavBot);
        bottomNavigationItemView.setSelectedItemId(R.id.Profile);
        bottomNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.Category){
                    Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                    startActivity(intent);
                    return true;
                }else if(id == R.id.Scan){
                    Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                    startActivity(intent);
                    return true;
                }else if(id == R.id.About){
                    Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                    startActivity(intent);
                    return true;
                }else {
                    return false;
                }
            }
        });
    }

    public boolean validation(){
        // Reset errors.
        editTextName.setError(null);
        editTextBirthday.setError(null);
        editTextMobile.setError(null);

        // Store values at the time of the login attempt.
        String name = editTextName.getText().toString();
        String bday = editTextBirthday.getText().toString();
        String contact = editTextMobile.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //Check contact
        if (TextUtils.isEmpty(contact)) {
            editTextMobile.setError(getString(R.string.error_field_required));
            focusView = editTextMobile;
            cancel = true;
        } else if (!isContactValid(contact) && !isContactValid1(contact)) {
            editTextMobile.setError(getString(R.string.error_invalid_contact_format));
            focusView = editTextMobile;
            cancel = true;
        }

        //Check birthday
        if (TextUtils.isEmpty(bday)) {
            editTextBirthday.setError(getString(R.string.error_field_required));
            focusView = editTextBirthday;
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

    public void updateRecord() {
        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
        Customer customer = new Customer();

        customer.setName(editTextName.getText().toString());
        customer.setEmail(sharedpreferences.getString("emailKey", ""));
        customer.setPasssword(sharedpreferences.getString("passwordKey", ""));
        customer.setDOB(editTextBirthday.getText().toString());
        customer.setMobile(editTextMobile.getText().toString());

        try {
            makeServiceCall(this, "https://wallcam.000webhostapp.com/update_customer.php", customer);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void makeServiceCall(Context context, String url, final Customer customer) {
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
                                if (success== 1) {
                                    //Update sharedPreferences
                                    SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("nameKey", editTextName.getText().toString());
                                    editor.putString("DOBkey", editTextBirthday.getText().toString());
                                    editor.putString("mobileKey", editTextMobile.getText().toString());
                                    editor.commit();

                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(getIntent());
                                }else{
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
                    params.put("name", customer.getName());
                    params.put("email", customer.getEmail());
                    params.put("password", customer.getPasssword());
                    params.put("DOB", customer.getDOB());
                    params.put("mobile", customer.getMobile());
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
