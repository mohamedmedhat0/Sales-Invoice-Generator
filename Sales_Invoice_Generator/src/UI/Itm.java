package UI;

public class Itm {
    int invoiceId;
    int itemId;
    String itemName;
    double itemPrice;
    double itemCount;
    double total;

    public Itm(){}
    public Itm(int invoiceId,int itemId, String itemName, double itemPrice,double itemCount,double total){
        this.invoiceId = invoiceId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemCount = itemCount;
        this.total = total;
    }

    public int getInvoiceId(){
        return invoiceId;
    }

}
