package com.example.womensempowerment;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Regions;

public class Cognito {
    private String poolID = "";
    // TODO Add this later
    private String clientID = "";
    // TODO Add this later
    private String clientSecret = "";
    // TODO Add this later
    private Regions awsRegion = Regions.US_EAST_1;
    private CognitoUserPool userPool;
    private CognitoUserAttributes userAttributes;
    private String username = "";
    private String password = "";
    private String mFACode = "";
    private Context myContext;


    public Cognito(Context myContext) {
        this.myContext = myContext;
        userPool = new CognitoUserPool(myContext, this.poolID, this.clientID, this.clientSecret, this.awsRegion);
        userAttributes = new CognitoUserAttributes();
    }

    public String getmFACode() {
        return mFACode;
    }

    public void signInUser (String username, String password) {
        CognitoUser user = userPool.getUser(username);
        this.password = password;
        user.getSessionInBackground(loginhandler);
    }
    AuthenticationHandler loginhandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {

        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
            AuthenticationDetails authdetails = new AuthenticationDetails(userId, password, null);
            authenticationContinuation.continueTask();
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
            continuation.setMfaCode(mFACode);
            continuation.continueTask();

        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {


        }

        @Override
        public void onFailure(Exception exception) {

        }
    };
    public void confirmUser(String id, String code) {
        CognitoUser user = userPool.getUser(id);
        //again, always use the in background
        user.confirmSignUpInBackground(code, false, confirmationcallback); // this is a generic call back
    }
    GenericHandler confirmationcallback = new GenericHandler() {
        @Override
        public void onSuccess() {
            Toast.makeText(myContext, "Confirmation passed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Exception exception) {
            Toast.makeText(myContext, "Confirmation Failed", Toast.LENGTH_LONG).show();

        }
    };

    // this is the API (public function) used to sign up users
    public void SignUpUser(String email, String username) {
        addAttribute("email", email);
        // always use signUpinBackground
        userPool.signUpInBackground(username, password, userAttributes, null, signUpHandler);

    }
    SignUpHandler signUpHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            if (!signUpConfirmationState){
                Toast.makeText(myContext, "Confirmation code not provided", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(myContext, "Confirmation is complete", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onFailure(Exception exception) {
            Log.d("Error", exception.toString());

        }
    };
    public void addAttribute(String key, String value){
        //could have name, address, telephone number, email, etc
        userAttributes.addAttribute(key, value);
    }
}
