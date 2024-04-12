package com.example.airquality;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.webkit.ValueCallback;

import java.util.concurrent.atomic.AtomicReference;

public class MyWebViewClient extends WebViewClient {
    private String redirectUri;
    private String username;
    private String email;
    private String password;
    private String Re_password;
    private boolean signUpSuccess = false;
    private  CookieManager cookieManager = CookieManager.getInstance();



    public MyWebViewClient(String redirectUri, String username, String email, String password, String Re_password) {
        this.redirectUri = redirectUri;
        this.username = username;
        this.email = email;
        this.password = password;
        this.Re_password = Re_password;
    }

        @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

            acceptCookies();
    }

    //@Override
   /* public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();

        // Kiểm tra xem URL chuyển hướng có khớp với redirectUri hay không
        if (url.startsWith(redirectUri)) {
            // Chặn việc tải URL chuyển hướng
            return true;
        }

        return super.shouldOverrideUrlLoading(view, request);
    }*/
    @Override
    public void onPageFinished(WebView view, String url) {
        if (url.contains("openid-connect/registrations")) {
           // Log.d(Utils.LOG_TAG, "onPageFinished: Fill form");

            // JavaScript code to fill the form
            String usrScript = "document.getElementById('username').value = '" + username + "';";
            String emailScript = "document.getElementById('email').value = '" + email+ "';";
            String pwdScript = "document.getElementById('password').value = '" + password + "';";
            String rePwdScript = "document.getElementById('password-confirm').value = '" + Re_password + "';";

            // Execute JavaScript code to fill the form
            view.evaluateJavascript(usrScript, null);
            view.evaluateJavascript(emailScript, null);
            view.evaluateJavascript(pwdScript, null);
            view.evaluateJavascript(rePwdScript, null);

            // JavaScript code to submit the form
            String submitScript = "document.querySelector('button[name=register]').click();";
            view.evaluateJavascript(submitScript, null);

        }
        // Kiểm tra xem quá trình đăng ký đã thành công hay không
        if (url.startsWith(redirectUri)) {
            Toast.makeText(view.getContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
            signUpSuccess = true;
            clearCookies();
            view.destroy();
            Intent loginIntent = new Intent(view.getContext(), LoginActivity.class);
            view.getContext().startActivity(loginIntent);
        }
        else{
            view.evaluateJavascript(
                    "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                    new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String html) {
                            System.out.println(html);
                            String check = html;
                            if (check.contains("Email already exists."))
                                Toast.makeText(view.getContext(), "Email already exists.", Toast.LENGTH_SHORT).show();
                            else if (check.contains("Username already exists.")) {
                                Toast.makeText(view.getContext(), "Username already exists.", Toast.LENGTH_SHORT).show();
                            }
                            else if (check.contains("Invalid email address."))
                                Toast.makeText(view.getContext(), "Invalid email address.", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

        }
    }
   /* private void showToast(WebView view,String message) {
        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
        }*/
    private void clearCookies() {
        cookieManager.removeAllCookies(null);
    }
    private void acceptCookies() {
        cookieManager.setAcceptCookie(true);
    }

}