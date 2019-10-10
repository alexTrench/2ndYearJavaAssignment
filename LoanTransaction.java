import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * LoanTransaction
 * class to record borrowing of items
 */
public class LoanTransaction {
  private Borrower bdBy;
  private Item item;
  private long timeStamp;
  
  public LoanTransaction(Borrower b, Item i, long ts) {
    bdBy = b;
    item = i;
    timeStamp = ts;
  }
  
  public Borrower getBorrower() 
  {
      return bdBy; 
  }
  
  public Item getItem()         
  {
      return item; 
  }
  
  public long getTimeStamp()   
  {
      return timeStamp; 
  }

  public String toString()
  {
      String date = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss").format(new Date(timeStamp));
      
      return String.format("Item [%s] borrowed by [%s]; Loan Issued %s",
                          item, bdBy, date);
  }

  //In most contexts, Item has correct Borrower reference
  public String toStringOmitBwr() 
  {
    String date = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss").format(new Date(timeStamp)); 
      
    return String.format("Item [%s] Date Taken %s", item, date);
  }
}
