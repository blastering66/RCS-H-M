package id.tech.hsmsjacket;

/**
 * Created by RebelCreative-A1 on 07/01/2016.
 */
public class RowData_Sms {
    String sender, message, dateArrived, viewed;
    String _id;

    public RowData_Sms(String sender, String message, String dateArrived, String viewed) {
        this.sender = sender;
        this.message = message;
        this.dateArrived = dateArrived;
        this.viewed = viewed;
    }

    public RowData_Sms(String _id,String sender, String message, String dateArrived, String viewed) {
        this._id = _id;
        this.sender = sender;
        this.message = message;
        this.dateArrived = dateArrived;
        this.viewed = viewed;
    }
}
