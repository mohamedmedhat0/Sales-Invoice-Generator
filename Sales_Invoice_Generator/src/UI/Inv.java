package UI;

public class Inv {

    int invoiceId;
    String date;
    String customerName;
    double total;

    public  Inv(){}
    public Inv (int invoiceId,String date, String customerName,double total){
        this.invoiceId = invoiceId;
        this.date = date;
        this.customerName = customerName;
        this.total = total;
    }
}
