// 依赖
@Grab("com.alibaba:fastjson:1.2.9")
@Grab("org.codehaus.groovy:http-builder:0.4.1")

// 导入包
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.HTTPBuilder
import com.alibaba.fastjson.JSON
import java.math.BigDecimal
import java.util.Date

// 程式执行
action(input)

// 主程式
def action(String params) {

    // 执行脚本输入对象
    GroovyExecuteScriptInputDTO input = JSON.parseObject(params, GroovyExecuteScriptInputDTO.class)

    // 渠道服务
    GroovyChannelService service = new GroovyChannelService()

    // 执行方法
    if ("create".equals(input.getScriptMethod())) {
        return service.create(input)
    }
    if ("query".equals(input.getScriptMethod())) {
        return service.query(input)
    }
    if ("parse".equals(input.getScriptMethod())) {
        return service.parse(input)
    }

    return null

}

// 渠道服务
class GroovyChannelService {

    // 创建订单
    def create(GroovyExecuteScriptInputDTO input) {

        // 在此处编写创建订单实现

        GroovyExecuteScriptOutputDTO output = new GroovyExecuteScriptOutputDTO(codeMemberId: "自研渠道必有码商会员编号", orderId: input.getOrderId(), calleeOrderId: "被调用方订单编号", orderStatus: "NP", amount: input.getAmount(), actualAmount: new BigDecimal("100"), channelReturnMessage: "乱七八糟一大堆", content: "{\"text\":\"html代码\",\"url\":\"url地址\"}")
        
        return JSON.toJSONString(output)
    }

    // 调单查询
    def query(GroovyExecuteScriptInputDTO input) {

        // 在此处编写调单查询实现

        GroovyExecuteScriptOutputDTO output = new GroovyExecuteScriptOutputDTO(orderId: input.getOrderId(), calleeOrderId: input.getCalleeOrderId(), orderStatus: "AP")
        
        return JSON.toJSONString(output)
    }

    // 渠道回调解析请求参数
    def parse(GroovyExecuteScriptInputDTO input) {

        // 在此处编写渠道回调解析请求参数实现

        GroovyExecuteScriptOutputDTO output = new GroovyExecuteScriptOutputDTO(orderId: "订单编号", calleeOrderId: "被调用方订单编号", orderStatus: "AP")

        return JSON.toJSONString(output)
    }

}

// 输入对象
class GroovyExecuteScriptInputDTO {
    String scriptMethod         // 脚本方法
    String memberId             // 会员编号
    String orderId              // 订单编号
    String calleeOrderId        // 被调用方订单编号
    BigDecimal amount           // 订单金额
    Date orderTime              // 订单时间
    String implPath             // 实现路径
    String bizContent           // 业务大字段[{"channelMerchnatId":"商户编号","channelMerchnatKey":"商户密钥","channelCode":"渠道编号","callbackUrl":"回调地址","redirectUrl":"重定向地址","createOrderUrl":"创建订单地址","queryOrderUrl":"调单查询地址"}]
    String args                 // 参数[{"parameter":{},"header":{},"body":{}}]
    String clientIp             // 商户客户端IP
    String redirectUrl          // 商户重定向地址
}

// 输出对象
class GroovyExecuteScriptOutputDTO {
    String codeMemberId         // 码商会员编号[自研渠道必须返回]
    String orderId              // 订单编号
    String calleeOrderId        // 被调用方订单编号
    String orderStatus          // 订单状态
    BigDecimal amount           // 订单金额
    BigDecimal actualAmount     // 实际金额
    String channelReturnMessage // 网关渠道返回信息
    String content              // 报文[作用一:商户下单后返回商户({"text":"html代码","url":"url地址"});作用二:渠道回调时返回渠道(不同渠道的成功标识)]
}