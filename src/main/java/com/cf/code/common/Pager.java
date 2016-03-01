package com.cf.code.common;

import java.util.ArrayList;
import java.util.List;

public class Pager {
	

	private int count;// 记录总数

	private int pageSize = 20;// 一页显示记录条数


	private Object data;

	private Object condition;

	private String requestUrl;

	private static int  PAGE_LENGTH =5; //分页索引显示长度（一边）  
	private boolean firstPage;  //是否为第一页
	private boolean lastPage;   //是否为最后一页
	private int prePage;      	//上一页数
	private int pageNo;			// 当前页面
	private int nextPage;    	//下一页数
	private int maxPageNo; 		//最大页数
	public Pager() {
		this("", 1, 10);
	}

	public Pager(String requestUrl, int pageNo, int pageSize) {
		this.requestUrl = requestUrl;
		this.pageNo = (pageNo < 1) ? 1 : pageNo;
		this.pageSize = pageSize <= 0 ? 10 : pageSize;
	}

	
	public void addParam(String name, String value) {
		requestUrl += ("&" + name + "=" + value);
	}

	private List getPrePageNos() {
		List ret = new ArrayList();

		int maxPageNum = getMaxPageNo();
		int firstPageNum = getFirstPageNo();

		int len = PAGE_LENGTH * 2;
		int preNum = pageNo - PAGE_LENGTH;
		int nextNum = pageNo + PAGE_LENGTH;
		if (preNum < firstPageNum) {
			preNum = firstPageNum;
			nextNum = preNum + len;
		}

		if (nextNum > maxPageNum) {
			nextNum = maxPageNum;
			preNum = nextNum - len;
		}
		preNum = preNum < firstPageNum ? firstPageNum : preNum;

		for (int i = preNum; i < pageNo; i++) {
			ret.add(new Integer(i));
		}

		return ret;
	}

	private List getNextPageNos() {
		List ret = new ArrayList();

		int maxPageNum = getMaxPageNo();
		int firstPageNum = getFirstPageNo();

		int len = PAGE_LENGTH * 2;
		int preNum = pageNo - PAGE_LENGTH;
		int nextNum = pageNo + PAGE_LENGTH;
		if (preNum < firstPageNum) {
			preNum = firstPageNum;
			nextNum = preNum + len;
		}

		if (nextNum > maxPageNum) {
			nextNum = maxPageNum;
			preNum = nextNum - len;
		}
		preNum = preNum < firstPageNum ? firstPageNum : preNum;

		for (int i = pageNo + 1; i <= nextNum; i++) {
			ret.add(new Integer(i));
		}

		return ret;
	}
	public List getPageIndexs(){
		List pageList = new ArrayList();
		pageList.addAll(getPrePageNos());
		pageList.add(pageNo);
		pageList.addAll(getNextPageNos());
		return pageList;
	}
@Deprecated
	public List getPageNumber() {
		List pageList = new ArrayList();
		if (count == 0) {
			return pageList;
		}
		int startIndex = 0;
		int endIndex = 0;
		int rount = getMaxPageNo() - PAGE_LENGTH + 1;
		if (rount > 0) {
			if (rount > PAGE_LENGTH) {
				if (pageNo >= PAGE_LENGTH && pageNo <= rount) {
					startIndex = pageNo;
					endIndex = PAGE_LENGTH + pageNo - 1;
				} else if (pageNo > rount && pageNo <= getMaxPageNo()) {
					startIndex = rount;
					endIndex = getMaxPageNo();
				} else {
					startIndex = 1;
					endIndex = PAGE_LENGTH;
				}
			} else {
				if (pageNo >= PAGE_LENGTH) {
					startIndex = rount;
					endIndex = getMaxPageNo();
				} else {
					startIndex = 1;
					endIndex = PAGE_LENGTH;
				}
			}
		} else {
			startIndex = 1;
			endIndex = getMaxPageNo();
		}

		for (int p = startIndex; p <= endIndex; p++) {
			pageList.add(p);
		}
		return pageList;
	}
	//用意不明
    @Deprecated
	public int[] getRount(int Max, int pageCount, int showPage) {
		int[] inter = new int[2];// 定义一个具有两个元素的数组
		int rount = pageCount - Max + 1;
		if (rount > 0) {
			if (rount > Max) {
				if (showPage >= Max && showPage <= rount) {
					inter[0] = showPage;
					inter[1] = Max + showPage - 1;
				} else if (showPage > rount && showPage <= pageCount) {
					inter[0] = rount;
					inter[1] = pageCount;
				} else {
					inter[0] = 1;
					inter[1] = Max;
				}
			} else {
				if (showPage >= Max) {
					inter[0] = rount;
					inter[1] = pageCount;
				} else {
					inter[0] = 1;
					inter[1] = Max;
				}
			}
		} else {
			inter[0] = 1;
			inter[1] = pageCount;
		}
		return inter;
	}
    
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		
		this.count = count;
		maxPageNo = count / pageSize;
		int m = count % pageSize;
		if (m != 0) {
			maxPageNo++;
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getParamUrl() {
		int index = requestUrl.indexOf("?");
		if (index < 0)
			return "";
		else
			index += 1;
		return requestUrl.substring(index);
	}

	public Object getCondition() {
		return condition;
	}

	public void setCondition(Object condition) {
		this.condition = condition;
	}

	public int getStartIndex() {
		return pageSize * (pageNo - 1);
	}
	
	public int getEndIndex() {
		return getStartIndex() + pageSize;
	}
	
	public int getMaxPageNo() {
		
		return maxPageNo;
	}

	public int getFirstPageNo() {
		return 1;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pageNo;
		result = prime * result + pageSize;
		result = prime * result
				+ ((requestUrl == null) ? 0 : requestUrl.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Pager other = (Pager) obj;
		if (pageNo != other.pageNo)
			return false;
		if (pageSize != other.pageSize)
			return false;
		if (requestUrl == null) {
			if (other.requestUrl != null)
				return false;
		} else if (!requestUrl.equals(other.requestUrl))
			return false;
		return true;
	}

	

	public int getPrePage() {
		return pageNo - 1 <= 1 ? 1 : pageNo - 1;
	}

	public int getNextPage() {
		return pageNo + 1 >= getMaxPageNo() ? getMaxPageNo() : pageNo + 1;
	}


	
	public boolean isFirstPage() {
		return pageNo == 1;
	}

	public boolean isLastPage() {
		return pageNo == getMaxPageNo();
	}


	public void setMaxPageNo(int maxPageNo) {
		this.maxPageNo = maxPageNo;
	}

	public int getPageLength() {
		return PAGE_LENGTH;
	}

	public void setPageLength(int pageLength) {
		this.PAGE_LENGTH = pageLength;
	}

	public static void main(String[] args) {
		Pager page = new Pager();
		page.setCount(9);
		page.setPageNo(1);//当前页
		System.out.println("每页显示条数: " + page.getPageSize());
		System.out.println("第一页 : " + page.getFirstPageNo());
		System.out.println("当前页 : " + page.getPageNo());
		System.out.println("最大页 : " + page.getMaxPageNo());
		System.out.println("当前页第一条记录索引 : " + page.getStartIndex());
		System.out.println("当前页最后一条记录索引(不包含) : " + page.getEndIndex());
//		System.out.println("翻页索引 : " + page.getPageNumber());
//		System.out.println(page.getPrePageNos());
//		System.out.println(page.getNextPageNos());
		System.out.println("翻页索引 : " + page.getPageIndexs());


	}
}