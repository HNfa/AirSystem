package cn.wyx.service.impl;

import cn.wyx.entity.*;
import cn.wyx.mapper.*;
import cn.wyx.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: czt
 * @Date: 2019/7/4 21:51
 * @Version 1.0
 */
@Service
public class OrderServiceIml implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private PassengerMapper passengerMapper;
    @Autowired
    private TicketsMapper ticketsMapper;
    @Autowired
    private NumberMapper numberMapper;

    @Override
    public InfoOfOrder findInfoOfOrderByOrderId(String orderId) {
        return this.orderMapper.findOneById(orderId);
    }

    /**
     * 创建新的订单
     *
     * @param order
     * @param ticketsId
     * @param passengerList
     */
    @Override
    public InfoOfOrder createNewOrder(Order order, String ticketsId, List<Passenger> passengerList) {
        /*订单数据处理*/

        //生成订单号
        String ss = timestampToString(order.getOrderSureTime());
        order.setOrderId("OD" + ss + String.format("%04d", order.getUserId() % 10000));
        //设置人数
        order.setOrderNums(passengerList.size());
        //计算总价
        int price = 0;
        int airportFee = 50;
        int fuel_surcharge = 20;
        Tickets tickets = ticketsMapper.findByTicketsId(ticketsId);
        price = (airportFee + fuel_surcharge + tickets.getTicketsPrice()) * order.getOrderNums();
        order.setOrderPrice(price);
        //设置订单状态
        order.setOrderState("未支付");
        this.orderMapper.insertOrder(order);
        /*售票信息处理*/
        //售票余数减少


        /*机票及乘客信息处理*/
        Ticket ticket;
        for (int i = 0; i < passengerList.size(); i++) {
            //生成机票编号
            String relyNumber = getRelyOnNumber();
            String ticketId = ticketsId + relyNumber;
            //匹配机票和乘客相关信息
            ticket = new Ticket(ticketId, ticketsId, order.getOrderId(), tickets.getTicketsPrice(), fuel_surcharge, airportFee, tickets.getTicketsNums());
            passengerList.get(i).setTicketId(ticketId);
            this.ticketsMapper.decreaseTicketsNum(1, ticketsId);
            tickets = this.ticketsMapper.findByTicketsId(ticketsId);
            //数据写入数据库
            this.ticketMapper.insertTicket(ticket);
            this.passengerMapper.insertPassenger(passengerList.get(i));
        }
        return this.orderMapper.findOneById(order.getOrderId());
    }

    /**
     * 查询一个用户的所有订单信息
     *
     * @param userId
     * @return
     */
    @Override
    public List<InfoOfOrder> findAllInfoOfOrderById(Long userId) {

        List<InfoOfOrder> infoOfOrderList = this.orderMapper.findAllById(userId);
        return infoOfOrderList;
    }

    @Override
    public List<InfoOfOrder> findAll() {
        return null;
    }

    @Override
    public InfoOfOrder findById(Long id) {
        return null;
    }

    /**
     * 通过orderId查询一个订单的信息
     *
     * @param id
     * @return
     */
    @Override
    public InfoOfOrder findById(String id) {

        InfoOfOrder infoOfOrder = this.orderMapper.findOneById(id);
        return infoOfOrder;
    }

    @Override
    public void create(InfoOfOrder infoOfOrder) {

    }

    /**
     * 更改订单状态
     *
     * @param OrderId
     * @param OrderState
     */
    @Override
    public void setOrderStateAndPayTime(String OrderId, String OrderState, Timestamp orderPayTime, String payType) {
        this.orderMapper.updateOrderStateAndPayTime(OrderId, OrderState, orderPayTime, payType);
    }

    @Override
    public void update(InfoOfOrder infoOfOrder) {

    }

    @Override
    public void delete(Long... ids) {

    }

    @Override
    public void delete(String... ids) {

    }

    /**
     * 将Timestamp转化为纯数字String类型
     *
     * @param time
     * @return
     */
    public String timestampToString(Timestamp time) {
        String ss = time.toString();
        String sb1 = ss.substring(0, 4), sb2 = ss.substring(5, 7), sb3 = ss.substring(8, 10);
        String sb4 = ss.substring(11, 13), sb5 = ss.substring(14, 16), sb6 = ss.substring(17, 19);
        String result = sb1 + sb2 + sb3 + sb4 + sb5 + sb6;
        return result;
    }

    /**
     * 从数据库中机票Id依赖数字
     *
     * @return
     */
    public String getRelyOnNumber() {
        Long number = this.numberMapper.getTicketRelyOnNumber();
        this.numberMapper.increaseTicketRelyOnNumber();
        String sNumber = String.format("%05d", number);
        return sNumber;
    }
}
