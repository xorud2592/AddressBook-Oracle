package com.ltk.phone.addressbook.dao;

public class AddressVo {
	private Long id;
	private String name;
	private String tel;
	private String hp;

	public AddressVo() {
	}

	public AddressVo(String name, String tel, String hp) {
		this.name = name.trim();
		this.tel = tel.trim();
		this.hp = hp.trim();
	}

	public AddressVo(Long id, String name, String tel, String hp) {
		this(name, tel, hp);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getHp() {
		return hp;
	}

	public void setHp(String hp) {
		this.hp = hp;
	}

	@Override
	public String toString() {
		return String.format("%d. %-6s %-15s %-15s", id, name, filter(tel), filter(hp));
	}

	private String filter(String number) {
		String[] numbers = number.split("-");
		return numbers[0] + "-" + (numbers[1].substring(0, numbers[1].length() - 2) + "**") + "-"
				+ (numbers[2].substring(0, numbers[2].length() - 2) + "**");
	}

}
