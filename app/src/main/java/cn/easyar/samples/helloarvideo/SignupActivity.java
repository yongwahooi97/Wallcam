package cn.easyar.samples.helloarvideo;

import android.app.DatePickerDialog;
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
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

/**
 * A login screen that offers login via email/password.
 */
public class SignupActivity extends AppCompatActivity {


    private static  final String TAG = "SignupActivity";
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private EditText editTextBirthday;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private EditText editTextName;
    private EditText editTextCpassword;
    private EditText editTextContact;
    private Button buttonSignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.buttonUpdate);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid = validation();
                if(valid) {
                    saveRecord();
                }
            }
        });

        editTextBirthday = (EditText) findViewById(R.id.editTextBirthday);
        editTextBirthday.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog diaglog = new DatePickerDialog(
                        SignupActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
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

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        editTextContact = (EditText) findViewById(R.id.editTextContact);
        editTextCpassword = (EditText) findViewById(R.id.editTextCPassword);
        editTextName = (EditText) findViewById(R.id.editTextName);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }

    private boolean validation() {

        // Reset errors.
        editTextName.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);
        editTextCpassword.setError(null);
        editTextBirthday.setError(null);
        editTextContact.setError(null);

        // Store values at the time of the login attempt.
        String name = editTextName.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String cpasword = editTextCpassword.getText().toString();
        String bday = editTextBirthday.getText().toString();
        String contact = editTextContact.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(contact)) {
            editTextContact.setError(getString(R.string.error_field_required));
            focusView = editTextContact;
            cancel = true;
        } else if (!isContactValid(contact) && !isContactValid1(contact)) {
            editTextContact.setError(getString(R.string.error_invalid_contact_format));
            focusView = editTextContact;
            cancel = true;
        }

        //Check birthday
        if (TextUtils.isEmpty(bday)) {
            editTextBirthday.setError(getString(R.string.error_field_required));
            focusView = editTextBirthday;
            cancel = true;
        }


        // Check for a match password
        if (TextUtils.isEmpty(cpasword)) {
            editTextCpassword.setError(getString(R.string.error_field_required));
            focusView = editTextCpassword;
            cancel = true;
        } else if (!isCPasswordValid(password, cpasword)) {
            editTextCpassword.setError(getString(R.string.error_invalid_cpassword));
            focusView = editTextCpassword;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordLengthValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password_type));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
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

    private boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPasswordLengthValid(String password) {
        return password.length() > 5;
    }

    private boolean isPasswordValid(String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-zA-z]).{6,20})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    private boolean isCPasswordValid(String password, String cpassword) {
        return password.equals(cpassword);
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


    public void saveRecord() {
        Customer customer = new Customer();

        customer.setName(editTextName.getText().toString());
        customer.setEmail(mEmailView.getText().toString());
        customer.setPasssword(mPasswordView.getText().toString());
        customer.setDOB(editTextBirthday.getText().toString());
        customer.setMobile(editTextContact.getText().toString());

        try {
            makeServiceCall(this, "https://wallcam.000webhostapp.com/insert_customer.php", customer);
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
                                if (success==-1) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    emailUsed();
                                }else if(success==0){
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    successRegister();
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

    public void successRegister(){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
    }

    public void emailUsed(){
        mEmailView.setError(null);
        View focusView = null;
        mEmailView.setError(getString(R.string.email_use));
        focusView = mEmailView;
        focusView.requestFocus();
    }
}

