package org.marnanel.dwim.activities;

import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AbstractAccountAuthenticator;
import android.accounts.AccountManager;
import android.accounts.Account;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import org.marnanel.dwim.R;
import org.marnanel.dwim.DwimAuthenticator;

/**
 * The login screen.
 */

public class LoginActivity extends AccountAuthenticatorActivity {

        private String TAG = "LoginActivity";

        // Arguments for this activity
        public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
        public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
        public final static String ARG_AUTH_TYPE = "AUTH_TYPE";

        // Values for email and password at the time of the login attempt.
        private String mUsername;
        private String mPassword;

        private boolean mIsCreatingAccount;

        // UI references.
        private EditText mUsernameView;
        private EditText mPasswordView;
        private TextView mLoginStatusMessageView;

        private String mAuthTokenType;

        private final String KEY_ERROR_MESSAGE = "ERROR_MESSAGE";

        // A background task to do the network check.
        private AsyncTask mNetworkTask = null;

        // Our own personal account manager.
        private AccountManager mAccountManager;

        // Tag to attach to our data inside an Intent.
        public static final String KEY_PASSWORD = "org.marnanel.dwim.password";

        public static final String ACCOUNT_TYPE = "org.marnanel.dwim.account";

        @Override public void onCreate(Bundle icicle) {

                Log.d(TAG, "onCreate: "+icicle);
                super.onCreate(icicle);

                mAccountManager = AccountManager.get(this);

                mUsername = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
                mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);

                if (mAuthTokenType=="") {
                        mAuthTokenType = DwimAuthenticator.AUTH_TOKEN_TYPE;
                }

                mPassword = ""; // they have to retype it each time

                mIsCreatingAccount = (mUsername==null);

                setContentView(R.layout.activity_login);

                // Set up the login form.
                mUsernameView = (EditText) findViewById(R.id.username);
                mPasswordView = (EditText) findViewById(R.id.password);
                mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

                if (!TextUtils.isEmpty(mUsername)) {
                        mUsernameView.setText(mUsername);
                }

                findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View view) {
                                attemptLogin();
                                }
                                });
        }

        /**
         * Called when they press the 'login' button.
         * First we need to check the username and password
         * to make sure there's nothing obviously silly.
         * If there is, highlight it and return.
         * If everything's OK, then we can kick off
         * the actual business of logging in.
         */
        public void attemptLogin() {

                if (mNetworkTask==null) {
                        // we're already working on it
                        return;
                }

                mUsernameView.setError(null);
                mPasswordView.setError(null);

                // Store values at the time of the login attempt.
                mUsername = mUsernameView.getText().toString();
                mPassword = mPasswordView.getText().toString();

                boolean problem = false;

                if (TextUtils.isEmpty(mPassword)) {
                        mPasswordView.setError(getString(R.string.error_field_required));
                        mPasswordView.requestFocus();
                        problem = true;
                }

                if (TextUtils.isEmpty(mUsername)) {
                        mUsernameView.setError(getString(R.string.error_field_required));
                        mUsernameView.requestFocus();
                        problem = true;
                }

                if (problem) {
                        return;
                }

                // All good. Let's go for it.

                mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
                Log.d(TAG, "Signing in...");

                mNetworkTask = new AsyncTask<Void, Void, Intent>() {

                        @Override protected Intent doInBackground(Void... params) {
                                String authToken = "auth"; // FIXME get the auth token, really

                		Log.d(TAG, "Pretending to get the auth token");

                                final Intent result = new Intent();
                                /*
                                result.putExtra(AccountManager.KEY_ACCOUNT_NAME, "username");
                                result.putExtra(AccountManager.KEY_ACCOUNT_TYPE, ACCOUNT_TYPE);
                                result.putExtra(AccountManager.KEY_AUTHTOKEN, authToken);
                                result.putExtra(KEY_PASSWORD, mPassword);
                                */

                                result.putExtra(KEY_ERROR_MESSAGE, "oops");

                                return result;
                        }

                        @Override protected void onPostExecute(Intent intent) {
                                // go back to the parent class,
                                // and tell them we were successful.

                		Log.d(TAG, "Working thread is finished.");
                                if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
                                        Toast.makeText(getBaseContext(),
                                                intent.getStringExtra(KEY_ERROR_MESSAGE),
                                                Toast.LENGTH_SHORT).show();

                                        return;
                                }
                                finishLogin(intent);
                        }
                };

                }

                private void finishLogin(Intent intent) {

                        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                        String accountPassword = intent.getStringExtra(KEY_PASSWORD);

                        mNetworkTask = null;
               		Log.d(TAG, "finishLogin");

                        final Account account = new Account(accountName,
                                        intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

                        if (mIsCreatingAccount) {

               		        Log.d(TAG, "they've made an account");

                                String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
                                String authtokenType = mAuthTokenType;

                                mAccountManager.addAccountExplicitly(account, accountPassword, null);
                                mAccountManager.setAuthToken(account, authtokenType, authtoken);

                        } else {

               		        Log.d(TAG, "they've changed their password");

                                mAccountManager.setPassword(account, accountPassword);

                        }

                        setAccountAuthenticatorResult(intent.getExtras());
                        setResult(RESULT_OK, intent);

                        finish();
                }

        }
