package jrange.myordy.dao;

import java.util.List;

import jrange.myordy.entity.Ordy;
import jrange.myordy.entity.list.OrdySearchRequest;
import jrange.myordy.entity.list.OrdySearchResponse;
import jrange.myordy.entity.list.SalesSummaryReportRequest;
import jrange.myordy.entity.list.SalesSummaryReportResponse;
import jrange.myordy.v1.shop.restaurant.OrderDetailsVO;

public interface OrdyDAO {

	public Ordy save(Ordy ordy);

	public Ordy getCustomerLastOrdy(final Integer customerId, final Integer shopId);

	public Ordy get(final Integer id, final Integer shopId);

	public Ordy get(final Integer id, final Integer shopId, final String deviceSessionId);

	public Ordy confirmOrdy(final Integer id, final Integer shopId);

    public OrdySearchResponse list(final OrdySearchRequest criteria);

	public OrderDetailsVO getV1(final Long id);

	public SalesSummaryReportResponse salesSummaryReport(final SalesSummaryReportRequest request);

	public List<Integer> getOrdyPendingConfirmationShopIdList();

}
