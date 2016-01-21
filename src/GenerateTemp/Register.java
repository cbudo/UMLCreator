package GenerateTemp;

/**
 * Created by budocf on 1/20/2016.
 */
public class Register {
    private Sale sale;

    public void checkout(int cashTendered) {
        sale.makePayment(cashTendered);
    }
}
