package com.parttime.enterprise.helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;


public class AppValidator {

    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    //    public static final String NAME_REGEX = "^[_A-Za-z0-9-\\+]";
    private static final String NAME_REGEX = "^[A-Za-z0-9\\s]+[.]?[A-Za-z0-9\\s]*$";
    private static final String CHAR_REGEX = ".*[a-zA-Z]+.*";
    private static final String ONLY_CHAR_REGEX = "^[a-zA-Z ]*$";
    // --Commented out by Inspection (9/17/2018 5:36 PM):public static final String MOBILE_REGEX = "\\d{10}";
    private static final String MOBILE_REGEX_TEST = "\\d{6}|.{10}|.{14}";
    private static final String YEAR_REGEX = "\\d{4}";
    private static final String PINCODE_REGEX = "^([1-9])([0-9]){5}$";
    private static final String EXPIRY_DATE_REGEX = "(?:0[1-9]|1[0-2])/[0-9]{2}";
    private static final String VEHICLE_REGEX = "^[A-Z]{2} [0-9]{2} [A-Z]{2} [0-9]{4}$";
    // --Commented out by Inspection (9/17/2018 5:36 PM):public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";
    private static final String[] IMAGE_EXTENSIONS = new String[]{"jpg", "jpeg", "png"};

    public static boolean isValidEmail(String email) {
        return email.trim().matches(EMAIL_REGEX);
    }

// --Commented out by Inspection START (9/17/2018 5:36 PM):
//    public static boolean isValidEmailMobile(EditText editText, String msg) {
//        if (editText.getText().toString().trim().equals("")) {
//
//            editText.setError(msg);
//            editText.addTextChangedListener(new RemoveErrorEditText(editText));
//            editText.requestFocus();
//            return false;
//        } else if (editText.getText().toString().matches(EMAIL_REGEX)) {
//            return true;
//        } else if (editText.getText().toString().matches(MOBILE_REGEX_TEST))
//            return true;
//        else {
//            editText.setError("Invalid Email ID/Mobile Number");
//            editText.addTextChangedListener(new RemoveErrorEditText(editText));
//            editText.requestFocus();
//            return false;
//        }
//    }
// --Commented out by Inspection STOP (9/17/2018 5:36 PM)

    public static boolean validateCardExpiryDate(String expiryDate) {
        return expiryDate.matches(EXPIRY_DATE_REGEX);
    }

    public static boolean isValidPassword(String password) {
        return password.trim().length() >= 6;
    }


    public static boolean isValidPhone(String phone) {

        return phone.length() >= 6;

    }

    public static boolean isValid(String editText) {
        return !editText.equals("");
    }

// --Commented out by Inspection START (9/17/2018 5:36 PM):
//    public static boolean isSame(EditText editText, EditText editText2, String msg) {
//        if (!editText.getText().toString().trim().equals(editText2.getText().toString().trim())) {
//
//            editText.setError(msg);
//            editText.addTextChangedListener(new RemoveErrorEditText(editText));
//            editText.requestFocus();
//            editText2.setError(msg);
//            editText2.addTextChangedListener(new RemoveErrorEditText(editText));
//            return false;
//        } else
//            return true;
//    }
// --Commented out by Inspection STOP (9/17/2018 5:36 PM)


    /*public static boolean isValidPassword(EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {

            editText.setError(msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().matches(PASSWORD_REGEX))
            return true;
        else {
            editText.setError("invalid password");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
    }*/

    public static boolean isValidMobile(String mobile) {


        return !mobile.trim().equals("") && mobile.length() == 10;

    }


    public static boolean isValidName(String msg) {
        return msg.matches(CHAR_REGEX);
    }

// --Commented out by Inspection START (9/17/2018 5:36 PM):
//    public static boolean isValidAddress(EditText editText, String msg) {
//        if (editText.getText().toString().trim().equals("")) {
//
//            editText.setError(msg);
//            editText.addTextChangedListener(new RemoveErrorEditText(editText));
//            editText.requestFocus();
//            return false;
//        } else if (editText.getText().toString().matches(NAME_REGEX))
//            return true;
//        else {
//            editText.setError("invalid address");
//            editText.addTextChangedListener(new RemoveErrorEditText(editText));
//            editText.requestFocus();
//            return false;
//        }
//    }
// --Commented out by Inspection STOP (9/17/2018 5:36 PM)

// --Commented out by Inspection START (9/17/2018 5:36 PM):
//    public static boolean isValidYear(String data) {
//        return data.matches(YEAR_REGEX);
//    }
// --Commented out by Inspection STOP (9/17/2018 5:36 PM)


// --Commented out by Inspection START (9/17/2018 5:36 PM):
//    public static boolean isValid(Context context, EditText editText, String msg) {
//        if (editText.getText().toString().trim().equals("")) {
//
//            editText.setError(msg);
//            AlertUtil.showToastShort(context, msg);
//            editText.addTextChangedListener(new RemoveErrorEditText(editText));
//            editText.requestFocus();
//            return false;
//        }
//        return true;
//    }
// --Commented out by Inspection STOP (9/17/2018 5:36 PM)


// --Commented out by Inspection START (9/17/2018 5:36 PM):
//    public static boolean isValidDob(int year, int month, int day) {
//        Calendar c = Calendar.getInstance();
//        int curYY = c.get(Calendar.YEAR);
//        int curMM = c.get(Calendar.MONTH);
//        int curDD = c.get(Calendar.DAY_OF_MONTH);
//        if (year <= curYY) {
//            if (year == curYY) {
//                return month <= curMM && day <= curDD;
//            } else {
//                return true;
//            }
//        } else
//            return false;
//
//    }
// --Commented out by Inspection STOP (9/17/2018 5:36 PM)


    static class RemoveErrorEditText implements TextWatcher {

        private EditText editText;


// --Commented out by Inspection START (9/17/2018 5:36 PM):
//        RemoveErrorEditText(EditText edittext) {
//            this.editText = edittext;
//
//        }
// --Commented out by Inspection STOP (9/17/2018 5:36 PM)

        @Override
        public void afterTextChanged(Editable s) {

            editText.setError(null);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            editText.setError(null);
        }

    }


    static class RemoveErrorTextView implements TextWatcher {

        private TextView editText;


// --Commented out by Inspection START (9/17/2018 5:36 PM):
//        RemoveErrorTextView(TextView edittext) {
//            this.editText = edittext;
//
//        }
// --Commented out by Inspection STOP (9/17/2018 5:36 PM)

        @Override
        public void afterTextChanged(Editable s) {

            editText.setError(null);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    }


}
