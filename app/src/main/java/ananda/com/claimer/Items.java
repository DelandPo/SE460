package ananda.com.claimer;

/**
 * Created by anan_ on 4/16/2018.
 */

public class Items {
    private String itemID, userID, modelNumber, serialNumber, itemName, roomType, pictureURL,itemCost;

    /** Constructor **/
    Items(String iid, String mNum, String sNum, String iName, String rType, String pURL) {
        itemID = iid;
        modelNumber = mNum;
        serialNumber = sNum;
        itemName = iName;
        roomType = rType;
        pictureURL = pURL;
    }

    Items(){

    }

    Items(String iName, String iCost,String iid,String mNum, String sNum, String rType, String pUri){

        this.itemName = iName;
        this.itemCost = iCost;
        this.itemID = iid;
        this.modelNumber = mNum;
        this.serialNumber = sNum;
        this.roomType = rType;
        this.pictureURL = pUri;
    }
    /** End Constructor **/

    /** Setters **/
    public void setItemID(String iid){
        itemID = iid;
    }
    public void setItemCost(String iic){
        itemCost = iic;
    }

    public String getItemDetails(){

        return itemName + " $" + itemCost ;
    }


    public void setUserID(String uid){
        userID = uid;
    }

    public void setModelNum(String mNum){
        modelNumber = mNum;
    }

    public void setSerialNumber(String sNum){
        serialNumber = sNum;
    }

    public void setItemName(String iName){
        itemName = iName;
    }

    public void setRoomType(String room){
        roomType = room;
    }

    public void setPictureURL(String pURL){
        pictureURL = pURL;
    }
    /** End Setters **/

    /** Getters **/
    public String getItemID(){
        return itemID;
    }

    public String getUserID(){
        return userID;
    }

    public String getModelNumber(){
        return modelNumber;
    }

    public String getSerialNumber(){
        return serialNumber;
    }

    public String getItemName(){
        return itemName;
    }

    public String getRoomType(){
        return roomType;
    }

    public String getPictureURL(){
        return pictureURL;
    }

    public String getItemCost(){return itemCost;}
    /** End Getters **/


}