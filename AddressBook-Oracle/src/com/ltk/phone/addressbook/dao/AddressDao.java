package com.ltk.phone.addressbook.dao;

import java.util.List;

public interface AddressDao {
	public List<AddressVo> getList();
	public boolean insert(AddressVo vo);
	public boolean delete(Long id);
	public List<AddressVo> search(String keyword);
}
