package online.nozzy.nozzytasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener{


    private static final String TAG = "LoginActivity";
    private static final int RC_GET_TOKEN = 9002;

    private GoogleSignInClient mGoogleSignInClient;
    private TextView mIdTokenTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Various setup things
        mIdTokenTextView = findViewById(R.id.detail);

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("606629875545-j5e053fkikrmgls4gaum2mpg9nu7uojm.apps.googleusercontent.com")
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getIdToken() {
        Intent logInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(logInIntent, RC_GET_TOKEN);
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask){
        try{
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);

        }catch(ApiException e){
            Log.w(TAG, "handleSignInResult:error", e);
            updateUI(null);
        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account){
        if (account != null){
            ((TextView)findViewById(R.id.status)).setText("Signed in");
            String idToken = account.getIdToken();
            mIdTokenTextView.setText(getString(R.string.id_token_fmt, idToken));
        }
        else{
            ((TextView)findViewById(R.id.status)).setText("Signed out");
            mIdTokenTextView.setText("ID Token: null");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GET_TOKEN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    @Override
    public void onClick(View v){
switch (v.getId()){
    case R.id.sign_in_button:
        getIdToken();
        break;
}
    }

}
