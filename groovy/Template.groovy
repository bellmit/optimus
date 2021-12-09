// 依赖
@Grab("com.alibaba:fastjson:1.2.9")
@Grab("org.codehaus.groovy:http-builder:0.4.1")

// 导入包
import com.alibaba.fastjson.JSON
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import java.math.BigDecimal
import java.util.Date

// 程式执行
action(input)

// 主程式
def action(String input) {

    // 执行脚本输入对象
    ExecuteScriptInputDTO esInput = JSON.parseObject(input, ExecuteScriptInputDTO.class)

    // 渠道服务
    ChannelService channelService = new ChannelService()

    // 执行方法
    if ("create".equals(esInput.getScriptMethod())) {
        return channelService.create(esInput)
    }
    if ("query".equals(esInput.getScriptMethod())) {
        return channelService.query(esInput)
    }
    if ("parse".equals(esInput.getScriptMethod())) {
        return channelService.parse(esInput)
    }

    return null

}

// 渠道服务
class ChannelService {

    // 创建订单
    def create(ExecuteScriptInputDTO esInput) {

        // 在此处编写创建订单实现

        ExecuteScriptOutputDTO output = new ExecuteScriptOutputDTO()
        output.setOrderStatus("AP")
        return JSON.toJSONString(output)
    }

    // 调单查询
    def query(ExecuteScriptInputDTO esInput) {

        // 在此处编写调单查询实现

        ExecuteScriptOutputDTO output = new ExecuteScriptOutputDTO()
        output.setOrderStatus("AP")
        return JSON.toJSONString(output)
    }

    // 渠道回调解析请求参数
    def parse(ExecuteScriptInputDTO esInput) {

        // 在此处编写渠道回调解析请求参数实现

        ExecuteScriptOutputDTO output = new ExecuteScriptOutputDTO()
        output.setOrderStatus("AP")
        return JSON.toJSONString(output)
    }

}

// 脚本输入对象
class ExecuteScriptInputDTO {
    String scriptMethod
    String memberId
    String orderId
    String calleeOrderId
    BigDecimal amount
    Date orderTime
    String implPath
    String bizContent
    String args
    String clientIp
    String redirectUrl
}

// 脚本输出对象
class ExecuteScriptOutputDTO {
    String codeMemberId
    String orderId
    String calleeOrderId
    String orderStatus
    BigDecimal amount
    BigDecimal actualAmount
    String memo
    String channelReturnMessage
}