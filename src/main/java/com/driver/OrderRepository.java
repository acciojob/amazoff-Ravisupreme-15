package com.driver;


import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    // Order Id
    HashMap<String, Order> orderHashMap = new HashMap<>();


     // partner
      HashMap<String,DeliveryPartner>  partner = new HashMap<>();



      // order partner pair
     HashMap<String,String> orderPartnerPair = new HashMap<>();


      // assigning orders to partnerid

    HashMap<String,List<String>>  partnerOrders = new HashMap<>();




    public void addOrder(Order order) {


          orderHashMap.put(order.getId(), order);
    }

    public void addPartner(String partnerId) {


         partner.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {

         if(orderHashMap.get(orderId)!=null && partner.get(partnerId)!=null)
         {

             orderPartnerPair.put(orderId,partnerId);

         }

         List<String> list = new ArrayList<>();

         if(partnerOrders.containsKey(partnerId)){

             list = partnerOrders.get(partnerId);
         }


        list.add(orderId);
         partnerOrders.put(partnerId,list);

        DeliveryPartner deliveryPartner = partner.get(partnerId);

        deliveryPartner.setNumberOfOrders(list.size());



    }

    public Order getOrderById(String orderId) {


           return orderHashMap.getOrDefault(orderId,null);

    }

    public DeliveryPartner getPartnerById(String partnerId) {

          // DeliveryPartner partner1 = partner.get(partnerId);

         return  partner.getOrDefault(partnerId,null);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {


        return  partner.get(partnerId).getNumberOfOrders();

    }

    public List<String> getOrdersByPartnerId(String partnerId) {


           return  partnerOrders.get(partnerId);

    }

    public List<String> getAllOrders() {


          List<String> orderslist = new ArrayList<>();

          for(String key : orderHashMap.keySet()) orderslist.add(key);

          return orderslist;

    }

    public Integer getCountOfUnassignedOrders() {


         return orderHashMap.size() - orderPartnerPair.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {


        String str[] = time.split(":");

        int hh = Integer.parseInt(str[0])*60;
        int mm = Integer.parseInt(str[1]);

        int  Time = hh+mm;


        int count=0;


        if(partnerOrders.containsKey(partnerId)){


              List<String>  list = partnerOrders.get(partnerId);
              for(int i=0;i<list.size();i++){

                  Order order = orderHashMap.get(list.get(i));

                  if(order.getDeliveryTime()>Time) count++;
              }
        }

        return count;

    }

    public void deletePartnerById(String partnerId) {


           partner.remove(partnerId);

           List<String> delList = partnerOrders.get(partnerId);

           partnerOrders.remove(partnerId);

           for( String order: delList)
           {
                orderPartnerPair.remove(order);
           }
    }

    public void deleteOrderById(String orderId) {


         orderHashMap.remove(orderId);

          String partnerid = orderPartnerPair.get(orderId);

          orderPartnerPair.remove(orderId);

         List<String>  delorderId = partnerOrders.get(partnerid);


            delorderId.remove(orderId);

          partner.get(partnerid).setNumberOfOrders(delorderId.size());
    }

    public String getDeliveryTime(String partnerId) {

         int mintime=0;

         List<String> orderList = partnerOrders.get(partnerId);

         for(String order: orderList){

              Order order1 = orderHashMap.get(order);

              int time = order1.getDeliveryTime();

              if(time>mintime){
                  mintime =time;
              }
         }

          String HH = String.valueOf(mintime/60);
          String MM = String.valueOf(mintime%60);

          if(HH.length()<2) {

               HH = "0"+HH;
          }
          if(MM.length()<2){
              MM = "0"+MM;
          }

          return  HH+':'+MM;

    }
}
