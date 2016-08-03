package org.marnanel.dwim;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.AccountManager;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.accounts.Account;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.text.TextUtils;
import org.marnanel.dwim.activities.LoginActivity;
import android.util.Log;

/**
 * This is where we deal with login sessions.
 * Generally, this is called by the device's AccountManager
 * in order to log the user in to DW.
 */

public class DwimAuthenticator extends AbstractAccountAuthenticator {

        private static final String TAG = "DwimAuthenticator";

        public static final String AUTH_TOKEN_TYPE = "org.marnanel.dwim";

        private final Context mContext;

        public DwimAuthenticator(Context context) {
                super(context);

                mContext = context;
        }

        /**
        * Called when the user wants to add a new account.
        * Since we're going to need to know the name and password
        * for the new account, we return an Intent to open
        * the login screen.
        */

        @Override public Bundle addAccount(AccountAuthenticatorResponse response,
                        String accountType, String authTokenType, String[] requiredFeatures,
                        Bundle options) throws NetworkErrorException {

                Log.d(TAG, "addAccount");
                final Intent intent = new Intent(mContext, LoginActivity.class);

                intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, accountType);
                intent.putExtra(LoginActivity.ARG_AUTH_TYPE, authTokenType);
                intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

                final Bundle bundle = new Bundle();
                bundle.putParcelable(AccountManager.KEY_INTENT, intent);
                return bundle;
        }

        @Override public Bundle confirmCredentials(AccountAuthenticatorResponse response,
                        Account account, Bundle options) {

                Log.d(TAG, "confirmCredentials");
                // Maybe do something here one day,
                // but for now...
                return null;
        }

        @Override public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
                        String authTokenType, Bundle options) throws NetworkErrorException {

                Log.d(TAG, "getAuthToken");

                // If the caller requested an authToken type we don't support, then
                // return an error

                if (!authTokenType.equals(AUTH_TOKEN_TYPE)) {
                        final Bundle result = new Bundle();
                        result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
                        return result;
                }

                // Does the account manager have an authToken for us?

                final AccountManager am = AccountManager.get(mContext);
                String authToken = am.peekAuthToken(account, authTokenType);
                Log.d(TAG, "authToken is '"+authToken+"'");

                if (TextUtils.isEmpty(authToken)) {

                        // The server doesn't have an authToken for us.
                        // Looks like we'll have to try to log into DW again.

                        Log.d(TAG, "no token; attempting to log in again");
                        final String password = am.getPassword(account);

                        if (password != null) {
                                // FIXME authToken = something we request from DW
                                // FIXME note that this has to be blocking
                        }
                }

                // We probably have an authToken by now.
                // If we do, great! Let's return it.

                if (!TextUtils.isEmpty(authToken)) {
                        Log.d(TAG, "token found; returning it");
                        final Bundle result = new Bundle();
                        result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                        result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
                        result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
                        return result;
                }

                // We don't have an authToken, because
                //    - we didn't have one stored, and
                //    - we didn't have the user's credentials
                //		so that we could log in and get a new one.
                // We're not going to get much further without
                // the user's credentials, so let's put up the LoginActivity.

                Log.d(TAG, "re-prompt for login");

                final Intent intent = new Intent(mContext, LoginActivity.class);
                intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
                intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);
                intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, account.type);

                final Bundle bundle = new Bundle();
                bundle.putParcelable(AccountManager.KEY_INTENT, intent);

                return bundle;
        }

        @Override public String getAuthTokenLabel(String authTokenType) {
                Log.d(TAG, "getAuthTokenLabel");

                if (authTokenType == AUTH_TOKEN_TYPE) {
                        return "login";
                } else {
                        return null;
                }
        }

        @Override public Bundle hasFeatures(
                        AccountAuthenticatorResponse response, Account account, String[] features) {

                Log.d(TAG, "hasFeatures");

                // we don't have any particular features.

                final Bundle result = new Bundle();
                result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
                return result;
        }


        @Override public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
                        String authTokenType, Bundle loginOptions) {

                Log.d(TAG, "updateCredentials");
                return null;
        }

        @Override public Bundle editProperties(AccountAuthenticatorResponse response,
                        String accountType) {

                return null;
         }

}

