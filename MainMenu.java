import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*; //ArrayList; HashMap; LinkedList;


public class MainMenu extends JFrame implements ActionListener {
  public static final long LOANMAX = 1814400000; //ms = 21 days
  // data collections

  
  private Map<Integer, Borrower> borrowers;
  private Map<Integer, Item> items;
  private List<LoanTransaction> loans;
  
  //GUI
  private ReturnDlg returnDlg;
  private BorrowDlg borrowDlg;
  private JButton btnReadData, btnSaveLoans, btnLendItems, btnReturnItems,
                  btnListLoans, btnListODLoans;

  public static void main(String[] args) {
    MainMenu app = new MainMenu();
    app.setVisible(true);
  }


  public MainMenu() { //constructor
    // Database
    borrowers = new HashMap<Integer, Borrower>();
    items = new HashMap<Integer, Item>();
    loans = new LinkedList<LoanTransaction>();

    // GUI - create custom dialog instances
    returnDlg = new ReturnDlg(this);
    borrowDlg = new BorrowDlg(this);
    

    // GUI - set window properties
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(200, 200, 250, 300);

    //GUI - main menu buttons    
    JPanel mainPnl = new JPanel();
    mainPnl.setLayout(new GridLayout(3,1));

    btnReadData = new JButton("Read Data");
    btnReadData.addActionListener(this);
    mainPnl.add(btnReadData);
    
    btnLendItems = new JButton("Lend Items");
    btnLendItems.addActionListener(this);
    mainPnl.add(btnLendItems);
    
    btnReturnItems = new JButton("Return Items");
    btnReturnItems.addActionListener(this);
    mainPnl.add(btnReturnItems);

    //To do: layout btnListLoans, btnListODLoans, btnSaveLoans 
    btnListLoans = new JButton("List Loans");
    btnListLoans.addActionListener(this);
    mainPnl.add(btnListLoans);
    
    btnListODLoans = new JButton("List Overdue Loans");
    btnListODLoans.addActionListener(this);
    mainPnl.add(btnListODLoans);
    
    btnSaveLoans = new JButton("Save Loans");
    btnSaveLoans.addActionListener(this);
    mainPnl.add(btnSaveLoans);
  
    add(mainPnl, BorderLayout.CENTER);
  } //end constructor

  //Accessors for data structures
  public Map<Integer, Borrower>  getBorrowers() 
  {
      return borrowers; 
  }
  
  public Map<Integer, Item>      getItems()     
  {
      return items;
  }
  
  public List<LoanTransaction> getLoans()       
  {
      return loans;
  }

  /**
   * Actions in response to buttons
   */
  public void actionPerformed(ActionEvent evt) {
    Object src = evt.getSource();
    //read borrowers, items, loans JUST ONCE to initialise the system
    if (src == btnReadData) { 
      readBorrowerData();
      listBorrowers();
      readItemData();
      readLoans(); // saved from a previous session 
      listItems(); // AFTER loans reloaded
      btnReadData.setEnabled(false);      
    } else if (src == btnReturnItems) { // returnDlg will do multiple returns
      returnDlg.setVisible(true);
    } else if(src == btnLendItems){
      borrowDlg.setVisible(true);
    }else if(src == btnListLoans){
      listLoans();
    }else if(src == btnListODLoans){
      ListODLoans();
    }else if(src == btnSaveLoans){
      saveLoans();
    }
    

    
    //To do: add functionality for listing loans, listing overdue loans, saving loans
  }
  
  /**
   * Read data from borrowers.txt using a Scanner; unpack and populate
   *   borrowers Map. List displyed on console.  
   */
  public void readBorrowerData() {
    String fnm="", snm="", pcd="";
    int num=0, id=1;
    try {
      Scanner scnr = new Scanner(new File("borrowers.txt"));
      scnr.useDelimiter("\\s*#\\s*");
      while (scnr.hasNextInt()) {
        id  = scnr.nextInt();
        snm = scnr.next();
        fnm = scnr.next();
        num = scnr.nextInt();
        pcd = scnr.next();
        borrowers.put(new Integer(id), new Borrower(id, snm, fnm, num, pcd));
      }
      scnr.close();
    } catch (NoSuchElementException e) {
      System.out.printf("%d %s %s %d %s\n", id, snm, fnm, num, pcd);
      JOptionPane.showMessageDialog(this, e.getMessage(),
          "fetch of next token failed ", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this, "Borrowers file not found",
          "Unable to open data file", JOptionPane.ERROR_MESSAGE);
    }
  } //end readBorrowerData
  
