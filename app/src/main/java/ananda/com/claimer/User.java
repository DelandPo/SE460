package ananda.com.claimer;

/**
 * Created by anan_ on 4/17/2018.
 */

public class User {
    private String userID, userName, password, firstName, lastName, street, state, email;
    private int zipCode;

    User(String uid, String uName, String pw, String fName, String lName, String str, String st, String e, int zip){
        userID = uid;
        userName = uName;
        password = pw;
        firstName = fName;
        lastName = lName;
        street = str;
        state = st;
        email = e;
        zipCode = zip;
    }

    /** End Constructor **/

    /** Setters **/
    public void setUserID(String uid){
        userID = uid;
    }

    public void setUserName(String un){
        userName = un;
    }

    public void setPassword(String p){
        password = p;
    }

    public void setFirstName(String fn){
        firstName = fn;
    }

    public void setLastName(String ln){
        lastName = ln;
    }

    public void setStreet(String str){
        street = str;
    }

    public void setState(String st){
        state = st;
    }

    public void setEmail(String e){
        email = e;
    }

    public void setZipCode(int z){
        zipCode = z;
    }

    /** End Setters **/

    /** Getters **/

    public String getUserID(){
        return userID;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword(){
        return password;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getStreet(){
        return street;
    }

    public String getState(){
        return state;
    }

    public String getEmail(){
        return email;
    }

    public int getZip(){
        return zipCode;
    }

}