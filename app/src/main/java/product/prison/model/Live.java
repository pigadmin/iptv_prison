package product.prison.model;


import java.io.Serializable;
import java.util.List;

public class Live implements Serializable {


    private List<Livesingles> livesingles ;

    private Ad liveAds;

    public void setLivesingles(List<Livesingles> livesingles){
        this.livesingles = livesingles;
    }
    public List<Livesingles> getLivesingles(){
        return this.livesingles;
    }
    public void setLiveAds(Ad liveAds){
        this.liveAds = liveAds;
    }
    public Ad getLiveAds(){
        return this.liveAds;
    }


}
