package product.prison.model;

public class OrderData {
    private int id;

    private Food dish;


    private int orderNum;

    private double totalPrice;

    private long orderTime;

    private int send_if;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setDish(Food dish) {
        this.dish = dish;
    }

    public Food getDish() {
        return this.dish;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getOrderNum() {
        return this.orderNum;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalPrice() {
        return this.totalPrice;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public long getOrderTime() {
        return this.orderTime;
    }

    public void setSend_if(int send_if) {
        this.send_if = send_if;
    }

    public int getSend_if() {
        return this.send_if;
    }

}