package com.lilma.legou.service.impl;

import com.lilma.legou.mapper.CartlistMapper;
import com.lilma.legou.pojo.CartList;
import com.lilma.legou.service.CartlistService;
import com.lilma.legou.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
public class CartlistServiceImpl implements CartlistService {
    @Autowired
    private CartlistMapper cartlistMapper;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    //将配置文件中的spring.mail.username属性值注入到这个字段。
    @Value("${spring.mail.username}")
    private String sendMailer;

    @Override
    public CartList[] getlist(Integer userid) {
        return cartlistMapper.getlist(userid);
    }

    @Override
    public void delete(Integer id) {
        CartList cartlistInfo = cartlistMapper.getCartlistInfo(id);
        if (cartlistInfo == null) {
            return; // 无对应记录，直接返回
        }
        // 2. 恢复商品库存
        cartlistMapper.restoreGoodsAmount(cartlistInfo.getGoodsid(), cartlistInfo.getGoodsnum());

        // 3. 删除购物车记录
        cartlistMapper.delCartlist(id);
    }

    @Override
    public boolean finish(CartList[] lists) {
        ArrayList<CartList> newlist=new ArrayList<>();
        for(CartList c:lists){
            Integer amount=cartlistMapper.getAmount(c.getGoodsid());
            Integer cartlistnum=cartlistMapper.getCartNum(c.getId());
            if (amount+cartlistnum>c.getGoodsnum()){
                cartlistMapper.addOrder(c);
                cartlistMapper.newAmount(c.getGoodsid(),amount,cartlistnum,c.getGoodsnum());
                cartlistMapper.delCartlist(c.getId());
                newlist.add(c);
            }
        }
        if(newlist.size()==0)return false;
        else {
            sendEmail(newlist);
            return true;
        }
    }

    @Override
    public void addCartlist(Integer goodsid, Integer goodsnum, Integer userid) {
        cartlistMapper.cutAmount(goodsid,goodsnum);
        cartlistMapper.addCartlist(goodsid,goodsnum,userid);
    }

    public void sendEmail(ArrayList<CartList> lists) {
        Map<String,Object> claim= ThreadLocalUtil.get();
        Integer userid=(Integer)claim.get("id");
        String email=cartlistMapper.getemail(userid);
        String title="欢迎使用乐购";
        StringBuilder text = new StringBuilder();
        text.append("您已成功购买：").append('\n');
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartList list : lists) {
            BigDecimal price=list.getGoodsprice().setScale(2, RoundingMode.HALF_UP);
            BigDecimal gtotalPrice=list.getTotalprice().setScale(2, RoundingMode.HALF_UP);
            text.append("商品: ").append(list.getGoodsname()).append("\n");
            text.append("商品单价: ").append("￥").append(price).append("\n");
            text.append("商品数量: ").append(list.getGoodsnum()).append("\n");
            text.append("商品价格: ").append("￥").append(gtotalPrice).append("\n");
            totalPrice=totalPrice.add(list.getTotalprice());
        }
        totalPrice=totalPrice.setScale(2,RoundingMode.HALF_UP);
        text.append("商品总价: ").append("￥").append(totalPrice).append("\n");
        text.append("您的商品已发货，收货后请在平台点击已收货");
        try {
            //创建一个MimeMessageHelper对象，
            // 这个对象帮助构建复杂的邮件内容（如附件、HTML内容等）。
            // true参数表示支持复杂类型。
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(),true);
            //邮件发信人
            mimeMessageHelper.setFrom(sendMailer);
            //邮件收信人
            mimeMessageHelper.setTo(email);
            //邮件主题
            mimeMessageHelper.setSubject(title);
            //邮件内容
            mimeMessageHelper.setText(text.toString());
            //邮件发送时间
            mimeMessageHelper.setSentDate(new Date());
            //发送邮件
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            //如果邮件发送成功，打印成功信息。
            System.out.println("发送邮件成功：" +sendMailer+"===>"+email);

        } catch (MessagingException e) {
            //如果发生异常，打印异常堆栈跟踪。
            //如果发生异常，打印失败信息和异常消息。
            e.printStackTrace();
            System.out.println("发送邮件失败："+e.getMessage());
        }

    }
}
