package TestingProjects.SampleASMOutput;

/**
 * Created by efronbs on 1/13/2016.
 */
public class Register {
    private Sale sale;

    public void checkout(int cashTendered) {
        // @TA:
        // 1. It is ok if students created a different method instead of checkout(int)
        // 2. It is also ok if the method did not have a parameter but student must pass
        // an int argument when calling makePayment
        sale.makePayment(cashTendered);
    }
}
