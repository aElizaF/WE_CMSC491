package com.example.womensempowerment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class Cognito extends AppCompatActivity {
    //AWS Cogntio Information User Pool
    private String poolID = "us-east-1_YA9Juv3Qg";
    private String clientID = "3tfs2ho7rojblv0os4lc8q6hdt";
    private String clientSecret = "fg1cbfnrfnmivg2ab8c08dgn1dcl2spaioaskjcq1ae02ksjsjf";
    private Regions awsRegion = Regions.US_EAST_1;
    // USer Attributes
    private CognitoUserPool userPool;
    private CognitoUserAttributes userAttributes;
    private Context appContext;
    private String username = "";
    private String password = "";
    private String mFACode = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Cognito(Context myContext) {
        this.appContext = myContext;
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
        user.confirmSignUpInBackground(code, false, confirmationcallback);
    }
    GenericHandler confirmationcallback = new GenericHandler() {
        @Override
        public void onSuccess() {
            Toast.makeText(appContext, "Confirmation passed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Exception exception) {
            Toast.makeText(appContext, "Confirmation Failed", Toast.LENGTH_LONG).show();

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
                Toast.makeText(appContext, "Confirmation code not provided", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(appContext, "Confirmation is complete", Toast.LENGTH_LONG).show();
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
