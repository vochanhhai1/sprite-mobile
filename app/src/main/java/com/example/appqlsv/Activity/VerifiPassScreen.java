package com.example.appqlsv.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.appqlsv.R;

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

public class VerifiPassScreen extends AppCompatActivity {
    private FrameLayout backBtn;
    private EditText codefield;
    private TextView reSendCode,subText;
    private AppCompatButton confirm_button;
    private String code;
    private  String userName;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifi_pass_screen);
        intview();
        userName = getIntent().getStringExtra("id");
        code= getIntent().getStringExtra("code");
        if (userName != null) {
            String text = getResources().getString(R.string.Codesendto) + "\n" + userName;
            subText.setText(text);
        }

        eventbuton();
    }

    private void eventbuton() {
        backBtn.setOnClickListener(view -> finish());
        confirm_button.setOnClickListener(view -> {
            if (code.equals(codefield.getText().toString())||verificationCode.equals(codefield.getText().toString()))
            {
                toSetNewPassScreen();
            }else
            {
                Toast.makeText(VerifiPassScreen.this, "Code không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
        reSendCode.setOnClickListener(view -> reSendCode());
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
    private class ResendVerificationTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String emailAddress = params[0];
            // Gửi lại mã xác nhận
            generateVerificationCode();
            sendVerificationCode(emailAddress);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Hiển thị thông báo hoặc thực hiện các xử lý khác sau khi gửi lại mã xác nhận
            Toast.makeText(VerifiPassScreen.this, "New verification code sent to " + userName, Toast.LENGTH_SHORT).show();
        }
    }

    private void reSendCode() {
        new ResendVerificationTask().execute(userName);
    }

    private void toSetNewPassScreen() {
        Intent it = new Intent(this, SetNewPassScreen.class);
        it.putExtra("id", userName);
        finishAffinity();
        startActivity(it);
        finish();
    }

    private void intview() {
        backBtn= findViewById(R.id.backBtn);
        codefield= findViewById(R.id.codefield);
        reSendCode= findViewById(R.id.reSendCode);
        confirm_button= findViewById(R.id.confirm_button);
        subText= findViewById(R.id.subText);
    }
}