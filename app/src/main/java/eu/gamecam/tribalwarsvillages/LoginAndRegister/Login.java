package eu.gamecam.tribalwarsvillages.LoginAndRegister;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import eu.gamecam.tribalwarsvillages.Config.FirebaseConfigManager;
import eu.gamecam.tribalwarsvillages.FetchDataAsynchTask;
import eu.gamecam.tribalwarsvillages.Menu;
import eu.gamecam.tribalwarsvillages.R;

public class Login extends AppCompatActivity {
    static String ACTUAL_PROJECT_VERSION="0.0.0";
    static int ACTUAL_PROJECT_VERSION_CODE=0;
    public static boolean cont=false;

    boolean actualVersion;
    private FirebaseAuth firebaseAuth;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignIn;

    String ACTUAL_VERSION;
    int ACTUAL_VERSION_CODE;
    String wc;

    FirebaseConfigManager fcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        fcm = new FirebaseConfigManager();
        fcm.getRemoteConfig(this);

        editTextEmail = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);



        //Aktuálne verzia appky
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            ACTUAL_PROJECT_VERSION = pInfo.versionName;
            ACTUAL_PROJECT_VERSION_CODE= pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        //toto bude musieť byť prerobené do asynch tasku

        /*FetchDataAsynchTask fts = new FetchDataAsynchTask(this,fcm);
        fts.execute();*/
        tryReadData();


        /*if (ACTUAL_VERSION.equals("0.0.0")) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();

        }*/

        System.out.println("Aktuálna verzia*******************************: "+ACTUAL_VERSION);

        if (!ACTUAL_VERSION.equals(ACTUAL_PROJECT_VERSION) || ACTUAL_PROJECT_VERSION_CODE!=ACTUAL_VERSION_CODE) {
            actualVersion=false;
            TextView version=(TextView) findViewById(R.id.version);
            version.setVisibility(View.VISIBLE);
        } else actualVersion=true;

        if (actualVersion==true) {
            isUserLogIn();
        }



        Button btn= (Button) findViewById(R.id.button_go_to_system);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actualVersion) {
                    userLogin();
                //startActivity(new Intent(getApplicationContext(),Menu.class));
                //finish();
                }  else {
                    Toast.makeText(getApplicationContext(),"Máš starú verziu appky",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btn2= (Button) findViewById(R.id.button_register_me);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterButton();
            }
        });
    }

    public void RegisterButton() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.register_dialog);
        Button buttonSignup = (Button) dialog.findViewById(R.id.buttonRegister);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(dialog);
            }
        });

        dialog.show();

    }

    private void registerUser(Dialog dg){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);

        EditText editTextEmail = (EditText) dg.findViewById(R.id.editTextEmail);
        EditText editTextPassword = (EditText) dg.findViewById(R.id.editTextPassword);

        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            final FirebaseUser user = firebaseAuth.getCurrentUser();
                            user.sendEmailVerification();
                            Toast.makeText(Login.this,"Na váš email bolo zaslané overenie",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Menu.class));
                            finish();


                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(Login.this,"Registrácia zlyhala, skúste to znova",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void isUserLogIn () {
        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Menu.class));
            finish();
        }
    }

    public void userLogin(){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Zadajte email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Zadajte heslo",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Login.... please wait");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(), Menu.class));
                            finish();
                        } else {
                            Toast.makeText(getApplication(),"Špatné meno alebo heslo",Toast.LENGTH_LONG).show();
                        }


                    }
                });

    }

    public void tryReadData() {
        ACTUAL_VERSION = fcm.getmFirebaseRemoteConfig().getString("actual_version");
        ACTUAL_VERSION_CODE =(int)(fcm.getmFirebaseRemoteConfig().getLong("actual_version_code"));
        wc= fcm.getmFirebaseRemoteConfig().getString("welcome_text");
    }
}
