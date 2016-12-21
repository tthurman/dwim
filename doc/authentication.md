How authentication works:

1) The HTML scraper can tell whether we're logged in.
If it sees that we're not logged in, and we haven't
set a flag to tell it not to, it will attempt to reauthenticate.

2) (Re)authentication: We attempt to log in using the
username and password stored in the config. If there's
no username or password in the config, or if login with them
fails, we go to the password dialogue.

3) The authenticator activity: asks for username and password.
Username might be filled in already, if we have existing
data but the password didn't work. When you hit OK,
attempt to authenticate with the new username and password.

You can also go straight to the authenticator activity
via the Accounts pane in the device's Settings app.
