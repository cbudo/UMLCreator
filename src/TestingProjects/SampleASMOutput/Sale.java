package TestingProjects.SampleASMOutput;

/**
 * Created by efronbs on 1/13/2016.
 */
public class Sale {
    private Payment p;

    public void makePayment(int cashTendered) {
        p = new Payment(cashTendered);
        p.authorize();
    }

    public boolean paymentAuthorized() {
        return p.isAuthorized();
    }
}
