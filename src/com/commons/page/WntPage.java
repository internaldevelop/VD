package com.commons.page;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;

/**
 * 分页处理Page
 * 
 * @author 赵俊鹏
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */
public class WntPage<T> {

	private int total_page; // 总页数
	private int total_count; // 总记录数
	private int current_page; // 当前页
	private int page_size = 15; // 每页的数量
	private int first_page;
	private int pre_page;
	private int next_page;
	private int last_page;
	private boolean is_first;
	private boolean is_last;

	private List<T> items;
	
	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public int getTotal_page() {
		return total_page;
	}

	public void setTotal_page(int total_page) {
		this.total_page = total_page;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public int getCurrent_page() {
		return current_page;
	}

	public void setCurrent_page(int current_page) {
		this.current_page = current_page;
	}

	public int getPage_size() {
		return page_size;
	}

	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}

	public int getFirst_page() {
		return first_page;
	}

	public void setFirst_page(int first_page) {
		this.first_page = first_page;
	}

	public int getPre_page() {
		return pre_page;
	}

	public void setPre_page(int pre_page) {
		this.pre_page = pre_page;
	}

	public int getNext_page() {
		return next_page;
	}

	public void setNext_page(int next_page) {
		this.next_page = next_page;
	}

	public int getLast_page() {
		return last_page;
	}

	public void setLast_page(int last_page) {
		this.last_page = last_page;
	}

	public boolean isIs_first() {
		return is_first;
	}

	public void setIs_first(boolean is_first) {
		this.is_first = is_first;
	}

	public boolean isIs_last() {
		return is_last;
	}

	public void setIs_last(boolean is_last) {
		this.is_last = is_last;
	}

	/**
	 * 分页处理
	 * 
	 * @param current_page
	 *            当前页
	 * @param page_size
	 *            每页的数量
	 * @param total_count
	 *            总记录数
	 */
	public void paging(int current_page, int page_size, int total_count) {
		this.current_page = current_page;
		this.page_size = page_size;
		paging(total_count);
	}

	/**
	 * 分页处理
	 * 
	 * @param total_count
	 *            总记录数
	 */
	public void paging(int total_count) {
		this.total_count = total_count;

		if (this.current_page < 1) {
			this.current_page = 1;
		}

		this.total_page = (this.total_count + this.page_size - 1)
				/ this.page_size;
		this.first_page = 1;
		this.last_page = total_page;

		if (this.current_page > 1) {
			this.pre_page = this.current_page - 1;
		} else {
			this.pre_page = 1;
		}

		if (this.current_page < total_page) {
			this.next_page = this.current_page + 1;
		} else {
			this.next_page = total_page;
		}

		if (this.current_page <= 1) {
			this.is_first = true;
		} else {
			this.is_first = false;
		}

		if (this.current_page >= total_page) {
			this.is_last = true;
		} else {
			this.is_last = false;
		}
	}

}
