package product.prison.view.msg;

import java.util.Comparator;
import java.util.Date;

import product.prison.model.Nt;

public class TimeSort implements Comparator {

    public int compare(Object arg0, Object arg1) {
        Nt user0 = (Nt) arg0;
        Nt user1 = (Nt) arg1;
        int flag = new Date(user1.getCtiem()).compareTo(new Date(user0.getCtiem()));
        return flag;
    }
}
