package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EM;

/**
 * Created by davidashirov on 12/11/17.
 */
public enum  InquiryAndChargebackReason {

    // Policy Violations
    C4507 (4507, "Agreement Violation","Incorrect Transaction Amount or Account Number Presented"),
    C4513 (4513, "Agreement Violation","Credit Not Presented"),
    C4512 (4512, "Agreement Violation","Multiple Processing"),
    C4515 (4515, "Agreement Violation","Paid through Other Means"),
    C4516 (4516, "Agreement Violation","Request for Support Not Fulfilled"),
    C4517 (4517, "Agreement Violation","Request for Support Illegible/Incomplete"),
    C4521 (4521, "Agreement Violation","Invalid Authorization"),
    C4523 (4523, "Agreement Violation","Unassigned C/M Account Number"),
    C4526 (4526, "Agreement Violation","Missing Signature"),
    C4527 (4527, "Agreement Violation","Missing Imprint"),
    C4530 (4530, "Agreement Violation","Currency Discrepancy"),
    C4534 (4534, "Agreement Violation","Multiple ROCs"),
    C4536 (4536, "Agreement Violation","Late Presentment"),
    C4540 (4540, "Agreement Violation","Card Not Present"),
    C4544 (4544, "Agreement Violation","Cancellation of Recurring Goods/Services services"),
    C4553 (4553, "Agreement Violation","Not as Described or defective merchandise"),
    C4554 (4554, "Agreement Violation","Goods and Services Not Received"),
    C4750 (4750, "Agreement Violation","Car Rental Charge in Dispute"),
    C4752 (4752, "Agreement Violation","Credit/Debit Presentment Error"),
    C4754 (4754, "Agreement Violation","Local Regulatory/Legal Dispute"),
    C4755 (4755, "Agreement Violation","No Valid Authorization"),
    C4758 (4758, "Agreement Violation","Expired/Not Yet valid card"),
    C4763 (4763, "Agreement Violation","Fraud Full Recourse"),
    C4798 (4798, "Agreement Violation","Fraud Liability Shift – Counterfeit"),
    C4799 (4799, "Agreement Violation","Fraud Liability Shift – Lost / Stolen"),
    C6003 (6003, "Agreement Violation","Chargeback Documentation"),
    C6006 (6006, "Agreement Violation","Legal Request or Fraud Analysis"),
    C6008 (6008, "Agreement Violation","C/M Requests Copy Bearing Signature (signed support) 6013 Repeat Documentation Request"),
    C6014 (6014, "Agreement Violation","C/M Does not recognize Transaction or Transaction Amount 6016 C/M needs for personal records"),
    C6304 (6304, "Agreement Violation","ATM Journal Roll/Audit Tape Request"),
        // Good faith
    C6306 (6306, "Good Faith","Outside Dispute Timeframes"),
    C6307 (6307, "Good Faith","Pre-Compliance Request"),
    C6308 (6308, "Good Faith","Processing Error"),
    C6309 (6309, "Good Faith","Resubmit GF Request"),
    C6310 (6310, "Good Faith","PSD Dispute – EU/EEA Only"),
    C6311 (6311, "Good Faith","Other (Requires Comments)"),
    C6320 (6320, "Good Faith","Duplicate Credit Processed"),
    C6321 (6321, "Good Faith","Duplicate Debit Processed"),
    C6322 (6322, "Good Faith","Promotional Program"),
    C6324 (6324, "Good Faith","Bonus Plan");

    private final int code;
    private final String type;
    private final String reason;
    private String reason(){
        return reason;
    }

    InquiryAndChargebackReason(int code, String type, String reason) {
        this.code = code;
        this.type = type;
        this.reason = reason;
    }
}
