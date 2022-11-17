package shpp.db.dto;

public class Balance {
    private Market market;
    private Goods goods;


    public Market getMarket() {
        return market;
    }

    public Balance setMarket(Market market) {
        this.market = market;
        return this;
    }

    public Goods getGoods() {
        return goods;
    }

    public Balance setGoods(Goods goods) {
        this.goods = goods;
        return this;
    }
}
