package io.github.gengkev.iondatapicker.model;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public int id;
    public String userType;
    public String username;
    public String fullName;
    public String firstName;
    public String lastName;
    public int gradeNum;

    public User(JSONObject obj) throws JSONException {
        id = obj.getInt("id");
        userType = obj.optString("user_type", "");
        username = obj.optString("username", "");
        fullName = obj.optString("full_name", "");
        firstName = obj.optString("first_name", "");
        lastName = obj.optString("last_name", "");

        JSONObject gradeObj = obj.optJSONObject("grade");
        gradeNum = (gradeObj != null) ? gradeObj.optInt("number", 0) : 0;
    }
}
