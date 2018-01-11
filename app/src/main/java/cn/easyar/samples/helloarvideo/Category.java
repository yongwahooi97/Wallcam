package cn.easyar.samples.helloarvideo;

/**
 * Created by OoiYongWah on 27/12/2017.
 */

public class Category {
    private String id;
    private String url;
    private String description;
    private String price;

    public Category() {
    }

    public Category(String id, String url, String description, String price) {
        this.id = id;
        this.url = url;
        this.description = description;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
