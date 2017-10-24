package jrange.myordy.entity.list;

public abstract class SearchRequest {

	public static final int MAX_RESULT_DEFAULT = 10;

//	private int firstResult;
	private int currentPageNumber;
	private int maxResults = MAX_RESULT_DEFAULT;

	public int getCurrentPageNumber() {
		return currentPageNumber;
	}
	public void setCurrentPageNumber(final int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}
	public int getMaxResults() {
		return maxResults;
	}
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

}