  // List Borrowers on console
  public void listBorrowers() {
    System.out.println("Borrowers:");
    for (Borrower b: borrowers.values()) {
      System.out.println(b);
    }
    System.out.println();
  }

  /**
   * Read data from items.txt using a Scanner; unpack and populate
   *   items Map. List displyed on console.  
   */
  public void readItemData() {
    String ttl="", aut="";
    int id=1;
    try {
      Scanner scnr = new Scanner(new File("items.txt"));
      scnr.useDelimiter("\\s*#\\s*");
      while (scnr.hasNextInt()) {
        id  = scnr.nextInt();
        ttl = scnr.next();
        aut = scnr.next();
        items.put(new Integer(id), new Item(id, ttl, aut));
      }
      scnr.close();
    } catch (NoSuchElementException e) {
      System.out.printf("%d %s %s\n", id, ttl, aut);
      JOptionPane.showMessageDialog(this, e.getMessage(),
          "fetch of next token failed ", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this, "Items file not found",
          "Unable to open data file", JOptionPane.ERROR_MESSAGE);
    }
  } //end readItemData

  // List Items on console
  public void listItems() {
    System.out.println("Items:");
    for (Item i: items.values()) {
      System.out.println(i);
    }
    System.out.println();
  }

  //Assumes borrowers, items have been loaded
  public void readLoans() {
    if (loans.size() > 0) {
      JOptionPane.showMessageDialog(this, "Already some loans!",
          "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    try {
      Scanner scnr = new Scanner(new File("loans.txt"));
      while (scnr.hasNextInt()) {
        Borrower b = borrowers.get(scnr.nextInt());
        Item i     = items.get(scnr.nextInt());
        LoanTransaction t = new LoanTransaction(b, i, scnr.nextLong());
        loans.add(t);
        i.setBorrowedBy(b);
      }
      System.out.printf("%d loan records added\n", loans.size());
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this, "Loans file not found",
          "Unable to open data file", JOptionPane.ERROR_MESSAGE);
    }
  } //end readloans

  //To do: functions to list loans, list overdue loans, save loan records
  public void ListODLoans(){
    
    try{
      Scanner scnr = new Scanner(new File("loans.txt"));
      while(scnr.hasNextInt()){
        Borrower b = borrowers.get(scnr.nextInt());
        Item i = items.get(scnr.nextInt());
        long time = scnr.nextLong();
        
        LoanTransaction t = new LoanTransaction(b, i, time);
        
        if((System.currentTimeMillis() - time  ) > LOANMAX){
         System.out.println(t); 
        
        }
        
      }   
    }
    catch(IOException e){
      JOptionPane.showMessageDialog(this, "Loans file not found",
      "Unable to open data file", JOptionPane.ERROR_MESSAGE);
    
    } 
  }
       
  
  
  public void listLoans(){
      System.out.println("All loans");
      System.out.println(Arrays.toString(loans.toArray()));
    }
  
    
  public void saveLoans(){  
      try {
      File transaction = new File("loans.txt");
      PrintWriter transactionWriter = new PrintWriter(transaction);
      
      for (LoanTransaction l : loans){
          int borrowerID = l.getBorrower().getBwrID();
          int itemID = l.getItem().getItemID();
          long date = l.getTimeStamp();
          transactionWriter.println(String.format("%d %d %d" , borrowerID, itemID, date));
      }
      transactionWriter.close();
      System.out.println("Loans saved");
    } catch (FileNotFoundException e){
          System.out.println("No file for an account or no file found for pending transaction");
    }
  
  }  
  }

