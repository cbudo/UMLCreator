package GenerateTemp;

/**
 * Created by budocf on 1/20/2016.
 */
public class Register {
    private static Register r;
    private Sale sale;

    private Register() {

    }

    public static Register getInstance() {
        if (r == null) {
            r = new Register();
        }
        return r;
    }
    public void checkout(int cashTendered) {
        sale.makePayment(cashTendered);
    }
}
