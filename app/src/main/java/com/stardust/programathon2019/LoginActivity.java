package com.stardust.programathon2019;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Patterns;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.stardust.programathon2019.Controller.Session;
import com.stardust.programathon2019.Controller.SessionManager;
import com.stardust.programathon2019.Controller.StudentController;
import com.stardust.programathon2019.Model.Awaitable;

import java.net.InetAddress;
import java.util.regex.Pattern;



public class LoginActivity extends AppCompatActivity implements Awaitable {
    private TextInputLayout id_entry;
    private TextInputLayout password_entry;
    private Button login_button;
    private static final Pattern ID_PATTERN = Pattern.compile("^[0-9]{1,10}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^.{1,10}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id_entry = findViewById(R.id.text_input_username);
        password_entry = findViewById(R.id.text_input_password);
        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmInput(view);
            }
        });
    }

    private boolean validateID() {
        String dni = id_entry.getEditText().getText().toString().trim();

        if (dni.isEmpty()) {
            id_entry.setError("El campo no puede estar vacío");
            return false;
        } else if (!ID_PATTERN.matcher(dni).matches()) {
            id_entry.setError("Debe ser una cédula valida");
            return false;
        } else {
            id_entry.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = password_entry.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password_entry.setError("El campo no puede estar vacío");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password_entry.setError("Debe ser una contraseña válida");
            return false;
        } else {
            password_entry.setError(null);
            return true;
        }


    }

    public void confirmInput(View v) {
        if (isOnline()) {
            if (!validateID() | !validatePassword()) {
                return;
            }
            String username = id_entry.getEditText().getText().toString();
            String password = password_entry.getEditText().getText().toString();
            Session session = SessionManager.getInstance().getSession();
            session.login(username,password,this);
            id_entry.getEditText().setText("");
            password_entry.getEditText().setText("");
        } else {
            Toast.makeText(this, "Esta aplicación ocupa una conección a internet para funcionar", Toast.LENGTH_LONG).show();
        }

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    public void onComplete() {
        Session session = SessionManager.getInstance().getSession();
        if (session.isLogged()) {
            // Toast.makeText(this, "inicio sesion", Toast.LENGTH_SHORT).show();
            Intent log = new Intent(this, KidsList.class);
            startActivity(log);
        } else {
            Toast.makeText(this, "El usuario o contraseña son incorrectos", Toast.LENGTH_SHORT).show();
        }

    }

}
