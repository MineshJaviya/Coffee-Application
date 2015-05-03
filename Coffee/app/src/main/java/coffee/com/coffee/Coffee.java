package coffee.com.coffee;

public class Coffee{
	private String name;
    private String id;
    private String desc;
    private String image_url;

	public Coffee(){
        name = null;
        id = null;
        desc = null;
        image_url = null;
        
    }
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
    
}
