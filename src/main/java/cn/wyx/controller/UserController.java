package cn.wyx.controller;

import cn.wyx.entity.*;
import cn.wyx.entity.request.OrderTicketData;
import cn.wyx.service.InfoOfOldFlightService;
import cn.wyx.service.OrderService;
import cn.wyx.service.UserPassService;
import cn.wyx.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: wyx
 * @Date: 2019/7/9 22:31
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserPassService userPassService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private InfoOfOldFlightService infoOfOldFlightService;

    @RequestMapping("/sureOrder")
    public InfoOfOrder sureOrder(@RequestBody OrderTicketData orderTicketData) {
        System.out.println("执行了 sureOrder ====");
        if (orderTicketData == null) return null;
        String ticketsId = orderTicketData.getTicketsId();
        Order order = orderTicketData.getOrder();
        List<Passenger> passengerList = orderTicketData.getPassengerList();
        try {
            Subject subject = SecurityUtils.getSubject();
            //未登录或者未选择rememberMe或已经失效
            if (subject == null || subject.getPrincipal() == null || (!subject.isAuthenticated() && !subject.isRemembered()))
                return null;
            String userName = (String) subject.getPrincipal();
            //找到对应的userid
            Long userId = this.userService.findByTel(userName).getUserId();
            if (userId == null) return null;
            order.setUserId(userId);
            InfoOfOrder infoOfOrder = this.orderService.createNewOrder(order, ticketsId, passengerList);
            return infoOfOrder;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/cancelOrder")
    public Result cancelOrder(@RequestParam String orderId) {
        Subject subject = SecurityUtils.getSubject();
        //未登录或者未选择rememberMe或已经失效
        if (subject == null || subject.getPrincipal() == null || (!subject.isAuthenticated() && !subject.isRemembered()))
            return new Result(false, "请重新登录");
        try {
            this.orderService.setOrderStateAndPayTime(orderId, "已取消", null, null);
            return new Result(true, "取消成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "请重新登录");
    }

    @RequestMapping("/postOrder")
    public Result postOrder(@RequestParam String orderId, @RequestParam String orderPayTime, @RequestParam String authPassword) {
        System.out.println(orderPayTime);

        Subject subject = SecurityUtils.getSubject();
        //未登录或者未选择rememberMe或已经失效
        if (subject == null || subject.getPrincipal() == null || (!subject.isAuthenticated() && !subject.isRemembered()))
            return new Result(false, "支付失败");
        try {
            UsernamePasswordToken token = new UsernamePasswordToken((String) subject.getPrincipal(), authPassword);
            subject.login(token);
            this.orderService.setOrderStateAndPayTime(orderId, "已支付", Timestamp.valueOf(orderPayTime), "网上支付");
            return new Result(true, "支付成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "支付失败");
    }

    @RequestMapping("/getTicketDetail")
    public InfoOfOrder getTicketDetail(@RequestParam String orderId) {
        Subject subject = SecurityUtils.getSubject();
        //未登录或者未选择rememberMe或已经失效
        if (subject == null || subject.getPrincipal() == null || (!subject.isAuthenticated() && !subject.isRemembered()))
            return null;
        try {
            InfoOfOrder info = this.orderService.findById(orderId);
            return info;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/getAllUserPass")
    public List<UserPass> getAllUserPass() {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null || subject.getPrincipal() == null) return null;
        try {
            String userName = (String) subject.getPrincipal();
            List<UserPass> userPasses = this.userPassService.findAllById(this.userService.findByTel(userName).getUserId());
            return userPasses;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/searchFlightByCityDate")
    public List<InfoOfOldFlight> searchFlightByCityDate(@RequestParam(required = false) String stCity, @RequestParam(required = false) String edCity, @RequestParam(required = false) Date planTime) {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null || subject.getPrincipal() == null) return null;
        try {
            List<InfoOfOldFlight> infoOfOldFlightList = this.infoOfOldFlightService.findOldFlightByCityDate(stCity, edCity, planTime);
            return infoOfOldFlightList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/searchFlightByIdDate")
    public List<InfoOfOldFlight> searchFlightByIdDate(@RequestParam(required = false) String flightsId, @RequestParam(required = false) Date date) {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null || subject.getPrincipal() == null) return null;
        try {
            List<InfoOfOldFlight> infoOfOldFlightList = this.infoOfOldFlightService.findOldFlightByFlightIdFlightsIdDate(null, flightsId, date);
            return infoOfOldFlightList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/savePass")
    public Result savePass(@RequestBody UserPass userPass) {
        try {
            Subject subject = SecurityUtils.getSubject();
            String userTel = (String) subject.getPrincipal();
            Long userId = this.userService.getId(userTel);
            userPass.setUserId(userId);
            this.userPassService.create(userPass);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "添加失败");
    }

    @RequestMapping("/updatePass")
    public Result updatePass(@RequestBody UserPass userPass) {
        try {
            Subject subject = SecurityUtils.getSubject();
            String userTel = (String) subject.getPrincipal();
            Long userId = this.userService.getId(userTel);
            userPass.setUserId(userId);
            this.userPassService.update(userPass);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "添加失败");
    }

    @RequestMapping("/delPass")
    public Result delPass(@RequestParam Long userpassId) {
        try {
            this.userPassService.delete(userpassId);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "删除失败");
    }

    @RequestMapping("/updateUserPass")
    public Result updateUserPass(@RequestBody UserPass userPass) {
        try {
            this.userPassService.update(userPass);
            return new Result(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "更新失败");
    }


    @RequestMapping("/findUserPassById")
    public UserPass findUserPassById(@RequestParam Long userpassId) {
        try {
            UserPass userPass = this.userPassService.findById(userpassId);
            return userPass;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/findAllInfoOfOrderById")
    public List<InfoOfOrder> findAllInfoOfOrderById() {
        try {
            Subject subject = SecurityUtils.getSubject();
            String userTel = (String) subject.getPrincipal();
            Long userId = this.userService.getId(userTel);
            List<InfoOfOrder> infoOfOrders = this.orderService.findAllInfoOfOrderById(userId);
            System.out.println(infoOfOrders);
            return infoOfOrders;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/findInfoOfOrderByOrderId")
    public InfoOfOrder findInfoOfOrderByOrderId(@RequestParam String orderId) {
        try {
            InfoOfOrder infoOfOrder = this.orderService.findInfoOfOrderByOrderId(orderId);
            System.out.println(infoOfOrder);
            return infoOfOrder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/changePass")
    public Result changePass(@RequestParam String newPass) {
        try {
            Subject subject = SecurityUtils.getSubject();
            String userTel = (String) subject.getPrincipal();
            Long userId = this.userService.getId(userTel);
            this.userService.changePassword(userId, newPass);
            return new Result(true, "更改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(true, "更改失败");
    }

}
