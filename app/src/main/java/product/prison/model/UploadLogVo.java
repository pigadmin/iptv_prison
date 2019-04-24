package product.prison.model;

import product.prison.app.MyApp;

public class UploadLogVo {
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getOperatetion() {
        return operatetion;
    }

    public void setOperatetion(String operatetion) {
        this.operatetion = operatetion;
    }

    private String mac = MyApp.mac;  //设备mac，不可为空
    private String operatetion = "";  //操作事件，不可为空
}
