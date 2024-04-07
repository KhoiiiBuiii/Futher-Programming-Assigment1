package system.entity;
/**
 * @author <Bui Minh Khoi - s3929015>
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Claim {
    private String id;
    private Date claimDate;
    private String insuredPerson;
    private String cardNumber;
    private Date examDate;
    private double claimAmount;
    private String status;
    private String receiverBankingInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    public String getInsuredPerson() {
        return insuredPerson;
    }

    public void setInsuredPerson(String insuredPerson) {
        this.insuredPerson = insuredPerson;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiverBankingInfo() {
        return receiverBankingInfo;
    }

    public void setReceiverBankingInfo(String receiverBankingInfo) {
        this.receiverBankingInfo = receiverBankingInfo;
    }

    public void setClaimDateFromString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.claimDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            System.out.println("Error parsing claim date: " + e.getMessage());
        }
    }

    public void setExamDateFromString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.examDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            System.out.println("Error parsing exam date: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String claimDateString = (claimDate != null) ? dateFormat.format(claimDate) : "null";
        String examDateString = (examDate != null) ? dateFormat.format(examDate) : "null";

        return "Claim{" +
                "id='" + id + '\'' +
                ", claimDate=" + claimDateString +
                ", insuredPerson='" + insuredPerson + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", examDate=" + examDateString +
                ", claimAmount=" + claimAmount +
                ", status='" + status + '\'' +
                ", receiverBankingInfo='" + receiverBankingInfo + '\'' +
                '}';
    }
}
