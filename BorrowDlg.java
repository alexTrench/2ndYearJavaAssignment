import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*; //Date, collection classes

public class BorrowDlg extends JDialog implements ActionListener {
    private MainMenu parent;
    private JTextField txtItemID,borrowerID;
    private JButton btnSubmit, btnHide;

    public BorrowDlg(MainMenu p){
        setTitle("Register the loaning of a book");
        parent = p;

        //Components -
        txtItemID = new JTextField(10);
        borrowerID = new JTextField(10);
        btnSubmit = new JButton("Submit");
        btnHide = new JButton("Hide");

        //Layout 
        JPanel pnl = new JPanel();   
        pnl.add(new JTable());
        pnl.add(new JLabel("Item Code"));
        pnl. add(txtItemID);
        pnl.add(new JLabel("Borrower Code"));
        pnl.add(borrowerID);
        add(pnl,BorderLayout.CENTER);

        pnl = new JPanel();
        pnl.add(btnSubmit);
        pnl.add(btnHide);
        add(pnl, BorderLayout.SOUTH);

        setBounds(300,300,600,300);

        //Action 
        btnSubmit.addActionListener(this);
        btnHide.addActionListener(this);

    
    }

    /*
     * Actions: on click of 'Submit', find loan record and delete from database
     * 
     */
    public void actionPerformed(ActionEvent evt){
        Object src = evt.getSource();
        if(src == btnHide){
            setVisible(false);
            txtItemID.setText("");
            borrowerID.setText("");
        }
        else if(src == btnSubmit){
            processLoan();
            txtItemID.setText("");
            borrowerID.setText("");

        }
    }

    /*
     * Finds the loan record
     * Also updates the borrowedBy
     */
    public void processLoan(){
       

        //Borrower ID and Item id have been input
        Integer itmID = new Integer(txtItemID.getText());
        Item itm = parent.getItems().get(itmID);
        Integer bwrID = new Integer(borrowerID.getText());
        Borrower brw = parent.getBorrowers().get(bwrID);

        long timeStamp = System.currentTimeMillis();
        if (itm == null){
            JOptionPane.showMessageDialog(this, "Item not found",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (brw == null){
            JOptionPane.showMessageDialog(this, "Borrower not found",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;

        } 

        //see if the loan is already being loaned
        if(itm.getBorrowedBy() != null){
            JOptionPane.showMessageDialog(this, "Item already on loan",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;

        }

        //if the item and borrower already have a transaction
        LoanTransaction loaned = null;
        for(LoanTransaction t: parent.getLoans()){
            if(t.getBorrower() == brw && t.getItem() == itm){
                loaned = t;
                break;

            }
        }
        if(loaned != null){
            JOptionPane.showMessageDialog(this, "Loan transaction already found",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;

        }
        
        //loan should not be allowed if the borrower has items overdue.
        for(LoanTransaction t: parent.getLoans()){
            if(t.getBorrower() == brw && (System.currentTimeMillis() - t.getTimeStamp() ) > MainMenu.LOANMAX){

                JOptionPane.showMessageDialog(this, "borrower has overdue loan",
                "Error", JOptionPane.ERROR_MESSAGE);
                return;

            }
        }
        LoanTransaction newLoan = new LoanTransaction(brw,itm,timeStamp);
        //loanTransation record should be recorded 
        System.out.println(newLoan);
        
        
        //loanTransaction record should be added to the collection of loans
        parent.getLoans().add(newLoan);
        //borrowed by attribute of item to be updated with the borrower
        itm.setBorrowedBy(brw);
        
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(parent.getLoans());
    }

}

