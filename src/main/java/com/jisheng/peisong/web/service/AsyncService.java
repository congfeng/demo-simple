/**
 * 
 */
package com.jisheng.peisong.web.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.jisheng.order.model.PrimaryOrder;
import com.jisheng.order.model.RoLine;
import com.jisheng.order.service.POService;
import com.jisheng.order.service.ROService;
import com.jisheng.peisong.entity.DeliveryCart;
import com.jisheng.peisong.entity.DeliveryOrder;
import com.jisheng.peisong.entity.DeliveryOrderGoods;
import com.jisheng.peisong.entity.DeliveryOrderReturnQueue;
import com.jisheng.peisong.entity.DeliveryOrderStateQueue;
import com.jisheng.peisong.entity.enums.SyncState;
import com.jisheng.peisong.service.DeliveryCartService;
import com.jisheng.peisong.service.DeliveryOrderService;
import com.jisheng.peisong.service.DeliveryQueueService;
import com.jisheng.util.constant.ReturnOrderType;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
@Service("AsyncService")
public class AsyncService {
	
	private static Logger log = LogManager.getLogger(AsyncService.class);

	@Resource(name = "deliveryQueueService")
	DeliveryQueueService deliveryQueueService;
	
	@Resource(name = "deliveryCartService")
	DeliveryCartService deliveryCartService;
	
	@Resource(name = "deliveryOrderService")
	DeliveryOrderService deliveryOrderService;
	
	@Resource(name = "poService_dubbo")
	POService poService;
	
	@Resource(name = "roService_dubbo")
	ROService roService;
	
	@Async
	public void test(){
		for(int i=0;i<10;i++){
			log.info(poService+"------"+deliveryQueueService);
		}
	}
	
	@Async
	public void sendDOrderState4Pickuped(List<DeliveryOrderStateQueue> dOrderStateQs){
		for(DeliveryOrderStateQueue dOrderStateQ:dOrderStateQs){
			poService.updateDOStatus(dOrderStateQ.getDeliveryOrderId(),"1");
			deliveryQueueService.updateDOrderStateQueueSynced(dOrderStateQ.getId(), SyncState.Synced, null);
		}
	}
	
	@Async
	public void sendDOrderState4Delivered(DeliveryOrderReturnQueue dOrderReturnQ,DeliveryOrderStateQueue dOrderStateQ){
		if(dOrderReturnQ != null){
			if(!deliveryQueueService.txToggleSyncState4DOrderReturn(dOrderReturnQ.getId(),SyncState.WaitSync,SyncState.Syncing)){
				log.warn("退货单生成并发，跳过本次执行，dOrderId："+dOrderReturnQ.getDeliveryOrderId());
			}else{
				StringBuilder resMsg = new StringBuilder("[");
				DeliveryOrder dOrder = deliveryOrderService.findDOrder(dOrderReturnQ.getDeliveryOrderId());
				DeliveryCart dCart = deliveryCartService.findDCart(dOrder.getDeliveryCartId());
				Integer driverId = dCart.getDriverId();
				List<DeliveryOrderGoods> dOrderGoodsList = deliveryOrderService.qryDOrderGoodsList(dOrderReturnQ.getDeliveryOrderId());
				PrimaryOrder porder = null;
				try{
					porder = poService.getPOById(dOrder.getOrderId());
					if(porder == null){
						deliveryQueueService.updateSynced4DOrderReturn(dOrderReturnQ.getId(), SyncState.SyncFailure, "无法获取订单系统PO："+dOrder.getOrderId());
					}
				}catch(Exception e){
					log.warn("调用订单系统查询po异常，orderId："+dOrder.getOrderId(), e);
					deliveryQueueService.txToggleSyncState4DOrderReturn(dOrderReturnQ.getId(),SyncState.Syncing,SyncState.WaitSync);
				}
				if(porder != null){
					resMsg.append(porder.getId()+","+dOrder.getId()+","+driverId+",roline:[");
					List<RoLine> rolist = new ArrayList<RoLine>();
					for(DeliveryOrderGoods dOrderGoods:dOrderGoodsList){
						BigDecimal backNum = dOrderGoods.getNum().subtract(dOrderGoods.getSignNum());
						if(backNum.compareTo(BigDecimal.ZERO) == 0){
							continue;
						}
						RoLine roLine = new RoLine();
						roLine.setGsId(dOrderGoods.getGoodsSnapId());
						roLine.setCreateTime(dOrderReturnQ.getOperateTime());
						roLine.setCreateBy(driverId);
						roLine.setGoodsNo(dOrderGoods.getGoodsSn());
						roLine.setStockInRequired("1");
						roLine.setUnitPrice(dOrderGoods.getPrice());
						roLine.setReturnQuantity(backNum);
						roLine.setReturnAmount(backNum.multiply(dOrderGoods.getPrice()));
						rolist.add(roLine);
						resMsg.append(dOrderGoods.getGoodsSnapId()+","+dOrderReturnQ.getOperateTime()+"|");
					}
					resMsg.append("]");
					try{
						int roId = roService.createROByDOrder(porder,dOrder.getId(), rolist, ReturnOrderType.ONSITERETURN, "1");
						if(roId > 0){
							deliveryOrderService.updateReturnOrderNo(dOrder.getId(), "R"+roId);
							deliveryQueueService.updateSynced4DOrderReturn(dOrderReturnQ.getId(), SyncState.Synced,"请求："+resMsg);
						}else{
							deliveryQueueService.updateSynced4DOrderReturn(dOrderReturnQ.getId(), SyncState.SyncFailure,"请求："+resMsg+"响应：-1，订单不存在");
						}
					}catch(Exception e){
						log.warn("调用订单系统生成退货单异常，dOrderId："+dOrder.getId(), e);
						deliveryQueueService.txToggleSyncState4DOrderReturn(dOrderReturnQ.getId(),SyncState.Syncing,SyncState.WaitSync);
					}
				}
			}
		}
		poService.updateDOStatus(dOrderStateQ.getDeliveryOrderId(),"2");
		deliveryQueueService.updateDOrderStateQueueSynced(dOrderStateQ.getId(), SyncState.Synced, null);
	}
	
}
