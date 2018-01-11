package cn.easyar.samples.helloarvideo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText editTextCurrentP, editTextNewP, editTextCNewP;
    private Button buttonUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextCurrentP = (EditText) findViewById(R.id.editTextCurrentP);
        editTextNewP = (EditText) findViewById(R.id.editTextNewP);
        editTextCNewP = (EditText) findViewById(R.id.editTextCNewP);
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
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }

    private boolean validation() {
        //get current password
        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
        String password = sharedpreferences.getString("passwordKey", "");

        // Reset errors.
        editTextCurrentP.setError(null);
        editTextNewP.setError(null);
        editTextCNewP.setError(null);

        // Store values at the time of the login attempt.
        String currentpass = editTextCurrentP.getText().toString();
        String newpass = editTextNewP.getText().toString();
        String cnewpasword = editTextCNewP.getText().toString();


        boolean cancel = false;
        View focusView = null;

        // Check for a match password
        if (TextUtils.isEmpty(cnewpasword)) {
            editTextCNewP.setError(getString(R.string.error_field_required));
            focusView = editTextCNewP;
            cancel = true;
        } else if (!isCPasswordValid(newpass, cnewpasword)) {
            editTextCNewP.setError(getString(R.string.error_invalid_cpassword));
            focusView = editTextCNewP;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(newpass)) {
            editTextNewP.setError(getString(R.string.error_field_required));
            focusView = editTextNewP;
            cancel = true;
        } else if(newpass.equals(password)){
            editTextNewP.setError(getString(R.string.new_password_same));
            focusView = editTextNewP;
            cancel = true;
        }else if (!isPasswordLengthValid(newpass)) {
            editTextNewP.setError(getString(R.string.error_invalid_password));
            focusView = editTextNewP;
            cancel = true;
        } else if (!isPasswordValid(newpass)) {
            editTextNewP.setError(getString(R.string.error_invalid_password_type));
            focusView = editTextNewP;
            cancel = true;
        }

        //check match with previous password
        if(!currentpass.equals(password)){
            editTextCurrentP.setError(getString(R.string.current_password));
            focusView = editTextCurrentP;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        } else {
            return true;
        }
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

    public void updateRecord() {
        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
        Customer customer = new Customer();

        customer.setName(sharedpreferences.getString("nameKey", ""));
        customer.setEmail(sharedpreferences.getString("emailKey", ""));
        customer.setPasssword(editTextNewP.getText().toString());
        customer.setDOB(sharedpreferences.getString("DOBKey", ""));
        customer.setMobile(sharedpreferences.getString("mobileKey", ""));


        try {
            makeServiceCall(this, "https://wallcam.000webhostapp.com/update_password_customer.php", customer);
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
                                if (success==1) {

                                    //Update sharedPreferences
                                    SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("passwordKey", editTextNewP.getText().toString());
                                    editor.commit();

                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                    startActivity(intent);
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
