package id.tech.hsmsjacket;

/**
 * Created by RebelCreative-A1 on 07/01/2016.
 */
public class RowData_History {
    String tv_transaction_id, tv_mason_id, tv_qty, tv_status, dateArrived, retailerId;

    public RowData_History(String tv_transaction_id, String tv_mason_id, String tv_qty,
                           String tv_status, String dateArrived, String retailerId) {
        this.tv_transaction_id = tv_transaction_id;
        this.tv_mason_id = tv_mason_id;
        this.tv_qty = tv_qty;
        this.tv_status = tv_status;
        this.dateArrived = dateArrived;
        this.retailerId = retailerId;
    }
}
