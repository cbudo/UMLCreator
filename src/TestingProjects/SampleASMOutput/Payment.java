package TestingProjects.SampleASMOutput;

/**
 * Created by efronbs on 1/13/2016.
 */
public class Payment {
    boolean authorized;
    @SuppressWarnings("unused")
    private int cashTendered;

    public Payment(int cashTendered) {
        this.cashTendered = cashTendered;
        this.authorized = false;
    }

    public void authorize() {
        if (cashTendered == 0)
            authorized = true;
    }

    public void Tenderize(int cashTendered) {
        this.cashTendered += cashTendered;
    }

    public boolean isAuthorized() {
        return authorized;
    }
}
