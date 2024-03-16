package com.example.appqlsv.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.Models.Users;
import com.example.appqlsv.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPassScreen extends AppCompatActivity {
    private FrameLayout backBtn;
    private EditText idfield;
    private AppCompatButton sendBtn;
    private ProgressBar bar;
    private ArrayList<Users> usersArrayList;
    private String verificationCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_screen);
        initview();
        usersArrayList= new ArrayList<>();

        eventbuton();

    }

    private void eventbuton() {
        backBtn.setOnClickListener(view -> finish());
        sendBtn.setOnClickListener(view -> toVerifyPassScreen());
    }

    private void sendVerificationCode(String emailAddress) {
        final String username = "vohai199833@gmail.com";
        final String password = "hdaebfvuulzcskbj";

        // Mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Session object to authenticate the sender
        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);
            // Set sender's email address
            message.setFrom(new InternetAddress(username));
            // Set recipient's email address
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailAddress));
            // Set email subject
            message.setSubject("Password Reset Verification Code");
            // Set email content
            message.setText("Your verification code is: " + verificationCode);
            // Send the email
            Transport.send(message);
        } catch (MessagingException e) {
            Log.e("SendEmailTask", "Error sending email: " + e.getMessage());
        }
    }

    private void generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        verificationCode = String.valueOf(code);
    }
    @SuppressLint("StaticFieldLeak")
    private class SendEmailTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String emailAddress = params[0];
            // Send email in background
            sendVerificationCode(emailAddress);
            return null;
        }
    }
    private void toVerifyPassScreen() {
        String emailAddress = idfield.getText().toString();
        if (!emailAddress.isEmpty()) {
            // Generate verification code
            generateVerificationCode();

            // Send verification code to email address
            new SendEmailTask().execute(emailAddress);
            idfield.setBackgroundResource(R.drawable.rounded_edittext_normal);
            Toast.makeText(ForgotPassScreen.this, "Verification code sent to " + emailAddress, Toast.LENGTH_SHORT).show();
        } else {
            idfield.setBackgroundResource(R.drawable.rounded_edittext_error);
            Toast.makeText(ForgotPassScreen.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
        }
        sendCodeFunc();
    }

    private void sendCodeFunc() {
        String url  ="https://spriteee.000webhostapp.com/getAdminAllUser.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        bar.setVisibility(View.VISIBLE);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, jsonArray -> {
            usersArrayList.clear();
            for (int i = 0 ; i< jsonArray.length();i++){
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (idfield.getText().toString().trim().equals(object.getString("email").trim()))
                    {
                        Intent intent = new Intent(getApplicationContext(), VerifiPassScreen.class);
                        intent.putExtra("id", idfield.getText().toString());
                        intent.putExtra("code",verificationCode);
                        startActivity(intent);
                        break;
                    }
                    bar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, volleyError -> Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show());
        requestQueue.add(jsonArrayRequest);
    }

    private void initview() {
        backBtn= findViewById(R.id.backBtn);
        idfield= findViewById(R.id.idfield);
        sendBtn= findViewById(R.id.sendBtn);
        bar= findViewById(R.id.bar);
    }
}