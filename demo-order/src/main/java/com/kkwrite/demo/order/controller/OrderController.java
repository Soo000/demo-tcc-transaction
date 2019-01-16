package com.kkwrite.demo.order.controller;

import com.kkwrite.demo.order.controller.vo.PlaceOrderRequest;
import com.kkwrite.demo.order.entity.OrderDO;
import com.kkwrite.demo.order.entity.ProductDO;
import com.kkwrite.demo.order.repository.ProductRepository;
import com.kkwrite.demo.order.service.AccountService;
import com.kkwrite.demo.order.service.OrderService;
import com.kkwrite.demo.order.service.PlaceOrderService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.List;

@Controller
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    ProductRepository productRepository;
    @Autowired
    private PlaceOrderService placeOrderService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("/index");
        return mv;
    }

    /**
     * 查新店铺商品
     * @param userId
     * @param shopId
     * @return
     */
    @RequestMapping("/user/{userId}/shop/{shopId}")
    public ModelAndView getProductsInShop(@PathVariable long userId, @PathVariable long shopId) {
        if (logger.isDebugEnabled()) {
            logger.debug("getProductsInShop");
        }
        ModelAndView mv = new ModelAndView("/shop");

        List<ProductDO> products = productRepository.findByShopId(shopId);

        mv.addObject("userId", userId);
        mv.addObject("shopId", shopId);
        mv.addObject("products", products);

        return mv;
    }

    /**
     * 查询商品详情
     * @param userId
     * @param shopId
     * @param productId
     * @return
     */
    @RequestMapping(value = "/user/{userId}/shop/{shopId}/product/{productId}/confirm", method = RequestMethod.GET)
    public ModelAndView productDetail(@PathVariable long userId,
                                      @PathVariable long shopId,
                                      @PathVariable long productId) {

        ModelAndView mv = new ModelAndView("/product_detail");

        mv.addObject("capitalAmount", accountService.getCapitalAccountByUserId(userId));
        mv.addObject("redPacketAmount", accountService.getRedPacketAccountByUserId(userId));

        mv.addObject("product", productRepository.findById(productId));

        mv.addObject("userId", userId);
        mv.addObject("shopId", shopId);

        return mv;
    }

    @RequestMapping(value = "/placeorder", method = RequestMethod.POST)
    public RedirectView placeOrder(@RequestParam long payerUserId,
                                   @RequestParam long shopId,
                                   @RequestParam long productId,
                                   @RequestParam String redPacketPayAmount) {

        PlaceOrderRequest request = buildRequest(redPacketPayAmount, shopId, payerUserId, productId);
        String merchantOrderNo = placeOrderService.placeOrder(request.getPayerUserId(), request.getShopId(),
                request.getProductQuantities(), request.getRedPacketPayAmount());

        return new RedirectView("/payresult/" + merchantOrderNo);
    }

    @RequestMapping(value = "/payresult/{merchantOrderNo}", method = RequestMethod.GET)
    public ModelAndView getPayResult(@PathVariable String merchantOrderNo) {
        ModelAndView mv = new ModelAndView("/pay_success");

        String payResultTip = null;
        OrderDO foundOrder = orderService.findOrderByMerchantOrderNo(merchantOrderNo);

        if ("CONFIRMED".equals(foundOrder.getStatus()))
            payResultTip = "支付成功";
        else if ("PAY_FAILED".equals(foundOrder.getStatus()))
            payResultTip = "支付失败";
        else
            payResultTip = "Unknown";

        mv.addObject("payResult", payResultTip);

        mv.addObject("capitalAmount", accountService.getCapitalAccountByUserId(foundOrder.getPayerUserId()));
        mv.addObject("redPacketAmount", accountService.getRedPacketAccountByUserId(foundOrder.getPayerUserId()));

        return mv;
    }

    private PlaceOrderRequest buildRequest(String redPacketPayAmount, long shopId, long payerUserId, long productId) {
        BigDecimal redPacketPayAmountInBigDecimal = new BigDecimal(redPacketPayAmount);
        if (redPacketPayAmountInBigDecimal.compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidParameterException("invalid red packet amount :" + redPacketPayAmount);

        PlaceOrderRequest request = new PlaceOrderRequest();
        request.setPayerUserId(payerUserId);
        request.setShopId(shopId);
        request.setRedPacketPayAmount(new BigDecimal(redPacketPayAmount));
        request.getProductQuantities().add(new ImmutablePair<Long, Integer>(productId, 1));
        return request;
    }

}
