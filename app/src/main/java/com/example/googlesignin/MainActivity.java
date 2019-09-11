package com.example.googlesignin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.signin.SignInOptions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int ReQ_CODE =9001;
    Button b;
    SignInButton b1;
    TextView t,t1,t2,t3;
    ImageView profile;
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b=(Button)findViewById(R.id.logout);
        b1=(SignInButton)findViewById(R.id.signin);
        t=(TextView)findViewById(R.id.name);
        t1=(TextView) findViewById(R.id.email);
        t2=(TextView) findViewById(R.id.oth);
        t3=(TextView) findViewById(R.id.oth1);
        profile=(ImageView)findViewById(R.id.imgpr);
        b.setOnClickListener(this);
        b1.setOnClickListener(this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

        }




    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.signin)
    {
        signin();
    }
        if(v.getId()==R.id.logout)
        {
            signout();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }
    private void signin()
    {

        Intent intent;
       intent= Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
       startActivityForResult(intent,ReQ_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==ReQ_CODE)
        {
            GoogleSignInResult res= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleresult(res);
        }
    }

    private void signout()
    {
     Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
         @Override
         public void onResult(@NonNull Status status) {
             updateui(false);
         }
     });

    }
    private void handleresult(GoogleSignInResult res)
    {
        if(res.isSuccess())
        {
            GoogleSignInAccount account= res.getSignInAccount();
            String name= account.getDisplayName();
            String email = account.getEmail();
            String other= account.getGivenName();
            String O1= account.getFamilyName();
                String ImgU = account.getPhotoUrl().toString();
                t.setText(name);
                t1.setText(email);
                t2.setText(other);
                t3.setText(O1);
                Glide.with(this).load(ImgU).into(profile);
                updateui(true);
                Toast.makeText(getApplicationContext(),"image available",Toast.LENGTH_LONG);




        }
        else {
            updateui(false);
        }

    }
    public void updateui( boolean dec)
    {
        if(dec)
        {

           b1.setVisibility(View.INVISIBLE);
           profile.setVisibility(View.VISIBLE);
        }
        else
        {
            t.setText(" ");
            t1.setText(" ");
            t2.setText(" ");
            t3.setText(" ");
            profile.setVisibility(View.INVISIBLE);
            b1.setVisibility(View.VISIBLE);
        }

    }
}
