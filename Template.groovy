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

        GroovyExecuteScriptOutputDTO output = new GroovyExecuteScriptOutputDTO()
        output.setOrderStatus("AP")
        return JSON.toJSONString(output)
    }

    // 调单查询
    def query(GroovyExecuteScriptInputDTO input) {

        // 在此处编写调单查询实现

        GroovyExecuteScriptOutputDTO output = new GroovyExecuteScriptOutputDTO()
        output.setOrderStatus("AP")
        return JSON.toJSONString(output)
    }

    // 渠道回调解析请求参数
    def parse(GroovyExecuteScriptInputDTO input) {

        // 在此处编写渠道回调解析请求参数实现

        GroovyExecuteScriptOutputDTO output = new GroovyExecuteScriptOutputDTO()
        output.setOrderStatus("AP")
        return JSON.toJSONString(output)
    }

}

// 输入对象
class GroovyExecuteScriptInputDTO {
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

// 输出对象
class GroovyExecuteScriptOutputDTO {
    String codeMemberId
    String orderId
    String calleeOrderId
    String orderStatus
    BigDecimal amount
    BigDecimal actualAmount
    String memo
    String channelReturnMessage
}