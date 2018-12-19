package cn.kt.mall.shop.shop.vo;

import java.util.List;

public class CommentVO {
	private List<String> img;

	public List<String> getImg() {
		return img;
	}

	public void setImg(List<String> img) {
		this.img = img;
	}

	public CommentVO(List<String> img) {
		super();
		this.img = img;
	}

}
